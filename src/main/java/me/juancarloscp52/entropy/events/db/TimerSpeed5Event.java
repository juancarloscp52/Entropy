package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class TimerSpeed5Event extends AbstractTimedEvent {

    @Override
    public void initClient() {
        Variables.timerMultiplier = 5;
    }

    @Override
    public void endClient() {
        Variables.timerMultiplier = 1;
        this.hasEnded = true;
    }

    @Override
    public void init() {
        Variables.timerMultiplier = 5;
    }

    @Override
    public void end() {
        Variables.timerMultiplier = 1;
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public String type() {
        return "timer";
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return (short) (Entropy.getInstance().settings.baseEventDuration*1.2f);
        //return 300;
    }
}
