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
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

public class FireEvent extends AbstractTimedEvent {
    public static final EventType<FireEvent> TYPE = EventType.builder(FireEvent::new).build();

    @Override
    public void initClient() {
        Variables.fireEvent = true;
    }

    @Override
    public void endClient() {
        Variables.fireEvent = false;
        super.endClient();
    }

    @Override
    public void tick() {
        if(tickCount%5==0){
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                ServerLevel world = serverPlayerEntity.level();
                BlockPos pos = serverPlayerEntity.blockPosition();
                if(world.getBlockState(pos).canBeReplaced()){
                    world.setBlockAndUpdate(pos,Blocks.FIRE.defaultBlockState());
                }
                pos = serverPlayerEntity.blockPosition().above();
                if(world.getBlockState(pos).canBeReplaced()){
                    world.setBlockAndUpdate(pos,Blocks.FIRE.defaultBlockState());
                }
                pos = serverPlayerEntity.blockPosition().below();
                if(world.getBlockState(pos).canBeReplaced()){
                    world.setBlockAndUpdate(pos,Blocks.FIRE.defaultBlockState());
                }
                serverPlayerEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,20,1));
            });
        }
        super.tick();
    }

    @Override
    public EventType<FireEvent> getType() {
        return TYPE;
    }
}
