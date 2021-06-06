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
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class StarterPackEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
            ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
            if(!serverPlayerEntity.getInventory().insertStack(pickaxe))
                serverPlayerEntity.dropStack(pickaxe);
            ItemStack axe = new ItemStack(Items.IRON_AXE);
            if(!serverPlayerEntity.getInventory().insertStack(axe))
                serverPlayerEntity.dropStack(axe);
            ItemStack shovel = new ItemStack(Items.IRON_SHOVEL);
            if(!serverPlayerEntity.getInventory().insertStack(shovel))
                serverPlayerEntity.dropStack(shovel);
            ItemStack sword = new ItemStack(Items.IRON_SWORD);
            if(!serverPlayerEntity.getInventory().insertStack(sword))
                serverPlayerEntity.dropStack(sword);
            ItemStack bread = new ItemStack(Items.BREAD,10);
            if(!serverPlayerEntity.getInventory().insertStack(bread))
                serverPlayerEntity.dropStack(bread);
            ItemStack torch = new ItemStack(Items.TORCH,10);
            if(!serverPlayerEntity.getInventory().insertStack(torch))
                serverPlayerEntity.dropStack(torch);
        });
    }
}
