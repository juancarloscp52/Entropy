package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void preventRenderingFireOverlay(CallbackInfo ci) {
        if(Variables.fireEvent && Entropy.getInstance().settings.accessibilityMode)
            ci.cancel();
    }
}
