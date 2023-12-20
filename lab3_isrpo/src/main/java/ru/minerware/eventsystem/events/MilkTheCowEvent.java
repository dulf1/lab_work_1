package ru.minerware.eventsystem.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;

import java.util.concurrent.ThreadLocalRandom;

public class MilkTheCowEvent extends Event {

    public MilkTheCowEvent() {
        super("Доилка", "Подои корову", 4, false);
    }

    @Override
    public void onEventStart() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int maxPlayers = Core.getInstance().getConfigParser().getMaxPlayers();
        Location spawnLocation = Core.getInstance().getConfigParser().getPlayRespawn();
        World world = Core.getInstance().getWorld();

        for (Location interactPos : Core.getInstance().getConfigParser().getInteractPoses()) {
            Block block = interactPos.getBlock();
            block.setType(Material.CHEST);

            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getInventory();

            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, new ItemStack(Material.BUCKET));
        }

        for (int i = 0; i < 20 * maxPlayers; i++)
        {
            int cowX = spawnLocation.getBlockX() + random.nextInt(-10, 10);
            int cowZ = spawnLocation.getBlockZ() + random.nextInt(-10, 10);

            world.spawnEntity(new Location(world, cowX, spawnLocation.getBlockY(), cowZ), EntityType.COW);
        }
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {

        Core.getInstance().getWorld().getLivingEntities()
                .stream()
                .filter(e -> e.getType() == EntityType.COW)
                .forEach(Entity::remove);

        // Фикс чтобы вещи из сундука не выпадали после уничтожения сундука
        for (Location interactPos : Core.getInstance().getConfigParser().getInteractPoses()) {
            Block block = interactPos.getBlock();
            block.setType(Material.CHEST);

            Chest chest = (Chest) block.getState();
            Inventory inventory = chest.getInventory();

            inventory.clear();
        }
    }

    @EventHandler
    public void onMilk (PlayerInteractAtEntityEvent event)
    {
        if (event.getRightClicked() instanceof Cow)
        {
            Player player = event.getPlayer();
            PlayerInfo info = PlayerInfo.get(player);

            if (player.getItemInHand().getType() != Material.BUCKET || isFinished(player))
                return;

            successEnd(info);
            info.addScore(1);
            player.getInventory().clear();

            event.setCancelled(true);
        }
    }
}
