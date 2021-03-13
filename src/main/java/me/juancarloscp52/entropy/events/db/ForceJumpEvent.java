package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.client.util.math.MatrixStack;

public class ForceJumpEvent extends AbstractTimedEvent {


    public ForceJumpEvent() {
        this.translationKey="entropy.events.forceJump";
    }

    @Override
    public void initClient() {
        Variables.forceJump=true;
    }

    @Override
    public void endClient() {
        Variables.forceJump=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public String type() {
        return "jump";
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
