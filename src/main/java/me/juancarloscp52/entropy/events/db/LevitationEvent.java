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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class LevitationEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.level().getEntities(serverPlayerEntity, new AABB(serverPlayerEntity.position().add(50, 50, 50), serverPlayerEntity.position().add(-50, -50, -50))).forEach(
                    entity ->  {
                        if(!(entity instanceof Player) && entity instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.DO_NOT_LEVITATE)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,(int) (Entropy.getInstance().settings.baseEventDuration*0.5),4, true, false));
                        }
                    }
                    );
            if(!serverPlayerEntity.getType().is(EntityTypeTags.DO_NOT_LEVITATE))
                serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,(int) (Entropy.getInstance().settings.baseEventDuration*0.5),4, true, false));
        });
    }
}
