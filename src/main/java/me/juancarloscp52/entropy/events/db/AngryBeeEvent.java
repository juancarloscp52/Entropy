/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;

public class AngryBeeEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 3; i++) {
                        Bee bee = EntityType.BEE.spawn(serverPlayerEntity.serverLevel(),serverPlayerEntity.blockPosition().east(2), EntitySpawnReason.EVENT);
                        bee.setPersistentAngerTarget(serverPlayerEntity.getUUID());
                        bee.startPersistentAngerTimer();
                    }
                }
        );
    }

}
