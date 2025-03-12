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
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class IntenseThunderStormEvent extends AbstractTimedEvent {

    public static final EventType<IntenseThunderStormEvent> TYPE = EventType.builder(IntenseThunderStormEvent::new).category(EventCategory.RAIN).build();
    Random random;

    @Override
    public void init() {
        random = new Random();
        Entropy.getInstance().eventHandler.server.overworld().setWeatherParameters(0, this.getDuration(), true, true);
    }

    @Override
    public void tick() {

        if (getTickCount() % 10 == 0) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(serverPlayerEntity.serverLevel(), EntitySpawnReason.EVENT);
                lightningEntity.moveTo(Vec3.atCenterOf(serverPlayerEntity.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(serverPlayerEntity.getBlockX() + (random.nextInt(50) - 25), serverPlayerEntity.getBlockY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getBlockZ() + (random.nextInt(60) - 25)))));
                serverPlayerEntity.level().addFreshEntity(lightningEntity);
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * 1.25);
    }

    @Override
    public EventType<IntenseThunderStormEvent> getType() {
        return TYPE;
    }
}
