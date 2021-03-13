package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

public class MouseDriftingEvent extends AbstractTimedEvent {

    Random random = new Random();

    public MouseDriftingEvent() {
        this.translationKey="entropy.events.mouseDrifting";
    }

    @Override
    public void initClient() {
        Variables.mouseDrifting=true;
        Variables.mouseDriftingSignX=random.nextBoolean()?-1:1;
        Variables.mouseDriftingSignY=random.nextBoolean()?-1:1;
    }

    @Override
    public void endClient() {
        Variables.mouseDrifting=false;
        Variables.mouseDriftingSignX=0;
        Variables.mouseDriftingSignY=0;
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
        return (short)(Entropy.getInstance().settings.baseEventDuration *2);
    }
}
