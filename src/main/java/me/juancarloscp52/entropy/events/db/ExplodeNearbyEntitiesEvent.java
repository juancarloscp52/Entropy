package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.util.math.Box;
import net.minecraft.world.explosion.Explosion;

public class ExplodeNearbyEntitiesEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.getEntityWorld().getOtherEntities(serverPlayerEntity, new Box(serverPlayerEntity.getBlockPos().add(70, 70, 70), serverPlayerEntity.getBlockPos().add(-70, -70, -70))).forEach(entity -> {
            entity.getEntityWorld().createExplosion(entity, entity.getX(), entity.getY() + 1.5f, entity.getZ(), 3.5f, Explosion.DestructionType.DESTROY);
            entity.kill();
        }));
    }


}
