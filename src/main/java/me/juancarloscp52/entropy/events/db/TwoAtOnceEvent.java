package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractMultiEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;

public class TwoAtOnceEvent extends AbstractMultiEvent {
    @Override
    public List<Event> selectEvents() {
        List<Event> selectedEvents = new ArrayList<>();
        List<Event> currentEvents = new ArrayList<>(Entropy.getInstance().eventHandler.currentEvents);
        Event firstEvent = EventRegistry.getRandomDifferentEvent(currentEvents);

        selectedEvents.add(firstEvent);
        currentEvents.add(firstEvent);
        selectedEvents.add(EventRegistry.getRandomDifferentEvent(currentEvents));
        System.out.println(selectedEvents);
        System.out.println(this);
        return selectedEvents;
    }

    @Override
    public String type() {
        return "multi";
    }
}
