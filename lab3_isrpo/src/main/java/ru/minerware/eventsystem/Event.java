package ru.minerware.eventsystem;

import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.utils.U;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Event implements Listener {

    public enum StatType
    {
        SUCCESS,
        LOOSE,
        TIMEEND
    }

    private final String name;

    /* Максимальное время до конца ивента (В секундах) */
    private final int timeLeft;

    /* Нынешнее время до конца ивента (В секундах) */
    private int time;

    /* При начале эвента данная строка будет выводится в чат для помощи игрокам в понятии сути эвента */
    private String help;

    /* Заглушка для ивента чтобы шедулер не стакал таймеры старта эвента */
    private boolean starting;
    private final boolean pvp;

    private final Map<PlayerInfo, StatType> stats = new HashMap<>();

    public Event (String name, String help, int maxTimeLeft, boolean pvp)
    {
        this.name = name;
        this.help = help;

        this.timeLeft = maxTimeLeft;
        this.time = maxTimeLeft;

        this.pvp = pvp;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public void incrementTime () {
        this.time--;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    public abstract void onEventStart ();
    public abstract void onEventUpdate ();
    public abstract void onEventEnd ();

    protected void addStat (Player player, StatType type)
    {
        addStat(PlayerInfo.get(player), type);
    }

    public void clearStats ()
    {
        stats.clear();
    }

    protected void addStat (PlayerInfo info, StatType type)
    {
        stats.put(info, type);
    }

    protected void successEnd (PlayerInfo playerInfo)
    {
        addStat(playerInfo, StatType.SUCCESS);
    }

    protected void looseEnd (PlayerInfo playerInfo)
    {
        addStat(playerInfo, StatType.LOOSE);
    }

    protected boolean isFinished (Player player)
    {
        return isFinished(PlayerInfo.get(player));
    }

    protected boolean isFinished (PlayerInfo info)
    {
        return getStats().containsKey(info);
    }

    public void stop ()
    {
        this.setTime(this.getTimeLeft());
        HandlerList.unregisterAll(this);
        this.onEventEnd();

        for (PlayerInfo info : PlayerInfo.PLAYERS) {
            Player player = info.getPlayer();
            Event.StatType playerStat = this.getStats().getOrDefault(info, Event.StatType.TIMEEND);

            switch (playerStat)
            {
                case SUCCESS:
                {
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                    player.sendTitle(U.color("&2Вы успешно прошли эвент &e" + this.getName()), "");
                    break;
                }

                case LOOSE:
                {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                    player.sendTitle(U.color("&cВы не прошли испытание " + this.getName()), "");
                    break;
                }

                case TIMEEND:
                {
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                    player.sendTitle(U.color("&7Вы не успели :("), "");
                    break;
                }
            }
        }

        this.clearStats();

        Core.getInstance().getWorld().getEntities()
                .stream()
                .filter(e -> !(e instanceof Player))
                .forEach(Entity::remove);
    }
}
