package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;

public class HauntedChestsEvent extends AbstractTimedEvent {
    public static final EventType<HauntedChestsEvent> TYPE = EventType.builder(HauntedChestsEvent::new).build();
    private List<ChestBlockEntity> openedChests = new ArrayList<>();

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        super.tickClient();

        if(tickCount % 20 == 0) {
            Player player = Minecraft.getInstance().player;
            BlockPos.MutableBlockPos pos = player.blockPosition().mutable();
            Level level = player.level();

            for(int x = -16; x <= 16; x += 16) {
                for(int z = -16; z <= 16; z += 16) {
                    pos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    LevelChunk chunk = level.getChunkAt(pos);

                    for(BlockEntity be : chunk.getBlockEntitiesPos().stream().map(chunk::getBlockEntity).toList()) {
                        if(player.getRandom().nextInt(10) >= 7 && be instanceof ChestBlockEntity chest) {
                            if(openedChests.contains(chest)) {
                                chest.stopOpen(player);
                                openedChests.remove(chest);
                                level.playSound(player, chest.getBlockPos(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1f, 1f);
                            }
                            else {
                                chest.startOpen(player);
                                openedChests.add(chest);
                                level.playSound(player, chest.getBlockPos(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1f, 1f);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        super.endClient();
        Player player = Minecraft.getInstance().player;

        if(!openedChests.isEmpty()) {
            openedChests.forEach(chest -> {
                chest.stopOpen(player);
                chest.getLevel().playSound(player, chest.getBlockPos(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1f, 1f);
            });
            openedChests.clear();
        }
    }

    @Override
    public EventType<HauntedChestsEvent> getType() {
        return TYPE;
    }
}
