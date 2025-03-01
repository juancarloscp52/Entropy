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
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class SinkingEvent extends AbstractTimedEvent {

    @Override
    public void init() {

    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void tick() {
        if(tickCount%30==0){
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                ServerWorld world = serverPlayerEntity.getServerWorld();
                int x = serverPlayerEntity.getBlockX(), y =serverPlayerEntity.getBlockY(), z = serverPlayerEntity.getBlockZ();
                for (int j = -1;j<2;j++){
                    for (int k = -1;k<2;k++){
                        BlockPos pos = new BlockPos(x+j,y-1,z+k);
                        if(world.getBlockState(pos).isIn(BlockTags.NOT_REPLACED_BY_EVENTS))
                            continue;
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            });
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration*0.75);
    }
}
