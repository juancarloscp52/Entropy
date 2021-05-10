package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.math.Direction;

public class PhantomEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> {
            for(int i =0; i<3;i++){
                PhantomEntity phantom = EntityType.PHANTOM.spawn(serverPlayerEntity.getServerWorld(), null, null, null, serverPlayerEntity.getBlockPos().offset(Direction.UP,5), SpawnReason.SPAWN_EGG, true, false);
                phantom.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,1500));
            }
        });
    }
}
