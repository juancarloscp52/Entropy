package me.juancarloscp52.entropy.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

    @Accessor
    void setAttackCooldown(int attackCooldown);

    @Invoker
    boolean callDoAttack();

    @Invoker
    void callHandleBlockBreaking(boolean breaking);

}
