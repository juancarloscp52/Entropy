package me.juancarloscp52.entropy.client.UIStyles;

import net.minecraft.client.util.math.MatrixStack;

public interface UIRenderer {
    public void renderTimer(MatrixStack matrixStack, int width, double time, double timerDuration);
    // TODO renderPoll and renderEventQueue
}


