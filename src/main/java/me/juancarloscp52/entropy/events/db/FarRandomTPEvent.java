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
        BlockPos randomLocation = new BlockPos(random.nextInt(20000) - 10000, 0, random.nextInt(20000) - 10000);
        server = Entropy.getInstance().eventHandler.server;
        PlayerLookup.all(server).forEach(serverPlayerEntity -> {
            serverPlayerEntity.stopRiding();
            server.getCommandManager().execute(server.getCommandSource(), "/spreadplayers " + randomLocation.getX() + " " + randomLocation.getZ() + " 0 120 false " + serverPlayerEntity.getEntityName());
            serverPlayerEntity.getServerWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
            serverPlayerEntity.getServerWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
            BlockHitResult blockHitResult = serverPlayerEntity.world.raycast(new RaycastContext(serverPlayerEntity.getPos(), serverPlayerEntity.getPos().subtract(0, -6, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, serverPlayerEntity));
            if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.getServerWorld().getBlockState(blockHitResult.getBlockPos()).getMaterial().isLiquid()) {
                serverPlayerEntity.getServerWorld().setBlockState(serverPlayerEntity.getBlockPos().down(), Blocks.STONE.getDefaultState());
            }
        });

    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                PlayerLookup.all(server).forEach(serverPlayerEntity -> {
                    serverPlayerEntity.getServerWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
                    serverPlayerEntity.getServerWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
                    BlockHitResult blockHitResult = serverPlayerEntity.world.raycast(new RaycastContext(serverPlayerEntity.getPos(), serverPlayerEntity.getPos().subtract(0, -6, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, serverPlayerEntity));
                    if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.getServerWorld().getBlockState(blockHitResult.getBlockPos()).getMaterial().isLiquid()) {
                        serverPlayerEntity.getServerWorld().setBlockState(serverPlayerEntity.getBlockPos().down(), Blocks.STONE.getDefaultState());
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
