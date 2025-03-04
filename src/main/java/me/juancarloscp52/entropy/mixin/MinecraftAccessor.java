package me.juancarloscp52.entropy.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor
    void setMissTime(int attackCooldown);

    @Invoker
    boolean callStartAttack();

    @Invoker
    void callContinueAttack(boolean breaking);

}
