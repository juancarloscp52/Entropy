package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class OnePunchEvent extends AbstractTimedEvent {

    @Override
    public void init() {
        Variables.shouldLounchEntity++;
        Variables.isOnePunchActivated++;
    }

    @Override
    public void end() {
        Variables.shouldLounchEntity--;
        Variables.isOnePunchActivated--;
        super.end();
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
