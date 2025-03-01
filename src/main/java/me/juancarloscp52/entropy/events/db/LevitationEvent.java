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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class LevitationEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            serverPlayerEntity.getWorld().getOtherEntities(serverPlayerEntity, new Box(serverPlayerEntity.getPos().add(50, 50, 50), serverPlayerEntity.getPos().add(-50, -50, -50))).forEach(
                    entity ->  {
                        if(!(entity instanceof PlayerEntity) && entity instanceof LivingEntity livingEntity && !livingEntity.getType().isIn(EntityTypeTags.DO_NOT_LEVITATE)){
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION,(int) (Entropy.getInstance().settings.baseEventDuration*0.5),4, true, false));
                        }
                    }
                    );
            if(!serverPlayerEntity.getType().isIn(EntityTypeTags.DO_NOT_LEVITATE))
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION,(int) (Entropy.getInstance().settings.baseEventDuration*0.5),4, true, false));
        });
    }
}
