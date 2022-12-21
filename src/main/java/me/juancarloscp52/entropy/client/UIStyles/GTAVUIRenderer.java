package me.juancarloscp52.entropy.client.UIStyles;

import me.juancarloscp52.entropy.client.VotingClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class GTAVUIRenderer implements UIRenderer{

    private VotingClient votingClient = null;
    public GTAVUIRenderer(VotingClient votingClient) {
        this.votingClient = votingClient;
    }

    @Override
    public void renderTimer(MatrixStack matrixStack, int width, double time, double timerDuration) {
        DrawableHelper.fill(matrixStack, 0, 0, width, 10, 150 << 24);
        DrawableHelper.fill(matrixStack, 0, 0, MathHelper.floor(width * (time / timerDuration)), 10, (this.votingClient != null ? votingClient.getColor() : MathHelper.packRgb(70, 150, 70)) + (255 << 24));
    }
    
}
