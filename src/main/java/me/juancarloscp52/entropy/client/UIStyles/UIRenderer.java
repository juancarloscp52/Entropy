package me.juancarloscp52.entropy.client.UIStyles;

import net.minecraft.client.gui.DrawContext;

public interface UIRenderer {
    public void renderTimer(DrawContext drawContext, int width, double time, double timerDuration);
    // TODO renderPoll and renderEventQueue
}


