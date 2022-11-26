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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

public class GiveRandomOreEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            Random random = new Random();
            ItemStack stack = switch (random.nextInt(0, 8)) {
                case 1 -> new ItemStack(Items.IRON_INGOT, random.nextInt(2, 6));
                case 2 -> new ItemStack(Items.COPPER_INGOT, random.nextInt(3, 10));
                case 3 -> new ItemStack(Items.GOLD_INGOT, random.nextInt(1, 2));
                case 4 -> new ItemStack(Items.DIAMOND, random.nextInt(1, 2));
                case 5 -> new ItemStack(Items.REDSTONE, random.nextInt(6, 16));
                case 6 -> new ItemStack(Items.LAPIS_LAZULI, random.nextInt(6, 16));
                case 7 -> new ItemStack(Items.NETHERITE_SCRAP, random.nextInt(1, 3));
                default -> new ItemStack(Items.COAL, random.nextInt(4, 10));
            };

            if(!serverPlayerEntity.getInventory().insertStack(stack))
                serverPlayerEntity.dropStack(stack);

        });
    }
}
