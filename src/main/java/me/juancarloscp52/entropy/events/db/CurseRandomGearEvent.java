package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class CurseRandomGearEvent extends AbstractInstantEvent {

    private static ArrayList<Enchantment> _curses = new ArrayList<Enchantment>() {
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
                if(itemStack.isIn(EntropyTags.DO_NOT_CURSE))
                    continue;

                for (var curse : _curses) {
                    if (curse.isAcceptableItem(itemStack)) {
                        var hasCurse = false;
                        var existingEnchantments = EnchantmentHelper.get(itemStack);

                        for (var enchantment : existingEnchantments.keySet())
                            if (enchantment == curse) {
                                hasCurse = true;
                                break;
                            }

                        if (!hasCurse) {
                            itemStack.addEnchantment(curse, curse.getMaxLevel());
                            return;
                        }
                    }
                }
            }
        });
    }
}
