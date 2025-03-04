package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.world.level.block.Blocks;

public class TrueFrostWalkerEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.level();
            var playerBlockPos = serverPlayerEntity.blockPosition();
            for (int ix = -3; ix <= 3; ix++) {
                for (int iz = -3; iz <= 3; iz++) {
                    if ((ix == -3 || ix == 3) && (iz == -3 || iz == 3))
                        continue;

                    var blockPos = playerBlockPos.offset(ix, -1, iz);
                    var blockState = world.getBlockState(blockPos);
                    var upBlockState = world.getBlockState(blockPos.above());
                    if (blockState.getBlock() == Blocks.WATER && upBlockState.getBlock() == Blocks.AIR)
                        world.setBlockAndUpdate(blockPos, Blocks.BLUE_ICE.defaultBlockState());
                    if (blockState.getBlock() == Blocks.LAVA && upBlockState.getBlock() == Blocks.AIR)
                        world.setBlockAndUpdate(blockPos, Blocks.MAGMA_BLOCK.defaultBlockState());
                }
            }
        }

        super.tick();
    }

}
