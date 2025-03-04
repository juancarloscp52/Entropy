package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.mixin.BossHealthOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import java.util.UUID;

public class MinecraftUIRenderer implements UIRenderer {
    private final LerpingBossEvent bar;

    public MinecraftUIRenderer() {
        UUID uuid = Mth.createInsecureUUID();
        this.bar=new LerpingBossEvent(uuid,Component.translatable("entropy.title"), 0, BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_20,false,false, false);
        ((BossHealthOverlayAccessor) Minecraft.getInstance().gui.getBossOverlay()).getEvents().put(uuid,bar);
    }

    @Override
    public void renderTimer(GuiGraphics drawContext, int width, double time, double timerDuration) {

        this.bar.setProgress((float)(time / timerDuration));

    }

}
