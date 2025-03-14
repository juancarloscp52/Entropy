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
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public class PoolEvent extends AbstractInstantEvent {
    public static final EventType<PoolEvent> TYPE = EventType.builder(PoolEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerLevel world = serverPlayerEntity.serverLevel();
            int x = serverPlayerEntity.getBlockX(), y =serverPlayerEntity.getBlockY(), z = serverPlayerEntity.getBlockZ();
            for(int i = y; i>y-6;i--){
                for (int j = -4;j<5;j++){
                    for (int k = -4;k<5;k++){
                        BlockPos pos = new BlockPos(x+j,i,z+k);

                        if(serverPlayerEntity.level().getBlockState(pos).is(BlockTags.NOT_REPLACED_BY_EVENTS))
                            continue;   //Do not replace blocks

                        if(i==y-5){
                            if(j%2==0){
                                world.setBlockAndUpdate(pos,(k%2==0)?Blocks.MAGMA_BLOCK.defaultBlockState():Blocks.SOUL_SAND.defaultBlockState());
                            }else{
                                world.setBlockAndUpdate(pos,(k%2!=0)?Blocks.MAGMA_BLOCK.defaultBlockState():Blocks.SOUL_SAND.defaultBlockState());
                            }
                        }else{
                            world.setBlockAndUpdate(pos,(j==-4 ||j==4 ||k==-4 ||k==4) ? Blocks.COBBLESTONE.defaultBlockState():Blocks.WATER.defaultBlockState());
                        }
                    }
                }
            }
        });
    }

    @Override
    public EventType<PoolEvent> getType() {
        return TYPE;
    }
}
