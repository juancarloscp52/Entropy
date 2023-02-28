package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;

public class XRayEvent extends AbstractTimedEvent {

    @Override
    public void initClient() {
        Variables.xrayActive = true;

        // Rerender the world because of the caching
        var client = MinecraftClient.getInstance();
        client.worldRenderer.reload();
    }

    @Override
    public void endClient() {
        Variables.xrayActive = false;

        // Rerender the world because of the caching
        var client = MinecraftClient.getInstance();
        client.worldRenderer.reload();

        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

}
