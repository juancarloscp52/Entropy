/**
 * @author Kanawanagasaki 
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.Heightmap.Type;

public class ZeusUltEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.getWorld();
            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            var pos = world.getTopPosition(Type.WORLD_SURFACE, serverPlayerEntity.getBlockPos());
            lightning.setPosition(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(lightning);
        }
    }

}
