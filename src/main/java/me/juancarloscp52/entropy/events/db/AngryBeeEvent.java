/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.bee.Bee;

public class AngryBeeEvent extends AbstractInstantEvent {
    public static final EventType<AngryBeeEvent> TYPE = EventType.builder(AngryBeeEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 3; i++) {
                        Bee bee = EntityType.BEE.spawn(serverPlayerEntity.level(),serverPlayerEntity.blockPosition().east(2), EntitySpawnReason.EVENT);
                        bee.setPersistentAngerTarget(EntityReference.of(serverPlayerEntity));
                        bee.startPersistentAngerTimer();
                    }
                }
        );
    }

    @Override
    public EventType<AngryBeeEvent> getType() {
        return TYPE;
    }
}
