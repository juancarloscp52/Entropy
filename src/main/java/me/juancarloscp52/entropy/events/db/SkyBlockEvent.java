/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.tick.ChunkTickScheduler;

public class SkyBlockEvent extends AbstractInstantEvent {

    private static byte[][][] _leavesPlacement = new byte[][][] {
            new byte[][] {
                    new byte[] { 0, 1, 1, 1, 1 },
                    new byte[] { 1, 1, 1, 1, 1 },
                    new byte[] { 1, 1, 0, 1, 1 },
                    new byte[] { 1, 1, 1, 1, 1 },
                    new byte[] { 1, 1, 1, 1, 0 },
            },
            new byte[][] {
                    new byte[] { 0, 1, 1, 1, 0 },
                    new byte[] { 1, 1, 1, 1, 1 },
                    new byte[] { 1, 1, 0, 1, 1 },
                    new byte[] { 1, 1, 1, 1, 1 },
                    new byte[] { 0, 1, 1, 1, 0 },
            },
            new byte[][] {
                    new byte[] { 0, 0, 0, 0, 0 },
                    new byte[] { 0, 1, 1, 0, 0 },
                    new byte[] { 0, 1, 0, 1, 0 },
                    new byte[] { 0, 0, 1, 0, 0 },
                    new byte[] { 0, 0, 0, 0, 0 },
            },
            new byte[][] {
                    new byte[] { 0, 0, 0, 0, 0 },
                    new byte[] { 0, 0, 1, 0, 0 },
                    new byte[] { 0, 1, 1, 1, 0 },
                    new byte[] { 0, 0, 1, 0, 0 },
                    new byte[] { 0, 0, 0, 0, 0 },
            }
    };

    @Override
    public void init() {

        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var world = serverPlayerEntity.getWorld();
            int height = 280;
            // Check if the player is in the nether or end.
            if(world.getRegistryKey() != World.OVERWORLD){
                height = 230;
                if(serverPlayerEntity.getWorld().getRegistryKey() == World.NETHER){
                    BlockPos pos = serverPlayerEntity.getBlockPos().withY(122);
                    for(int i= -3; i<=4;i++) {
                        for (int j = -3; j <= 4; j++) {
                            for (int z = -2; z <= 6; z++){
                                serverPlayerEntity.getWorld().setBlockState(new BlockPos(pos.getX()+i,pos.getY()+z,pos.getZ()+j),Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }
            }

            var startPos = serverPlayerEntity.getBlockPos().withY(height).east(4).north(1);

            // Main island
            for (int ix = 0; ix < 6; ix++) {
                for (int iy = 0; iy < 3; iy++) {
                    for (int iz = 0; iz < 3; iz++) {
                        if (iy == 2)
                            world.setBlockState(startPos.add(-ix, iy, iz), Blocks.GRASS_BLOCK.getDefaultState());
                        else
                            world.setBlockState(startPos.add(-ix, iy, iz), Blocks.DIRT.getDefaultState());
                    }
                }
            }
            for (int ix = 3; ix < 6; ix++) {
                for (int iy = 0; iy < 3; iy++) {
                    for (int iz = 3; iz < 6; iz++) {
                        if (iy == 2 && (ix != 5 || iz != 5))
                            world.setBlockState(startPos.add(-ix, iy, iz), Blocks.GRASS_BLOCK.getDefaultState());
                        else
                            world.setBlockState(startPos.add(-ix, iy, iz), Blocks.DIRT.getDefaultState());
                    }
                }
            }

            // Tree
            for (int iy = 3; iy < 9; iy++)
                world.setBlockState(startPos.add(-5, iy, 5), Blocks.OAK_LOG.getDefaultState());
            // Leaves
            for (int iy = 0; iy < _leavesPlacement.length; iy++)
                for (int ix = 0; ix < _leavesPlacement[iy].length; ix++)
                    for (int iz = 0; iz < _leavesPlacement[iy][ix].length; iz++)
                        if (_leavesPlacement[iy][ix][iz] == 1)
                            world.setBlockState(startPos.add(-3 - ix, 6 + iy, 3 + iz),
                                    Blocks.OAK_LEAVES.getDefaultState());

            // Chest on main island
            world.setBlockState(startPos.add(0, 3, 1),
                    Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.WEST));
            var chest1 = (ChestBlockEntity) world.getBlockEntity(startPos.add(0, 3, 1));
            chest1.setStack(0, new ItemStack(Items.LAVA_BUCKET));
            chest1.setStack(1, new ItemStack(Items.ICE));

            // Sand island
            for (int ix = 100; ix < 103; ix++)
                for (int iy = 0; iy < 3; iy++)
                    for (int iz = 0; iz < 3; iz++) {
                        var sandPos = startPos.add(-ix, iy, iz);

                        // Prevent sand from falling
                        world.getBlockTickScheduler().removeChunkTickScheduler(new ChunkPos(sandPos));

                        world.setBlockState(sandPos, Blocks.SAND.getDefaultState());

                        // Restore tick scheduler
                        world.getBlockTickScheduler().addChunkTickScheduler(new ChunkPos(sandPos), new ChunkTickScheduler<Block>());
                    }

            // Cactus
            world.setBlockState(startPos.add(-102, 3, 2), Blocks.CACTUS.getDefaultState());
            world.setBlockState(startPos.add(-102, 4, 2), Blocks.CACTUS.getDefaultState());

            // Chest on sand island
            world.setBlockState(startPos.add(-101, 3, 1),
                    Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.EAST));
            var chest2 = (ChestBlockEntity) world.getBlockEntity(startPos.add(-101, 3, 1));
            chest2.setStack(0, new ItemStack(Items.OBSIDIAN, 10));
            chest2.setStack(1, new ItemStack(Items.MELON_SLICE));
            chest2.setStack(2, new ItemStack(Items.PUMPKIN_SEEDS));

            var playerPos = startPos.add(-4, 3, 1);

            serverPlayerEntity.stopRiding();
            serverPlayerEntity.teleport(playerPos.getX() + .5, playerPos.getY(), playerPos.getZ() + .5);
        }
    }

}
