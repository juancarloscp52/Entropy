package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class HighPitchEvent extends AbstractTimedEvent {

    public HighPitchEvent() {
        this.translationKey="entropy.events.highPitch";
    }

    @Override
    public void initClient() {
        Variables.forcePitch=true;
        Variables.forcedPitch=3;
    }

    @Override
    public void endClient() {
        Variables.forcePitch=false;
        Variables.forcedPitch=0;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public String type() {
        return "pitch";
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration *2);
    }
}
