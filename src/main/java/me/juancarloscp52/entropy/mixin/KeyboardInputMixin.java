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
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyEvents(boolean slowDown, float f, CallbackInfo ci) {
        if (Variables.forceForward) {
            this.up = true;
            this.forwardImpulse = 1;
        } else if (Variables.onlySidewaysMovement) {
            this.forwardImpulse = 0;
            this.up = false;
            this.down = false;
        } else if (Variables.onlyBackwardsMovement) {
            this.up = false;
            this.left = false;
            this.right = false;
            this.leftImpulse = 0;
            this.forwardImpulse = this.forwardImpulse <= 0 ? this.forwardImpulse : 0;
        } else if (Variables.invertedControls) {
            this.leftImpulse = -leftImpulse;
            this.forwardImpulse = -forwardImpulse;
        }
        if (Variables.forceJump) {
            this.jumping = true;
        } else if (Variables.noJump) {
            this.jumping = false;
        }
        if (Variables.forceSneak) {
            this.shiftKeyDown = true;
        }

    }

}
