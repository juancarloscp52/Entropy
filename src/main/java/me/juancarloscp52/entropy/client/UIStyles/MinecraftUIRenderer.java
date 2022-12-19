package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.client.VotingClient;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;

public class MinecraftUIRenderer implements UIRenderer {
    private ServerBossBar bar;
    private VotingClient votingClient = null;

    public MinecraftUIRenderer(VotingClient votingClient) {
        this.votingClient = votingClient;

        

        this.bar=new ServerBossBar(Text.of("Timer"), BossBar.Color.GREEN, BossBar.Style.NOTCHED_20);
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player ->  bar.addPlayer(player));
    }

    @Override
    public void renderTimer(MatrixStack matrixStack, int width, double time, double timerDuration) {
       
        this.bar.setPercent((float)(time / timerDuration));
        
    }
    
}
