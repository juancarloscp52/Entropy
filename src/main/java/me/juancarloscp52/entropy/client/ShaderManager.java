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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ShaderManager {

    public static ShaderEffect register(Identifier id){
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            ShaderEffect shader;
            System.out.println(client.getFramebuffer());
            shader = new ShaderEffect(client.getTextureManager(), client.getResourceManager(),
                    client.getFramebuffer(), id);
            shader.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            return shader;
        }catch (IOException e){
            System.out.println("Could not read shader: "+e.getMessage());
        }
        return null;
    }

    public static void render(ShaderEffect shader, float tickDelta){
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.resetTextureMatrix();
        shader.render(tickDelta);
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
        RenderSystem.disableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // restore blending
        RenderSystem.enableDepthTest();
    }

}
