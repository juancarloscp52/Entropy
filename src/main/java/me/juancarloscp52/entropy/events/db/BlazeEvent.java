/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;

public class BlazeEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                        Blaze blaze = EntityType.BLAZE.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
                        blaze.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,9999,2));
                        blaze.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,120, 1));
                }
        );
    }

}
