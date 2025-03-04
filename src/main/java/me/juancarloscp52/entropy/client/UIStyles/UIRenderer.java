package me.juancarloscp52.entropy.client.UIStyles;

import net.minecraft.client.gui.GuiGraphics;

public interface UIRenderer {
    public void renderTimer(GuiGraphics drawContext, int width, double time, double timerDuration);
    // TODO renderPoll and renderEventQueue
}


