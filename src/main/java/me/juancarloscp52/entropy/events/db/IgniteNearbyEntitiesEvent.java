package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.util.math.Box;

public class IgniteNearbyEntitiesEvent extends AbstractInstantEvent {

    public IgniteNearbyEntitiesEvent() {
        this.translationKey="entropy.events.igniteEntities";
    }

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.getServerWorld().getOtherEntities(serverPlayerEntity, new Box(serverPlayerEntity.getBlockPos().add(50, 50, 50), serverPlayerEntity.getBlockPos().add(-50, -50, -50))).forEach(entity -> entity.setOnFireFor(30)));
    }

}
