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
import net.minecraft.util.Mth;

public class DamageItemsEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getInventory().items.forEach(itemStack -> {
                if(itemStack.isDamageableItem() && !itemStack.is(ItemTags.DO_NOT_DAMAGE)){
                    itemStack.hurtAndBreak(Mth.ceil((itemStack.getMaxDamage()-itemStack.getDamageValue())*serverPlayerEntity.getRandom().nextFloat()), serverPlayerEntity.serverLevel(), serverPlayerEntity, item -> {});
                }
            });
            serverPlayerEntity.getInventory().armor.forEach(itemStack -> {
                if(itemStack.isDamageableItem() && !itemStack.is(ItemTags.DO_NOT_DAMAGE)){
                    itemStack.hurtAndBreak(Mth.ceil((itemStack.getMaxDamage()-itemStack.getDamageValue())*serverPlayerEntity.getRandom().nextFloat()), serverPlayerEntity.serverLevel(), serverPlayerEntity, item -> {});
                }
            });
            serverPlayerEntity.getInventory().offhand.forEach(itemStack -> {
                if(itemStack.isDamageableItem() && !itemStack.is(ItemTags.DO_NOT_DAMAGE)){
                    itemStack.hurtAndBreak(Mth.ceil((itemStack.getMaxDamage()-itemStack.getDamageValue())*serverPlayerEntity.getRandom().nextFloat()), serverPlayerEntity.serverLevel(), serverPlayerEntity, item -> {});
                }
            });
        });
    }
}
