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
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ExplodeNearbyEntitiesEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> serverPlayerEntity.getEntityWorld().getOtherEntities(serverPlayerEntity, new Box(serverPlayerEntity.getPos().add(70, 70, 70), serverPlayerEntity.getPos().add(-70, -70, -70))).forEach(entity -> {
            entity.getEntityWorld().createExplosion(entity, entity.getX(), entity.getY() + 1f, entity.getZ(), 2.1f, World.ExplosionSourceType.MOB);
            if(!entity.getType().isIn(EntityTypeTags.DO_NOT_EXPLODE))
                entity.kill();
        }));
    }


}
