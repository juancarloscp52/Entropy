package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractMultiEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;

public class FiveAtOnceEvent extends AbstractMultiEvent {
    @Override
    public List<Event> selectEvents() {
        List<Event> selectedEvents = new ArrayList<>();
        List<Event> currentEvents = new ArrayList<>(Entropy.getInstance().eventHandler.currentEvents);

        for(int i = 0; i < 5; i++) {
            Event event = EventRegistry.getRandomDifferentEvent(currentEvents);

            selectedEvents.add(event);
            currentEvents.add(event);
        }

        return selectedEvents;
    }

    @Override
    public String type() {
        return "multi";
    }
}
