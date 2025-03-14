package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;

public class StutteringEvent extends AbstractTimedEvent {
    public static final EventType<StutteringEvent> TYPE = EventType.builder(StutteringEvent::new).disabledByAccessibilityMode().build();

    @Override
    public void initClient() {
        Variables.stuttering = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.stuttering = false;
    }

    @Override
    public EventType<StutteringEvent> getType() {
        return TYPE;
    }
}
