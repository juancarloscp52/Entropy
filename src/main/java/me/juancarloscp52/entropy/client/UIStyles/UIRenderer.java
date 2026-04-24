package me.juancarloscp52.entropy.client.UIStyles;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface UIRenderer {
    void renderTimer(GuiGraphicsExtractor drawContext, int width, double time, double timerDuration);
    // TODO renderPoll and renderEventQueue
}


