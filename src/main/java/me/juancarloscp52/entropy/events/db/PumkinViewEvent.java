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

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PumkinViewEvent extends AbstractTimedEvent {

    private static final Identifier PUMKIN_TEXTURE = new Identifier("textures/misc/pumpkinblur.png");
    MinecraftClient client;

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = MinecraftClient.getInstance();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        this.hasEnded = true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickdelta) {
        renderVignetteOverlay();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickClient() {
        super.tickClient();
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration * 1.25);
    }

    @Environment(EnvType.CLIENT)
    private void renderVignetteOverlay() {

        int scaledHeight = client.getWindow().getScaledHeight();
        int scaledWidth = client.getWindow().getScaledWidth();RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, PUMKIN_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, scaledHeight, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

}
