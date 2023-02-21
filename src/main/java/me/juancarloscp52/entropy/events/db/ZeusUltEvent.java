/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.Heightmap.Type;

public class ZeusUltEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.getWorld();
            var playerPos = serverPlayerEntity.getBlockPos();
            var pos = world.getTopPosition(Type.WORLD_SURFACE, playerPos);
            for (int iy = playerPos.getY(); iy < pos.getY(); iy++)
                if (!world.getBlockState(pos).isIn(EntropyTags.NOT_REPLACED_BY_EVENTS))
                    world.breakBlock(playerPos.withY(iy), true);
            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPosition(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            world.spawnEntity(lightning);
        }
    }

}
