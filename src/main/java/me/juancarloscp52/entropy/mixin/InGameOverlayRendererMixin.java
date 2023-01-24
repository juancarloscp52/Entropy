package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void preventRenderingFireOverlay(CallbackInfo ci) {
        if(Variables.fireEvent && Entropy.getInstance().settings.accessibilityMode)
            ci.cancel();
    }
}
