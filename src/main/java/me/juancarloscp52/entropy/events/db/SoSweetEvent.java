/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;

public class SoSweetEvent extends AbstractInstantEvent {
    private final BlockState sweetBerryBush = Blocks.SWEET_BERRY_BUSH.getDefaultState().with(Properties.AGE_3, 2);;

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {

            var world = serverPlayerEntity.getWorld();
            var playerPos = serverPlayerEntity.getBlockPos();

            for (int ix = -4; ix < 5; ix++) {
                for (int iz = -4; iz < 5; iz++) {
                    if ((ix + iz + 100) % 2 == 1)
                        continue;

                    var blockPos = world.getTopPosition(Type.MOTION_BLOCKING, new BlockPos(playerPos.getX() + ix, 0, playerPos.getZ() + iz));
                    var blockState = world.getBlockState(blockPos);
                    var downBlockState = world.getBlockState(blockPos.down());
                    if ((blockState.getBlock().equals(Blocks.AIR) || blockState.canBucketPlace(Fluids.WATER))
                            && downBlockState.isFullCube(world, blockPos.down())) {
                        world.setBlockState(blockPos, sweetBerryBush);
                    }
                }
            }
        }
    }
}
