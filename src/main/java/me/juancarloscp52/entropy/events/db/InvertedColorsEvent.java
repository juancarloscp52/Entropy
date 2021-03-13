package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class InvertedColorsEvent extends AbstractTimedEvent {


    public InvertedColorsEvent() {
        this.translationKey="entropy.events.invertedColor";
    }

    @Override
    public void initClient() {
        Variables.invertedShader=true;
    }

    @Override
    public void endClient() {
        Variables.invertedShader=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public String type() {
        return "shader";
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
