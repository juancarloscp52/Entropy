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
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public class ArrowRainEvent extends AbstractTimedEvent {

    public static final EventType<ArrowRainEvent> TYPE = EventType.builder(ArrowRainEvent::new).category(EventCategory.RAIN).build();
    Random random;

    @Override
    public void init() {
        random = new Random();
    }

    @Override
    public void tick() {
        if (getTickCount() % 10 == 0) {
            for (int i = 0; i < 10; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    Arrow arrow = new Arrow(serverPlayerEntity.level(), serverPlayerEntity.getX() + (random.nextInt(50) - 25), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(50) - 25), new ItemStack(Items.ARROW), null);
                    serverPlayerEntity.level().addFreshEntity(arrow);
                });
            }
        }
        super.tick();
    }

    @Override
    public EventType<ArrowRainEvent> getType() {
        return TYPE;
    }
}
