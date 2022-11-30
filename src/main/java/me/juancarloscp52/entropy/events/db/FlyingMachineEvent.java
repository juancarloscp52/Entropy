package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FlyingMachineEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if(player.getRandom().nextInt(2) == 0)
                spawnEastWest(player.getWorld(), player.getBlockPos().add(1, 2, -4));
            else
                spawnNorthSouth(player.getWorld(), player.getBlockPos().add(-4, 2, 1));
        }
    }

    private boolean tryClearArea(ServerWorld world, BlockPos startPos, int i, int i2) {
        for (int ix = 0; ix < i; ix++) {
            for (int iy = 0; iy < 4; iy++) {
                for (int iz = 0; iz < i2; iz++) {
                    var blockPos = startPos.add(ix, iy, iz);
                    var currentBlock = world.getBlockState(blockPos);
                    if (currentBlock.getBlock() == Blocks.BEDROCK ||
                            currentBlock.getBlock() == Blocks.END_PORTAL_FRAME ||
                            currentBlock.getBlock() == Blocks.END_PORTAL)
                        return false;

                    // Make sure flying machine have space to spawn
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                }
            }
        }
        return true; // Do not spawn flyting machine at all
    }

    private void spawnEastWest(ServerWorld world, BlockPos startPos) {
        if (!tryClearArea(world, startPos, 4, 8)) return;

        world.setBlockState(startPos.add(1, 1, 3), Blocks.STICKY_PISTON.getDefaultState().with(FacingBlock.FACING, Direction.EAST));
        world.setBlockState(startPos.add(1, 1, 4), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(1, 1, 5), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(1, 1, 6), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.SOUTH));
        
        world.setBlockState(startPos.add(2, 1, 1), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.NORTH));
        world.setBlockState(startPos.add(2, 1, 2), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(2, 1, 3), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(2, 1, 4), Blocks.STICKY_PISTON.getDefaultState().with(FacingBlock.FACING, Direction.WEST));
        
        world.setBlockState(startPos.add(1, 2, 4), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.SOUTH));
        world.setBlockState(startPos.add(1, 2, 5), Blocks.REDSTONE_LAMP.getDefaultState());
        
        world.setBlockState(startPos.add(2, 2, 2), Blocks.REDSTONE_LAMP.getDefaultState());
        world.setBlockState(startPos.add(2, 2, 3), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.NORTH));
    }

    private void spawnNorthSouth(ServerWorld world, BlockPos startPos) {
        if (!tryClearArea(world, startPos, 8, 4)) return;

        world.setBlockState(startPos.add(3, 1, 1), Blocks.STICKY_PISTON.getDefaultState().with(FacingBlock.FACING, Direction.SOUTH));
        world.setBlockState(startPos.add(4, 1, 1), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(5, 1, 1), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(6, 1, 1), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.EAST));
        
        world.setBlockState(startPos.add(1, 1, 2), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.WEST));
        world.setBlockState(startPos.add(2, 1, 2), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(3, 1, 2), Blocks.SLIME_BLOCK.getDefaultState());
        world.setBlockState(startPos.add(4, 1, 2), Blocks.STICKY_PISTON.getDefaultState().with(FacingBlock.FACING, Direction.NORTH));
        
        world.setBlockState(startPos.add(4, 2, 1), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.EAST));
        world.setBlockState(startPos.add(5, 2, 1), Blocks.REDSTONE_LAMP.getDefaultState());
        
        world.setBlockState(startPos.add(2, 2, 2), Blocks.REDSTONE_LAMP.getDefaultState());
        world.setBlockState(startPos.add(3, 2, 2), Blocks.OBSERVER.getDefaultState().with(FacingBlock.FACING, Direction.WEST));
    }


}
