package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ShearSheepEvent extends Event {
               // UUID Игрока, кол-во подстриженных овец
    private final Map<UUID, Integer> playerSheers = new HashMap<>();

    public ShearSheepEvent() {
        super("&f&lСтрижка", "Подстреги больше всех овец", 15, false);
    }

    @Override
    public void onEventStart() {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int maxPlayers = Core.getInstance().getConfigParser().getMaxPlayers();
        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn();
        World world = Core.getInstance().getWorld();

        for (int i = 0; i < 20 * maxPlayers; i++)
        {
            int sheepX = spawnLocation.getBlockX() + random.nextInt(-10, 10);
            int sheepZ = spawnLocation.getBlockZ() + random.nextInt(-10, 10);

            world.spawnEntity(new Location(world, sheepX, spawnLocation.getBlockY(), sheepZ), EntityType.SHEEP);
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().addItem(new ItemStack(Material.SHEARS)));
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {

        Core.getInstance().getWorld().getLivingEntities()
                .stream()
                .filter(e -> e.getType() == EntityType.SHEEP)
                .forEach(Entity::remove);

        Map.Entry<UUID, Integer> winnerStat = playerSheers.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
        PlayerInfo winner;

        if (winnerStat == null)
        {
            U.broadcast("Победил %s, он постриг %d!");
        }
        else
        {
            winner = PlayerInfo.get(Bukkit.getPlayer(winnerStat.getKey()));

            U.broadcast("Победил %s, он постриг %d!",
                    winner.getPlayer().getName(), winnerStat.getValue());

            successEnd(winner);
            winner.addScore(1);
        }

        PlayerInfo.PLAYERS.stream().filter(info -> !isFinished(info)).forEach(this::looseEnd);
        playerSheers.clear();
    }

    @EventHandler
    public void onSheer (PlayerShearEntityEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        playerSheers.put(uuid, playerSheers.getOrDefault(uuid, 0) + 1);

        U.chat(player, "Вы &f&lпостригли &eовечку");
    }
}
