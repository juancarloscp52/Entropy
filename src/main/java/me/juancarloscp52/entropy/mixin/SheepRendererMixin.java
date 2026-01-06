package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepRenderer.class)
public class SheepRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void makeAllSheepRainbow2(Sheep sheep, SheepRenderState renderState, float partialTick, CallbackInfo ci) {
        if (Variables.rainbowSheepEverywhere)
            renderState.isJebSheep = true;
    }
}
