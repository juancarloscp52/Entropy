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
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class PitEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
        {
            BlockPos pos = serverPlayerEntity.getBlockPos();
            int x = pos.getX(),y = pos.getY(),z = pos.getZ();
            for(int h = y-50; h<=319; h++){
                for(int i= -9; i<=9;i++) {
                    for (int j = -9; j <= 9; j++) {
                        BlockPos currentPos = new BlockPos(x+i,h,z+j);
                        if(Math.abs(i)>2|| Math.abs(j)>2){
                            if(!(serverPlayerEntity.getWorld().getBlockState(currentPos).getBlock().equals(Blocks.BEDROCK) || serverPlayerEntity.getWorld().getBlockState(currentPos).getBlock().equals(Blocks.END_PORTAL_FRAME) || serverPlayerEntity.getWorld().getBlockState(currentPos).getBlock().equals(Blocks.END_PORTAL))){
                                if(h<(y-45)){
                                    serverPlayerEntity.getWorld().setBlockState(currentPos, Blocks.WATER.getDefaultState());
                                }else{
                                    serverPlayerEntity.getWorld().setBlockState(currentPos, Blocks.AIR.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }
            serverPlayerEntity.stopRiding();
            serverPlayerEntity.refreshPositionAfterTeleport(pos.getX(),pos.getY(),pos.getZ());
        });
    }

}
