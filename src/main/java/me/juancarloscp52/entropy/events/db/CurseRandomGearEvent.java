package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Collections;

public class CurseRandomGearEvent extends AbstractInstantEvent {

    private static ArrayList<RegistryKey<Enchantment>> _curses = new ArrayList<>() {
        {
            add(Enchantments.BINDING_CURSE);
            add(Enchantments.VANISHING_CURSE);
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
            Collections.shuffle(_curses);

            for (var itemStack : inventory) {
                if(itemStack.isIn(ItemTags.DO_NOT_CURSE))
                    continue;

                final Registry<Enchantment> enchantments = serverPlayerEntity.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
                for (var curseKey : _curses) {
                    final RegistryEntry<Enchantment> curse = enchantments.getEntry(curseKey).get();
                    if (curse.value().isAcceptableItem(itemStack)) {
                        var hasCurse = false;
                        var existingEnchantments = itemStack.getEnchantments();

                        for (var enchantment : existingEnchantments.getEnchantments())
                            if (enchantment == curse) {
                                hasCurse = true;
                                break;
                            }

                        if (!hasCurse) {
                            itemStack.addEnchantment(curse, curse.value().getMaxLevel());
                            return;
                        }
                    }
                }
            }
        });
    }
}
