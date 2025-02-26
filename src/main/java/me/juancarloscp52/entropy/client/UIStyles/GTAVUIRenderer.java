package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.client.VotingClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class GTAVUIRenderer implements UIRenderer{

    private VotingClient votingClient = null;
    public GTAVUIRenderer(VotingClient votingClient) {
        this.votingClient = votingClient;
    }

    @Override
    public void renderTimer(DrawContext drawContext, int width, double time, double timerDuration) {
        drawContext.fill(0, 0, width, 10, 150 << 24);
        drawContext.fill(0, 0, MathHelper.floor(width * (time / timerDuration)), 10, (this.votingClient != null ? votingClient.getColor(255) : ColorHelper.Argb.getArgb(255,70,150,70)));
    }

}
