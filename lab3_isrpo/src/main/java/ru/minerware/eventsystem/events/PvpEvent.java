package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;

public class PvpEvent extends Event {

    public PvpEvent() {
        super("&c&lРезня", "&cУбейте &fкак можно больше &cигроков", Core.getInstance().getConfigParser().getMaxPlayers() * 2 + 5, true);
    }

    @Override
    public void onEventStart() {
        Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD)));
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {
        for (PlayerInfo info : PlayerInfo.PLAYERS) {
            if (info.getPlayer().getGameMode() == GameMode.SURVIVAL)
                successEnd(info);
            else
                looseEnd(info);
        }
    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent event)
    {
        Player jertva = event.getEntity();
        Player killer = jertva.getKiller();

        if (killer != null && killer != jertva)
        {
            PlayerInfo.get(killer).addScore(2);

            U.chat(killer, "Вы &cубили &fигрока &c%s",
                    jertva.getName()
            );
        }
    }
}
