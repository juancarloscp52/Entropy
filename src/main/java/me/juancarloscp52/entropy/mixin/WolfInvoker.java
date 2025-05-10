package me.juancarloscp52.entropy.mixin;

import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Wolf.class)
public interface WolfInvoker {
    @Invoker("setCollarColor")
    void invokeSetCollarColor(DyeColor color);
}
