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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;

import java.util.Random;

public class FarRandomTPEvent extends AbstractInstantEvent {
    int count = 0;
    MinecraftServer server;

    @Override
    public void init() {
        Random random = new Random();
        BlockPos randomLocation = new BlockPos(random.nextInt(5000) - 2500, 0, random.nextInt(5000) - 2500);
        server = Entropy.getInstance().eventHandler.server;
        PlayerLookup.all(server).forEach(serverPlayerEntity -> {
            serverPlayerEntity.stopRiding();
            server.getCommandManager().executeWithPrefix(server.getCommandSource(), "spreadplayers " + randomLocation.getX() + " " + randomLocation.getZ() + " 0 120 false " + serverPlayerEntity.getEntityName());
            serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
            serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
            BlockHitResult blockHitResult = serverPlayerEntity.world.raycast(new RaycastContext(serverPlayerEntity.getPos(), serverPlayerEntity.getPos().subtract(0, -6, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, serverPlayerEntity));
            if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).getMaterial().isLiquid()) {
                serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos().down(), Blocks.STONE.getDefaultState());
            }
        });

    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                PlayerLookup.all(server).forEach(serverPlayerEntity -> {
                    serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
                    serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
                    BlockHitResult blockHitResult = serverPlayerEntity.world.raycast(new RaycastContext(serverPlayerEntity.getPos(), serverPlayerEntity.getPos().subtract(0, -6, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, serverPlayerEntity));
                    if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).getMaterial().isLiquid()) {
                        serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos().down(), Blocks.STONE.getDefaultState());
                    }
                });
            }
            count++;
        }
        super.tick();
    }

    @Override
    public boolean hasEnded() {
        return count > 2;
    }
}
