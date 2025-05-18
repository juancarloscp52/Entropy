package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;

public class BouncyBlocksEvent extends AbstractTimedEvent {
    public static final EventType<BouncyBlocksEvent> TYPE = EventType.builder(BouncyBlocksEvent::new).category(EventCategory.MOVEMENT).build();

    @Override
    public void initClient() {
        Variables.bouncyBlocks = true;
    }

    @Override
    public void endClient() {
        Variables.bouncyBlocks = false;
        super.endClient();
    }

    @Override
    public EventType<BouncyBlocksEvent> getType() {
        return TYPE;
    }
}