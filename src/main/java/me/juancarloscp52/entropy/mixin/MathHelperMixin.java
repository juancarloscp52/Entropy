package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.util.math.MathHelper;

@Mixin(MathHelper.class)
public class MathHelperMixin {
    @Inject(method = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", at = @At("HEAD"), cancellable = true)
    private static void lerp(float delta, float start, float end, CallbackInfoReturnable<Float> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }

    @Inject(method = "Lnet/minecraft/util/math/MathHelper;lerp(DDD)D", at = @At("HEAD"), cancellable = true)
    private static void lerp(double delta, double start, double end, CallbackInfoReturnable<Double> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }

    @Inject(method = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F", at = @At("HEAD"), cancellable = true)
    private static void lerpAngleDegrees(float delta, float start, float end, CallbackInfoReturnable<Float> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }
}
