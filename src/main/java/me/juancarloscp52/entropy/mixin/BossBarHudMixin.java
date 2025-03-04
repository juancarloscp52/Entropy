package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BossHealthOverlay.class)

/*
  Move bossbar down if GTA V timer is active.
 */
public abstract class BossBarHudMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V"),index = 2)
    public int applyScreenBorderToBossBar(int y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"),index = 3)
    public int applyScreenBorderToBossName(int y){
        return y + (Entropy.getInstance().settings.UIstyle == EntropySettings.UIStyle.GTAV ? 10:0);
    }

}
