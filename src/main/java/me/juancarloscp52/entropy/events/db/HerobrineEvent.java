/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyClientUtils;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class HerobrineEvent extends AbstractTimedEvent {

    public static final EventType<HerobrineEvent> TYPE = EventType.builder(HerobrineEvent::new).build();
    private static final ResourceLocation VIGNETTE_TEXTURE = ResourceLocation.fromNamespaceAndPath("entropy", "textures/vignette.png");
    Random random;
    Minecraft client;

    public HerobrineEvent() {
        random = new Random();
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = Minecraft.getInstance();
        Variables.customFog = true;
        client.getSoundManager().pause();

    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        Variables.customFog = false;
        client = Minecraft.getInstance();
        client.getSoundManager().stop(EntropyClient.herobrineAmbienceID, SoundSource.BLOCKS);
        client.getSoundManager().resume();
        super.endClient();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
        float sin = 0.75f + Mth.abs(0.25f * Mth.sin(getTickCount() * 0.0625f));
        EntropyClientUtils.renderOverlay(drawContext, VIGNETTE_TEXTURE, ARGB.colorFromFloat(1.0F, sin, sin, sin));
    }

    @Override
    public void tick() {
        if (getTickCount() % 20 == 0)
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                if (random.nextInt(100) >= 95)
                    serverPlayerEntity.hurt(serverPlayerEntity.damageSources().generic(), 1);
            });

        super.tick();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        Player player = client.player;
        if (getTickCount() % 10 == 0) {
            playStepSound(getLandingPos(), player.getCommandSenderWorld().getBlockState(getLandingPos()));
        }

        if (getTickCount() % 70 == 0) {
            player.getCommandSenderWorld().playSound(player, player.blockPosition(), EntropyClient.herobrineAmbience, SoundSource.BLOCKS, 1, 0.9f);
        }

        super.tickClient();
    }

    @Override
    public short getDuration() {
        return (short) (super.getDuration() * 1.25);
    }


    @Environment(EnvType.CLIENT)
    private BlockPos getLandingPos() {
        Player player = client.player;
        int i = Mth.floor(player.position().x);
        int j = Mth.floor(player.position().y - 0.20000000298023224D);
        int k = Mth.floor(player.position().z);
        BlockPos blockPos = new BlockPos(i, j, k);
        if (player.getCommandSenderWorld().getBlockState(blockPos).isAir()) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState = player.getCommandSenderWorld().getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block.defaultBlockState().is(BlockTags.FENCES) || block.defaultBlockState().is(BlockTags.WALLS) || block instanceof FenceGateBlock) {
                return blockPos2;
            }
        }
        return blockPos;
    }

    @Environment(EnvType.CLIENT)
    private void playStepSound(BlockPos pos, BlockState state) {
        Player player = client.player;
        if (!state.liquid()) {
            BlockState blockState = player.getCommandSenderWorld().getBlockState(pos.above());
            SoundType blockSoundGroup = blockState.is(Blocks.SNOW) ? blockState.getSoundType() : state.getSoundType();
            player.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.25F, blockSoundGroup.getPitch());
        }
    }

    @Override
    public EventType<HerobrineEvent> getType() {
        return TYPE;
    }
}
