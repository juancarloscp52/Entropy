package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

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
        this.hasEnded = true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(DrawContext drawContext, float tickdelta) {
        Variables.cameraRoll = (Variables.cameraRoll+(sign*1.25f*MinecraftClient.getInstance().getLastFrameDuration()))%360;
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
