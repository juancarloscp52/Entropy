package me.juancarloscp52.entropy.mixin;

import net.minecraft.world.entity.animal.rabbit.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Rabbit.class)
public interface RabbitInvoker {
    @Invoker
    void callSetVariant(Rabbit.Variant variant);
}
