package me.juancarloscp52.entropy;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Set;

public class EntropyUtils {

    public static void teleportPlayer(final ServerPlayerEntity serverPlayerEntity, final double x, final double y, final double z) {
        serverPlayerEntity.stopRiding();
        serverPlayerEntity.teleport(serverPlayerEntity.getServerWorld(), x, y, z, Set.of(), serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
    }

    public static void teleportPlayer(final ServerPlayerEntity serverPlayerEntity, final Vec3d pos) {
        teleportPlayer(serverPlayerEntity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void clearPlayerArea(ServerPlayerEntity serverPlayerEntity){
        serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
        serverPlayerEntity.getWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
        BlockHitResult blockHitResult = serverPlayerEntity.getWorld().raycast(new RaycastContext(serverPlayerEntity.getPos(), serverPlayerEntity.getPos().subtract(0, -6, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, serverPlayerEntity));
        if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.getWorld().getBlockState(blockHitResult.getBlockPos()).isLiquid()) {
            serverPlayerEntity.getWorld().setBlockState(serverPlayerEntity.getBlockPos().down(), Blocks.STONE.getDefaultState());
        }
    }
}
