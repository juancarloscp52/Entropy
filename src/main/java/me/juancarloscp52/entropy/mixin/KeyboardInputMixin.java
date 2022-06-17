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
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyEvents(boolean slowDown, float f, CallbackInfo ci) {
        if (Variables.forceForward) {
            this.pressingForward = true;
            this.movementForward = 1;
        } else if (Variables.onlySidewaysMovement) {
            this.movementForward = 0;
            this.pressingForward = false;
            this.pressingBack = false;
        } else if (Variables.onlyBackwardsMovement) {
            this.pressingForward = false;
            this.pressingLeft = false;
            this.pressingRight = false;
            this.movementSideways = 0;
            this.movementForward = this.movementForward <= 0 ? this.movementForward : 0;
        } else if (Variables.invertedControls) {
            this.movementSideways = -movementSideways;
            this.movementForward = -movementForward;
        }
        if (Variables.forceJump) {
            this.jumping = true;
        } else if (Variables.noJump) {
            this.jumping = false;
        }


    }

}
