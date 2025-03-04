/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class ZeusUltEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.level();
            var playerPos = serverPlayerEntity.blockPosition();
            var pos = world.getHeightmapPos(Types.WORLD_SURFACE, playerPos);
            for (int iy = playerPos.getY(); iy < pos.getY(); iy++)
                if (!world.getBlockState(pos).is(BlockTags.NOT_REPLACED_BY_ZEUS_ULT))
                    world.destroyBlock(playerPos.atY(iy), true);
            var lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
            lightning.setPos(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            world.addFreshEntity(lightning);
        }
    }

}
