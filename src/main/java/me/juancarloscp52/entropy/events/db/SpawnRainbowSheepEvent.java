/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;

public class SpawnRainbowSheepEvent extends AbstractInstantEvent {
    public static final EventType<SpawnRainbowSheepEvent> TYPE = EventType.builder(SpawnRainbowSheepEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    for (int i = 0; i < 5; i++) {
                        EntityType.SHEEP.spawn(serverPlayerEntity.level(), sheepEntity -> {
                            sheepEntity.setCustomName(Component.nullToEmpty("jeb_"));
                            sheepEntity.setCustomNameVisible(false);
                        }, serverPlayerEntity.blockPosition(), EntitySpawnReason.EVENT, true,false);
                    }
                }
        );
    }

    @Override
    public EventType<SpawnRainbowSheepEvent> getType() {
        return TYPE;
    }
}
