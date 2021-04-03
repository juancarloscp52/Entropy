package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "hasExtendedReach", at = @At("RETURN"), cancellable = true)
    private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (Variables.reducedReachDistance)
            cir.setReturnValue(false);
    }

    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void getReachDistance(CallbackInfoReturnable<Float> cir) {
        if (Variables.reducedReachDistance)
            cir.setReturnValue(1.5f);
    }

}
