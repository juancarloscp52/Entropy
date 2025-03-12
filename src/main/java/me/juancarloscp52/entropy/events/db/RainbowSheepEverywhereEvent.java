package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;

public class RainbowSheepEverywhereEvent extends AbstractTimedEvent {
    public static final EventType<RainbowSheepEverywhereEvent> TYPE = EventType.builder(RainbowSheepEverywhereEvent::new).build();

    @Override
    public void initClient() {
        Variables.rainbowSheepEverywhere = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.rainbowSheepEverywhere = false;
    }

    @Override
    public EventType<RainbowSheepEverywhereEvent> getType() {
        return TYPE;
    }
}
