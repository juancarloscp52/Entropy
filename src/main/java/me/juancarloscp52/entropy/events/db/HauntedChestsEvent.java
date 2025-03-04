package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.ArrayList;
import java.util.List;

public class HauntedChestsEvent extends AbstractTimedEvent {
    private List<ChestBlockEntity> openedChests = new ArrayList<>();

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        super.tickClient();

        if(tickCount % 20 == 0) {
            Player player = Minecraft.getInstance().player;
            BlockPos.MutableBlockPos pos = player.blockPosition().mutable();
            Level world = player.level();
            boolean chestOpened = false;
            boolean chestClosed = false;

            for(int x = -16; x <= 16; x += 16) {
                for(int z = -16; z <= 16; z += 16) {
                    pos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    LevelChunk chunk = world.getChunkAt(pos);

                    for(BlockEntity be : chunk.getBlockEntitiesPos().stream().map(chunk::getBlockEntity).toList()) {
                        if(player.getRandom().nextInt(10) >= 7 && be instanceof ChestBlockEntity chest) {
                            if(openedChests.contains(chest)) {
                                chest.stopOpen(player);
                                openedChests.remove(chest);
                                chestClosed = true;
                            }
                            else {
                                chest.startOpen(player);
                                openedChests.add(chest);
                                chestOpened = true;
                            }
                        }
                    }
                }
            }

            if(chestOpened)
                player.playSound(SoundEvents.CHEST_OPEN, 1f, 1f);

            if(chestClosed)
                player.playSound(SoundEvents.CHEST_CLOSE, 1f, 1f);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        super.endClient();
        Player player = Minecraft.getInstance().player;

        if(openedChests.size() > 0) {
            openedChests.forEach(chest -> chest.stopOpen(player));
            openedChests.clear();
            player.playSound(SoundEvents.CHEST_CLOSE, 1f, 1f);
        }
    }
}
