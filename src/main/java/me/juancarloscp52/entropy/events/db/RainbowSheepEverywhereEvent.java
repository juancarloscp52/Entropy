package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.gui.DrawContext;

public class RainbowSheepEverywhereEvent extends AbstractTimedEvent {
    @Override
    public void initClient() {
        Variables.rainbowSheepEverywhere = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.rainbowSheepEverywhere = false;
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
