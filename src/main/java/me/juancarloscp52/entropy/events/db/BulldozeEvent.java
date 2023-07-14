package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.gui.DrawContext;

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
                        if (state.isIn(BlockTags.NOT_REPLACED_BY_EVENTS))
                            continue;
                        world.breakBlock(blockPos, true);
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration * .5);
    }

}
