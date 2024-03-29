/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

public class EndermiteEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 4; i++) {
                        EntityType.ENDERMITE.spawn(serverPlayerEntity.getWorld(), serverPlayerEntity.getBlockPos(), SpawnReason.SPAWN_EGG);
                    }
                }
        );
    }

}
