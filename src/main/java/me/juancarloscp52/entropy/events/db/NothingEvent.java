package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;

public class NothingEvent extends AbstractInstantEvent {
    private final boolean placeholder;

    public NothingEvent() {
        this(false);
    }

    public NothingEvent(boolean placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }
}
