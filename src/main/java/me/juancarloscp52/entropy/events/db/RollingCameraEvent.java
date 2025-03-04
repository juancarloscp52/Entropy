package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Random;

public class RollingCameraEvent extends AbstractTimedEvent {
    int sign = 0;
    @Override
    public void initClient() {
        super.initClient();
        sign = new Random().nextBoolean() ? -1 : 1;
    }

    @Override
    public void endClient() {
        Variables.cameraRoll = 0f;
        super.endClient();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        Variables.cameraRoll = (Variables.cameraRoll + (sign * 1.25f * tickCounter.getLastFrameDuration())) % 360;
    }

    @Override
    public String type() {
        return "camera";
    }

    @Override
    public boolean isDisabledByAccessibilityMode() {
        return true;
    }
}
