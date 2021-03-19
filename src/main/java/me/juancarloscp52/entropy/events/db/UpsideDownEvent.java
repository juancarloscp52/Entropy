package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class UpsideDownEvent extends AbstractTimedEvent {

    public UpsideDownEvent() {
        this.translationKey="entropy.events.upsideDown";
    }

    @Override
    public void initClient() {
        Variables.invertedFov=true;
    }

    @Override
    public void endClient() {
        Variables.invertedFov=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public String type() {
        return "fov";
    }
}
