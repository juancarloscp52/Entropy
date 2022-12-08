package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class NoiseMachineEvent extends AbstractInstantEvent {

    private final int numModules = 2;

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
            ServerWorld world = serverPlayerEntity.getWorld();
            BlockPos pos = serverPlayerEntity.getBlockPos().up(15);
            // Check for end portal blocks & bedrock
            // Retry to place the noise machine higher up to 3 times, afterwards abort
            boolean placeable = false;
            outerloop:
            for (int retries = 3; retries >= 0; retries--) {
                for (int ix = -2; ix <= 2; ix++) {
                    for (int iy = -1; iy <= 2; iy++) {
                        for (int iz = -((numModules / 2) + 1); iz <= (numModules + 1) / 2; iz++) {
                            var testPos = pos.add(ix, iy, iz);
                            var currentBlock = world.getBlockState(testPos);
                            if (currentBlock.getBlock() == Blocks.BEDROCK ||
                                    currentBlock.getBlock() == Blocks.END_PORTAL_FRAME ||
                                    currentBlock.getBlock() == Blocks.END_PORTAL) {
                                pos = pos.up(4);
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
                world.setBlockState(pos, Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.EAST));
                world.setBlockState(pos.west(1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(pos.east(1), Blocks.BELL.getDefaultState());
                world.setBlockState(pos.up(1), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.WEST));
                world.setBlockState(pos.up(1).east(1), Blocks.OBSIDIAN.getDefaultState());
                world.setBlockState(pos.up(1).west(1), Blocks.BELL.getDefaultState());
                // Obsidian casing
                for (int i = -2; i <= 2; i++) {
                    world.setBlockState(pos.add(i, -1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(i, 2, 0), Blocks.OBSIDIAN.getDefaultState());
                    if (Math.abs(i) == 2) {
                        world.setBlockState(pos.add(i, 0, 0), Blocks.OBSIDIAN.getDefaultState());
                        world.setBlockState(pos.add(i, 1, 0), Blocks.OBSIDIAN.getDefaultState());
                    }
                }
                pos = pos.south();
            }
            // Remaining obsidian walls
            for (int iz = 0; iz < 2; iz++) {
                for (int ix = -2; ix <= 2; ix++) {
                    for (int iy = -1; iy <= 2; iy++) {
                        world.setBlockState(pos.add(ix, iy, 0), Blocks.OBSIDIAN.getDefaultState());
                    }
                }
                pos = pos.north(numModules + 1);
            }
        });
    }

}
