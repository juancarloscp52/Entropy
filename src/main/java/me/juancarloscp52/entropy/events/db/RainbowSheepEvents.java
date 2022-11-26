/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.text.Text;

public class RainbowSheepEvents extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 5; i++) {
                        EntityType.SHEEP.spawn(serverPlayerEntity.getWorld(),null, Text.of("jeb_"), null, serverPlayerEntity.getBlockPos(), SpawnReason.SPAWN_EGG, true,false);
                    }
                }
        );
    }

}
