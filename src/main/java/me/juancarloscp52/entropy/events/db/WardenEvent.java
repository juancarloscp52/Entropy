/**
 * @author Curiositas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.warden.Warden;


public class WardenEvent extends AbstractInstantEvent {
    public static final EventType<WardenEvent> TYPE = EventType.builder(WardenEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    Warden warden = EntityType.WARDEN.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT);
                    if(warden!=null) {
                        warden.setHealth(4);
                        warden.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 9999, 5));
                    }
                }
        );
    }

    @Override
    public EventType<WardenEvent> getType() {
        return TYPE;
    }
}
