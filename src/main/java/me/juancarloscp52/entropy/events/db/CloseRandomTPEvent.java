package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;

public class CloseRandomTPEvent extends AbstractInstantEvent {

    int count = 0;
    MinecraftServer server;

    @Override
    public void init() {
        server = Entropy.getInstance().eventHandler.server;
        PlayerLookup.all(server).forEach(serverPlayerEntity -> server.getCommandManager().execute(server.getCommandSource(), "/spreadplayers " + serverPlayerEntity.getX() + " " + serverPlayerEntity.getZ() + " 0 50 false " + serverPlayerEntity.getEntityName()));

    }

    @Override
    public void tick() {
        if (count <= 2) {
            if (count == 2) {
                PlayerLookup.all(server).forEach(serverPlayerEntity -> {
                    serverPlayerEntity.getEntityWorld().breakBlock(serverPlayerEntity.getBlockPos(), false);
                    serverPlayerEntity.getEntityWorld().breakBlock(serverPlayerEntity.getBlockPos().up(), false);
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
