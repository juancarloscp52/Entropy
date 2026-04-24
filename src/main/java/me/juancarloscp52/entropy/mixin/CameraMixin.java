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
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    final int cameraYDistance = 8;

    @Shadow private Entity entity;

    @Shadow private Minecraft minecraft;

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void setPosition(double x, double y, double z);

    @Shadow private float eyeHeightOld;

    @Shadow private float eyeHeight;

    @Inject(method = "update",at=@At("TAIL"))
    private void update(DeltaTracker deltaTracker, CallbackInfo ci){
        if(!Variables.topView)
            return;
        Entity focusedEntity = this.entity;
        if (focusedEntity == null) {
            return;
        }
        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(true);
        this.setRotation(0, +90);
        this.setPosition(Mth.lerp(tickDelta, focusedEntity.xo, focusedEntity.getX()), Mth.lerp(tickDelta, focusedEntity.yo+cameraYDistance, focusedEntity.getY()+cameraYDistance) + (double)Mth.lerp(tickDelta, this.eyeHeightOld+cameraYDistance, this.eyeHeight+cameraYDistance), Mth.lerp(tickDelta, focusedEntity.zo, focusedEntity.getZ()));
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void changeFov(CallbackInfoReturnable<Float> cir) {
        if (Variables.forcedFov) {
            cir.setReturnValue((float) Variables.fov);
        }
    }

}
