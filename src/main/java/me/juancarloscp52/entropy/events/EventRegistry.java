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
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
    public static final StreamCodec<RegistryFriendlyByteBuf, Event> STREAM_CODEC = ByteBufCodecs.registry(REGISTRY_KEY).dispatch(Event::getType, EventType::streamCodec);
    public static final Registry<EventType<?>> EVENTS = bootstrap();

    private static Registry<EventType<?>> bootstrap() {
        WritableRegistry<EventType<?>> registry = new MappedRegistry<>(REGISTRY_KEY, Lifecycle.stable());
        register(registry, "remove_enchantments", RemoveEnchantmentsEvent.TYPE);
        register(registry, "armor_curse", ArmorCurseEvent.TYPE);
        register(registry, "raid", RaidEvent.TYPE);
        register(registry, "arrow_rain", ArrowRainEvent.TYPE);
        register(registry, "warden", WardenEvent.TYPE);
        register(registry, "blur", BlurEvent.TYPE);
        register(registry, "chicken_rain", ChickenRainEvent.TYPE);
        register(registry, "cinematic_screen", CinematicScreenEvent.TYPE);
        register(registry, "close_random_tp", CloseRandomTPEvent.TYPE);
        register(registry, "creeper", CreeperEvent.TYPE);
        register(registry, "crt", CRTEvent.TYPE);
        register(registry, "drop_hand_item", DropHandItemEvent.TYPE);
        register(registry, "drop_inventory", DropInventoryEvent.TYPE);
        register(registry, "dvd", DVDEvent.TYPE);
        register(registry, "explode_nearby_entities", ExplodeNearbyEntitiesEvent.TYPE);
        register(registry, "extreme_explosion", ExtremeExplosionEvent.TYPE);
        register(registry, "far_random_tp", FarRandomTPEvent.TYPE);
        register(registry, "force_forward", ForceForwardEvent.TYPE);
        register(registry, "force_jump_extreme", ForceJump2Event.TYPE);
        register(registry, "force_jump", ForceJumpEvent.TYPE);
        register(registry, "herobrine", HerobrineEvent.TYPE);
        register(registry, "high_pitch", HighPitchEvent.TYPE);
        register(registry, "hungry", HungryEvent.TYPE);
        register(registry, "hyper_slow", HyperSlowEvent.TYPE);
        register(registry, "hyper_speed", HyperSpeedEvent.TYPE);
        register(registry, "ignite_nearby_entities", IgniteNearbyEntitiesEvent.TYPE);
        register(registry, "intense_thunder_storm", IntenseThunderStormEvent.TYPE);
        register(registry, "inverted_colors", InvertedColorsEvent.TYPE);
        register(registry, "inverted_controls", InvertedControlsEvent.TYPE);
        register(registry, "item_rain", ItemRainEvent.TYPE);
        register(registry, "low_gravity", LowGravityEvent.TYPE);
        register(registry, "low_pitch", LowPitchEvent.TYPE);
        register(registry, "low_render_distance", LowRenderDistanceEvent.TYPE);
        register(registry, "lsd", LSDEvent.TYPE);
        register(registry, "lucky_drops", LuckyDropsEvent.TYPE);
        register(registry, "meteor_rain", MeteorRainEvent.TYPE);
        register(registry, "mouse_drifting", MouseDriftingEvent.TYPE);
        register(registry, "no_drops", NoDropsEvent.TYPE);
        register(registry, "no_jump", NoJumpEvent.TYPE);
        register(registry, "half_hearted", HalfHeartedEvent.TYPE);
        register(registry, "only_backwards", OnlyBackwardsEvent.TYPE);
        register(registry, "only_sideways", OnlySidewaysEvent.TYPE);
        register(registry, "place_lava_block", PlaceLavaBlockEvent.TYPE);
        register(registry, "random_drops", RandomDropsEvent.TYPE);
        register(registry, "reduced_reach", ReducedReachEvent.TYPE);
        register(registry, "roll_credits", RollCreditsEvent.TYPE);
        register(registry, "slippery", SlipperyEvent.TYPE);
        register(registry, "teleport_spawn", Teleport0Event.TYPE);
        register(registry, "teleport_heaven", TeleportHeavenEvent.TYPE);
        register(registry, "timelapse", TimelapseEvent.TYPE);
        register(registry, "tnt", TntEvent.TYPE);
        register(registry, "ultra_fov", UltraFovEvent.TYPE);
        register(registry, "ultra_low_fov", UltraLowFovEvent.TYPE);
        register(registry, "upside_down", UpsideDownEvent.TYPE);
        register(registry, "vertical_screen", VerticalScreenEvent.TYPE);
        //register(registry, "where_is_everything", WhereIsEverythingEvent.TYPE); // No longer works on >1.19
        register(registry, "xp_rain", XpRainEvent.TYPE);
        register(registry, "heal", HealEvent.TYPE);
        register(registry, "randomize_armor", RandomizeArmorEvent.TYPE);
        register(registry, "force_third_person", ForceThirdPersonEvent.TYPE);
        register(registry, "force_front_view", ForceFrontViewEvent.TYPE);
        register(registry, "hide_events", HideEventsEvent.TYPE);
        register(registry, "top_down_view", TopDownViewEvent.TYPE);
        register(registry, "phantom", PhantomEvent.TYPE);
        register(registry, "timer_speed_2", TimerSpeed2Event.TYPE);
        register(registry, "timer_speed_5", TimerSpeed5Event.TYPE);
        register(registry, "timer_speed_half", TimerSpeedHalfEvent.TYPE);
        register(registry, "resistance", ResistanceEvent.TYPE);
        register(registry, "fatigue", FatigueEvent.TYPE);
        register(registry, "blindness", BlindnessEvent.TYPE);
        register(registry, "speed", SpeedEvent.TYPE);
        register(registry, "starter_pack", StarterPackEvent.TYPE);
        register(registry, "damage_items", DamageItemsEvent.TYPE);
        register(registry, "levitation", LevitationEvent.TYPE);
        register(registry, "spinning_mobs", SpinningMobsEvent.TYPE);
        register(registry, "sinkhole", SinkholeEvent.TYPE);
        register(registry, "pool", PoolEvent.TYPE);
        register(registry, "random_creeper", RandomCreeperEvent.TYPE);
        register(registry, "sinking", SinkingEvent.TYPE);
        register(registry, "slime", SlimeEvent.TYPE);
        register(registry, "horse", HorseEvent.TYPE);
        register(registry, "fire", FireEvent.TYPE);
        register(registry, "adventure", AdventureEvent.TYPE);
        register(registry, "pit", PitEvent.TYPE);
        register(registry, "sky", SkyEvent.TYPE);
        register(registry, "pumpkin_view", PumpkinViewEvent.TYPE);
        register(registry, "night_vision", NightVisionEvent.TYPE);
        register(registry, "explosive_pickaxe", ExplosivePickaxeEvent.TYPE);
        register(registry, "highlight_all_mobs", HighlightAllMobsEvent.TYPE);
        register(registry, "upgrade_random_gear", UpgradeRandomGearEvent.TYPE);
        register(registry, "downgrade_random_gear", DowngradeRandomGearEvent.TYPE);
        register(registry, "curse_random_gear", CurseRandomGearEvent.TYPE);
        register(registry, "enchant_random_gear", EnchantRandomGearEvent.TYPE);
        register(registry, "invisible_player", InvisiblePlayerEvent.TYPE);
        register(registry, "invisible_hostile_mobs", InvisibleHostileMobsEvent.TYPE);
        register(registry, "invisible_everyone", InvisibleEveryoneEvent.TYPE);
        register(registry, "vex_attack", VexAttackEvent.TYPE);
        register(registry, "zeus_ult", ZeusUltEvent.TYPE);
        register(registry, "gravity_sight", GravitySightEvent.TYPE);
        register(registry, "death_sight", DeathSightEvent.TYPE);
        register(registry, "glass_sight", GlassSightEvent.TYPE);
        register(registry, "void_sight", VoidSightEvent.TYPE);
        register(registry, "mining_sight", MiningSightEvent.TYPE);
        register(registry, "sky_block", SkyBlockEvent.TYPE);
        register(registry, "ride_closest_mob", RideClosestMobEvent.TYPE);
        register(registry, "true_frost_walker", TrueFrostWalkerEvent.TYPE);
        register(registry, "so_sweet", SoSweetEvent.TYPE);
        register(registry, "place_cobweb_block", PlaceCobwebBlockEvent.TYPE);
        register(registry, "flip_mobs", FlipMobsEvent.TYPE);
        register(registry, "spawn_rainbow_sheep", SpawnRainbowSheepEvent.TYPE);
        register(registry, "fix_items", FixItemsEvent.TYPE);
        register(registry, "midas_touch", MidasTouchEvent.TYPE);
        register(registry, "give_random_ore", GiveRandomOreEvent.TYPE);
        register(registry, "bee", BeeEvent.TYPE);
        register(registry, "angry_bee", AngryBeeEvent.TYPE);
        register(registry, "silverfish", SilverfishEvent.TYPE);
        register(registry, "blaze", BlazeEvent.TYPE);
        register(registry, "endermite", EndermiteEvent.TYPE);
        register(registry, "satiation", SatiationEvent.TYPE);
        register(registry, "vitals", VitalsEvent.TYPE);
        register(registry, "teleport_nearby_entities", TeleportNearbyEntitiesEvent.TYPE);
        register(registry, "force_sneak", ForceSneakEvent.TYPE);
        register(registry, "shuffle_inventory", ShuffleInventoryEvent.TYPE);
        register(registry, "bulldoze", BulldozeEvent.TYPE);
        register(registry, "slime_pyramid", SlimePyramidEvent.TYPE);
        register(registry, "flying_machine", FlyingMachineEvent.TYPE);
        register(registry, "add_heart", AddHeartEvent.TYPE);
        register(registry, "remove_heart", RemoveHeartEvent.TYPE);
        register(registry, "noise_machine", NoiseMachineEvent.TYPE);
        register(registry, "xray", XRayEvent.TYPE);
        register(registry, "lag", LagEvent.TYPE);
        register(registry, "low_fps", LowFPSEvent.TYPE);
        register(registry, "infinite_lava", InfiniteLavaEvent.TYPE);
        register(registry, "random_camera_tilt", RandomCameraTiltEvent.TYPE);
        register(registry, "no_attacking", NoAttackingEvent.TYPE);
        register(registry, "constant_attacking", ConstantAttackingEvent.TYPE);
        register(registry, "spawn_killer_bunny", SpawnKillerBunnyEvent.TYPE);
        register(registry, "spawn_pet_dog", SpawnPetDogEvent.TYPE);
        register(registry, "spawn_pet_cat", SpawnPetCatEvent.TYPE);
        register(registry, "haunted_chests", HauntedChestsEvent.TYPE);
        register(registry, "no_use_key", NoUseKeyEvent.TYPE);
        register(registry, "constant_interacting", ConstantInteractingEvent.TYPE);
        register(registry, "mlg_bucket", MLGBucketEvent.TYPE);
        register(registry, "stuttering", StutteringEvent.TYPE);
        register(registry, "force_horse_riding", ForceHorseRidingEvent.TYPE);
        register(registry, "jumpscare", JumpscareEvent.TYPE);
        register(registry, "rolling_camera", RollingCameraEvent.TYPE);
        register(registry, "fling_entities", FlingEntitiesEvent.TYPE);
        register(registry, "black_and_white", BlackAndWhiteEvent.TYPE);
        register(registry, "creative_flight", CreativeFlightEvent.TYPE);
        register(registry, "fake_teleport", FakeTeleportEvent.TYPE);
        register(registry, "fake_fake_teleport", FakeFakeTeleportEvent.TYPE);
        register(registry, "forcefield", ForcefieldEvent.TYPE);
        register(registry, "entity_magnet", EntityMagnetEvent.TYPE);
        register(registry, "one_punch", OnePunchEvent.TYPE);
        register(registry, "infestation", InfestationEvent.TYPE);
        register(registry, "rainbow_path", RainbowPathEvent.TYPE);
        register(registry, "silence", SilenceEvent.TYPE);
        register(registry, "nothing", NothingEvent.TYPE);
        register(registry, "rainbow_trails", RainbowTrailsEvent.TYPE);
        register(registry, "rainbow_sheep_everywhere", RainbowSheepEverywhereEvent.TYPE);
        register(registry, "armor_trim", ArmorTrimEvent.TYPE);
        return FabricRegistryBuilder.from(registry).buildAndRegister();
    }

    private static void register(Registry<EventType<?>> registry, String id, EventType<?> type) {
        Registry.register(registry, ResourceLocation.fromNamespaceAndPath("entropy", id), type);
    }

    public static Event getRandomDifferentEvent(List<Event> currentEvents){

        Map<ResourceLocation, EventType<?>> eventCandidates = EVENTS.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().location(), Map.Entry::getValue));
        Set<ResourceLocation> eventsToRemove = new HashSet<>(Entropy.getInstance().settings.disabledEventTypes);
        Set<EventCategory> ignoredEventCategories = new HashSet<>();

        currentEvents.forEach(event -> {
            EventType<?> type = event.getType();
            eventsToRemove.add(EVENTS.getKey(type));

            if(event.getTickCount()>0 && !event.hasEnded() && type.category() != EventCategory.NONE)
                ignoredEventCategories.add(type.category());
        });

        Level overworld = Entropy.getInstance().eventHandler.server.overworld();
        eventCandidates.forEach((eventId, type) -> {
            if (!type.doesWorldHaveRequiredFeatures(overworld)
                || !type.isEnabled()
                || ignoredEventCategories.contains(type.category())) {
                eventsToRemove.add(eventId);
            }
        });

        //Only enable the stuttering event on a dedicated server, because otherwise worldgen will be all wrong.
        //The MathHelper mixin turning off linear interpolation is only applied on the client, but if this is a singleplayer environment,
        //the integrated server has the modified MathHelper, too, causing incorrect worldgen.
        if(FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            eventsToRemove.add(getEventId(StutteringEvent.TYPE));

        eventsToRemove.forEach(eventCandidates::remove);
        return getRandomEvent(new ArrayList<>(eventCandidates.values()));
    }

    private static Event getRandomEvent(List<EventType<?>> eventTypes) {
        if(eventTypes.isEmpty())
            return null;

        int index = random.nextInt(eventTypes.size());
        EventType<?> newEventType = eventTypes.get(index);
        return newEventType.create();
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
}
