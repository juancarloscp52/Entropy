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

import com.mojang.serialization.Lifecycle;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.db.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class EventRegistry {
    private static final Random random = new Random();
    public static final ResourceKey<Registry<EventType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("entropy", "events"));
    public static final Registry<EventType<?>> EVENTS = bootstrap();

    private static Registry<EventType<?>> bootstrap() {
        String attack = "attack";
        String camera = "camera";
        String drops = "drops";
        String fov = "fov";
        String health = "health";
        String invisibility = "invisibility";
        String jump = "jump";
        String movement = "movement";
        String pitch = "pitch";
        String rain = "rain";
        String renderDistance = "render_distance";
        String screenAspect = "screen_aspect";
        String shader = "shader";
        String sight = "sight";
        String speed = "speed";
        String timer = "timer";
        String use = "use";
        WritableRegistry<EventType<?>> registry = new MappedRegistry<>(REGISTRY_KEY, Lifecycle.stable());
        register(registry, "remove_enchantments", RemoveEnchantmentsEvent::new);
        register(registry, "armor_curse", ArmorCurseEvent::new);
        register(registry, "raid", RaidEvent::new);
        register(registry, "arrow_rain", EventType.builder(ArrowRainEvent::new).category(rain));
        register(registry, "warden", WardenEvent::new);
        register(registry, "blur", EventType.builder(BlurEvent::new).category(shader));
        register(registry, "chicken_rain", EventType.builder(ChickenRainEvent::new).category(rain));
        register(registry, "cinematic_screen", EventType.builder(CinematicScreenEvent::new).category(screenAspect));
        register(registry, "close_random_tp", CloseRandomTPEvent::new);
        register(registry, "creeper", CreeperEvent::new);
        register(registry, "crt", EventType.builder(CRTEvent::new).category(shader));
        register(registry, "drop_hand_item", DropHandItemEvent::new);
        register(registry, "drop_inventory", DropInventoryEvent::new);
        register(registry, "dvd", EventType.builder(DVDEvent::new).category(screenAspect));
        register(registry, "explode_nearby_entities", ExplodeNearbyEntitiesEvent::new);
        register(registry, "extreme_explosion", ExtremeExplosionEvent::new);
        register(registry, "far_random_tp", FarRandomTPEvent::new);
        register(registry, "force_forward", EventType.builder(ForceForwardEvent::new).category(movement));
        register(registry, "force_jump_extreme", EventType.builder(ForceJump2Event::new).category(jump));
        register(registry, "force_jump", EventType.builder(ForceJumpEvent::new).category(jump));
        register(registry, "herobrine", HerobrineEvent::new);
        register(registry, "high_pitch", EventType.builder(HighPitchEvent::new).category(pitch));
        register(registry, "hungry", HungryEvent::new);
        register(registry, "hyper_slow", EventType.builder(HyperSlowEvent::new).category(speed));
        register(registry, "hyper_speed", EventType.builder(HyperSpeedEvent::new).category(speed));
        register(registry, "ignite_nearby_entities", IgniteNearbyEntitiesEvent::new);
        register(registry, "intense_thunder_storm", EventType.builder(IntenseThunderStormEvent::new).category(rain));
        register(registry, "inverted_colors", EventType.builder(InvertedColorsEvent::new).category(shader));
        register(registry, "inverted_controls", EventType.builder(InvertedControlsEvent::new).category(movement));
        register(registry, "item_rain", EventType.builder(ItemRainEvent::new).category(rain));
        register(registry, "low_gravity", LowGravityEvent::new);
        register(registry, "low_pitch", EventType.builder(LowPitchEvent::new).category(pitch));
        register(registry, "low_render_distance", EventType.builder(LowRenderDistanceEvent::new).category(renderDistance));
        register(registry, "lsd", EventType.builder(LSDEvent::new).category(shader).disabledByAccessibilityMode());
        register(registry, "lucky_drops", EventType.builder(LuckyDropsEvent::new).category(drops));
        register(registry, "meteor_rain", EventType.builder(MeteorRainEvent::new).category(rain));
        register(registry, "mouse_drifting", MouseDriftingEvent::new);
        register(registry, "no_drops", EventType.builder(NoDropsEvent::new).category(drops));
        register(registry, "no_jump", EventType.builder(NoJumpEvent::new).category(jump));
        register(registry, "half_hearted", EventType.builder(HalfHeartedEvent::new).category(health));
        register(registry, "only_backwards", EventType.builder(OnlyBackwardsEvent::new).category(movement));
        register(registry, "only_sideways", EventType.builder(OnlySidewaysEvent::new).category(movement));
        register(registry, "place_lava_block", PlaceLavaBlockEvent::new);
        register(registry, "random_drops", EventType.builder(RandomDropsEvent::new).category(drops));
        register(registry, "reduced_reach", ReducedReachEvent::new);
        register(registry, "roll_credits", RollCreditsEvent::new);
        register(registry, "slippery", SlipperyEvent::new);
        register(registry, "teleport_spawn", Teleport0Event::new);
        register(registry, "teleport_heaven", TeleportHeavenEvent::new);
        register(registry, "timelapse", TimelapseEvent::new);
        register(registry, "tnt", TntEvent::new);
        register(registry, "ultra_fov", EventType.builder(UltraFovEvent::new).category(fov));
        register(registry, "ultra_low_fov", EventType.builder(UltraLowFovEvent::new).category(fov));
        register(registry, "upside_down", EventType.builder(UpsideDownEvent::new).category(camera).disabledByAccessibilityMode());
        register(registry, "vertical_screen", EventType.builder(VerticalScreenEvent::new).category(screenAspect));
        //register(registry, "where_is_everything", EventType.builder(WhereIsEverythingEvent::new).category(renderDistance)); // No longer works on >1.19
        register(registry, "xp_rain", EventType.builder(XpRainEvent::new).category(rain));
        register(registry, "heal", EventType.builder(HealEvent::new).category(health));
        register(registry, "randomize_armor", RandomizeArmorEvent::new);
        register(registry, "force_third_person", EventType.builder(ForceThirdPersonEvent::new).category(camera));
        register(registry, "force_front_view", EventType.builder(ForceFrontViewEvent::new).category(camera));
        register(registry, "hide_events", HideEventsEvent::new);
        register(registry, "top_down_view", EventType.builder(TopDownViewEvent::new).category(camera));
        register(registry, "phantom", PhantomEvent::new);
        register(registry, "timer_speed_2", EventType.builder(TimerSpeed2Event::new).category(timer));
        register(registry, "timer_speed_5", EventType.builder(TimerSpeed5Event::new).category(timer));
        register(registry, "timer_speed_half", EventType.builder(TimerSpeedHalfEvent::new).category(timer));
        register(registry, "resistance", ResistanceEvent::new);
        register(registry, "fatigue", FatigueEvent::new);
        register(registry, "blindness", BlindnessEvent::new);
        register(registry, "speed", EventType.builder(SpeedEvent::new).category(speed));
        register(registry, "starter_pack", StarterPackEvent::new);
        register(registry, "damage_items", DamageItemsEvent::new);
        register(registry, "levitation", LevitationEvent::new);
        register(registry, "spinning_mobs", SpinningMobsEvent::new);
        register(registry, "sinkhole", SinkholeEvent::new);
        register(registry, "pool", PoolEvent::new);
        register(registry, "random_creeper", RandomCreeperEvent::new);
        register(registry, "sinking", SinkingEvent::new);
        register(registry, "slime", SlimeEvent::new);
        register(registry, "horse", HorseEvent::new);
        register(registry, "fire", FireEvent::new);
        register(registry, "adventure", AdventureEvent::new);
        register(registry, "pit", PitEvent::new);
        register(registry, "sky", EventType.builder(SkyEvent::new).category(health));
        register(registry, "pumpkin_view", PumpkinViewEvent::new);
        register(registry, "night_vision", NightVisionEvent::new);
        register(registry, "explosive_pickaxe", ExplosivePickaxeEvent::new);
        register(registry, "highlight_all_mobs", HighlightAllMobsEvent::new);
        register(registry, "upgrade_random_gear", UpgradeRandomGearEvent::new);
        register(registry, "downgrade_random_gear", DowngradeRandomGearEvent::new);
        register(registry, "curse_random_gear", CurseRandomGearEvent::new);
        register(registry, "enchant_random_gear", EnchantRandomGearEvent::new);
        register(registry, "invisible_player", EventType.builder(InvisiblePlayerEvent::new).category(invisibility));
        register(registry, "invisible_hostile_mobs", EventType.builder(InvisibleHostileMobsEvent::new).category(invisibility));
        register(registry, "invisible_everyone", EventType.builder(InvisibleEveryoneEvent::new).category(invisibility));
        register(registry, "vex_attack", VexAttackEvent::new);
        register(registry, "zeus_ult", ZeusUltEvent::new);
        register(registry, "gravity_sight", EventType.builder(GravitySightEvent::new).category(sight));
        register(registry, "death_sight", EventType.builder(DeathSightEvent::new).category(sight));
        register(registry, "glass_sight", EventType.builder(GlassSightEvent::new).category(sight));
        register(registry, "void_sight", EventType.builder(VoidSightEvent::new).category(sight));
        register(registry, "mining_sight", EventType.builder(MiningSightEvent::new).category(sight));
        register(registry, "sky_block", SkyBlockEvent::new);
        register(registry, "ride_closest_mob", RideClosestMobEvent::new);
        register(registry, "true_frost_walker", TrueFrostWalkerEvent::new);
        register(registry, "so_sweet", SoSweetEvent::new);
        register(registry, "place_cobweb_block", PlaceCobwebBlockEvent::new);
        register(registry, "flip_mobs", FlipMobsEvent::new);
        register(registry, "spawn_rainbow_sheep", SpawnRainbowSheepEvent::new);
        register(registry, "fix_items", FixItemsEvent::new);
        register(registry, "midas_touch", MidasTouchEvent::new);
        register(registry, "give_random_ore", GiveRandomOreEvent::new);
        register(registry, "bee", BeeEvent::new);
        register(registry, "angry_bee", AngryBeeEvent::new);
        register(registry, "silverfish", SilverfishEvent::new);
        register(registry, "blaze", BlazeEvent::new);
        register(registry, "endermite", EndermiteEvent::new);
        register(registry, "satiation", SatiationEvent::new);
        register(registry, "vitals", VitalsEvent::new);
        register(registry, "teleport_nearby_entities", TeleportNearbyEntitiesEvent::new);
        register(registry, "force_sneak", EventType.builder(ForceSneakEvent::new).category(movement));
        register(registry, "shuffle_inventory", ShuffleInventoryEvent::new);
        register(registry, "bulldoze", BulldozeEvent::new);
        register(registry, "slime_pyramid", SlimePyramidEvent::new);
        register(registry, "flying_machine", FlyingMachineEvent::new);
        register(registry, "add_heart", EventType.builder(AddHeartEvent::new).category(health));
        register(registry, "remove_heart", EventType.builder(RemoveHeartEvent::new).category(health));
        register(registry, "noise_machine", NoiseMachineEvent::new);
        register(registry, "xray", XRayEvent::new);
        register(registry, "lag", EventType.builder(LagEvent::new).category(movement).disabledByAccessibilityMode());
        register(registry, "low_fps", EventType.builder(LowFPSEvent::new).disabledByAccessibilityMode());
        register(registry, "infinite_lava", InfiniteLavaEvent::new);
        register(registry, "random_camera_tilt", EventType.builder(RandomCameraTiltEvent::new).category(camera).disabledByAccessibilityMode());
        register(registry, "no_attacking", EventType.builder(NoAttackingEvent::new).category(attack));
        register(registry, "constant_attacking", EventType.builder(ConstantAttackingEvent::new).category(attack));
        register(registry, "spawn_killer_bunny", SpawnKillerBunnyEvent::new);
        register(registry, "spawn_pet_dog", SpawnPetDogEvent::new);
        register(registry, "spawn_pet_cat", SpawnPetCatEvent::new);
        register(registry, "haunted_chests", HauntedChestsEvent::new);
        register(registry, "no_use_key", EventType.builder(NoUseKeyEvent::new).category(use));
        register(registry, "constant_interacting", EventType.builder(ConstantInteractingEvent::new).category(use));
        register(registry, "mlg_bucket", MLGBucketEvent::new);
        register(registry, "stuttering", EventType.builder(StutteringEvent::new).disabledByAccessibilityMode());
        register(registry, "force_horse_riding", ForceHorseRidingEvent::new);
        register(registry, "jumpscare", JumpscareEvent::new);
        register(registry, "rolling_camera", EventType.builder(RollingCameraEvent::new).category(camera).disabledByAccessibilityMode());
        register(registry, "fling_entities", FlingEntitiesEvent::new);
        register(registry, "black_and_white", EventType.builder(BlackAndWhiteEvent::new).category(shader));
        register(registry, "creative_flight", CreativeFlightEvent::new);
        register(registry, "fake_teleport", EventType.builder(FakeTeleportEvent::new).streamCodec(FakeTeleportEvent.STREAM_CODEC));
        register(registry, "fake_fake_teleport", EventType.builder(FakeFakeTeleportEvent::new).streamCodec(FakeFakeTeleportEvent.STREAM_CODEC));
        register(registry, "forcefield", ForcefieldEvent::new);
        register(registry, "entity_magnet", EntityMagnetEvent::new);
        register(registry, "one_punch", OnePunchEvent::new);
        register(registry, "infestation", InfestationEvent::new);
        register(registry, "rainbow_path", RainbowPathEvent::new);
        register(registry, "silence", SilenceEvent::new);
        register(registry, "nothing", NothingEvent::new);
        register(registry, "rainbow_trails", RainbowTrailsEvent::new);
        register(registry, "rainbow_sheep_everywhere", RainbowSheepEverywhereEvent::new);
        register(registry, "armor_trim", ArmorTrimEvent::new);
        return registry.freeze();
    }

    private static void register(Registry<EventType<?>> registry, String id, EventType.EventSupplier<?> eventSupplier) {
        register(registry, id, EventType.builder(eventSupplier).build());
    }

    private static void register(Registry<EventType<?>> registry, String id, EventType.Builder<?> builder) {
        register(registry, id, builder.build());
    }

    private static void register(Registry<EventType<?>> registry, String id, EventType<?> type) {
        Registry.register(registry, ResourceLocation.fromNamespaceAndPath("entropy", id), type);
    }

    public static <T extends Event> TypedEvent<T> getRandomDifferentEvent(List<TypedEvent<?>> currentEvents){

        Map<ResourceLocation, EventType<T>> eventCandidates = EVENTS.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().location(), e -> (EventType<T>) e.getValue()));
        Set<ResourceLocation> eventsToRemove = new HashSet<>(Entropy.getInstance().settings.disabledEventTypes);
        Set<String> ignoredEventCategories = new HashSet<>();

        currentEvents.forEach(typedEvent -> {
            EventType<?> type = typedEvent.type();
            eventsToRemove.add(EVENTS.getKey(type));

            Event event = typedEvent.event();
            if(event.getTickCount()>0 && !event.hasEnded() && !type.category().equalsIgnoreCase("none"))
                ignoredEventCategories.add(type.category().toLowerCase());
        });

        Level overworld = Entropy.getInstance().eventHandler.server.overworld();
        eventCandidates.forEach((eventId, type) -> {
            if(ignoredEventCategories.contains(type.category().toLowerCase())){
                eventsToRemove.add(eventId);
            }
            else if (!EventRegistry.doesWorldHaveRequiredFeatures(type, overworld))
                eventsToRemove.add(eventId);
        });

        //Only enable the stuttering event on a dedicated server, because otherwise worldgen will be all wrong.
        //The MathHelper mixin turning off linear interpolation is only applied on the client, but if this is a singleplayer environment,
        //the integrated server has the modified MathHelper, too, causing incorrect worldgen.
        if(FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            eventsToRemove.add(ResourceLocation.fromNamespaceAndPath("entropy", "stuttering"));

        eventsToRemove.forEach(eventCandidates::remove);
        return getRandomEvent(new ArrayList<>(eventCandidates.values()));
    }

    private static <T extends Event> TypedEvent<T> getRandomEvent(List<EventType<T>> eventKeys) {
        if(eventKeys.isEmpty())
            return null;

        int index = random.nextInt(eventKeys.size());
        EventType<T> newEventType = eventKeys.get(index);

        if(Entropy.getInstance().settings.accessibilityMode && newEventType.disabledByAccessibilityMode()) {
            eventKeys.remove(index);
            return getRandomEvent(eventKeys);
        }

        return TypedEvent.fromEventType(newEventType);
    }

    public static ResourceLocation getEventId(EventType<?> eventType) {
        return EVENTS.getKey(eventType);
    }

    public static String getTranslationKey(EventType<?> eventType) {
        return getTranslationKey(getEventId(eventType));
    }

    public static String getTranslationKey(ResourceLocation eventId) {
        return "events." + eventId.toLanguageKey();
    }

    public static boolean doesWorldHaveRequiredFeatures(EventType<?> eventType, Level world) {
        return eventType.requiredFeatures().isSubsetOf(world.enabledFeatures());
    }
}
