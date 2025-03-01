package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantRandomGearEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {

            var inventory = new ArrayList<ItemStack>();
            inventory.addAll(serverPlayerEntity.getInventory().main);
            inventory.addAll(serverPlayerEntity.getInventory().armor);
            inventory.addAll(serverPlayerEntity.getInventory().offHand);

            Collections.shuffle(inventory);

            final Registry<Enchantment> enchantmentsRegistry = serverPlayerEntity.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            final List<RegistryEntry.Reference<Enchantment>> enchantments = enchantmentsRegistry.streamEntries().collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(enchantments);

            for (var itemStack : inventory) {
                for (var enchantment : enchantments) {
                    if (enchantment.isIn(EnchantmentTags.DO_NOT_ENCHANT_WITH))
                        continue;

                    if (enchantment.value().isAcceptableItem(itemStack)) {
                        var hasEnchantment = false;
                        var existingEnchantments = itemStack.getEnchantments();

                        for (var existingEnchantment : existingEnchantments.getEnchantments())
                            if (existingEnchantment == enchantment || !Enchantment.canBeCombined(existingEnchantment, enchantment)) {
                                hasEnchantment = true;
                                break;
                            }

                        if (!hasEnchantment) {
                            itemStack.addEnchantment(enchantment,
                                    serverPlayerEntity.getRandom().nextInt(enchantment.value().getMaxLevel()) + 1);
                            return;
                        }
                    }
                }
            }
        });
    }
}
