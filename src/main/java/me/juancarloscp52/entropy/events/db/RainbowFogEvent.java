package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class RainbowFogEvent extends AbstractTimedEvent {
    public static final EventType<RainbowFogEvent> TYPE = EventType.builder(RainbowFogEvent::new).category(EventCategory.FOG).disabledByAccessibilityMode().build();

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        Variables.rainbowFog = true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void endClient() {
        Variables.rainbowFog = false;
        super.endClient();
    }

    @Override
    public EventType<RainbowFogEvent> getType() {
        return TYPE;
    }
}
