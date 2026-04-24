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

package me.juancarloscp52.entropy.mixin;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.client.ShaderManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    public void renderShaders(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (Variables.blur) {
            ShaderManager.render(ShaderManager.BLUR, minecraft, resourcePool);
        } else if (Variables.invertedShader) {
            ShaderManager.render(ShaderManager.INVERTED, minecraft, resourcePool);
        } else if (Variables.wobble) {
            ShaderManager.render(ShaderManager.WOBBLE, minecraft, resourcePool);
        } else if (Variables.monitor) {
            ShaderManager.render(ShaderManager.CRT, minecraft, resourcePool);
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void renderBlackWhiteShader(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        if (Variables.blackAndWhite) {
            ShaderManager.render(ShaderManager.BLACK_AND_WHITE, minecraft, resourcePool);
        }
    }

    @ModifyArg(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;ZLnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)V"
        ),
        index = 4
    )
    private Matrix4fc rotateLevelMatrix(Matrix4fc matrix) {
        if (Variables.cameraRoll != 0f) {
            return new Matrix4f(matrix).rotateZ((float) Math.toRadians(Variables.cameraRoll));
        }
        return matrix;
    }
}
