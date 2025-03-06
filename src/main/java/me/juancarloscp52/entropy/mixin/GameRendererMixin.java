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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.ShaderManager;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow private float oldFovModifier;

    @Shadow private float fovModifier;

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void changeFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (Variables.forcedFov) {
            if (Variables.ignoreVariableFov) {
                cir.setReturnValue((double) Variables.fov * Mth.lerp(minecraft.options.fovEffectScale().get(), Variables.fov, 1.0D));
            } else {
                cir.setReturnValue(updateFov(camera, tickDelta, changingFov, Variables.fov));
            }
        }
    }
    private double updateFov(Camera camera, float tickDelta, boolean changingFov, double fovValue) {
        {
            double fov = 70.0D;
            if (changingFov) {
                fov = fovValue;
                fov *= Mth.lerp(tickDelta, this.oldFovModifier, this.fovModifier);
            }

            if (camera.getEntity() instanceof LivingEntity && ((LivingEntity) camera.getEntity()).isDeadOrDying()) {
                float f = Math.min((float) ((LivingEntity) camera.getEntity()).deathTime + tickDelta, 20.0F);
                fov /= ((1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F);
            }
            FogType cameraSubmersionType = camera.getFluidInCamera();
            if (cameraSubmersionType == FogType.LAVA || cameraSubmersionType == FogType.WATER) {
                fov *= Mth.lerp(this.minecraft.options.fovEffectScale().get(), 1.0F, 0.85714287F);
            }
//            FluidState fluidState = camera.getSubmergedFluidState();
//            if (!fluidState.isEmpty()) {
//                fov = fov * 60.0D / 70.0D;
//            }

            return fov;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    public void renderShaders(DeltaTracker tickCounter, boolean tick, CallbackInfo ci){
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
    public void renderBlackWhiteShader(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
        if (Variables.blackAndWhite) {
            ShaderManager.render(ShaderManager.BLACK_AND_WHITE, minecraft, resourcePool);
        }
    }

    @ModifyVariable(method = "renderLevel", at = @At("STORE"), ordinal = 0)
    private PoseStack matrixStack(PoseStack matrixStack) {
        if (Variables.cameraRoll != 0f) {
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Variables.cameraRoll));
        }
        return matrixStack;
    }
}
