package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.mixin.BossBarHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class MinecraftUIRenderer implements UIRenderer {
    private final ClientBossBar bar;
    private VotingClient votingClient = null;

    public MinecraftUIRenderer(VotingClient votingClient) {
        this.votingClient = votingClient;

        
        UUID uuid = MathHelper.randomUuid();
        this.bar=new ClientBossBar(uuid,Text.translatable("entropy.title"), 0, BossBar.Color.GREEN, BossBar.Style.NOTCHED_20,false,false, false);
        ((BossBarHudAccessor) MinecraftClient.getInstance().inGameHud.getBossBarHud()).getBossBars().put(uuid,bar);
    }

    @Override
    public void renderTimer(MatrixStack matrixStack, int width, double time, double timerDuration) {
       
        this.bar.setPercent((float)(time / timerDuration));
        
    }
    
}
