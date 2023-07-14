/**
 * @author Curiositas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;


public class WardenEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    WardenEntity warden = EntityType.WARDEN.spawn(serverPlayerEntity.getServerWorld(), serverPlayerEntity.getBlockPos(), SpawnReason.SPAWN_EGG);
                    if(warden!=null) {
                        warden.setHealth(4);
                        warden.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 9999, 5));
                    }
                }
                );
    }

}
