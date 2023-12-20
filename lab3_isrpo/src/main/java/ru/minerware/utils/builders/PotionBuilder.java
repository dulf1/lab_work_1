package ru.minerware.utils.builders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionBuilder {

    private final ItemStack potion;
    private final PotionMeta meta;

    public PotionBuilder ()
    {
        this(1);
    }

    public PotionBuilder (int amount)
    {
        potion = new ItemStack(Material.POTION, amount, (short) 8193);
        meta = (PotionMeta) potion.getItemMeta();
    }

    public PotionBuilder name (String name)
    {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public PotionBuilder addEffect (PotionEffect effect, boolean overwrite)
    {
        meta.addCustomEffect(effect, overwrite);
        return this;
    }

    public PotionBuilder setMainEffect (PotionEffectType effect)
    {
        meta.setMainEffect(effect);
        return this;
    }

    public PotionBuilder removeCustomEffect(PotionEffectType effectType)
    {
        meta.removeCustomEffect(effectType);

        return this;
    }

    public ItemStack make()
    {
        potion.setItemMeta(meta);
        return potion;
    }
}
