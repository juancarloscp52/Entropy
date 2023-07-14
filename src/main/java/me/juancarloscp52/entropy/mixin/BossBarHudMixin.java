package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import net.minecraft.client.gui.hud.BossBarHud;

@Mixin(BossBarHud.class)

/*
  Move bossbar down if GTA V timer is active.
 */
public abstract class BossBarHudMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/BossBarHud;renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V"),index = 2)
    public int applyScreenBorderToBossBar(int y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"),index = 3)
    public int applyScreenBorderToBossName(int y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

}
