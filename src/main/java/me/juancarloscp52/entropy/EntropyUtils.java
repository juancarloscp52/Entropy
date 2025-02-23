package me.juancarloscp52.entropy;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class EntropyUtils {

    public static void teleportPlayer(final ServerPlayerEntity serverPlayerEntity, final double x, final double y, final double z) {
        serverPlayerEntity.stopRiding();
        serverPlayerEntity.teleport(serverPlayerEntity.getServerWorld(), x, y, z, Set.of(), serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
    }

    public static void teleportPlayer(final ServerPlayerEntity serverPlayerEntity, final Vec3d pos) {
        teleportPlayer(serverPlayerEntity, pos.getX(), pos.getY(), pos.getZ());
    }
}
