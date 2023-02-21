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
import me.juancarloscp52.entropy.EntropyTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;


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
            if(!item.getRequiredFeatures().isSubsetOf(serverPlayerEntity.world.getEnabledFeatures()))
                return getRandomItem(serverPlayerEntity, slot);
            ItemStack stack = new ItemStack(item);
            for(int i=0;i< random.nextInt(4);i++){
                Enchantment enchantment = getRandomEnchantment(stack);
                stack.addEnchantment(enchantment,getRandomLevel(enchantment));
            }
            return stack;
        }
        else
            return getRandomItem(serverPlayerEntity, slot);
    }
    private Enchantment getRandomEnchantment(ItemStack item){
        Enchantment enchantment = Registries.ENCHANTMENT.get(Random.create().nextInt(Registries.ENCHANTMENT.getIds().size()));
        if(enchantment!=null && enchantment.isAcceptableItem(item) && !Registries.ENCHANTMENT.getEntry(enchantment).isIn(EntropyTags.DO_NOT_ENCHANT_WITH))
            return enchantment;
        else
            return getRandomEnchantment(item);
    }
    private int getRandomLevel(Enchantment enchantment){
        return Random.create().nextInt(enchantment.getMaxLevel()-enchantment.getMinLevel()+1)+ enchantment.getMinLevel();
    }
}
