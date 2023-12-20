package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;

import java.util.concurrent.ThreadLocalRandom;

public class FloatingProblemsEvent extends Event {

    private BukkitTask task;

    public FloatingProblemsEvent() {
        super("&fЛетящие &cпроблемы", "Уклоняйтесь от летящий сверху стрел", 12, false);
    }

    @Override
    public void onEventStart() {

        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn();
        World world = Core.getInstance().getWorld();

        task = Bukkit.getScheduler().runTaskTimer(Core.getInstance(), () -> {

            int arrowX = spawnLocation.getBlockX() + ThreadLocalRandom.current().nextInt(-10, 10);
            int arrowY = spawnLocation.getBlockY() + 10;
            int arrowZ = spawnLocation.getBlockZ() + ThreadLocalRandom.current().nextInt(-10, 10);

            Core.getInstance().getWorld().spawnArrow(
                    new Location(world, arrowX, arrowY, arrowZ),
                    new Vector(0, -0.2, 0),
                    0,
                    0
            );

        }, 1L, 1L);

    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {
        Bukkit.getScheduler().cancelTask(task.getTaskId());

        PlayerInfo.PLAYERS
                .stream()
                .filter(info -> info.getPlayer().getGameMode() == GameMode.SURVIVAL)
                .forEach(info -> {
                    info.addScore(1);
                    successEnd(info);
                });

        PlayerInfo.PLAYERS
                .stream()
                .filter(info -> info.getPlayer().getGameMode() == GameMode.SPECTATOR)
                .forEach(this::looseEnd);
    }

    @EventHandler
    public void onPlayerSniped (EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile)
        {
            Player player = (Player) event.getEntity();

            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(U.color("В вас &cпопали &fс лука &c:("), "");
        }
    }
}
