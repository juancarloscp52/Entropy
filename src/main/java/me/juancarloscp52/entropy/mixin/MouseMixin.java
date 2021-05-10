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
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void driftMouse(CallbackInfo ci) {
        if (Variables.mouseDrifting) {
            this.cursorDeltaX += Variables.mouseDriftingSignX * 1.5d;
            this.cursorDeltaY += Variables.mouseDriftingSignY * 0.1d;
        }
    }

    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    public void changeLookDirection(ClientPlayerEntity clientPlayerEntity, double cursorDeltaX, double cursorDeltaY) {
        int i = Variables.invertedControls ? -1 : 1;
        clientPlayerEntity.changeLookDirection(i * cursorDeltaX, i * cursorDeltaY);
    }

}
