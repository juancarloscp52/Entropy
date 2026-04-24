package me.juancarloscp52.entropy.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void preventRenderingFireOverlay(PoseStack poseStack, MultiBufferSource bufferSource, TextureAtlasSprite sprite, CallbackInfo ci) {
        if(Variables.fireEvent && Entropy.getInstance().settings.accessibilityMode)
            ci.cancel();
    }
}
