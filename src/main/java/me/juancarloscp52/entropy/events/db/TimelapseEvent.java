package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.world.ServerWorld;

public class TimelapseEvent extends AbstractTimedEvent {

    public TimelapseEvent() {
        this.translationKey="entropy.events.timelapse";
    }

    @Override
    public void init() {
    }

    @Override
    public void end() {
        this.hasEnded=true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {}

    @Override
    public void tick() {
        for (ServerWorld serverWorld : Entropy.getInstance().eventHandler.server.getWorlds()) {
            serverWorld.setTimeOfDay(serverWorld.getTimeOfDay() + (long) 125);
        }
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
