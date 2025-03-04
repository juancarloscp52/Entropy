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
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.Box;

public class TeleportNearbyEntitiesEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getWorld().getOtherEntities(serverPlayerEntity, new Box(serverPlayerEntity.getPos().add(50, 50, 50), serverPlayerEntity.getPos().add(-50, -50, -50))).forEach(entity -> {
                if(!(entity instanceof EnderDragonEntity))
                    entity.requestTeleport(serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ());
            });
        });
    }
}
