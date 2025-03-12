package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;

public class BulldozeEvent extends AbstractTimedEvent {
    public static final EventType<BulldozeEvent> TYPE = EventType.builder(BulldozeEvent::new).build();

    @Override
    public void tick() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = player.level();
            var playerBlockPos = player.blockPosition();
            for (int ix = -1; ix <= 1; ix++) {
                for (int iy = 0; iy <= 2; iy++) {
                    for (int iz = -1; iz <= 1; iz++) {
                        var blockPos = playerBlockPos.offset(ix, iy, iz);
                        var state = world.getBlockState(blockPos);
                        if (state.is(BlockTags.NOT_REPLACED_BY_EVENTS))
                            continue;
                        world.destroyBlock(blockPos, true);
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * .5);
    }

    @Override
    public EventType<BulldozeEvent> getType() {
        return TYPE;
    }
}
