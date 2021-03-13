package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.client.util.math.MatrixStack;

public class OnlyBackwardsEvent extends AbstractTimedEvent {


    public OnlyBackwardsEvent() {
        this.translationKey="entropy.events.onlyBackwards";
    }

    @Override
    public void initClient() {
        Variables.onlyBackwardsMovement=true;
    }

    @Override
    public void endClient() {
        Variables.onlyBackwardsMovement=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}
    @Override
    public String type() {
        return "movement";
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
