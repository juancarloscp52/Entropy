/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.db.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.*;
import java.util.function.Supplier;


public class EventRegistry {
    private static int last_index = 0;
    private static final Random random = new Random();
    //Store constructors for all Entropy Events.
    public static HashMap<String, Supplier<Event>> entropyEvents;

    public static void register() {

        entropyEvents = new HashMap<>();
        entropyEvents.put("RemoveEnchantmentsEvent", RemoveEnchantmentsEvent::new);
        entropyEvents.put("ArmorCurseEvent", ArmorCurseEvent::new);
        entropyEvents.put("RaidEvent", RaidEvent::new);
        entropyEvents.put("ArrowRainEvent", ArrowRainEvent::new);
        entropyEvents.put("WardenEvent", WardenEvent::new);
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
        entropyEvents.put("HalfHeartedEvent", HalfHeartedEvent::new);
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
        //entropyEvents.put("WhereIsEverythingEvent", WhereIsEverythingEvent::new); // No longer works on >1.19
        entropyEvents.put("XpRainEvent", XpRainEvent::new);
        entropyEvents.put("HealEvent", HealEvent::new);
        entropyEvents.put("RandomizeArmorEvent", RandomizeArmorEvent::new);
        entropyEvents.put("ForceThirdPersonEvent", ForceThirdPersonEvent::new);
        entropyEvents.put("ForceFrontViewEvent", ForceFrontViewEvent::new);
        entropyEvents.put("HideEventsEvent", HideEventsEvent::new);
        entropyEvents.put("TopDownViewEvent", TopDownViewEvent::new);
        entropyEvents.put("PhantomEvent", PhantomEvent::new);
        entropyEvents.put("TimerSpeed2Event", TimerSpeed2Event::new);
        entropyEvents.put("TimerSpeed5Event", TimerSpeed5Event::new);
        entropyEvents.put("TimerSpeedHalfEvent", TimerSpeedHalfEvent::new);
        entropyEvents.put("ResistanceEvent", ResistanceEvent::new);
        entropyEvents.put("FatigueEvent", FatigueEvent::new);
        entropyEvents.put("BlindnessEvent", BlindnessEvent::new);
        entropyEvents.put("SpeedEvent", SpeedEvent::new);
        entropyEvents.put("StarterPackEvent", StarterPackEvent::new);
        entropyEvents.put("DamageItemsEvent", DamageItemsEvent::new);
        entropyEvents.put("LevitationEvent", LevitationEvent::new);
        entropyEvents.put("SpinningMobsEvent", SpinningMobsEvent::new);
        entropyEvents.put("SinkholeEvent", SinkholeEvent::new);
        entropyEvents.put("PoolEvent", PoolEvent::new);
        entropyEvents.put("RandomCreeperEvent", RandomCreeperEvent::new);
        entropyEvents.put("SinkingEvent", SinkingEvent::new);
        entropyEvents.put("SlimeEvent", SlimeEvent::new);
        entropyEvents.put("HorseEvent", HorseEvent::new);
        entropyEvents.put("FireEvent", FireEvent::new);
        entropyEvents.put("AdventureEvent", AdventureEvent::new);
        entropyEvents.put("PitEvent", PitEvent::new);
        entropyEvents.put("SkyEvent", SkyEvent::new);
        entropyEvents.put("PumpkinViewEvent", PumpkinViewEvent::new);
        entropyEvents.put("NightVisionEvent", NightVisionEvent::new);
        entropyEvents.put("ExplosivePickaxeEvent", ExplosivePickaxeEvent::new);
        entropyEvents.put("HighlightAllMobsEvent", HighlightAllMobsEvent::new);
        entropyEvents.put("UpgradeRandomGearEvent", UpgradeRandomGearEvent::new);
        entropyEvents.put("DowngradeRandomGearEvent", DowngradeRandomGearEvent::new);
        entropyEvents.put("CurseRandomGearEvent", CurseRandomGearEvent::new);
        entropyEvents.put("EnchantRandomGearEvent", EnchantRandomGearEvent::new);
        entropyEvents.put("InvisiblePlayerEvent", InvisiblePlayerEvent::new);
        entropyEvents.put("InvisibleHostileMobsEvent", InvisibleHostileMobsEvent::new);
        entropyEvents.put("InvisibleEveryoneEvent", InvisibleEveryoneEvent::new);
        entropyEvents.put("VexAttackEvent", VexAttackEvent::new);
        entropyEvents.put("ZeusUltEvent", ZeusUltEvent::new);
        entropyEvents.put("GravitySightEvent", GravitySightEvent::new);
        entropyEvents.put("DeathSightEvent", DeathSightEvent::new);
        entropyEvents.put("GlassSightEvent", GlassSightEvent::new);
        entropyEvents.put("VoidSightEvent", VoidSightEvent::new);
        entropyEvents.put("MiningSightEvent", MiningSightEvent::new);
        entropyEvents.put("SkyBlockEvent", SkyBlockEvent::new);
        entropyEvents.put("RideClosestMobEvent", RideClosestMobEvent::new);
        entropyEvents.put("TrueFrostWalkerEvent", TrueFrostWalkerEvent::new);
        entropyEvents.put("SoSweetEvent", SoSweetEvent::new);
        entropyEvents.put("PlaceCobwebBlockEvent", PlaceCobwebBlockEvent::new);
        entropyEvents.put("FlipMobsEvent", FlipMobsEvent::new);
        entropyEvents.put("SpawnRainbowSheepEvent", SpawnRainbowSheepEvent::new);
        entropyEvents.put("FixItemsEvent", FixItemsEvent::new);
        entropyEvents.put("MidasTouchEvent", MidasTouchEvent::new);
        entropyEvents.put("GiveRandomOreEvent", GiveRandomOreEvent::new);
        entropyEvents.put("BeeEvent", BeeEvent::new);
        entropyEvents.put("AngryBeeEvent", AngryBeeEvent::new);
        entropyEvents.put("SilverfishEvent", SilverfishEvent::new);
        entropyEvents.put("BlazeEvent", BlazeEvent::new);
        entropyEvents.put("EndermiteEvent", EndermiteEvent::new);
        entropyEvents.put("SatiationEvent", SatiationEvent::new);
        entropyEvents.put("VitalsEvent", VitalsEvent::new);
        entropyEvents.put("TeleportNearbyEntitiesEvent", TeleportNearbyEntitiesEvent::new);
        entropyEvents.put("ForceSneakEvent", ForceSneakEvent::new);
        entropyEvents.put("ShuffleInventoryEvent", ShuffleInventoryEvent::new);
        entropyEvents.put("BulldozeEvent", BulldozeEvent::new);
        entropyEvents.put("SlimePyramidEvent", SlimePyramidEvent::new);
        entropyEvents.put("FlyingMachineEvent", FlyingMachineEvent::new);
        entropyEvents.put("AddHeartEvent", AddHeartEvent::new);
        entropyEvents.put("RemoveHeartEvent", RemoveHeartEvent::new);
        entropyEvents.put("NoiseMachineEvent", NoiseMachineEvent::new);
        entropyEvents.put("XRayEvent", XRayEvent::new);
        entropyEvents.put("LagEvent", LagEvent::new);
        entropyEvents.put("LowFPSEvent", LowFPSEvent::new);
        entropyEvents.put("InfiniteLavaEvent", InfiniteLavaEvent::new);
        entropyEvents.put("RandomCameraTiltEvent", RandomCameraTiltEvent::new);
        entropyEvents.put("NoAttackingEvent", NoAttackingEvent::new);
        entropyEvents.put("ConstantAttackingEvent", ConstantAttackingEvent::new);
        entropyEvents.put("SpawnKillerBunnyEvent", SpawnKillerBunnyEvent::new);
        entropyEvents.put("SpawnPetDogEvent", SpawnPetDogEvent::new);
        entropyEvents.put("SpawnPetCatEvent", SpawnPetCatEvent::new);
        entropyEvents.put("HauntedChestsEvent", HauntedChestsEvent::new);
        entropyEvents.put("NoUseKeyEvent", NoUseKeyEvent::new);
        entropyEvents.put("ConstantInteractingEvent", ConstantInteractingEvent::new);
        entropyEvents.put("MLGBucketEvent", MLGBucketEvent::new);
        entropyEvents.put("StutteringEvent", StutteringEvent::new);
        entropyEvents.put("ForceHorseRidingEvent", ForceHorseRidingEvent::new);
        entropyEvents.put("JumpscareEvent", JumpscareEvent::new);
        entropyEvents.put("RollingCameraEvent", RollingCameraEvent::new);
        entropyEvents.put("FlingEntitiesEvent", FlingEntitiesEvent::new);
        entropyEvents.put("BlackAndWhiteEvent", BlackAndWhiteEvent::new);
        entropyEvents.put("CreativeFlightEvent", CreativeFlightEvent::new);
        entropyEvents.put("FakeTeleportEvent", FakeTeleportEvent::new);
        entropyEvents.put("FakeFakeTeleportEvent", FakeFakeTeleportEvent::new);
        entropyEvents.put("ForcefieldEvent", ForcefieldEvent::new);
        entropyEvents.put("EntityMagnetEvent", EntityMagnetEvent::new);
        entropyEvents.put("OnePunchEvent", OnePunchEvent::new);
        entropyEvents.put("InfestationEvent", InfestationEvent::new);
        entropyEvents.put("RainbowPathEvent", RainbowPathEvent::new);
        entropyEvents.put("SilenceEvent", SilenceEvent::new);
        entropyEvents.put("NothingEvent", NothingEvent::new);
        entropyEvents.put("RainbowTrailsEvent", RainbowTrailsEvent::new);
        entropyEvents.put("RainbowSheepEverywhereEvent", RainbowSheepEverywhereEvent::new);
        entropyEvents.put("TwoAtOnceEvent", TwoAtOnceEvent::new);
        entropyEvents.put("FiveAtOnceEvent", FiveAtOnceEvent::new);

    }

    public static Event getRandomDifferentEvent(List<Event> events){

        ArrayList<String> eventKeys = new ArrayList<>(entropyEvents.keySet());
        eventKeys.removeAll(Entropy.getInstance().settings.disabledEvents);

        Set<String> ignoreCurrentEvents = new HashSet<>();
        events.forEach(event -> ignoreCurrentEvents.add(event.getClass().getSimpleName()));
        if(eventKeys.size()>ignoreCurrentEvents.size())
            eventKeys.removeAll(ignoreCurrentEvents);

        Set<String> ignoreTypes = new HashSet<>();
        events.forEach(event -> {
            if(!event.hasEnded()) {
                if(event instanceof AbstractMultiEvent multiEvent) {
                    if(!event.type().equalsIgnoreCase("none"))
                        ignoreTypes.add(event.type().toLowerCase());

                    ignoreTypes.addAll(multiEvent.selectedEvents().stream().map(ev -> ev.type().toLowerCase()).toList());
                    return;
                }

                if(event.getTickCount()>0) {
                    if(!event.type().equalsIgnoreCase("none"))
                        ignoreTypes.add(event.type().toLowerCase());
                }
            }
        });
        Set<String> ignoreEventsByType = new HashSet<>();
        eventKeys.forEach(eventName -> {
            if(ignoreTypes.contains(entropyEvents.get(eventName).get().type().toLowerCase())){
                ignoreEventsByType.add(eventName);
            }
        });
        if(eventKeys.size()>=ignoreEventsByType.size())
            eventKeys.removeAll(ignoreEventsByType);

        //Only enable the stuttering event on a dedicated server, because otherwise worldgen will be all wrong.
        //The MathHelper mixin turning off linear interpolation is only applied on the client, but if this is a singleplayer environment,
        //the integrated server has the modified MathHelper, too, causing incorrect worldgen.
        if(FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            eventKeys.remove("StutteringEvent");

        return getRandomEvent(eventKeys);
    }

    private static Event getRandomEvent(List<String> eventKeys) {
        if(eventKeys.isEmpty())
            return Event.INVALID;

        int index = random.nextInt(eventKeys.size());
        String newEventName = eventKeys.get(index);
        Event event = entropyEvents.get(newEventName).get();

        if(Entropy.getInstance().settings.accessibilityMode && event.isDisabledByAccessibilityMode()) {
            eventKeys.remove(index);
            return getRandomEvent(eventKeys);
        }

        return event;
    }

    public static Event get(String eventName) {
        Supplier<Event> newEvent = entropyEvents.get(eventName);
        if (newEvent != null)
            return newEvent.get();
        else
            return null;
    }

    public static Event getNextEventOrdered(){
        Supplier<Event> newEvent = entropyEvents.get(entropyEvents.keySet().stream().sorted().toList().get(last_index));
        last_index = (last_index + 1) % entropyEvents.size();
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
