package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class StutteringEvent extends AbstractTimedEvent {
    @Override
    public void initClient() {
        Variables.stuttering = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.stuttering = false;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public boolean isDisabledByAccessibilitySetting() {
        return true;
    }
}
