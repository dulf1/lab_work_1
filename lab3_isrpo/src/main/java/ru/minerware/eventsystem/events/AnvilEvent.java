package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;

import java.util.*;
import java.util.stream.Collectors;

public class AnvilEvent extends Event {

    private static final ItemStack ENCHANT_ITEM = new ItemStack(Material.DIAMOND_SWORD);

    private final Map<Player, Enchantment> playerEnchant = new HashMap<>();

    public AnvilEvent() {
        super("Ковальщик", "Скрестите алмазный меч с &dзачарованной книгой:", 7, false);
    }

    @Override
    public void onEventStart() {
        Random random = new Random();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Enchantment neededEnchant = getRandomEnchant();
            PlayerInventory inventory = player.getInventory();

            Bukkit.getScheduler()
                    .runTaskLater(Core.getInstance(), () -> U.chat(player, "&7 %s I", neededEnchant.getName()), 1L);

            playerEnchant.put(player, neededEnchant);

            for (int i = 0; i < 5 + random.nextInt(10); i++)
            {
                inventory.setItem(random.nextInt(30), U.createEnchantedBook(getRandomEnchant()));
            }

            inventory.setItem(random.nextInt(30), U.createEnchantedBook(neededEnchant));
            player.setItemInHand(ENCHANT_ITEM.clone());


        });

        Core.getInstance()
                .getConfigParser()
                .getInteractPoses()
                .forEach(pos -> pos.getBlock().setType(Material.ANVIL));
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {

        PlayerInfo.PLAYERS.stream().filter(info -> playerEnchant.containsKey(info.getPlayer())).forEach(this::looseEnd);
        playerEnchant.clear();
    }

    public Enchantment getRandomEnchant ()
    {
        List<Enchantment> enchants = Arrays.stream(Enchantment.values())
                .filter(ench -> ench.canEnchantItem(ENCHANT_ITEM))
                .collect(Collectors.toList());

        Collections.shuffle(enchants);

        return enchants.get(0);
    }

    @EventHandler
    public void onAnvil (InventoryClickEvent event)
    {
        PlayerInfo info = PlayerInfo.get((Player) event.getWhoClicked());
        Inventory clickedInventory = event.getClickedInventory();
        Player player = info.getPlayer();
        Enchantment neededEnchant = playerEnchant.get(player);

        if (clickedInventory.getType() != InventoryType.ANVIL || neededEnchant == null)
            return;

        AnvilInventory anvil = (AnvilInventory) clickedInventory;

        if (event.getSlot() == 2 && anvil.getItem(2).getEnchantments().containsKey(neededEnchant))
        {
            playerEnchant.remove(player);
            successEnd(info);
            info.addScore(2);
        }
    }
}
