package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.client.VotingClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class GTAVUIRenderer implements UIRenderer{

    private VotingClient votingClient = null;
    public GTAVUIRenderer(VotingClient votingClient) {
        this.votingClient = votingClient;
    }

    @Override
    public void renderTimer(GuiGraphics drawContext, int width, double time, double timerDuration) {
        drawContext.fill(0, 0, width, 10, 150 << 24);
        drawContext.fill(0, 0, Mth.floor(width * (time / timerDuration)), 10, (this.votingClient != null ? votingClient.getColor(255) : ARGB.color(255,70,150,70)));
    }

}
