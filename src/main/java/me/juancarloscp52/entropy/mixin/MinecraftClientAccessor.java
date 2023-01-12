package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

    @Accessor
    public void setAttackCooldown(int attackCooldown);

    @Invoker
    public boolean callDoAttack();

    @Invoker
    public void callHandleBlockBreaking(boolean breaking);

}
