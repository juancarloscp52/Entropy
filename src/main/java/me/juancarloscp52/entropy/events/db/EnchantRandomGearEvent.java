package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantRandomGearEvent extends AbstractInstantEvent {
    public static final EventType<EnchantRandomGearEvent> TYPE = EventType.builder(EnchantRandomGearEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {

            var inventory = new ArrayList<ItemStack>();
            serverPlayerEntity.getInventory().forEach(inventory::add);

            Collections.shuffle(inventory);

            final Registry<Enchantment> enchantmentsRegistry = serverPlayerEntity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            final List<Holder.Reference<Enchantment>> enchantments = enchantmentsRegistry.listElements().collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(enchantments);

            for (var itemStack : inventory) {
                for (var enchantment : enchantments) {
                    if (enchantment.is(EnchantmentTags.DO_NOT_ENCHANT_WITH))
                        continue;

                    if (enchantment.value().canEnchant(itemStack)) {
                        var hasEnchantment = false;
                        var existingEnchantments = itemStack.getEnchantments();

                        for (var existingEnchantment : existingEnchantments.keySet())
                            if (existingEnchantment == enchantment || !Enchantment.areCompatible(existingEnchantment, enchantment)) {
                                hasEnchantment = true;
                                break;
                            }

                        if (!hasEnchantment) {
                            itemStack.enchant(enchantment,
                                    serverPlayerEntity.getRandom().nextInt(enchantment.value().getMaxLevel()) + 1);
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public EventType<EnchantRandomGearEvent> getType() {
        return TYPE;
    }
}
