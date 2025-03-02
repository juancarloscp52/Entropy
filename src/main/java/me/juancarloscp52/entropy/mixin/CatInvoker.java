package me.juancarloscp52.entropy.mixin;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CatEntity.class)
public interface CatInvoker {
    @Invoker("setCollarColor")
    void invokeSetCollarColor(DyeColor color);
}
