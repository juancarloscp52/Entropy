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
