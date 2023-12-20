package ru.minerware.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import ru.minerware.Core;
import ru.minerware.GamePhase;
import ru.minerware.data.PlayerInfo;
import ru.minerware.utils.builders.TimerBuilder;
import ru.minerware.utils.U;

public class LobbyListener implements Listener {

    private final Core core;
    private TimerBuilder timer;

    public LobbyListener(Core core)
    {
        this.core = core;
    }

    @EventHandler
    public void onAsyncPrePlayerLoginEvent (AsyncPlayerPreLoginEvent event)
    {
        if (core.getGamePhase() != GamePhase.WAITING)
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, U.color("&cИгра уже начинается"));
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event)
    {
        if (core.getGamePhase() != GamePhase.WAITING
                && core.getGamePhase() != GamePhase.STARTING)
            return;

        PlayerInfo playerInfo = new PlayerInfo(event.getPlayer());
        Player player = playerInfo.getPlayer();
        int online = Bukkit.getOnlinePlayers().size();
        int maxPlayers = core.getConfigParser().getMaxPlayers();

        U.clearPlayer(player);
        player.teleport(core.getConfigParser().getLobbyRespawn());

        U.broadcast("[%d/%d] %s подключился к игре",
                online, maxPlayers, player.getName()
        );

        if (online == maxPlayers)
        {
            core.setGamePhase(GamePhase.STARTING);

            timer = new TimerBuilder(core, 5, 20, true)
                    .tick(value -> U.broadcast(String.format("Игра начнется через %d секунд", value)))
                    .finish(() -> core.startGame())
                    .start();
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event)
    {
        if (core.getGamePhase() != GamePhase.WAITING
                && core.getGamePhase() != GamePhase.STARTING)
            return;

        Player player = event.getPlayer();
        int online = Bukkit.getOnlinePlayers().size() - 1;
        int maxPlayers = core.getConfigParser().getMaxPlayers();

        U.broadcast("[%d/%d] %s отключился от игры",
                online, maxPlayers, player.getName()
                );

        timer.stop();
        U.broadcast("Отсчет остановлен из-за выхода игрока");
    }

    @EventHandler
    public void onPlayerRespawn (PlayerRespawnEvent event)
    {
        if (core.getGamePhase() != GamePhase.WAITING
                && core.getGamePhase() != GamePhase.STARTING)
            return;

        event.setRespawnLocation(core.getConfigParser().getLobbyRespawn());
        U.clearPlayer(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamagedByEntity (EntityDamageByEntityEvent event)
    {
        if (core.getGamePhase() != GamePhase.WAITING
                && core.getGamePhase() != GamePhase.STARTING)
            return;

        event.setCancelled(true);
    }
}
