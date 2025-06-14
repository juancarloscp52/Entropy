/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelChunkTicks;

public class SkyBlockEvent extends AbstractInstantEvent {

    public static final EventType<SkyBlockEvent> TYPE = EventType.builder(SkyBlockEvent::new).build();
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
            var world = serverPlayerEntity.level();
            int height = 280;
            // Check if the player is in the nether or end.
            if(world.dimension() != Level.OVERWORLD){
                height = 230;
                if(serverPlayerEntity.level().dimension() == Level.NETHER){
                    BlockPos pos = serverPlayerEntity.blockPosition().atY(122);
                    for(int i= -3; i<=4;i++) {
                        for (int j = -3; j <= 4; j++) {
                            for (int z = -2; z <= 6; z++){
                                serverPlayerEntity.level().setBlockAndUpdate(new BlockPos(pos.getX()+i,pos.getY()+z,pos.getZ()+j),Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }

            var startPos = serverPlayerEntity.blockPosition().atY(height).east(4).north(1);

            // Main island
            for (int ix = 0; ix < 6; ix++) {
                for (int iy = 0; iy < 3; iy++) {
                    for (int iz = 0; iz < 3; iz++) {
                        if (iy == 2)
                            world.setBlockAndUpdate(startPos.offset(-ix, iy, iz), Blocks.GRASS_BLOCK.defaultBlockState());
                        else
                            world.setBlockAndUpdate(startPos.offset(-ix, iy, iz), Blocks.DIRT.defaultBlockState());
                    }
                }
            }
            for (int ix = 3; ix < 6; ix++) {
                for (int iy = 0; iy < 3; iy++) {
                    for (int iz = 3; iz < 6; iz++) {
                        if (iy == 2 && (ix != 5 || iz != 5))
                            world.setBlockAndUpdate(startPos.offset(-ix, iy, iz), Blocks.GRASS_BLOCK.defaultBlockState());
                        else
                            world.setBlockAndUpdate(startPos.offset(-ix, iy, iz), Blocks.DIRT.defaultBlockState());
                    }
                }
            }

            // Tree
            for (int iy = 3; iy < 9; iy++)
                world.setBlockAndUpdate(startPos.offset(-5, iy, 5), Blocks.OAK_LOG.defaultBlockState());
            // Leaves
            for (int iy = 0; iy < _leavesPlacement.length; iy++)
                for (int ix = 0; ix < _leavesPlacement[iy].length; ix++)
                    for (int iz = 0; iz < _leavesPlacement[iy][ix].length; iz++)
                        if (_leavesPlacement[iy][ix][iz] == 1)
                            world.setBlockAndUpdate(startPos.offset(-3 - ix, 6 + iy, 3 + iz),
                                    Blocks.OAK_LEAVES.defaultBlockState());

            // Chest on main island
            world.setBlockAndUpdate(startPos.offset(0, 3, 1),
                    Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST));
            var chest1 = (ChestBlockEntity) world.getBlockEntity(startPos.offset(0, 3, 1));
            chest1.setItem(0, new ItemStack(Items.LAVA_BUCKET));
            chest1.setItem(1, new ItemStack(Items.ICE));

            // Sand island
            for (int ix = 100; ix < 103; ix++)
                for (int iy = 0; iy < 3; iy++)
                    for (int iz = 0; iz < 3; iz++) {
                        var sandPos = startPos.offset(-ix, iy, iz);

                        // Prevent sand from falling
                        world.getBlockTicks().removeContainer(new ChunkPos(sandPos));

                        world.setBlockAndUpdate(sandPos, Blocks.SAND.defaultBlockState());

                        // Restore tick scheduler
                        world.getBlockTicks().addContainer(new ChunkPos(sandPos), new LevelChunkTicks<Block>());
                    }

            // Cactus
            world.setBlockAndUpdate(startPos.offset(-102, 3, 2), Blocks.CACTUS.defaultBlockState());
            world.setBlockAndUpdate(startPos.offset(-102, 4, 2), Blocks.CACTUS.defaultBlockState());

            // Chest on sand island
            world.setBlockAndUpdate(startPos.offset(-101, 3, 1),
                    Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.EAST));
            var chest2 = (ChestBlockEntity) world.getBlockEntity(startPos.offset(-101, 3, 1));
            chest2.setItem(0, new ItemStack(Items.OBSIDIAN, 10));
            chest2.setItem(1, new ItemStack(Items.MELON_SLICE));
            chest2.setItem(2, new ItemStack(Items.PUMPKIN_SEEDS));

            var playerPos = startPos.offset(-4, 3, 1);

            EntropyUtils.teleportPlayer(serverPlayerEntity, Vec3.atBottomCenterOf(playerPos));
        }
    }

    @Override
    public EventType<SkyBlockEvent> getType() {
        return TYPE;
    }
}
