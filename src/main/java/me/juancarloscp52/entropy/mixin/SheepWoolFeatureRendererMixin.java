package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;

@Mixin(SheepFurLayer.class)
public class SheepWoolFeatureRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Sheep;hasCustomName()Z"))
    private boolean makeAllSheepRainbow1(Sheep sheep) {
        return Variables.rainbowSheepEverywhere || sheep.hasCustomName();
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"), index = 0)
    private Object makeAllSheepRainbow2(Object sheepName) {
        if (Variables.rainbowSheepEverywhere)
            return "jeb_";
        else
            return sheepName;
    }
}
