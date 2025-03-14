/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class BeeEvent extends AbstractInstantEvent {
    public static final EventType<BeeEvent> TYPE = EventType.builder(BeeEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 10; i++) {
                        EntityType.BEE.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT);
                    }
                }
        );
    }

    @Override
    public EventType<BeeEvent> getType() {
        return TYPE;
    }
}
