package ru.minerware.utils.builders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.minerware.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class InventoryBuilder implements Listener {

    private static final ConcurrentLinkedQueue<InventoryInfo> INVENTORY_UPDATE_MAP = new ConcurrentLinkedQueue<>();

    static
    {
        Bukkit.getPluginManager().registerEvents(new InventoryBuilder(), Core.getInstance());

        Bukkit.getScheduler().runTaskTimer(Core.getInstance(), () ->
        {
            INVENTORY_UPDATE_MAP.forEach(info -> info.getUpdate().accept(info.getInventory()));
        }, 1L, 1L);
    }

    private boolean isSimilarInventory(final Inventory one, final Inventory other_inv) {
        return other_inv.getTitle().equals(one.getTitle()) && other_inv.getContents().length == one.getContents().length && other_inv.getSize() == one.getSize() && other_inv.getType() == one.getType();
    }

    @EventHandler
    public void onOpen (InventoryOpenEvent event) {

        INVENTORY_UPDATE_MAP.forEach(info ->
        {
            if (!isSimilarInventory(event.getInventory(), info.getInventory()))
                return;

            if (!event.getPlayer().getUniqueId().equals(info.getPlayer().getUniqueId()))
                return;

            info.open.accept(event);
        });
    }

    @EventHandler
    public void onClick (InventoryClickEvent event)
    {
        INVENTORY_UPDATE_MAP.forEach(info ->
        {
            if (!isSimilarInventory(event.getView().getTopInventory(), info.getInventory()))
                return;

            if (!event.getWhoClicked().getUniqueId().equals(info.getPlayer().getUniqueId()))
                return;

            if (info.isOnlyRead()) event.setCancelled(true);

            info.getClicks().forEach((slot, consumer) ->
            {
                if (event.getSlot() != slot) return;

                consumer.accept(event);
            });

            info.click.accept(event);
        });
    }

    @EventHandler
    public void onClose (InventoryCloseEvent event)
    {
        INVENTORY_UPDATE_MAP.forEach(info ->
        {
            if (!isSimilarInventory(event.getInventory(), info.getInventory()))
                return;

            if (!event.getPlayer().getUniqueId().equals(info.getPlayer().getUniqueId()))
                return;

            INVENTORY_UPDATE_MAP.remove(info);
            info.close.accept(event);
        });
    }

    private Player player;
    private String title;
    private int size;

    private Inventory inventory;
    private InventoryInfo info;

    private InventoryBuilder ()
    {

    }

    public InventoryBuilder (Player player, Inventory inventory)
    {
        this.inventory = inventory;
        this.player = player;

        info = new InventoryInfo();
    }

    public InventoryBuilder (Player player, String title, int size)
    {
        this.player = player;
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.size = size;

        this.inventory = Bukkit.createInventory(null, size, this.title);

        info = new InventoryInfo();
    }

    public InventoryBuilder setItem (int slot, ItemStack stack)
    {
        inventory.setItem(slot, stack);

        return this;
    }

    public InventoryBuilder open (Consumer<InventoryOpenEvent> consumer)
    {
        info.setOpen(consumer);
        return this;
    }

    public InventoryBuilder click (Consumer<InventoryClickEvent> consumer)
    {
        info.setClick(consumer);
        return this;
    }

    public InventoryBuilder click (int slot, Consumer<InventoryClickEvent> consumer)
    {
        info.addClick(slot, consumer);
        return this;
    }

    public InventoryBuilder close (Consumer<InventoryCloseEvent> consumer)
    {
        info.setClose(consumer);
        return this;
    }

    public InventoryBuilder update (Consumer<Inventory> consumer)
    {
        info.setUpdate(consumer);
        return this;
    }

    public Inventory make ()
    {
        info.setPlayer(player);
        info.setInventory(inventory);

        INVENTORY_UPDATE_MAP.add(info);

        return inventory;
    }

    public InventoryBuilder setOnlyRead (boolean onlyRead) {
        info.setOnlyRead(onlyRead);
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public class InventoryInfo
    {
        private Player player;

        private Inventory inventory;

        private Consumer<InventoryOpenEvent> open = event -> {};
        private Consumer<Inventory> update = event -> {};
        private Consumer<InventoryClickEvent> click = event -> {};
        private Consumer<InventoryCloseEvent> close = event -> {};

        private Map<Integer, Consumer<InventoryClickEvent>> clicks = new HashMap<>();

        private boolean onlyRead = false;

        public void addClick (int slot, Consumer<InventoryClickEvent> consumer)
        {
            clicks.put(slot, consumer);
        }

        public void setOpen(Consumer<InventoryOpenEvent> open) {
            this.open = open;
        }

        public Consumer<Inventory> getUpdate() {
            return update;
        }

        public void setUpdate(Consumer<Inventory> update) {
            this.update = update;
        }

        public void setClick(Consumer<InventoryClickEvent> click) {
            this.click = click;
        }

        public void setClose(Consumer<InventoryCloseEvent> close) {
            this.close = close;
        }

        public boolean isOnlyRead() {
            return onlyRead;
        }

        public void setOnlyRead(boolean onlyRead) {
            this.onlyRead = onlyRead;
        }

        public Map<Integer, Consumer<InventoryClickEvent>> getClicks() {
            return clicks;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }
}
