package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isEntityUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void flipUpsidedownEvent(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(Variables.flipEntities){
            cir.setReturnValue(true);
        }
    }

}
