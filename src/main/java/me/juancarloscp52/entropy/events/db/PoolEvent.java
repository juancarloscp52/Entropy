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
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PoolEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerWorld world = serverPlayerEntity.getWorld();
            int x = serverPlayerEntity.getBlockX(), y =serverPlayerEntity.getBlockY(), z = serverPlayerEntity.getBlockZ();
            for(int i = y; i>y-6;i--){
                for (int j = -4;j<5;j++){
                    for (int k = -4;k<5;k++){
                        BlockPos pos = new BlockPos(x+j,i,z+k);

                        if((serverPlayerEntity.getWorld().getBlockState(pos).getBlock().equals(Blocks.BEDROCK) ||
                                serverPlayerEntity.getWorld().getBlockState(pos).getBlock().equals(Blocks.END_PORTAL_FRAME) ||
                                serverPlayerEntity.getWorld().getBlockState(pos).getBlock().equals(Blocks.END_PORTAL)))
                            continue;   //Do not replace blocks

                        if(i==y-5){
                            if(j%2==0){
                                world.setBlockState(pos,(k%2==0)?Blocks.MAGMA_BLOCK.getDefaultState():Blocks.SOUL_SAND.getDefaultState());
                            }else{
                                world.setBlockState(pos,(k%2!=0)?Blocks.MAGMA_BLOCK.getDefaultState():Blocks.SOUL_SAND.getDefaultState());
                            }
                        }else{
                            world.setBlockState(pos,(j==-4 ||j==4 ||k==-4 ||k==4) ? Blocks.COBBLESTONE.getDefaultState():Blocks.WATER.getDefaultState());
                        }
                    }
                }
            }
        });
    }

}
