package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mth.class)
public class MathHelperMixin {
    @Inject(method = "lerpInt(FII)I", at = @At("HEAD"), cancellable = true)
    private static void lerp(float delta, int start, int end, CallbackInfoReturnable<Integer> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }

    @Inject(method = "lerp(FFF)F", at = @At("HEAD"), cancellable = true)
    private static void lerp(float delta, float start, float end, CallbackInfoReturnable<Float> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }

    @Inject(method = "lerp(DDD)D", at = @At("HEAD"), cancellable = true)
    private static void lerp(double delta, double start, double end, CallbackInfoReturnable<Double> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }

    @Inject(method = "rotLerp(FFF)F", at = @At("HEAD"), cancellable = true)
    private static void lerpAngleDegrees(float delta, float start, float end, CallbackInfoReturnable<Float> cir) {
        if(Variables.stuttering)
            cir.setReturnValue(start);
    }
}
