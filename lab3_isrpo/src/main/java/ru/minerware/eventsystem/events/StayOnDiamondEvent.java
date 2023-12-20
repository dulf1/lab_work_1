package ru.minerware.eventsystem.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;
import ru.minerware.utils.builders.ItemBuilder;
import ru.minerware.utils.builders.TimerBuilder;

public class StayOnDiamondEvent extends Event {

    public StayOnDiamondEvent() {
        super("Стой на &dалмазе", "Не дай себя скинуть другим игрокам и останься на алмазном блоке до конца отсчета", 13, true);
    }

    @Override
    public void onEventStart() {

        for (Location interactPos : Core.getInstance().getConfigParser().getInteractPoses()) {
            interactPos.getBlock().setType(Material.DIAMOND_BLOCK);
        }

        for (PlayerInfo info : PlayerInfo.PLAYERS)
            info.getPlayer().getInventory().addItem(
                    new ItemBuilder(Material.STICK)
                            .enchantment(Enchantment.KNOCKBACK, 1)
                            .make()
            );

        new TimerBuilder(Core.getInstance(), 10, 20, false)
                .tick(value -> U.broadcast("&6&lM&e&lW &7| &fВсе кто не стоят на алмазном блоке - &cумрут&f через &a%d", value))
                .finish(() -> {
                    for (PlayerInfo info : PlayerInfo.PLAYERS) {
                        Player player = info.getPlayer();

                        if (player.getLocation()
                                .add(0, -1, 0)
                                .getBlock()
                                .getType() != Material.DIAMOND_BLOCK)
                        {
                            player.setGameMode(GameMode.SPECTATOR);
                            looseEnd(info);
                        }
                        else
                        {
                            info.addScore(2);
                            successEnd(info);
                        }
                    }
                }).start();
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {
    }

    @EventHandler
    public void onDamage (EntityDamageByEntityEvent event)
    {
        event.setDamage(0);
    }
}
