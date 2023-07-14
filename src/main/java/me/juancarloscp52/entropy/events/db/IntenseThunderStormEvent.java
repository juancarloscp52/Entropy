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

import java.util.Random;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

public class IntenseThunderStormEvent extends AbstractTimedEvent {

    Random random;

    @Override
    public void init() {
        random = new Random();
        Entropy.getInstance().eventHandler.server.getOverworld().setWeather(0, this.getDuration(), true, true);
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {
    }

    @Override
    public void tick() {

        if (getTickCount() % 10 == 0) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(serverPlayerEntity.getWorld());
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofCenter(serverPlayerEntity.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING, new BlockPos(serverPlayerEntity.getBlockX() + (random.nextInt(50) - 25), serverPlayerEntity.getBlockY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getBlockZ() + (random.nextInt(60) - 25)))));
                serverPlayerEntity.getWorld().spawnEntity(lightningEntity);
            });
        }
        super.tick();
    }

    @Override
    public String type() {
        return "rain";
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration*1.25);
    }
}
