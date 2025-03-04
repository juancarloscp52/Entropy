/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class SoSweetEvent extends AbstractInstantEvent {
    private final BlockState sweetBerryBush = Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(SweetBerryBushBlock.AGE, 2);

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {

            var world = serverPlayerEntity.level();
            var playerPos = serverPlayerEntity.blockPosition();

            for (int ix = -4; ix < 5; ix++) {
                for (int iz = -4; iz < 5; iz++) {
                    if ((ix + iz + 100) % 2 == 1)
                        continue;

                    for (int iy = -4; iy < 5; iy++) {
                        var blockPos = playerPos.offset(ix, iy, iz);
                        var downBlockPos = blockPos.below();
                        var blockState = world.getBlockState(blockPos);
                        if ((blockState.getBlock().equals(Blocks.AIR) || blockState.canBeReplaced(Fluids.WATER))
                                && world.getBlockState(downBlockPos).isFaceSturdy(world, downBlockPos, Direction.UP)) {
                            world.setBlockAndUpdate(blockPos, sweetBerryBush);
                            iy++; //no need to check the position above this berry bush, because one won't be placed there anyway
                        }
                    }
                }
            }
        }
    }
}
