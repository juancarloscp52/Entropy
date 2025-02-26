/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;

public class BlazeEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                        BlazeEntity blaze = EntityType.BLAZE.spawn(serverPlayerEntity.getServerWorld(), serverPlayerEntity.getBlockPos(), SpawnReason.SPAWN_EGG);
                        blaze.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,9999,2));
                        blaze.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,120, 1));
                }
        );
    }

}
