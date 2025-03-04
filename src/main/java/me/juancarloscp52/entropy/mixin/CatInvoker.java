package me.juancarloscp52.entropy.mixin;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Cat.class)
public interface CatInvoker {
    @Invoker("setCollarColor")
    void invokeSetCollarColor(DyeColor color);
}
