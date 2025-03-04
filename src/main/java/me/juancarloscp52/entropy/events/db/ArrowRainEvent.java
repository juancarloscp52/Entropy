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
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

public class ArrowRainEvent extends AbstractTimedEvent {

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
                    ArrowEntity arrow = new ArrowEntity(serverPlayerEntity.getWorld(), serverPlayerEntity.getX() + (random.nextInt(50) - 25), serverPlayerEntity.getY() + 50 + (random.nextInt(10) - 5), serverPlayerEntity.getZ() + (random.nextInt(50) - 25), new ItemStack(Items.ARROW), null);
                    serverPlayerEntity.getWorld().spawnEntity(arrow);
                });
            }
        }
        super.tick();
    }

    @Override
    public String type() {
        return "rain";
    }
}