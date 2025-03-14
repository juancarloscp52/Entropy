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
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ExplodeNearbyEntitiesEvent extends AbstractInstantEvent {
    public static final EventType<ExplodeNearbyEntitiesEvent> TYPE = EventType.builder(ExplodeNearbyEntitiesEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> serverPlayerEntity.getCommandSenderWorld().getEntities(serverPlayerEntity, new AABB(serverPlayerEntity.position().add(70, 70, 70), serverPlayerEntity.position().add(-70, -70, -70))).forEach(entity -> {
            entity.getCommandSenderWorld().explode(entity, entity.getX(), entity.getY() + 1f, entity.getZ(), 2.1f, Level.ExplosionInteraction.MOB);
            if(!entity.getType().is(EntityTypeTags.DO_NOT_EXPLODE))
                entity.kill(serverPlayerEntity.serverLevel());
        }));
    }


    @Override
    public EventType<ExplodeNearbyEntitiesEvent> getType() {
        return TYPE;
    }
}
