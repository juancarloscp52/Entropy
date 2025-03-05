package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.HashMap;

public class InfestationEvent extends AbstractInstantEvent {

    private static HashMap<Block, Block> _blockConvertion = new HashMap<Block, Block>() {
        {
            put(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
            put(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
            put(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
            put(Blocks.DEEPSLATE, Blocks.INFESTED_DEEPSLATE);
            put(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
            put(Blocks.STONE, Blocks.INFESTED_STONE);
            put(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
        }
    };

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var rng = player.getRandom();
            var world = player.level();
            var startPos = player.blockPosition().offset(-32, -32, -32);
            var endPos = startPos.offset(64, 64, 64);
            for (int ix = startPos.getX(); ix < endPos.getX(); ix++) {
                for (int iy = startPos.getY(); iy < endPos.getY(); iy++) {
                    for (int iz = startPos.getZ(); iz < endPos.getZ(); iz++) {
                        if(rng.nextDouble() < 0.5d)
                            continue;
                        var blockPos = new BlockPos(ix, iy, iz);
                        var block = world.getBlockState(blockPos).getBlock();
                        if (_blockConvertion.containsKey(block))
                            world.setBlockAndUpdate(blockPos, _blockConvertion.get(block).defaultBlockState());
                    }
                }
            }
        }
    }

}
