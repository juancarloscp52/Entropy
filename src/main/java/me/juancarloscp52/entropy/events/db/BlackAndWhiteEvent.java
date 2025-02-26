package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.gui.DrawContext;

public class BlackAndWhiteEvent extends AbstractTimedEvent {
    @Override
    public void initClient() {
        Variables.blackAndWhite = true;
    }

    @Override
    public void endClient() {
        Variables.blackAndWhite = false;
        this.hasEnded = true;
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {}

    @Override
    public String type() {
        return "shader";
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration*1.5);
    }
}
