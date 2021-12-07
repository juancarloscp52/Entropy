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

public class SkyEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity ->
        {
            BlockPos pos = serverPlayerEntity.getBlockPos().withY(319);
            for(int i= -3; i<=4;i++) {
                for (int j = -3; j <= 4; j++) {
                    serverPlayerEntity.getWorld().setBlockState(new BlockPos(pos.getX()+i,pos.getY(),pos.getZ()+j),Blocks.GLASS.getDefaultState());
                }
            }
            serverPlayerEntity.stopRiding();
            serverPlayerEntity.refreshPositionAfterTeleport(pos.getX(),pos.getY()+2,pos.getZ());

        });
    }
    @Override
    public String type() {
        return "health";
    }
}
