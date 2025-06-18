/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;

public class BlazeEvent extends AbstractInstantEvent {
    public static final EventType<BlazeEvent> TYPE = EventType.builder(BlazeEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                        Blaze blaze = EntityType.BLAZE.spawn(serverPlayerEntity.level(), serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT);
                        blaze.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,9999,2));
                        blaze.addEffect(new MobEffectInstance(MobEffects.STRENGTH,120, 1));
                }
        );
    }

    @Override
    public EventType<BlazeEvent> getType() {
        return TYPE;
    }
}
