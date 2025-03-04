package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.ArrayList;
import java.util.HashMap;

public class RainbowPathEvent extends AbstractTimedEvent {

    private static ArrayList<Block> _rainbowBlocks = new ArrayList<Block>() {
        {
            add(Blocks.RED_CONCRETE);
            add(Blocks.ORANGE_CONCRETE);
            add(Blocks.YELLOW_CONCRETE);
            add(Blocks.GREEN_CONCRETE);
            add(Blocks.LIGHT_BLUE_CONCRETE);
            add(Blocks.BLUE_CONCRETE);
            add(Blocks.PURPLE_CONCRETE);
        }
    };

    private HashMap<ServerPlayer, Integer> _playerStates = new HashMap<>();

    @Override
    public void init() {
        _playerStates.clear();
    }

    @Override
    public void tick() {

        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var playerState = 0;
            if(_playerStates.containsKey(player))
                playerState = _playerStates.get(player);

            var world = player.level();
            var blockPos = player.blockPosition().offset(0, -1, 0);
            var state = world.getBlockState(blockPos);
            if(state.is(BlockTags.NOT_REPLACED_BY_EVENTS))
                continue;

            if(state.getBlock().equals(_rainbowBlocks.get(playerState % _rainbowBlocks.size())))
                continue;

            playerState++;
            world.setBlockAndUpdate(blockPos, _rainbowBlocks.get(playerState % _rainbowBlocks.size()).defaultBlockState());
            _playerStates.put(player, playerState);
        }

        super.tick();
    }

}
