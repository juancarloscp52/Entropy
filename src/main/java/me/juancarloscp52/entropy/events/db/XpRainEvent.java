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
import net.minecraft.world.entity.ExperienceOrb;

import java.util.Random;

public class XpRainEvent extends AbstractTimedEvent {

    public static final EventType<XpRainEvent> TYPE = EventType.builder(XpRainEvent::new).category(EventCategory.RAIN).build();
    Random random;

    @Override
    public void init() {
        random = new Random();
    }

    @Override
    public void tick() {

        if (getTickCount() % 10 == 0) {
            for (int i = 0; i < 7; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    ExperienceOrb orb = new ExperienceOrb(serverPlayerEntity.level(), serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), random.nextInt(20) + 1);
                    serverPlayerEntity.level().addFreshEntity(orb);
                });

            }
        }
        super.tick();
    }

    @Override
    public EventType<XpRainEvent> getType() {
        return TYPE;
    }
}
