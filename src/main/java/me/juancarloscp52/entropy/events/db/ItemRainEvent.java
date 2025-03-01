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
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

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
    public String type() {
        return "rain";
    }

    @Override
    public void tick() {
        if (getTickCount() % 15 == 0) {
            for (int i = 0; i < 5; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    World world = serverPlayerEntity.getWorld();
                    ItemEntity item = new ItemEntity(world, serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), new ItemStack(getRandomItem(world), 1));
                    serverPlayerEntity.getWorld().spawnEntity(item);
                });
            }
        }
        super.tick();
    }

    private Item getRandomItem(World world) {
        Item item = Registries.ITEM.getRandom(Random.create()).get().value();
        if (item.getRegistryEntry().isIn(ItemTags.DOES_NOT_RAIN)) {
            item = getRandomItem(world);
        }
        return item.getRequiredFeatures().isSubsetOf(world.getEnabledFeatures()) ? item : getRandomItem(world);
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
