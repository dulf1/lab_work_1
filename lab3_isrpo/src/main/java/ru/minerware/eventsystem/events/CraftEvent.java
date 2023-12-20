package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CraftEvent extends Event {

    private Player bestCrafter;
    private ShapedRecipe recipe;

    public CraftEvent() {
        super("Лучший крафтер", "Успей первее всех скрафтить NONE", 7, false);
    }

    @Override
    public void onEventStart() {

        Random random = new Random();

        for (Location pos : Core.getInstance().getConfigParser().getInteractPoses()) {
            pos.getBlock().setType(Material.WORKBENCH);
        }

        recipe = getRandomRecipe();

        setHelp("Успей первее всех скрафтить " + recipe.getResult().getType());

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerInventory inv = player.getInventory();

            for (ItemStack ingredient : recipe.getIngredientMap().values())
            {
                if (ingredient == null)
                    continue;

                ingredient.setAmount(ingredient.getAmount() + random.nextInt(10));

                inv.setItem(random.nextInt(30), ingredient);
            }

            for (int i = 0; i < random.nextInt(10); i++)
                inv.setItem(random.nextInt(30), new ItemStack(Material.getMaterial(random.nextInt(100)), 3));
        }
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {

        if (bestCrafter == null)
        {
            U.broadcast("Никто не успел скрафтить за отведенное время %s",
                    recipe.getResult().getType().toString()
            );
            return;
        }

        U.broadcast("Лучшим крафтером оказался %s",
                bestCrafter.getName()
        );

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            PlayerInfo playerInfo = PlayerInfo.get(onlinePlayer);

            if (onlinePlayer == bestCrafter)
                successEnd(playerInfo);
            else
                looseEnd(playerInfo);
        }

        bestCrafter = null;
    }

    @EventHandler
    public void onCraft (CraftItemEvent event)
    {
        if (event.getRecipe().getResult().equals(recipe.getResult())
                && bestCrafter == null
                && event.getResult() == org.bukkit.event.Event.Result.ALLOW)
        {
            bestCrafter = (Player) event.getWhoClicked();

            PlayerInfo.get(bestCrafter).addScore(1);
        }
    }

    private ShapedRecipe getRandomRecipe ()
    {

        while (true)
        {
            ItemStack stack = new ItemStack(Material.getMaterial(ThreadLocalRandom.current().nextInt(15, 100)));
            List<Recipe> recipe = Bukkit.getRecipesFor(stack);

            if (recipe != null && recipe.size() > 0 && recipe.get(0) instanceof ShapedRecipe)
                return (ShapedRecipe) recipe.get(0);
        }
    }
}
