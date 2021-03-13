package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.events.db.*;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


public class EventRegistry {
    private static final Random random = new Random();

    //Store constructors for all Entropy Events.
    private static final Supplier<Event>[] entropyEvents = new Supplier[] {
        ArrowRainEvent::new,
        BlurEvent::new,
        ChickenRainEvent::new,
        CinematicScreenEvent::new,
        CloseRandomTPEvent::new,
        CreeperEvent::new,
        CRTEvent::new,
        DropHandItemEvent::new,
        DropInventoryEvent::new,
        DVDEvent::new,
        ExplodeNearbyEntitiesEvent::new,
        ExtremeExplosionEvent::new,
        FarRandomTPEvent::new,
        ForceForwardEvent::new,
        ForceJump2Event::new,
        ForceJumpEvent::new,
        HerobrineEvent::new,
        HighPitchEvent::new,
        HungryEvent::new,
        HyperSlowEvent::new,
        HyperSpeedEvent::new,
        IgniteNearbyEntitiesEvent::new,
        IntenseThunderStormEvent::new,
        InvertedColorsEvent::new,
        InvertedControlsEvent::new,
        ItemRainEvent::new,
        LowGravityEvent::new,
        LowPitchEvent::new,
        LowRenderDistanceEvent::new,
        LSDEvent::new,
        LuckyDropsEvent::new,
        MeteorRainEvent::new,
        MouseDriftingEvent::new,
        NoDropsEvent::new,
        NoJumpEvent::new,
        OneHeartEvent::new,
        OnlyBackwardsEvent::new,
        OnlySidewaysEvent::new,
        PlaceLavaBlockEvent::new,
        RandomDropsEvent::new,
        ReducedReachEvent::new,
        RollCreditsEvent::new,
        SlipperyEvent::new,
        Teleport0Event::new,
        TeleportHeavenEvent::new,
        TimelapseEvent::new,
        TntEvent::new,
        UltraFovEvent::new,
        UltraLowFovEvent::new,
        UpsideDownEvent::new,
        VerticalScreenEvent::new,
        WhereIsEverythingEvent::new,
        XpRainEvent::new
    };

    public static Pair<Event, Short> getRandomDifferentEvent(List<Pair<Event,Short>> events, boolean isVoting){
        short index = (short) random.nextInt(entropyEvents.length);
        Event newEvent = entropyEvents[index].get();
        for (Pair<Event,Short> event : events){

            if(event.getLeft().getClass().getName().equals(newEvent.getClass().getName()) && (!event.getLeft().hasEnded() || isVoting)){
                return getRandomDifferentEvent(events, isVoting);
            }

            if(!event.getLeft().type().equals("none") && event.getLeft().type().equals(newEvent.type()) && (!event.getLeft().hasEnded() || isVoting) ){
                return getRandomDifferentEvent(events, isVoting);
            }

        }
        return new Pair<>(newEvent,index);
    }

    public static Pair<Event,Short> get(short index){
        return new Pair<>(entropyEvents[index].get(),index);
    }

}
