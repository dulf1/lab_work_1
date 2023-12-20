package ru.minerware.eventsystem.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.minerware.Core;
import ru.minerware.data.PlayerInfo;
import ru.minerware.eventsystem.Event;
import ru.minerware.utils.U;
import ru.minerware.utils.builders.PotionBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PotionEvent extends Event {

    private final Map<Player, PotionEffectType> playerPotion = new HashMap<>();

    public PotionEvent() {
        super("&eЗельевар", "Выпей &eзелье &7 с эффектом:", 12, false);
    }

    @Override
    public void onEventStart() {
        Random random = new Random();

        Bukkit.getOnlinePlayers().forEach(player -> {
            PotionEffectType neededPotion = getRandomEffect(random);
            PlayerInventory inventory = player.getInventory();

            Bukkit.getScheduler()
                    .runTaskLater(Core.getInstance(), () -> U.chat(player, "&7 %s", neededPotion.getName()), 1L);

            playerPotion.put(player, neededPotion);

            for (int i = 0; i < 5 + random.nextInt(10); i++)
            {
                PotionEffect randomEffect = new PotionEffect(getRandomEffect(random), 1, 0);

                inventory.setItem(
                        random.nextInt(30),
                        new PotionBuilder()
                                .addEffect(randomEffect, true)
                                .setMainEffect(randomEffect.getType())
                                .make()
                );
            }

            inventory.setItem(
                    random.nextInt(30),
                    new PotionBuilder()
                            .addEffect(new PotionEffect(neededPotion, 1, 0), true)
                            .setMainEffect(neededPotion)
                            .make()
            );


        });
    }

    @Override
    public void onEventUpdate() {

    }

    @Override
    public void onEventEnd() {
        playerPotion.clear();
    }

    private PotionEffectType getRandomEffect (Random random)
    {
        PotionEffectType[] potionArray = PotionEffectType.values();

        PotionEffectType randomEffect;

        // По какой то причине в листе эффектов есть нулевой, по этому я луплю до бесконечности чтобы найти нормальный
        while ( (randomEffect = potionArray[random.nextInt(potionArray.length)]) == null) {}

        return randomEffect;
    }

    @EventHandler
    public void onPotionDrink (PlayerItemConsumeEvent event)
    {
        ItemStack stack = event.getItem();
        Player player = event.getPlayer();
        PotionEffectType neededPotionType = playerPotion.get(player);

        if (stack.getType() != Material.POTION || isFinished(player) || event.isCancelled())
            return;

        PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();

        boolean isDrinkedNeededPotion = potionMeta
                .getCustomEffects()
                .stream()
                .anyMatch(potion -> potion.getType() == neededPotionType);

        addStat(player, isDrinkedNeededPotion ? StatType.SUCCESS : StatType.LOOSE);

        if (isDrinkedNeededPotion)
            PlayerInfo.get(player).addScore(1);
    }
}
