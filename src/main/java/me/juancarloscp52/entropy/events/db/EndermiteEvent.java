/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class EndermiteEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 4; i++) {
                        EntityType.ENDERMITE.spawn(serverPlayerEntity.serverLevel(), serverPlayerEntity.blockPosition(), MobSpawnType.SPAWN_EGG);
                    }
                }
        );
    }

}
