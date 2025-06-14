/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class EndermiteEvent extends AbstractInstantEvent {
    public static final EventType<EndermiteEvent> TYPE = EventType.builder(EndermiteEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 4; i++) {
                        EntityType.ENDERMITE.spawn(serverPlayerEntity.level(), serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT);
                    }
                }
        );
    }

    @Override
    public EventType<EndermiteEvent> getType() {
        return TYPE;
    }
}
