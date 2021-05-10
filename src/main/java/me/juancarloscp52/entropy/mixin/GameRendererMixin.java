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

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private float lastMovementFovMultiplier;

    @Shadow
    private float movementFovMultiplier;

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void changeFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (Variables.invertedFov) {
            cir.setReturnValue(updateFov(camera, tickDelta, changingFov, -client.options.fov));
        } else if (Variables.forcedFov) {
            if (Variables.ignoreVariableFov) {
                cir.setReturnValue((double) Variables.fov);
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
                fov *= MathHelper.lerp(tickDelta, this.lastMovementFovMultiplier, this.movementFovMultiplier);
            }

            if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity) camera.getFocusedEntity()).isDead()) {
                float f = Math.min((float) ((LivingEntity) camera.getFocusedEntity()).deathTime + tickDelta, 20.0F);
                fov /= ((1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F);
            }

            FluidState fluidState = camera.getSubmergedFluidState();
            if (!fluidState.isEmpty()) {
                fov = fov * 60.0D / 70.0D;
            }

            return fov;
        }
    }
}
