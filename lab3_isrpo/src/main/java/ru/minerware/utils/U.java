package ru.minerware.utils;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class U {

    public static String color (String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void chat (Object player, String text)
    {
        ((Player)player).sendMessage(color(text));
    }

    public static void chat (Object player, String format, Object... args)
    {
        ((Player)player).sendMessage(color(String.format(format, args)));
    }

    public static void sendAction (String title, String subtitle)
    {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(U.color(title), U.color(subtitle)));
    }

    public static void broadcast (String msg)
    {
        Bukkit.broadcastMessage(color(msg));
    }

    public static void broadcast (String format, Object... args)
    {
        Bukkit.broadcastMessage(color(String.format(format, args)));
    }

    public static void clearPlayer(final Player player) {
        player.closeInventory();
        player.getVelocity().add(new Vector(0, 0, 0));
        player.setFallDistance(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setWalkSpeed(0.2f);
        player.setExp(0);
        player.setLevel(0);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.setFireTicks(0);
        player.setNoDamageTicks(0);

        if (!player.isDead()) {
            player.setMaxHealth(20.0);
            player.setHealth(20.0);
        }

        for (PotionEffect pe : player.getActivePotionEffects()) {
            player.removePotionEffect(pe.getType());
        }

        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setItemInHand(new ItemStack(Material.AIR));
    }

    public static ItemStack createEnchantedBook (Enchantment... enchantments)
    {
        ItemStack enchBook = new ItemStack(Material.ENCHANTED_BOOK);

        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta)enchBook.getItemMeta();

        for (Enchantment enchantment : enchantments) {
            storageMeta.addStoredEnchant(enchantment, 1, false);
        }

        enchBook.setItemMeta(storageMeta);

        return enchBook;
    }
}
