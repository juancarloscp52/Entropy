package me.juancarloscp52.entropy.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import me.juancarloscp52.entropy.Variables;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolLayer.class)
public class SheepWoolLayerMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void makeAllSheepRainbow2(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SheepRenderState renderState, float yRot, float xRot, CallbackInfo ci) {
        if (Variables.rainbowSheepEverywhere)
            renderState.customName = Component.literal("jeb_");
    }
}
