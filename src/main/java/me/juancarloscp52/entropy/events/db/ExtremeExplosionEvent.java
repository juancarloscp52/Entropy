package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.world.explosion.Explosion;

public class ExtremeExplosionEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.getServerWorld().createExplosion(serverPlayerEntity, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), 10f, true, Explosion.DestructionType.BREAK));
    }
}
