/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class SilverfishEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 6; i++) {
                        EntityType.SILVERFISH.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT);
                    }
                }
        );
    }

}
