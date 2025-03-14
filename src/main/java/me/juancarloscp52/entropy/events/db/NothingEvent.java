package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;

public class NothingEvent extends AbstractInstantEvent {
    public static final EventType<NothingEvent> TYPE = EventType.builder(NothingEvent::new).build();

    @Override
    public EventType<NothingEvent> getType() {
        return TYPE;
    }
}
