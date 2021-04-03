package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class ForceForwardEvent extends AbstractTimedEvent {

    @Override
    public void initClient() {
        Variables.forceForward = true;
    }

    @Override
    public void endClient() {
        Variables.forceForward = false;
        this.hasEnded = true;
    }

    @Override
    public String type() {
        return "movement";
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
