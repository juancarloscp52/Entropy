package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.Collections;

public class EnchantRandomGearEvent extends AbstractInstantEvent {

    private static ArrayList<Enchantment> _enchantments = new ArrayList<Enchantment>() {
        {
            for (var enchantment : Registries.ENCHANTMENT)
                add(enchantment);
        }
    };

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {

            var inventory = new ArrayList<ItemStack>();
            inventory.addAll(serverPlayerEntity.getInventory().main);
            inventory.addAll(serverPlayerEntity.getInventory().armor);
            inventory.addAll(serverPlayerEntity.getInventory().offHand);

            Collections.shuffle(inventory);
            Collections.shuffle(_enchantments);

            for (var itemStack : inventory) {
                for (var enchantment : _enchantments) {
                    if (Registries.ENCHANTMENT.getEntry(enchantment).isIn(EnchantmentTags.DO_NOT_ENCHANT_WITH))
                        continue;

                    if (enchantment.isAcceptableItem(itemStack)) {
                        var hasEnchantment = false;
                        var existingEnchantments = itemStack.getEnchantments();

                        for (var existingEnchantment : existingEnchantments.getEnchantments())
                            if (existingEnchantment.value() == enchantment || !existingEnchantment.value().canCombine(enchantment)) {
                                hasEnchantment = true;
                                break;
                            }

                        if (!hasEnchantment) {
                            itemStack.addEnchantment(enchantment,
                                    serverPlayerEntity.getRandom().nextInt(enchantment.getMaxLevel()) + 1);
                            return;
                        }
                    }
                }
            }
        });
    }
}
