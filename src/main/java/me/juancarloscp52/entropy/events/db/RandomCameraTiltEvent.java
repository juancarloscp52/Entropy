package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.random.Random;

public class RandomCameraTiltEvent extends AbstractTimedEvent {
    @Override
    public void initClient() {
        Random random = MinecraftClient.getInstance().world.getRandom();
        Variables.cameraRoll = random.nextInt(360) + random.nextFloat();
    }

    @Override
    public void endClient() {
        Variables.cameraRoll = 0f;
        super.endClient();
    }

    @Override
    public String type() {
        return "camera";
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public boolean isDisabledByAccessibilityMode() {
        return true;
    }
}
