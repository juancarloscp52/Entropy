package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class HauntedChestsEvent extends AbstractTimedEvent {
    private List<ChestBlockEntity> openedChests = new ArrayList<>();

    @Override
    public void tickClient() {
        super.tick();

        if(tickCount % 20 == 0) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            BlockPos.Mutable pos = player.getBlockPos().mutableCopy();
            World world = player.getWorld();
            boolean chestOpened = false;
            boolean chestClosed = false;

            for(int x = -16; x <= 16; x += 16) {
                for(int z = -16; z <= 16; z += 16) {
                    pos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    WorldChunk chunk = world.getWorldChunk(pos);

                    for(BlockEntity be : chunk.getBlockEntityPositions().stream().map(chunk::getBlockEntity).toList()) {
                        if(player.getRandom().nextInt(10) >= 7 && be instanceof ChestBlockEntity chest) {
                            if(openedChests.contains(chest)) {
                                chest.onClose(player);
                                openedChests.remove(chest);
                                chestClosed = true;
                            }
                            else {
                                chest.onOpen(player);
                                openedChests.add(chest);
                                chestOpened = true;
                            }
                        }
                    }
                }
            }

            if(chestOpened)
                player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 1f, 1f);

            if(chestClosed)
                player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1f, 1f);
        }
    }

    @Override
    public void endClient() {
        super.endClient();
        PlayerEntity player = MinecraftClient.getInstance().player;

        if(openedChests.size() > 0) {
            openedChests.forEach(chest -> chest.onClose(player));
            openedChests.clear();
            player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 1f, 1f);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
