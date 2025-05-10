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

package me.juancarloscp52.entropy.client;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class ShaderManager {
    public static final ResourceLocation BLACK_AND_WHITE = ResourceLocation.fromNamespaceAndPath("entropy", "black_and_white");
    public static final ResourceLocation BLUR = ResourceLocation.fromNamespaceAndPath("entropy", "blur");
    public static final ResourceLocation CRT = ResourceLocation.fromNamespaceAndPath("entropy", "crt");
    public static final ResourceLocation INVERTED = ResourceLocation.withDefaultNamespace("invert");
    public static final ResourceLocation WOBBLE = ResourceLocation.fromNamespaceAndPath("entropy", "wobble");

    public static void render(ResourceLocation postEffectId, Minecraft minecraft, GraphicsResourceAllocator graphicsResourceAllocator) {
        PostChain postChain = minecraft.getShaderManager().getPostChain(postEffectId, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            postChain.process(minecraft.getMainRenderTarget(), graphicsResourceAllocator, null);
        }
    }
}
