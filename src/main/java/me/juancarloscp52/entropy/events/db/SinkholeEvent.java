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
import me.juancarloscp52.entropy.EntropyTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class SinkholeEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerWorld world = serverPlayerEntity.getWorld();
            int x = serverPlayerEntity.getBlockX(), y =serverPlayerEntity.getBlockY(), z = serverPlayerEntity.getBlockZ();
            System.out.println(world.getHeight());
            for(int i = y+2; i> (y-40);i--){
                for (int j = -1;j<2;j++){
                    for (int k = -1;k<2;k++){
                        BlockPos pos = new BlockPos(x+j,i,z+k);
                        if(world.getBlockState(pos).isIn(EntropyTags.NOT_REPLACED_BY_EVENTS))
                            continue;

                        if(i<(y-38)){
                            serverPlayerEntity.getWorld().setBlockState(pos, Blocks.WATER.getDefaultState());
                        }else{
                            serverPlayerEntity.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
                        }

                    }
                }
            }
        });
    }

}
