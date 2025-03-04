package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MLGBucketEvent extends AbstractTimedEvent {
    private Map<Level, List<BlockPos>> placedWaterSourcePositions = new HashMap<>();

    @Override
    public void tick() {
        super.tick();
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::doTickForPlayer);

        if(tickCount % 20 == 0)
            removeAllPlacedWaterSources();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        super.tickClient();
        doTickForPlayer(Minecraft.getInstance().player);

        if(tickCount % 20 == 0)
            removeAllPlacedWaterSources();
    }

    @Override
    public void end() {
        super.end();
        removeAllPlacedWaterSources();
    }

    @Override
    public void endClient() {
        super.endClient();
        removeAllPlacedWaterSources();
    }

    private void doTickForPlayer(Player player) {
        Level world = player.level();
        BlockPos posDown = player.blockPosition().below();
        BlockState state = world.getBlockState(posDown);

        //place water if there is a replaceable block (includes air) directly below the player, and a solid block below that one
        if(state.canBeReplaced()) {
            state = world.getBlockState(posDown.below());

            if(!state.canBeReplaced()) {
                world.setBlockAndUpdate(posDown, Blocks.WATER.defaultBlockState());
                addPositionToList(world, posDown);
            }
        }
    }

    private void addPositionToList(Level world, BlockPos pos) {
        if(!placedWaterSourcePositions.containsKey(world))
            placedWaterSourcePositions.put(world, new ArrayList<>());

        placedWaterSourcePositions.get(world).add(pos);
    }

    private void removeAllPlacedWaterSources() {
        placedWaterSourcePositions.keySet().forEach(world -> {
            List<BlockPos> positions = placedWaterSourcePositions.get(world);
            positions.forEach(pos -> world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()));
            positions.clear();
        });
    }
}
