package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;

public class BulldozeEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = player.getWorld();
            var playerBlockPos = player.getBlockPos();
            for (int ix = -1; ix <= 1; ix++) {
                for (int iy = 0; iy <= 2; iy++) {
                    for (int iz = -1; iz <= 1; iz++) {
                        var blockPos = playerBlockPos.add(ix, iy, iz);
                        var state = world.getBlockState(blockPos);
                        var block = state.getBlock();
                        if (block == Blocks.BEDROCK || block == Blocks.END_PORTAL || block == Blocks.END_PORTAL_FRAME)
                            continue;
                        world.breakBlock(blockPos, true);
                    }
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
        return (short) (Entropy.getInstance().settings.baseEventDuration * .5);
    }

}
