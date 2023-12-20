package ru.minerware;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.eventsystem.EventManager;
import ru.minerware.listeners.CoreListener;
import ru.minerware.listeners.DisableListener;
import ru.minerware.listeners.LobbyListener;
import ru.minerware.utils.U;
import ru.minerware.utils.builders.ScoreboardBuilder;
import ru.minerware.utils.builders.TimerBuilder;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

@Getter
public class Core extends JavaPlugin {

    private static Core instance;

    private EventManager eventManager;
    private GamePhase gamePhase = GamePhase.WAITING;
    private World world;
    private ConfigParser configParser;
    private long startTime;

    @Override
    public void onEnable() {
        instance = this;

        loadConfigs();
        initListeners();
        startSchedulers();

        configParser = new ConfigParser(this);
        eventManager = new EventManager();
        world = Bukkit.getWorld("world");

        if (!configParser.parseConfig())
        {
            this.setEnabled(false);
            return;
        }
    }

    @Override
    public void onDisable() {

        Event currentEvent = eventManager.getCurrentEvent();

        if (currentEvent != null)
            currentEvent.stop();
    }

    private void loadConfigs ()
    {
        File config = new File(getDataFolder() + File.separator + "config.yml");

        if (!config.exists())
        {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    private void initListeners ()
    {
        Bukkit.getPluginManager().registerEvents(new CoreListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DisableListener(), this);
    }

    private void startSchedulers ()
    {
        // Шедулер для обработок быстрых задач
        Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            if (gamePhase != GamePhase.PLAY)
                return;

            Event currentEvent = this.eventManager.getCurrentEvent();

            if (currentEvent != null && !currentEvent.isStarting())
            {
                currentEvent.onEventUpdate();
            }
        }, 1L, 1L);

        // Шедулер для обновления тяжеловестных задач, так же здесь меняется время нынешнего эвента
        Bukkit.getScheduler().runTaskTimer(this, () ->
        {
            if (gamePhase != GamePhase.PLAY)
                return;

            Event currentEvent = this.eventManager.getCurrentEvent();

            if (currentEvent == null)
            {
                startNextEvent(null);
                return;
            }

            if (eventManager.isEventStarting())
                return;

            currentEvent.incrementTime();

            Bukkit.getOnlinePlayers().forEach(p -> p.setLevel(currentEvent.getTime()));

            if (currentEvent.getTime() <= 0)
            {
                currentEvent.stop();
                Collections.shuffle(this.eventManager.getEvents());
                Event newEvent = this.eventManager.getEvents().get(0);

                newEvent.setStarting(true);

                new TimerBuilder(this, 3, 20, true)
                        .tick(value -> U.sendAction("Следующая &dмини-игра &fначнется через", "&e" + value))
                        .finish(() -> startNextEvent(newEvent))
                        .start();
            }

        }, 20L, 20L);
    }

    private void startNextEvent (Event newEvent)
    {
        if (startTime + ((long) configParser.getPlayingTime() * 60 * 1000) < System.currentTimeMillis())
        {
            finishGame();
            return;
        }

        for (Location interactPos : Core.getInstance().getConfigParser().getInteractPoses()) {
            interactPos.getBlock().setType(Material.AIR);
        }

        if (newEvent == null)
        {
            newEvent = this.eventManager.getRandomEvent();
        }

        this.eventManager.setCurrentEvent(newEvent);

        for (PlayerInfo info : PlayerInfo.PLAYERS) {
            Player player = info.getPlayer();

            player.teleport(configParser.getPlayRespawn());
            U.clearPlayer(player);
        }

        updateScoreboard();

        Bukkit.getPluginManager().registerEvents(newEvent, this);
        newEvent.onEventStart();
        newEvent.setStarting(false);

        U.broadcast("&fНачался эвент &e%s \n&7Подсказка: %s",
                newEvent.getName(), newEvent.getHelp()
        );
    }

    public void startGame ()
    {
        setGamePhase(GamePhase.PLAY);
        startTime = System.currentTimeMillis();

        for (PlayerInfo player : PlayerInfo.PLAYERS) {
            player.getPlayer().teleport(configParser.getPlayRespawn());
        }
    }

    public void finishGame ()
    {
        setGamePhase(GamePhase.ENDING);

        PlayerInfo.PLAYERS.sort(Comparator.comparingInt(PlayerInfo::getScore));
        Collections.reverse(PlayerInfo.PLAYERS);

        PlayerInfo winner = PlayerInfo.PLAYERS.get(0);

        U.broadcast("&6&l1.&e Победитель: &f&l%s &7(&a%d очков&7)",
                winner.getPlayer().getName(), winner.getScore()
        );

        StringBuilder endingText = new StringBuilder();

        for (int i = 1; i < PlayerInfo.PLAYERS.size(); i++)
        {
            PlayerInfo info = PlayerInfo.PLAYERS.get(i);

            if (i >= 5)
                break;

            endingText.append(String.format("&6&l%d. &f%s &7(&a%d очков&7) \n",
                    i + 1, info.getPlayer().getName(), info.getScore())
            );
        }

        U.broadcast(endingText.toString());

        new TimerBuilder(this, 5, 20, true)
                .finish(Bukkit::shutdown)
                .start();
    }

    private void updateScoreboard ()
    {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder("&6&lMiner&e&lWare");
        Event currentEvent = eventManager.getCurrentEvent();

        scoreboardBuilder.addLine("&fТоп по &dочкам");

        PlayerInfo.PLAYERS.sort(Comparator.comparingInt(PlayerInfo::getScore));
        Collections.reverse(PlayerInfo.PLAYERS);

        for (int i = 0; i < PlayerInfo.PLAYERS.size(); i++)
        {
            if (i >= 5)
                break;

            PlayerInfo info = PlayerInfo.PLAYERS.get(i);

            scoreboardBuilder.addLine("&e%d &7%s",
                    info.getScore(), info.getPlayer().getName()
            );
        }

        scoreboardBuilder.addLine(" ");
        scoreboardBuilder.addLine("&2Мини-игра:");
        scoreboardBuilder.addLine("&6%s",
                currentEvent == null ? "Нема" : currentEvent.getName()
        );

        Scoreboard scoreboard = scoreboardBuilder.build();

        Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(scoreboard));
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public static Core getInstance() {
        return instance;
    }
}
