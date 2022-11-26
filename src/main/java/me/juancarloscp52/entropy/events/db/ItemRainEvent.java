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
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;


public class ItemRainEvent extends AbstractTimedEvent {

    Random random;

    @Override
    public void init() {
        random = Random.create();
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public String type() {
        return "rain";
    }

    @Override
    public void tick() {
        if (getTickCount() % 15 == 0) {
            for (int i = 0; i < 5; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    ItemEntity item = new ItemEntity(serverPlayerEntity.getWorld(), serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), new ItemStack(getRandomItem(), 1));
                    serverPlayerEntity.getWorld().spawnEntity(item);
                });
            }
        }
        super.tick();
    }

    private Item getRandomItem() {
        Item item = Registry.ITEM.getRandom(Random.create()).get().value();
        if (item.toString().equals("debug_stick") || item.toString().contains("spawn_egg") || item.toString().contains("command_block") || item.toString().contains("structure_void") || item.toString().contains("barrier")) {
            item = getRandomItem();
        }
        return item;
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
