package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;

public class TrueFrostWalkerEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            var world = serverPlayerEntity.getWorld();
            var playerBlockPos = serverPlayerEntity.getBlockPos();
            for (int ix = -3; ix <= 3; ix++) {
                for (int iz = -3; iz <= 3; iz++) {
                    if ((ix == -3 || ix == 3) && (iz == -3 || iz == 3))
                        continue;

                    var blockPos = playerBlockPos.add(ix, -1, iz);
                    var blockState = world.getBlockState(blockPos);
                    var upBlockState = world.getBlockState(blockPos.up());
                    if (blockState.getBlock() == Blocks.WATER && upBlockState.getBlock() == Blocks.AIR)
                        world.setBlockState(blockPos, Blocks.BLUE_ICE.getDefaultState());
                    if (blockState.getBlock() == Blocks.LAVA && upBlockState.getBlock() == Blocks.AIR)
                        world.setBlockState(blockPos, Blocks.MAGMA_BLOCK.getDefaultState());
                }
            }
        }

        super.tick();
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

}
