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
import net.minecraft.entity.projectile.FireballEntity;

public class MeteorRainEvent extends AbstractTimedEvent {

    Random random;

    @Override
    public void init() {
        random = new Random();
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

        if (getTickCount() % 20 == 0) {
            for (int i = 0; i < 7; i++) {
                Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                    FireballEntity meteor = new FireballEntity(serverPlayerEntity.getWorld(), serverPlayerEntity, 0, -1 * (random.nextInt(4) + 1), 0,2);
                    meteor.refreshPositionAndAngles(serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), meteor.getYaw(), meteor.getPitch());
                    serverPlayerEntity.getWorld().spawnEntity(meteor);
                });

            }
        }
        if (getTickCount() == getTickCount() / 2) {
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                FireballEntity meteor = new FireballEntity(serverPlayerEntity.getWorld(), serverPlayerEntity, 0, -1 * (random.nextInt(4) + 1), 0,4);
                meteor.refreshPositionAndAngles(serverPlayerEntity.getX() + (random.nextInt(100) - 50), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(100) - 50), meteor.getYaw(), meteor.getPitch());
                serverPlayerEntity.getWorld().spawnEntity(meteor);
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
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
