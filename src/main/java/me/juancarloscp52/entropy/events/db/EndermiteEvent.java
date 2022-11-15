/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

public class EndermiteEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 4; i++) {
                        EntityType.ENDERMITE.spawn(serverPlayerEntity.getWorld(),null, null, null, serverPlayerEntity.getBlockPos(), SpawnReason.SPAWN_EGG, true,false);
                    }
                }
        );
    }

}
