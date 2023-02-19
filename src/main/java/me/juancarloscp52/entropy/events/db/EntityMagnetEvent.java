package me.juancarloscp52.entropy.events.db;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityMagnetEvent extends ForcefieldEvent {
    @Override
    public Vec3d getVelocity(BlockPos relativePos) {
        return new Vec3d(relativePos.getX(), relativePos.getY(), relativePos.getZ());
    }
}
