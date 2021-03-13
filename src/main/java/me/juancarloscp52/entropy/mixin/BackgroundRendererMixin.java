package me.juancarloscp52.entropy.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(method = "applyFog", at=@At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setupNvFogDistance()V"))
    private static void changeFogDistance(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci){
        if(Variables.customFog ){
            RenderSystem.fogStart(0.05f);
            RenderSystem.fogEnd(50*0.5f);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
        }
    }

}
