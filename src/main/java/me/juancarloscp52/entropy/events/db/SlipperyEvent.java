package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class SlipperyEvent extends AbstractTimedEvent {

    @Override
    public void init() {
        Variables.slippery = true;
    }

    @Override
    public void end() {
        this.hasEnded = true;
        Variables.slippery = false;
    }

    @Override
    public void initClient() {
        Variables.slippery = true;
    }

    @Override
    public void endClient() {
        this.hasEnded = true;
        Variables.slippery = false;
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
