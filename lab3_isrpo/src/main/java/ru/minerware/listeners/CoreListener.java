package ru.minerware.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.minerware.Core;
import ru.minerware.GamePhase;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;

public class CoreListener implements Listener {

    private final Core core;

    public CoreListener (Core core)
    {
        this.core = core;
    }

    @EventHandler
    public void onBreak (BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace (BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPvP (EntityDamageByEntityEvent event)
    {
        Event currentEvent = core.getEventManager().getCurrentEvent();

        if (currentEvent == null)
            return;

        event.setCancelled(!currentEvent.isPvp());
    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent event)
    {
        if (core.getGamePhase() == GamePhase.PLAY)
        {
            PlayerInfo.get(event.getEntity()).respawn();
        }
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event)
    {
        if (Core.getInstance().getConfigParser().getYLimit() > event.getTo().getY())
        {
            PlayerInfo.get(event.getPlayer()).respawn();
        }
    }
}
