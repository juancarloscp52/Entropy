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
import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class FixItemsEvent extends AbstractInstantEvent {

    public static final EventType<FixItemsEvent> TYPE = EventType.builder(FixItemsEvent::new).build();

    public void fixItem(ItemStack stack, ServerPlayer player){
        if(stack.is(ItemTags.UNFIXABLE))
            return;

        CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, stack, stack.getMaxDamage());
        stack.setDamageValue(0);
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getInventory().items.forEach(itemStack -> {
                if(itemStack.isDamageableItem()){
                    fixItem(itemStack,serverPlayerEntity);
                }
            });
            serverPlayerEntity.getInventory().armor.forEach(itemStack -> {
                if(itemStack.isDamageableItem()){
                    fixItem(itemStack,serverPlayerEntity);
                }
            });
            serverPlayerEntity.getInventory().offhand.forEach(itemStack -> {
                if(itemStack.isDamageableItem()){
                    fixItem(itemStack,serverPlayerEntity);
                }
            });
        });
    }

    @Override
    public EventType<FixItemsEvent> getType() {
        return TYPE;
    }
}
