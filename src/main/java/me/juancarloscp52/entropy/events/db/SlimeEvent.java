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
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

import java.util.Random;

public class SlimeEvent extends AbstractInstantEvent {
    public static final EventType<SlimeEvent> TYPE = EventType.builder(SlimeEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            Random random = new Random();
            int slimes = random.nextInt(4,12);
            for(int i = 0; i<slimes; i++){
                EntityType.SLIME.spawn(serverPlayerEntity.level(), serverPlayerEntity.blockPosition().offset(random.nextInt(-4,5),random.nextInt(2),random.nextInt(-4,5)), EntitySpawnReason.EVENT);
            }
        });
    }

    @Override
    public EventType<SlimeEvent> getType() {
        return TYPE;
    }
}
