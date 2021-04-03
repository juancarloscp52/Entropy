package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(method = "getAdjustedPitch", at = @At("RETURN"), cancellable = true)
    private void forcePitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> cir) {
        if (Variables.forcePitch) {
            cir.setReturnValue(Variables.forcedPitch);
        }
    }

}
