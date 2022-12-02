/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BeeEntity;

public class AngryBeeEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 3; i++) {
                        BeeEntity bee = EntityType.BEE.spawn(serverPlayerEntity.getWorld(),serverPlayerEntity.getBlockPos().east(2), SpawnReason.SPAWN_EGG);
                        bee.setAngryAt(serverPlayerEntity.getUuid());
                        bee.chooseRandomAngerTime();
                    }
                }
        );
    }

}
