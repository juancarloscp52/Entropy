package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;

public class LuckyDropsEvent extends AbstractTimedEvent {

    public LuckyDropsEvent() {
        this.translationKey="entropy.events.luckyDrops";
    }


    @Override
    public void init() {
        Variables.luckyDrops=true;
    }

    @Override
    public void end() {
        Variables.luckyDrops=false;
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public String type() {
        return "drops";
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
