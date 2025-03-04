package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;

public class FlyingMachineEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if(player.getRandom().nextInt(2) == 0)
                spawnEastWest(player.serverLevel(), player.blockPosition().offset(1, 2, -4));
            else
                spawnNorthSouth(player.serverLevel(), player.blockPosition().offset(-4, 2, 1));
        }
    }

    private boolean tryClearArea(ServerLevel world, BlockPos startPos, int i, int i2) {
        for (int ix = 0; ix < i; ix++) {
            for (int iy = 0; iy < 4; iy++) {
                for (int iz = 0; iz < i2; iz++) {
                    var blockPos = startPos.offset(ix, iy, iz);
                    var currentBlock = world.getBlockState(blockPos);
                    if (currentBlock.getBlock() == Blocks.BEDROCK ||
                            currentBlock.getBlock() == Blocks.END_PORTAL_FRAME ||
                            currentBlock.getBlock() == Blocks.END_PORTAL)
                        return false;

                    // Make sure flying machine have space to spawn
                    world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }
            }
        }
        return true; // Do not spawn flyting machine at all
    }

    private void spawnEastWest(ServerLevel world, BlockPos startPos) {
        if (!tryClearArea(world, startPos, 4, 8)) return;

        world.setBlockAndUpdate(startPos.offset(1, 1, 3), Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));
        world.setBlockAndUpdate(startPos.offset(1, 1, 4), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(1, 1, 5), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(1, 1, 6), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH));

        world.setBlockAndUpdate(startPos.offset(2, 1, 1), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH));
        world.setBlockAndUpdate(startPos.offset(2, 1, 2), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(2, 1, 3), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(2, 1, 4), Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));

        world.setBlockAndUpdate(startPos.offset(1, 2, 4), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH));
        world.setBlockAndUpdate(startPos.offset(1, 2, 5), Blocks.REDSTONE_LAMP.defaultBlockState());

        world.setBlockAndUpdate(startPos.offset(2, 2, 2), Blocks.REDSTONE_LAMP.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(2, 2, 3), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH));
    }

    private void spawnNorthSouth(ServerLevel world, BlockPos startPos) {
        if (!tryClearArea(world, startPos, 8, 4)) return;

        world.setBlockAndUpdate(startPos.offset(3, 1, 1), Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH));
        world.setBlockAndUpdate(startPos.offset(4, 1, 1), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(5, 1, 1), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(6, 1, 1), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));

        world.setBlockAndUpdate(startPos.offset(1, 1, 2), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));
        world.setBlockAndUpdate(startPos.offset(2, 1, 2), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(3, 1, 2), Blocks.SLIME_BLOCK.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(4, 1, 2), Blocks.STICKY_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH));

        world.setBlockAndUpdate(startPos.offset(4, 2, 1), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));
        world.setBlockAndUpdate(startPos.offset(5, 2, 1), Blocks.REDSTONE_LAMP.defaultBlockState());

        world.setBlockAndUpdate(startPos.offset(2, 2, 2), Blocks.REDSTONE_LAMP.defaultBlockState());
        world.setBlockAndUpdate(startPos.offset(3, 2, 2), Blocks.OBSERVER.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));
    }


}
