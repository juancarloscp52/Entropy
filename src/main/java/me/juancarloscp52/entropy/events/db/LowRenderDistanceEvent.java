package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class LowRenderDistanceEvent extends AbstractTimedEvent {

    MinecraftClient client;
    private int viewDistance = 0;

    @Override
    public void initClient() {
        client = MinecraftClient.getInstance();
        viewDistance = this.client.options.viewDistance;
        this.client.options.viewDistance = 1;
    }

    @Override
    public void endClient() {
        this.hasEnded = true;
        this.client.options.viewDistance = viewDistance;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public String type() {
        return "renderDistance";
    }
}
