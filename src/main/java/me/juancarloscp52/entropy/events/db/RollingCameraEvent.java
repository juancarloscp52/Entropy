package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Random;

public class RollingCameraEvent extends AbstractTimedEvent {
    public static final EventType<RollingCameraEvent> TYPE = EventType.builder(RollingCameraEvent::new).category(EventCategory.CAMERA).disabledByAccessibilityMode().build();
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
    public void render(GuiGraphics drawContext, DeltaTracker tickCounter) {
        Variables.cameraRoll = (Variables.cameraRoll + (sign * 1.25f * tickCounter.getGameTimeDeltaTicks())) % 360;
    }

    @Override
    public EventType<RollingCameraEvent> getType() {
        return TYPE;
    }
}
