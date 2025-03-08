package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import java.util.ArrayList;
import java.util.Collections;

public class CurseRandomGearEvent extends AbstractInstantEvent {

    private static ArrayList<ResourceKey<Enchantment>> _curses = new ArrayList<>() {
        {
            add(Enchantments.BINDING_CURSE);
            add(Enchantments.VANISHING_CURSE);
        }
    };

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {

            var inventory = new ArrayList<ItemStack>();
            inventory.addAll(serverPlayerEntity.getInventory().items);
            inventory.addAll(serverPlayerEntity.getInventory().armor);
            inventory.addAll(serverPlayerEntity.getInventory().offhand);

            Collections.shuffle(inventory);
            Collections.shuffle(_curses);

            for (var itemStack : inventory) {
                if(itemStack.is(ItemTags.DO_NOT_CURSE))
                    continue;

                final Registry<Enchantment> enchantments = serverPlayerEntity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                for (var curseKey : _curses) {
                    final Holder<Enchantment> curse = enchantments.get(curseKey).get();
                    if (curse.value().canEnchant(itemStack)) {
                        var hasCurse = false;
                        var existingEnchantments = itemStack.getEnchantments();

                        for (var enchantment : existingEnchantments.keySet())
                            if (enchantment == curse) {
                                hasCurse = true;
                                break;
                            }

                        if (!hasCurse) {
                            itemStack.enchant(curse, curse.value().getMaxLevel());
                            return;
                        }
                    }
                }
            }
        });
    }
}
