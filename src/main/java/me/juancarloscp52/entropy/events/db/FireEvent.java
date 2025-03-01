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
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FireEvent extends AbstractTimedEvent {

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
                ServerWorld world = serverPlayerEntity.getServerWorld();
                BlockPos pos = serverPlayerEntity.getBlockPos();
                if(world.getBlockState(pos).isReplaceable()){
                    world.setBlockState(pos,Blocks.FIRE.getDefaultState());
                }
                pos = serverPlayerEntity.getBlockPos().up();
                if(world.getBlockState(pos).isReplaceable()){
                    world.setBlockState(pos,Blocks.FIRE.getDefaultState());
                }
                pos = serverPlayerEntity.getBlockPos().down();
                if(world.getBlockState(pos).isReplaceable()){
                    world.setBlockState(pos,Blocks.FIRE.getDefaultState());
                }
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,20,1));
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
