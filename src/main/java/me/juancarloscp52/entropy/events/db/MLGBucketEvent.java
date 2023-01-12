package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MLGBucketEvent extends AbstractTimedEvent {
    private Map<World, List<BlockPos>> placedWaterSourcePositions = new HashMap<>();

    @Override
    public void tick() {
        super.tick();
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::doTickForPlayer);

        if(tickCount % 20 == 0 )
            removeAllPlacedWaterSources();
    }

    @Override
    public void tickClient() {
        super.tickClient();
        doTickForPlayer(MinecraftClient.getInstance().player);
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

    private void doTickForPlayer(PlayerEntity player) {
        World world = player.world;
        BlockPos posDown = player.getBlockPos().down();
        BlockState state = world.getBlockState(posDown);

        //place water if there is a replaceable block (includes air) directly below the player, and a solid block below that one
        if(state.isReplaceable()) {
            state = world.getBlockState(posDown.down());

            if(!state.isReplaceable()) {
                world.setBlockState(posDown, Blocks.WATER.getDefaultState());
                addPositionToList(world, posDown);
            }
        }
    }

    private void addPositionToList(World world, BlockPos pos) {
        if(!placedWaterSourcePositions.containsKey(world))
            placedWaterSourcePositions.put(world, new ArrayList<>());

        placedWaterSourcePositions.get(world).add(pos);
    }

    private void removeAllPlacedWaterSources() {
        placedWaterSourcePositions.keySet().forEach(world -> {
            List<BlockPos> positions = placedWaterSourcePositions.get(world);
            positions.forEach(pos -> world.setBlockState(pos, Blocks.AIR.getDefaultState()));
            positions.clear();
        });
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
