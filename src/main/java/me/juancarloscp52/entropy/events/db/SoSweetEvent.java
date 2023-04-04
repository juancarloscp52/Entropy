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

public class SoSweetEvent extends AbstractInstantEvent {
    private final BlockState sweetBerryBush = Blocks.SWEET_BERRY_BUSH.getDefaultState().with(Properties.AGE_3, 2);

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {

            var world = serverPlayerEntity.getWorld();
            var playerPos = serverPlayerEntity.getBlockPos();

            for (int ix = -4; ix < 5; ix++) {
                for (int iz = -4; iz < 5; iz++) {
                    if ((ix + iz + 100) % 2 == 1)
                        continue;

                    for (int iy = -4; iy < 5; iy++) {
                        var blockPos = playerPos.add(ix, iy, iz);
                        var downBlockPos = blockPos.down();
                        var blockState = world.getBlockState(blockPos);
                        if ((blockState.getBlock().equals(Blocks.AIR) || blockState.canBucketPlace(Fluids.WATER))
                                && world.getBlockState(downBlockPos).isFullCube(world, downBlockPos)) {
                            world.setBlockState(blockPos, sweetBerryBush);
                            iy++; //no need to check the position above this berry bush, because once won't be placed there anyway
                        }
                    }
                }
            }
        }
    }
}
