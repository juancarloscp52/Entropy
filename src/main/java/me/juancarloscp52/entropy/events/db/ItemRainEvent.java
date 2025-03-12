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
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemRainEvent extends AbstractTimedEvent {

    public static final EventType<ItemRainEvent> TYPE = EventType.builder(ItemRainEvent::new).category(EventCategory.RAIN).build();
    RandomSource random;

    @Override
    public void init() {
        random = RandomSource.create();
    }

    @Override
    public void tick() {
        if (getTickCount() % 15 == 0) {
            for (int i = 0; i < 5; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    Level world = serverPlayerEntity.level();
                    ItemEntity item = new ItemEntity(world, serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), new ItemStack(getRandomItem(world), 1));
                    serverPlayerEntity.level().addFreshEntity(item);
                });
            }
        }
        super.tick();
    }

    private Item getRandomItem(Level world) {
        Item item = BuiltInRegistries.ITEM.getRandom(RandomSource.create()).get().value();
        if (item.builtInRegistryHolder().is(ItemTags.DOES_NOT_RAIN)) {
            item = getRandomItem(world);
        }
        return item.requiredFeatures().isSubsetOf(world.enabledFeatures()) ? item : getRandomItem(world);
    }

    @Override
    public EventType<ItemRainEvent> getType() {
        return TYPE;
    }
}
