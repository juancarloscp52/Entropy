package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

public class CreeperEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> EntityType.CREEPER.spawn(serverPlayerEntity.getServerWorld(), null, null, null, serverPlayerEntity.getBlockPos().north(), SpawnReason.SPAWN_EGG, true, false));
    }
}
