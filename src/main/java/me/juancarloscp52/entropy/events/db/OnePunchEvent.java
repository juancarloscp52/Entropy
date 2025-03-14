package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;

public class OnePunchEvent extends AbstractTimedEvent {
    public static final EventType<OnePunchEvent> TYPE = EventType.builder(OnePunchEvent::new).build();

    @Override
    public void init() {
        Variables.shouldLaunchEntity++;
        Variables.isOnePunchActivated++;
    }

    @Override
    public void end() {
        Variables.shouldLaunchEntity--;
        Variables.isOnePunchActivated--;
        super.end();
    }

    @Override
    public EventType<OnePunchEvent> getType() {
        return TYPE;
    }
}
