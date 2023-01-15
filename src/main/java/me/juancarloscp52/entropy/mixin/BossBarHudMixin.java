package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BossBarHud.class)

/*
  Move bossbar down if GTA V timer is active.
 */
public abstract class BossBarHudMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/BossBarHud;renderBossBar(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V"),index = 2)
    public int applyScreenBorderToBossBar(int y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"),index = 3)
    public float applyScreenBorderToBossName(float y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

}
