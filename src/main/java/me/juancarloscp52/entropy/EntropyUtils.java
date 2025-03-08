package me.juancarloscp52.entropy;

import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntropyUtils {

    public static void teleportPlayer(final ServerPlayer serverPlayerEntity, final double x, final double y, final double z) {
        serverPlayerEntity.stopRiding();
        serverPlayerEntity.teleportTo(serverPlayerEntity.serverLevel(), x, y, z, Set.of(), serverPlayerEntity.getYRot(), serverPlayerEntity.getXRot(), true);
    }

    public static void teleportPlayer(final ServerPlayer serverPlayerEntity, final Vec3 pos) {
        teleportPlayer(serverPlayerEntity, pos.x(), pos.y(), pos.z());
    }

    public static void clearPlayerArea(ServerPlayer serverPlayerEntity){
        serverPlayerEntity.level().destroyBlock(serverPlayerEntity.blockPosition(), false);
        serverPlayerEntity.level().destroyBlock(serverPlayerEntity.blockPosition().above(), false);
        BlockHitResult blockHitResult = serverPlayerEntity.level().clip(new ClipContext(serverPlayerEntity.position(), serverPlayerEntity.position().subtract(0, -6, 0), ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, serverPlayerEntity));
        if (blockHitResult.getType() == HitResult.Type.MISS || serverPlayerEntity.level().getBlockState(blockHitResult.getBlockPos()).liquid()) {
            serverPlayerEntity.level().setBlockAndUpdate(serverPlayerEntity.blockPosition().below(), Blocks.STONE.defaultBlockState());
        }
    }
}
