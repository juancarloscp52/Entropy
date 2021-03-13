package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.client.util.math.MatrixStack;

public class OnlySidewaysEvent extends AbstractTimedEvent {


    public OnlySidewaysEvent() {
        this.translationKey="entropy.events.onlySideways";
    }

    @Override
    public void initClient() {
        Variables.onlySidewaysMovement=true;
    }

    @Override
    public void endClient() {
        Variables.onlySidewaysMovement=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {
        super.tick();
    }
    @Override
    public String type() {
        return "movement";
    }
    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
