package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SafeGroundEvent extends Event {

    private static final int RADIUS = 10;
    private static final int RADIUS_UP = 40;

    private final List<Location> placedBlocks = new ArrayList<>();

    public SafeGroundEvent() {
        super("Крутой спуск", "Спустить вниз не умерев", 15, false);
    }

    @Override
    public void onEventStart() {

        Random random = new Random();
        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn().clone().add(0, 1, 0);

        int spawnX = spawnLocation.getBlockX();
        int spawnY = spawnLocation.getBlockY();
        int spawnZ = spawnLocation.getBlockZ();

        for (int x = spawnX - RADIUS; x < spawnX + RADIUS; x++)
        {
            for (int y = spawnY; y < spawnY + RADIUS_UP; y++)
            {
                for (int z = spawnZ - RADIUS; z < spawnZ + RADIUS; z++)
                {
                    Location blockLocation = new Location(
                            Core.getInstance().getWorld(),
                            x,
                            y,
                            z
                    );

                    if (random.nextInt(100) < 15)
                    {
                        blockLocation.getBlock().setType(Material.STONE);
                        placedBlocks.add(blockLocation);
                    }
                }
            }
        }

        Location playerSpawnPosition = spawnLocation.clone().add(0, RADIUS_UP, 0);
        placedBlocks.add(playerSpawnPosition);

        playerSpawnPosition.getBlock().setType(Material.BEDROCK);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.teleport(playerSpawnPosition.clone().add(0, 1, 0));

            p.setMaxHealth(5 * 2);
            p.setHealth(5 * 2);
        });
    }

    @EventHandler
    public void onMove (PlayerMoveEvent event)
    {
        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn();
        Player player = event.getPlayer();

        if (event.getTo().getBlockY() < spawnLocation.getBlockY() + 2
                && !isFinished(player)
                && player.getGameMode() == GameMode.SURVIVAL
                && player.isOnGround())
        {
            addStat(player, StatType.SUCCESS);
            PlayerInfo.get(player).addScore(1);
        }
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {
        placedBlocks.forEach(loc -> loc.getBlock().setType(Material.AIR));
    }
}
