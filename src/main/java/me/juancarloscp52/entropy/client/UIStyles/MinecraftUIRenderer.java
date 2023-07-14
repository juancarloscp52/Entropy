package me.juancarloscp52.entropy.client.UIStyles;

import java.util.UUID;

import me.juancarloscp52.entropy.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class MinecraftUIRenderer implements UIRenderer {
    private final ClientBossBar bar;

    public MinecraftUIRenderer() {
        UUID uuid = MathHelper.randomUuid();
        this.bar=new ClientBossBar(uuid,Text.translatable("entropy.title"), 0, BossBar.Color.GREEN, BossBar.Style.NOTCHED_20,false,false, false);
        ((BossBarHudAccessor) MinecraftClient.getInstance().inGameHud.getBossBarHud()).getBossBars().put(uuid,bar);
    }

    @Override
    public void renderTimer(DrawContext drawContext, int width, double time, double timerDuration) {

        this.bar.setPercent((float)(time / timerDuration));

    }

}
