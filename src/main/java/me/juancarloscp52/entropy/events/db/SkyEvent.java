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
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SkyEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity ->
        {
            int height = 319;
            // Check if the player is in the nether or end.
            if(serverPlayerEntity.level().dimension() != Level.OVERWORLD){
                height = 254;
                if(serverPlayerEntity.level().dimension() == Level.NETHER){
                    BlockPos pos = serverPlayerEntity.blockPosition().atY(122);
                    for(int i= -3; i<=4;i++) {
                        for (int j = -3; j <= 4; j++) {
                            for (int z = -2; z <= 6; z++){
                                serverPlayerEntity.level().setBlockAndUpdate(new BlockPos(pos.getX()+i,pos.getY()+z,pos.getZ()+j),Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }

            BlockPos pos = serverPlayerEntity.blockPosition().atY(height);
            for(int i= -3; i<=4;i++) {
                for (int j = -3; j <= 4; j++) {
                    serverPlayerEntity.level().setBlockAndUpdate(new BlockPos(pos.getX()+i,pos.getY(),pos.getZ()+j),Blocks.GLASS.defaultBlockState());
                }
            }
            EntropyUtils.teleportPlayer(serverPlayerEntity, Vec3.atBottomCenterOf(pos.above(2)));

        });
    }
}
