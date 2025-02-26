package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.gui.DrawContext;

public class OnePunchEvent extends AbstractTimedEvent {

    @Override
    public void init() {
        Variables.shouldLaunchEntity++;
        Variables.isOnePunchActivated++;
    }

    @Override
    public void end() {
        Variables.shouldLaunchEntity--;
        Variables.isOnePunchActivated--;
        super.end();
    }

    @Override
    public void render(DrawContext drawContext, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
