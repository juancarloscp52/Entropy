/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;

import java.util.Optional;


public class RandomizeArmorEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity ->
        {
            serverPlayerEntity.getInventory().armor.set(3, getRandomItem(serverPlayerEntity, EquipmentSlot.HEAD));
            serverPlayerEntity.getInventory().armor.set(2, getRandomItem(serverPlayerEntity, EquipmentSlot.CHEST));
            serverPlayerEntity.getInventory().armor.set(1, getRandomItem(serverPlayerEntity, EquipmentSlot.LEGS));
            serverPlayerEntity.getInventory().armor.set(0, getRandomItem(serverPlayerEntity, EquipmentSlot.FEET));

        });

    }

    private ItemStack getRandomItem(ServerPlayerEntity serverPlayerEntity, EquipmentSlot slot){
        Random random = Random.create();
        Item item = Registries.ITEM.getRandom(random).get().value();
        if(item instanceof ArmorItem && ((ArmorItem)item).getSlotType()==slot){
            if(!item.getRequiredFeatures().isSubsetOf(serverPlayerEntity.getWorld().getEnabledFeatures()))
                return getRandomItem(serverPlayerEntity, slot);
            ItemStack stack = new ItemStack(item);
            for(int i=0;i< random.nextInt(4);i++){
                RegistryEntry<Enchantment> enchantment = getRandomEnchantment(serverPlayerEntity, stack);
                stack.addEnchantment(enchantment, getRandomLevel(enchantment.value()));
            }
            return stack;
        }
        else
            return getRandomItem(serverPlayerEntity, slot);
    }
    private RegistryEntry<Enchantment> getRandomEnchantment(ServerPlayerEntity player, ItemStack item) {
        final DynamicRegistryManager registries = player.getRegistryManager();
        Optional<RegistryEntry.Reference<Enchantment>> enchantment = registries.get(RegistryKeys.ENCHANTMENT).getRandom(player.getRandom());
        if (enchantment.isPresent() && enchantment.get().value().isAcceptableItem(item) && !enchantment.get().isIn(EnchantmentTags.DO_NOT_ENCHANT_WITH))
            return enchantment.get();
        else
            return getRandomEnchantment(player, item);
    }
    private int getRandomLevel(Enchantment enchantment){
        return Random.create().nextInt(enchantment.getMaxLevel()-enchantment.getMinLevel()+1)+ enchantment.getMinLevel();
    }
}
