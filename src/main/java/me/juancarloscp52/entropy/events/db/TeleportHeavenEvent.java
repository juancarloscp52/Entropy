package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

public class TeleportHeavenEvent extends AbstractInstantEvent {
    public TeleportHeavenEvent() {
        this.translationKey="entropy.events.teleportHeaven";
    }

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.teleport(serverPlayerEntity.getX(),260,serverPlayerEntity.getZ()));
    }
}
