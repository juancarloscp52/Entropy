package me.juancarloscp52.entropy.mixin;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WolfEntity.class)
public interface WolfInvoker {
    @Invoker("setCollarColor")
    void invokeSetCollarColor(DyeColor color);
}
