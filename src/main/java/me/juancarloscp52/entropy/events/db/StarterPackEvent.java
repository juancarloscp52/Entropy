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
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StarterPackEvent extends AbstractInstantEvent {
    public static final EventType<StarterPackEvent> TYPE = EventType.builder(StarterPackEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerLevel level = serverPlayerEntity.serverLevel();
            ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
            if(!serverPlayerEntity.getInventory().add(pickaxe))
                serverPlayerEntity.spawnAtLocation(level, pickaxe);
            ItemStack axe = new ItemStack(Items.IRON_AXE);
            if(!serverPlayerEntity.getInventory().add(axe))
                serverPlayerEntity.spawnAtLocation(level, axe);
            ItemStack shovel = new ItemStack(Items.IRON_SHOVEL);
            if(!serverPlayerEntity.getInventory().add(shovel))
                serverPlayerEntity.spawnAtLocation(level, shovel);
            ItemStack sword = new ItemStack(Items.IRON_SWORD);
            if(!serverPlayerEntity.getInventory().add(sword))
                serverPlayerEntity.spawnAtLocation(level, sword);
            ItemStack bread = new ItemStack(Items.BREAD,10);
            if(!serverPlayerEntity.getInventory().add(bread))
                serverPlayerEntity.spawnAtLocation(level, bread);
            ItemStack torch = new ItemStack(Items.TORCH,10);
            if(!serverPlayerEntity.getInventory().add(torch))
                serverPlayerEntity.spawnAtLocation(level, torch);
        });
    }

    @Override
    public EventType<StarterPackEvent> getType() {
        return TYPE;
    }
}
