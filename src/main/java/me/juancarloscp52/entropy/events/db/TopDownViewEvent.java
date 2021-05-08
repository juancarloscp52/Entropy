package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class TopDownViewEvent extends AbstractTimedEvent {

    @Override
    public void initClient() {
        Variables.topView = true;
        Variables.thirdPersonView = true;
    }

    @Override
    public void endClient() {
        Variables.thirdPersonView = false;
        Variables.topView = false;
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public String type() {
        return "camera";
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short)(Entropy.getInstance().settings.baseEventDuration*1.5f);
    }
}
