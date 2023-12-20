package ru.minerware.utils.builders;

import java.util.*;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import ru.minerware.Core;
import ru.minerware.utils.U;

/**
 * NOTICE: This utility was developer as part of AeolusLib. While you can use it for your own projects, You are NOT allowed to delete or move this header comment.
 *
 * Utility:
 * Chainable {@link ItemStack}s
 *
 * Example Usage(s):
 * {@code ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).amount(1).durability(3).skullOwner("MCAeolus").name(ChatColor.RED+"MCAeolus's Skull").make())}
 * {@code ItemStack itemStack = new ItemBuilder().type(Material.BEDROCK).lores(new String[]{"Lore1",ChatColor.RED+"Lore2"}).enchantment(Enchantment.DAMAGE_ALL, 99).make()}
 *
 * @author MCAeolus
 * @version 1.0
 */

public class ItemBuilder implements Listener {

    private ItemStack item;
    private final ItemMeta itemM;
    private Consumer<PlayerInteractEvent> interactOnFinish;

    private static final Map<ItemStack, Consumer<PlayerInteractEvent>> ITEM_STACK_CONSUMER_MAP = new HashMap<>();

    static
    {
        Bukkit.getPluginManager().registerEvents(new ItemBuilder(Material.AIR), Core.getInstance());
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent event)
    {
        if (ITEM_STACK_CONSUMER_MAP.containsKey(event.getItem()))
            ITEM_STACK_CONSUMER_MAP.get(event.getItem()).accept(event);
    }

    /**
     * Init item chainable via given Material parameter.
     *
     * @param itemType
     *              the {@link Material} to initiate the instance with.
     *
     * @since 1.0
     */
    public ItemBuilder(final Material itemType){
        item = new ItemStack(itemType);
        itemM = item.getItemMeta();
    }

    /**
     * Init item chainable via given ItemStack parameter.
     *
     * @param itemStack
     *              the {@link ItemStack} to initialize the instance with.
     *
     * @since 1.0
     */
    public ItemBuilder(final ItemStack itemStack){
        item = itemStack;
        itemM = item.getItemMeta();
    }

    /**
     * Init the item chainable with no defined Material/ItemStack
     *
     * @since 1.0
     */
    private ItemBuilder(){
        itemM = null;
    }

    /**
     * Changes the Material type of the {@link ItemStack}
     *
     * @param material
     *              the new {@link Material} to set for the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder type(final Material material){
        make().setType(material);
        return this;
    }

    public ItemBuilder interact (Consumer<PlayerInteractEvent> consumer)
    {
        interactOnFinish = consumer;

        return this;
    }

    /**
     * Changes the {@link ItemStack}s size.
     *
     * @param itemAmt
     *              the new Integer count of the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder amount(final Integer itemAmt){
        make().setAmount(itemAmt);
        return this;
    }

    public ItemBuilder setGlowing ()
    {
//    	net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
//
//    	NBTTagCompound nbt = nmsStack.getTag() == null ? new NBTTagCompound() : nmsStack.getTag();
//
//    	NBTTagList nbtList = new NBTTagList();
//
//    	nbt.set("ench", nbtList);
//
//    	nmsStack.setTag(nbt);
//
//    	item = CraftItemStack.asBukkitCopy(nmsStack);

        meta().addEnchant(Enchantment.LUCK, 1, false);
        meta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return this;
    }

    public ItemBuilder setUnbreakable ()
    {
        meta().spigot().setUnbreakable(true);

        return this;
    }

    /**
     * Changes the {@link ItemStack}s display name.
     *
     * @param name
     *          the new String for the ItemStack's display name to be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder name(final String name){
        meta().setDisplayName(U.color(name));
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore
     *          String you want to add to the ItemStack's lore.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder lore(final String lore){
        List<String> lores = meta().getLore();
        if(lores == null){lores = new ArrayList<>();}
        lores.add(U.color(lore));
        meta().setLore(lores);
        make().setItemMeta(meta());
        return this;
    }

    public List<String> lores ()
    {
        return meta().getLore();
    }

    public ItemBuilder addLore (final String lore)
    {
        if (!meta().hasLore()) meta().setLore(new ArrayList<>());

        List<String> lores = meta().getLore();

        lores.add(U.color(lore));

        meta().setLore(lores);
        item.setItemMeta(meta());

        return this;
    }

    /**
     * Clears the {@link ItemStack}s lore and replaces it with the defined String array.
     *
     * @param lores
     *            String array you want to set the ItemStack's lore to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder lores(final String... lores){
        List<String> loresList = meta().getLore();
        if(loresList == null){loresList = new ArrayList<>();}
        else{loresList.clear();}
        Collections.addAll(loresList, lores);
        meta().setLore(loresList);
        return this;
    }

    public ItemBuilder lores (final ArrayList<String> lores)
    {
        meta().setLore(lores);
        return this;
    }

    /**
     * Changes the durability of the current {@link ItemStack}
     *
     * @param durability
     *              the new int amount to set the ItemStack's durability to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder durability(final int durability){
        make().setDurability((short) durability);
        return this;
    }

    /**
     * Changes the data value of the {@link ItemStack}
     *
     * @param data
     *          the new int data value (parsed as byte) to set the ItemStack's durability to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder data(final int data){
        make().setData(new MaterialData(make().getType(), (byte)data));
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@link ItemStack} with a defined level int value.
     *
     * @param enchantment
     *              the {@link Enchantment} to add to the ItemStack.
     *
     * @param level
     *          the int amount that the Enchantment's level will be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level){
//    	item.addUnsafeEnchantment(enchantment, level);
        itemM.addEnchant(enchantment, level, false);
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@Link} with a level int value of 1.
     *
     * @param enchantment
     *              the {@link Enchantment} to add to the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment){
//        make().addUnsafeEnchantment(enchantment, 1);
        itemM.addEnchant(enchantment, 1, false);
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack.
     *
     * @param enchantments
     *              the Enchantment array to replace any current enchantments applied on the ItemStack.
     *
     * @param level
     *              the int level value for all Enchantments to be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments, final int level){
        make().getEnchantments().clear();
        for(Enchantment enchantment : enchantments){
            make().addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack with a level int value of 1.
     *
     * @param enchantments
     *              the Enchantment array to replace any current enchantments applied on the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments){
        make().getEnchantments().clear();
        for(Enchantment enchantment : enchantments){
            make().addUnsafeEnchantment(enchantment, 1);
        }
        return this;
    }

    /**
     * Clears the defined {@link Enchantment} from the {@link ItemStack}
     *
     * @param enchantment
     *              the Enchantment to remove from the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantment(final Enchantment enchantment){
        Map<Enchantment, Integer> itemEnchantments = make().getEnchantments();
        for(Enchantment enchantmentC : itemEnchantments.keySet()){
            if(enchantment == enchantmentC){
                itemEnchantments.remove(enchantmentC);
            }
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantments(){
        make().getEnchantments().clear();
        return this;
    }

    /**
     * Clears the defined {@link String} of lore from the {@link ItemStack}
     *
     * @param lore
     *          the String to be removed from the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearLore(final String lore){
        if(meta().getLore().contains(lore)){
            meta().getLore().remove(lore);
        }
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Clears all lore {@link String}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearLores(){
        meta().getLore().clear();
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Sets the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @param color
     *          the Color to set the LEATHER_ARMOR ItemStack to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder color(final Color color){
        if(make().getType() == Material.LEATHER_HELMET
                || make().getType() == Material.LEATHER_CHESTPLATE
                || make().getType() == Material.LEATHER_LEGGINGS
                || make().getType() == Material.LEATHER_BOOTS ){
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(color);
            make().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clears the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearColor(){
        if(make().getType() == Material.LEATHER_HELMET
                || make().getType() == Material.LEATHER_CHESTPLATE
                || make().getType() == Material.LEATHER_LEGGINGS
                || make().getType() == Material.LEATHER_BOOTS ){
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(null);
            make().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Sets the skullOwner {@link SkullMeta} of the current SKULL_ITEM {@link Material} type {@link ItemStack}
     *
     * @param name
     *          the {@link String} value to set the SkullOwner meta to for the SKULL_ITEM Material type ItemStack.
     *
     * @return the current instance for chainable application
     * @since 1.0
     */
    public ItemBuilder skullOwner(final String name){
        if(make().getType() == Material.SKULL_ITEM && make().getDurability() == (byte) 3){
            SkullMeta skullMeta = (SkullMeta) meta();
            skullMeta.setOwner(name);
            make().setItemMeta(meta());
        }
        return this;
    }

    /**
     * Returns the {@link ItemMeta} of the {@link ItemStack}
     *
     * @return the ItemMeta of the ItemStack.
     */
    public ItemMeta meta(){
        return itemM;
    }

    /**
     * Returns the {@link ItemStack} of the {@link ItemBuilder} instance.
     *
     * @return the ItemStack of the ItemBuilder instance.
     */
    public ItemStack make(){

        item.setItemMeta(itemM);

        if (interactOnFinish != null)
        {
            ITEM_STACK_CONSUMER_MAP.put(item, interactOnFinish);
        }

        return item;
    }

}
