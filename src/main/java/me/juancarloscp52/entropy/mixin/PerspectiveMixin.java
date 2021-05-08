package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.options.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Perspective.class)
public class PerspectiveMixin {

    @Inject(method = "isFirstPerson",at=@At("RETURN"), cancellable = true)
    private void isFirstPerson(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue()&& !Variables.thirdPersonView);
    }
    @Inject(method = "isFrontView",at=@At("RETURN"), cancellable = true)
    private void isFrontView(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue()&& !Variables.thirdPersonView || Variables.frontView);
    }
}
