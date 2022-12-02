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

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class HerobrineEvent extends AbstractTimedEvent {

    private static final Identifier VIGNETTE_TEXTURE = new Identifier("entropy", "textures/vignette.png");
    Random random;
    MinecraftClient client;

    public HerobrineEvent() {
        random = new Random();
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = MinecraftClient.getInstance();
        Variables.customFog = true;
        client.getSoundManager().pauseAll();

    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        Variables.customFog = false;
        client = MinecraftClient.getInstance();
        client.getSoundManager().stopSounds(EntropyClient.herobrineAmbienceID, SoundCategory.BLOCKS);
        client.getSoundManager().resumeAll();
        this.hasEnded = true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickdelta) {
        renderVignetteOverlay();
    }

    @Override
    public void tick() {
        if (getTickCount() % 20 == 0)
            Entropy.getInstance().eventHandler.getActivePlayers().forEach(serverPlayerEntity -> {
                if (random.nextInt(100) >= 95)
                    serverPlayerEntity.damage(DamageSource.GENERIC, 1);
            });

        super.tick();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        PlayerEntity player = client.player;
        if (getTickCount() % 10 == 0) {
            playStepSound(getLandingPos(), player.getEntityWorld().getBlockState(getLandingPos()));
        }

        if (getTickCount() % 70 == 0) {
            player.getEntityWorld().playSound(player, player.getBlockPos(), EntropyClient.herobrineAmbience, SoundCategory.BLOCKS, 1, 0.9f);
        }

        super.tickClient();
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration * 1.25);
    }


    @Environment(EnvType.CLIENT)
    private BlockPos getLandingPos() {
        PlayerEntity player = client.player;
        int i = MathHelper.floor(player.getPos().x);
        int j = MathHelper.floor(player.getPos().y - 0.20000000298023224D);
        int k = MathHelper.floor(player.getPos().z);
        BlockPos blockPos = new BlockPos(i, j, k);
        if (player.getEntityWorld().getBlockState(blockPos).isAir()) {
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState = player.getEntityWorld().getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block.getDefaultState().isIn(BlockTags.FENCES) || block.getDefaultState().isIn(BlockTags.WALLS) || block instanceof FenceGateBlock) {
                return blockPos2;
            }
        }
        return blockPos;
    }

    @Environment(EnvType.CLIENT)
    private void playStepSound(BlockPos pos, BlockState state) {
        PlayerEntity player = client.player;
        if (!state.getMaterial().isLiquid()) {
            BlockState blockState = player.getEntityWorld().getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = blockState.isOf(Blocks.SNOW) ? blockState.getSoundGroup() : state.getSoundGroup();
            player.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.25F, blockSoundGroup.getPitch());
        }
    }

    @Environment(EnvType.CLIENT)
    private void renderVignetteOverlay() {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        float sin = 0.75f + MathHelper.abs(0.25f * MathHelper.sin(getTickCount() * 0.0625f));
        RenderSystem.setShaderColor(sin, sin, sin, 1.0f);
        int scaledHeight = client.getWindow().getScaledHeight();
        int scaledWidth = client.getWindow().getScaledWidth();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }

}
