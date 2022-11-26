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
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;


public class RandomizeArmorEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity ->
        {
            serverPlayerEntity.getInventory().armor.set(3, getRandomItem(EquipmentSlot.HEAD));
            serverPlayerEntity.getInventory().armor.set(2, getRandomItem(EquipmentSlot.CHEST));
            serverPlayerEntity.getInventory().armor.set(1, getRandomItem(EquipmentSlot.LEGS));
            serverPlayerEntity.getInventory().armor.set(0, getRandomItem(EquipmentSlot.FEET));

        });

    }

    private ItemStack getRandomItem(EquipmentSlot slot){
        Random random = Random.create();
        Item item = Registry.ITEM.getRandom(random).get().value();
        if(item instanceof ArmorItem && ((ArmorItem)item).getSlotType()==slot){
            ItemStack stack = new ItemStack(item);
            for(int i=0;i< random.nextInt(4);i++){
                Enchantment enchantment = getRandomEnchantment(stack);
                stack.addEnchantment(enchantment,getRandomLevel(enchantment));
            }
            return stack;
        }
        else
            return getRandomItem(slot);
    }
    private Enchantment getRandomEnchantment(ItemStack item){
        Enchantment enchantment = Registry.ENCHANTMENT.get(Random.create().nextInt(Registry.ENCHANTMENT.getIds().size()));
        if(enchantment!=null && enchantment.isAcceptableItem(item))
            return enchantment;
        else
            return getRandomEnchantment(item);
    }
    private int getRandomLevel(Enchantment enchantment){
        return Random.create().nextInt(enchantment.getMaxLevel()-enchantment.getMinLevel()+1)+ enchantment.getMinLevel();
    }
}
