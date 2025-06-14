package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;

public class NoiseMachineEvent extends AbstractInstantEvent {

    public static final EventType<NoiseMachineEvent> TYPE = EventType.builder(NoiseMachineEvent::new).build();
    private final int numModules = 2;

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerLevel world = serverPlayerEntity.level();
            BlockPos pos = serverPlayerEntity.blockPosition().above(15);
            // Check for end portal blocks & bedrock
            // Retry to place the noise machine higher up to 3 times, afterwards abort
            boolean placeable = false;
            outerloop:
                for (int retries = 3; retries >= 0; retries--) {
                    for (int ix = -2; ix <= 2; ix++) {
                        for (int iy = -1; iy <= 2; iy++) {
                            for (int iz = -((numModules / 2) + 1); iz <= (numModules + 1) / 2; iz++) {
                                var testPos = pos.offset(ix, iy, iz);
                                var currentBlock = world.getBlockState(testPos);
                                if (currentBlock.is(BlockTags.NOT_REPLACED_BY_EVENTS)) {
                                    pos = pos.above(4);
                                    continue outerloop;
                                }
                            }
                        }
                    }
                    placeable = true;
                    break;
                }
            if (!placeable) return;

            // Noise machine
            pos = pos.north((numModules / 2));
            for (int iz = numModules; iz > 0; iz--) {
                world.setBlockAndUpdate(pos, Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));
                world.setBlockAndUpdate(pos.west(1), Blocks.OBSIDIAN.defaultBlockState());
                world.setBlockAndUpdate(pos.east(1), Blocks.BELL.defaultBlockState());
                world.setBlockAndUpdate(pos.above(1), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));
                world.setBlockAndUpdate(pos.above(1).east(1), Blocks.OBSIDIAN.defaultBlockState());
                world.setBlockAndUpdate(pos.above(1).west(1), Blocks.BELL.defaultBlockState());
                // Obsidian casing
                for (int i = -2; i <= 2; i++) {
                    world.setBlockAndUpdate(pos.offset(i, -1, 0), Blocks.OBSIDIAN.defaultBlockState());
                    world.setBlockAndUpdate(pos.offset(i, 2, 0), Blocks.OBSIDIAN.defaultBlockState());
                    if (Math.abs(i) == 2) {
                        world.setBlockAndUpdate(pos.offset(i, 0, 0), Blocks.OBSIDIAN.defaultBlockState());
                        world.setBlockAndUpdate(pos.offset(i, 1, 0), Blocks.OBSIDIAN.defaultBlockState());
                    }
                }
                pos = pos.south();
            }
            // Remaining obsidian walls
            for (int iz = 0; iz < 2; iz++) {
                for (int ix = -2; ix <= 2; ix++) {
                    for (int iy = -1; iy <= 2; iy++) {
                        world.setBlockAndUpdate(pos.offset(ix, iy, 0), Blocks.OBSIDIAN.defaultBlockState());
                    }
                }
                pos = pos.north(numModules + 1);
            }
        });
    }

    @Override
    public EventType<NoiseMachineEvent> getType() {
        return TYPE;
    }
}
