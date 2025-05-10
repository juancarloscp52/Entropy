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
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends ClientInput {

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyEvents(CallbackInfo ci) {
        if (Variables.forceForward) {
            this.keyPresses = new Input(
                true,
                keyPresses.backward(),
                keyPresses.left(),
                keyPresses.right(),
                keyPresses.jump(),
                keyPresses.shift(),
                keyPresses.sprint()
            );
            moveVector = new Vec2(moveVector.x, 1);
        } else if (Variables.onlySidewaysMovement) {
            moveVector = new Vec2(moveVector.x, 0);
            this.keyPresses = new Input(
                false,
                false,
                keyPresses.left(),
                keyPresses.right(),
                keyPresses.jump(),
                keyPresses.shift(),
                keyPresses.sprint()
            );
        } else if (Variables.onlyBackwardsMovement) {
            this.keyPresses = new Input(
                false,
                keyPresses.backward(),
                false,
                false,
                keyPresses.jump(),
                keyPresses.shift(),
                keyPresses.sprint()
            );
            moveVector = new Vec2(0, hasForwardImpulse() ? 0 : moveVector.y);
        } else if (Variables.invertedControls) {
            moveVector = new Vec2(-moveVector.x, -moveVector.y);
        }
        if (Variables.forceJump) {
            this.keyPresses = new Input(
                keyPresses.forward(),
                keyPresses.backward(),
                keyPresses.left(),
                keyPresses.right(),
                true,
                keyPresses.shift(),
                keyPresses.sprint()
            );
        } else if (Variables.noJump) {
            this.keyPresses = new Input(
                keyPresses.forward(),
                keyPresses.backward(),
                keyPresses.left(),
                keyPresses.right(),
                false,
                keyPresses.shift(),
                keyPresses.sprint()
            );
        }
        if (Variables.forceSneak) {
            this.keyPresses = new Input(
                keyPresses.forward(),
                keyPresses.backward(),
                keyPresses.left(),
                keyPresses.right(),
                keyPresses.jump(),
                true,
                keyPresses.sprint()
            );
        }

    }
}
