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
    private void applyEvents(boolean slowDown, CallbackInfo ci) {
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
