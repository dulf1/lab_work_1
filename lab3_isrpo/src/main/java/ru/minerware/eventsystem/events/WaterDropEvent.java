package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.builders.TimerBuilder;

public class WaterDropEvent extends Event {

    private static final int PLATFORM_SIZE = 5;

    public WaterDropEvent() {
        super("&fПадение в &dводу", "Попади в &dводу &7при падении", 8, false);
    }

    @Override
    public void onEventStart() {

        Location dropPosition = Core.getInstance().getConfigParser().getPlayRespawn().clone().add(0, 60, 0);

        int xCoord = dropPosition.getBlockX();
        int zCoord = dropPosition.getBlockZ();

        for (int x = xCoord; x < xCoord + PLATFORM_SIZE; x++)
        {
            for (int z = zCoord; z < zCoord + PLATFORM_SIZE; z++)
            {
                new Location(Core.getInstance().getWorld(), x, dropPosition.getBlockY() - 3, z).getBlock().setType(Material.STONE);
            }
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.teleport(dropPosition));

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
            p.teleport(dropPosition);
        });
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {

        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn();
        Location dropPosition = spawnLocation.clone().add(0, 60, 0);

        int xCoord = dropPosition.getBlockX();
        int zCoord = dropPosition.getBlockZ();

        for (int x = xCoord; x < xCoord+ PLATFORM_SIZE; x++)
        {
            for (int z = zCoord; z < zCoord + PLATFORM_SIZE; z++)
            {
                new Location(Core.getInstance().getWorld(), x, dropPosition.getBlockY() - 3, z).getBlock().setType(Material.AIR);
            }
        }

        for (PlayerInfo info : PlayerInfo.PLAYERS) {

            Player player = info.getPlayer();

            if (player.getLocation().getBlockY() <= (spawnLocation.getBlockY() + 2) && player.getGameMode() == GameMode.SURVIVAL)
            {
                successEnd(info);
                info.addScore(2);
            }
            else
            {
                looseEnd(info);
            }
        }
    }

    @EventHandler
    public void onPlayerPlaceWater (PlayerBucketEmptyEvent event)
    {
        if (event.isCancelled())
            return;

        BlockFace face = event.getBlockFace();

        Block waterBlock = event
                .getBlockClicked()
                .getLocation()
                .add(face.getModX(), face.getModY(), face.getModZ())
                .getBlock();

        new TimerBuilder(Core.getInstance(), 2, 20, true)
                .finish(() -> waterBlock.setType(Material.AIR))
                .start();
    }
}
