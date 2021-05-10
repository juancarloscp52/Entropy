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
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    int cameraYDistance = 8;
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Shadow private float lastCameraY;

    @Shadow private float cameraY;

    @Inject(method = "update",at=@At("TAIL"))
    private void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci){
        if(!Variables.topView)
            return;
        this.setRotation(0, +90);
        this.setPos(MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()), MathHelper.lerp(tickDelta, focusedEntity.prevY+cameraYDistance, focusedEntity.getY()+cameraYDistance) + (double)MathHelper.lerp(tickDelta, this.lastCameraY+cameraYDistance, this.cameraY+cameraYDistance), MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
    }


}
