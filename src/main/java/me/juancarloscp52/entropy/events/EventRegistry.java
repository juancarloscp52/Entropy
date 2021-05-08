package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.db.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


public class EventRegistry {
    private static final Random random = new Random();
    //Store constructors for all Entropy Events.
    public static HashMap<String, Supplier<Event>> entropyEvents;

    public static void register() {

        entropyEvents = new HashMap<>();
        entropyEvents.put("ArrowRainEvent", ArrowRainEvent::new);
        entropyEvents.put("BlurEvent", BlurEvent::new);
        entropyEvents.put("ChickenRainEvent", ChickenRainEvent::new);
        entropyEvents.put("CinematicScreenEvent", CinematicScreenEvent::new);
        entropyEvents.put("CloseRandomTPEvent", CloseRandomTPEvent::new);
        entropyEvents.put("CreeperEvent", CreeperEvent::new);
        entropyEvents.put("CRTEvent", CRTEvent::new);
        entropyEvents.put("DropHandItemEvent", DropHandItemEvent::new);
        entropyEvents.put("DropInventoryEvent", DropInventoryEvent::new);
        entropyEvents.put("DVDEvent", DVDEvent::new);
        entropyEvents.put("ExplodeNearbyEntitiesEvent", ExplodeNearbyEntitiesEvent::new);
        entropyEvents.put("ExtremeExplosionEvent", ExtremeExplosionEvent::new);
        entropyEvents.put("FarRandomTPEvent", FarRandomTPEvent::new);
        entropyEvents.put("ForceForwardEvent", ForceForwardEvent::new);
        entropyEvents.put("ForceJump2Event", ForceJump2Event::new);
        entropyEvents.put("ForceJumpEvent", ForceJumpEvent::new);
        entropyEvents.put("HerobrineEvent", HerobrineEvent::new);
        entropyEvents.put("HighPitchEvent", HighPitchEvent::new);
        entropyEvents.put("HungryEvent", HungryEvent::new);
        entropyEvents.put("HyperSlowEvent", HyperSlowEvent::new);
        entropyEvents.put("HyperSpeedEvent", HyperSpeedEvent::new);
        entropyEvents.put("IgniteNearbyEntitiesEvent", IgniteNearbyEntitiesEvent::new);
        entropyEvents.put("IntenseThunderStormEvent", IntenseThunderStormEvent::new);
        entropyEvents.put("InvertedColorsEvent", InvertedColorsEvent::new);
        entropyEvents.put("InvertedControlsEvent", InvertedControlsEvent::new);
        entropyEvents.put("ItemRainEvent", ItemRainEvent::new);
        entropyEvents.put("LowGravityEvent", LowGravityEvent::new);
        entropyEvents.put("LowPitchEvent", LowPitchEvent::new);
        entropyEvents.put("LowRenderDistanceEvent", LowRenderDistanceEvent::new);
        entropyEvents.put("LSDEvent", LSDEvent::new);
        entropyEvents.put("LuckyDropsEvent", LuckyDropsEvent::new);
        entropyEvents.put("MeteorRainEvent", MeteorRainEvent::new);
        entropyEvents.put("MouseDriftingEvent", MouseDriftingEvent::new);
        entropyEvents.put("NoDropsEvent", NoDropsEvent::new);
        entropyEvents.put("NoJumpEvent", NoJumpEvent::new);
        entropyEvents.put("OneHeartEvent", OneHeartEvent::new);
        entropyEvents.put("OnlyBackwardsEvent", OnlyBackwardsEvent::new);
        entropyEvents.put("OnlySidewaysEvent", OnlySidewaysEvent::new);
        entropyEvents.put("PlaceLavaBlockEvent", PlaceLavaBlockEvent::new);
        entropyEvents.put("RandomDropsEvent", RandomDropsEvent::new);
        entropyEvents.put("ReducedReachEvent", ReducedReachEvent::new);
        entropyEvents.put("RollCreditsEvent", RollCreditsEvent::new);
        entropyEvents.put("SlipperyEvent", SlipperyEvent::new);
        entropyEvents.put("Teleport0Event", Teleport0Event::new);
        entropyEvents.put("TeleportHeavenEvent", TeleportHeavenEvent::new);
        entropyEvents.put("TimelapseEvent", TimelapseEvent::new);
        entropyEvents.put("TntEvent", TntEvent::new);
        entropyEvents.put("UltraFovEvent", UltraFovEvent::new);
        entropyEvents.put("UltraLowFovEvent", UltraLowFovEvent::new);
        entropyEvents.put("UpsideDownEvent", UpsideDownEvent::new);
        entropyEvents.put("VerticalScreenEvent", VerticalScreenEvent::new);
        entropyEvents.put("WhereIsEverythingEvent", WhereIsEverythingEvent::new);
        entropyEvents.put("XpRainEvent", XpRainEvent::new);
        entropyEvents.put("HealEvent", HealEvent::new);
        entropyEvents.put("RandomizeArmorEvent", RandomizeArmorEvent::new);
        entropyEvents.put("ForceThirdPersonEvent", ForceThirdPersonEvent::new);
        entropyEvents.put("ForceFrontViewEvent", ForceFrontViewEvent::new);
        entropyEvents.put("HideEventsEvent", HideEventsEvent::new);
        entropyEvents.put("TopDownViewEvent", TopDownViewEvent::new);

    }

//    public static void removeDisabled() {
//        Entropy.getInstance().settings.disabledEvents.forEach(eventID -> entropyEvents.remove(eventID));
//    }

    public static Event getRandomDifferentEvent(List<Event> events) {
        short index = (short) random.nextInt(entropyEvents.size());
        String newEventName = (String) entropyEvents.keySet().toArray()[index];
        Event newEvent = entropyEvents.get(newEventName).get();

        if (Entropy.getInstance().settings.disabledEvents.contains(newEventName)) {
            return getRandomDifferentEvent(events);
        }

        for (Event event : events) {
            if (event.getClass().getName().contains(newEventName) && !event.hasEnded()) {
                return getRandomDifferentEvent(events);
            }

            if (!event.type().equals("none") && event.type().equals(newEvent.type()) && !event.hasEnded()) {
                return getRandomDifferentEvent(events);
            }

        }
        return newEvent;
    }

    public static Event get(String eventName) {
        Supplier<Event> newEvent = entropyEvents.get(eventName);
        if (newEvent != null)
            return newEvent.get();
        else
            return null;
    }

    public static String getEventId(Event event) {
        String[] name = event.getClass().getName().split("\\.");
        return name[name.length - 1];
    }

    public static String getTranslationKey(Event event) {
        return "entropy.events." + getEventId(event);
    }

    public static String getTranslationKey(String eventID) {
        return "entropy.events." + eventID;
    }
}
