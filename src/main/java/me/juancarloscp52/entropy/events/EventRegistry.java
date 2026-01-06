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
import me.juancarloscp52.entropy.events.db.AddHeartEvent;
import me.juancarloscp52.entropy.events.db.AdventureEvent;
import me.juancarloscp52.entropy.events.db.AngryBeeEvent;
import me.juancarloscp52.entropy.events.db.ArmorCurseEvent;
import me.juancarloscp52.entropy.events.db.ArmorTrimEvent;
import me.juancarloscp52.entropy.events.db.ArrowRainEvent;
import me.juancarloscp52.entropy.events.db.BalloonRaceEvent;
import me.juancarloscp52.entropy.events.db.BeeEvent;
import me.juancarloscp52.entropy.events.db.BlackAndWhiteEvent;
import me.juancarloscp52.entropy.events.db.BlazeEvent;
import me.juancarloscp52.entropy.events.db.BlindnessEvent;
import me.juancarloscp52.entropy.events.db.BlurEvent;
import me.juancarloscp52.entropy.events.db.BouncyBlocksEvent;
import me.juancarloscp52.entropy.events.db.BulldozeEvent;
import me.juancarloscp52.entropy.events.db.CRTEvent;
import me.juancarloscp52.entropy.events.db.ChickenRainEvent;
import me.juancarloscp52.entropy.events.db.CinematicScreenEvent;
import me.juancarloscp52.entropy.events.db.ConstantAttackingEvent;
import me.juancarloscp52.entropy.events.db.ConstantInteractingEvent;
import me.juancarloscp52.entropy.events.db.CreativeFlightEvent;
import me.juancarloscp52.entropy.events.db.CreeperEvent;
import me.juancarloscp52.entropy.events.db.CurseRandomGearEvent;
import me.juancarloscp52.entropy.events.db.DVDEvent;
import me.juancarloscp52.entropy.events.db.DamageItemsEvent;
import me.juancarloscp52.entropy.events.db.DeathSightEvent;
import me.juancarloscp52.entropy.events.db.DowngradeRandomGearEvent;
import me.juancarloscp52.entropy.events.db.DropHandItemEvent;
import me.juancarloscp52.entropy.events.db.DropInventoryEvent;
import me.juancarloscp52.entropy.events.db.EnchantRandomGearEvent;
import me.juancarloscp52.entropy.events.db.EndermiteEvent;
import me.juancarloscp52.entropy.events.db.EntityMagnetEvent;
import me.juancarloscp52.entropy.events.db.ExplodeNearbyEntitiesEvent;
import me.juancarloscp52.entropy.events.db.ExplosivePickaxeEvent;
import me.juancarloscp52.entropy.events.db.ExtremeExplosionEvent;
import me.juancarloscp52.entropy.events.db.FakeFakeTeleportEvent;
import me.juancarloscp52.entropy.events.db.FakeTeleportEvent;
import me.juancarloscp52.entropy.events.db.FatigueEvent;
import me.juancarloscp52.entropy.events.db.FireEvent;
import me.juancarloscp52.entropy.events.db.FixItemsEvent;
import me.juancarloscp52.entropy.events.db.FlingEntitiesEvent;
import me.juancarloscp52.entropy.events.db.FlipMobsEvent;
import me.juancarloscp52.entropy.events.db.FlyingMachineEvent;
import me.juancarloscp52.entropy.events.db.ForceForwardEvent;
import me.juancarloscp52.entropy.events.db.ForceFrontViewEvent;
import me.juancarloscp52.entropy.events.db.ForceHorseRidingEvent;
import me.juancarloscp52.entropy.events.db.ForceJump2Event;
import me.juancarloscp52.entropy.events.db.ForceJumpEvent;
import me.juancarloscp52.entropy.events.db.ForceSneakEvent;
import me.juancarloscp52.entropy.events.db.ForceThirdPersonEvent;
import me.juancarloscp52.entropy.events.db.ForcefieldEvent;
import me.juancarloscp52.entropy.events.db.GiveRandomOreEvent;
import me.juancarloscp52.entropy.events.db.GlassSightEvent;
import me.juancarloscp52.entropy.events.db.GravitySightEvent;
import me.juancarloscp52.entropy.events.db.HalfHeartedEvent;
import me.juancarloscp52.entropy.events.db.HauntedChestsEvent;
import me.juancarloscp52.entropy.events.db.HealEvent;
import me.juancarloscp52.entropy.events.db.HerobrineEvent;
import me.juancarloscp52.entropy.events.db.HideEventsEvent;
import me.juancarloscp52.entropy.events.db.HighPitchEvent;
import me.juancarloscp52.entropy.events.db.HighlightAllMobsEvent;
import me.juancarloscp52.entropy.events.db.HorseEvent;
import me.juancarloscp52.entropy.events.db.HungryEvent;
import me.juancarloscp52.entropy.events.db.HyperSlowEvent;
import me.juancarloscp52.entropy.events.db.HyperSpeedEvent;
import me.juancarloscp52.entropy.events.db.IgniteNearbyEntitiesEvent;
import me.juancarloscp52.entropy.events.db.InfestationEvent;
import me.juancarloscp52.entropy.events.db.InfiniteLavaEvent;
import me.juancarloscp52.entropy.events.db.IntenseThunderStormEvent;
import me.juancarloscp52.entropy.events.db.InvertedColorsEvent;
import me.juancarloscp52.entropy.events.db.InvertedControlsEvent;
import me.juancarloscp52.entropy.events.db.InvisibleEveryoneEvent;
import me.juancarloscp52.entropy.events.db.InvisibleHostileMobsEvent;
import me.juancarloscp52.entropy.events.db.InvisiblePlayerEvent;
import me.juancarloscp52.entropy.events.db.ItemRainEvent;
import me.juancarloscp52.entropy.events.db.JumpscareEvent;
import me.juancarloscp52.entropy.events.db.LSDEvent;
import me.juancarloscp52.entropy.events.db.LagEvent;
import me.juancarloscp52.entropy.events.db.LevitationEvent;
import me.juancarloscp52.entropy.events.db.LowFPSEvent;
import me.juancarloscp52.entropy.events.db.LowGravityEvent;
import me.juancarloscp52.entropy.events.db.LowPitchEvent;
import me.juancarloscp52.entropy.events.db.LowRenderDistanceEvent;
import me.juancarloscp52.entropy.events.db.LuckyDropsEvent;
import me.juancarloscp52.entropy.events.db.MLGBucketEvent;
import me.juancarloscp52.entropy.events.db.MeteorRainEvent;
import me.juancarloscp52.entropy.events.db.MidasTouchEvent;
import me.juancarloscp52.entropy.events.db.MiningSightEvent;
import me.juancarloscp52.entropy.events.db.MouseDriftingEvent;
import me.juancarloscp52.entropy.events.db.NightVisionEvent;
import me.juancarloscp52.entropy.events.db.NoAttackingEvent;
import me.juancarloscp52.entropy.events.db.NoDropsEvent;
import me.juancarloscp52.entropy.events.db.NoJumpEvent;
import me.juancarloscp52.entropy.events.db.NoUseKeyEvent;
import me.juancarloscp52.entropy.events.db.NoiseMachineEvent;
import me.juancarloscp52.entropy.events.db.NothingEvent;
import me.juancarloscp52.entropy.events.db.OnePunchEvent;
import me.juancarloscp52.entropy.events.db.OnlyBackwardsEvent;
import me.juancarloscp52.entropy.events.db.OnlySidewaysEvent;
import me.juancarloscp52.entropy.events.db.PhantomEvent;
import me.juancarloscp52.entropy.events.db.PitEvent;
import me.juancarloscp52.entropy.events.db.PlaceCobwebBlockEvent;
import me.juancarloscp52.entropy.events.db.PlaceLavaBlockEvent;
import me.juancarloscp52.entropy.events.db.PoolEvent;
import me.juancarloscp52.entropy.events.db.PumpkinViewEvent;
import me.juancarloscp52.entropy.events.db.RaidEvent;
import me.juancarloscp52.entropy.events.db.RainbowFogEvent;
import me.juancarloscp52.entropy.events.db.RainbowPathEvent;
import me.juancarloscp52.entropy.events.db.RainbowSheepEverywhereEvent;
import me.juancarloscp52.entropy.events.db.RainbowTrailsEvent;
import me.juancarloscp52.entropy.events.db.RandomCameraTiltEvent;
import me.juancarloscp52.entropy.events.db.RandomCreeperEvent;
import me.juancarloscp52.entropy.events.db.RandomDropsEvent;
import me.juancarloscp52.entropy.events.db.RandomTPEvent;
import me.juancarloscp52.entropy.events.db.RandomizeArmorEvent;
import me.juancarloscp52.entropy.events.db.ReducedReachEvent;
import me.juancarloscp52.entropy.events.db.RemoveEnchantmentsEvent;
import me.juancarloscp52.entropy.events.db.RemoveHeartEvent;
import me.juancarloscp52.entropy.events.db.ResistanceEvent;
import me.juancarloscp52.entropy.events.db.RideClosestMobEvent;
import me.juancarloscp52.entropy.events.db.RollCreditsEvent;
import me.juancarloscp52.entropy.events.db.RollingCameraEvent;
import me.juancarloscp52.entropy.events.db.SatiationEvent;
import me.juancarloscp52.entropy.events.db.ShuffleInventoryEvent;
import me.juancarloscp52.entropy.events.db.SilenceEvent;
import me.juancarloscp52.entropy.events.db.SilverfishEvent;
import me.juancarloscp52.entropy.events.db.SinkholeEvent;
import me.juancarloscp52.entropy.events.db.SinkingEvent;
import me.juancarloscp52.entropy.events.db.SkyBlockEvent;
import me.juancarloscp52.entropy.events.db.SkyEvent;
import me.juancarloscp52.entropy.events.db.SlimeEvent;
import me.juancarloscp52.entropy.events.db.SlimePyramidEvent;
import me.juancarloscp52.entropy.events.db.SlipperyEvent;
import me.juancarloscp52.entropy.events.db.SoSweetEvent;
import me.juancarloscp52.entropy.events.db.SpawnKillerBunnyEvent;
import me.juancarloscp52.entropy.events.db.SpawnPetEvent;
import me.juancarloscp52.entropy.events.db.SpawnRainbowSheepEvent;
import me.juancarloscp52.entropy.events.db.SpeedEvent;
import me.juancarloscp52.entropy.events.db.SpinningMobsEvent;
import me.juancarloscp52.entropy.events.db.StarterPackEvent;
import me.juancarloscp52.entropy.events.db.StutteringEvent;
import me.juancarloscp52.entropy.events.db.Teleport0Event;
import me.juancarloscp52.entropy.events.db.TeleportHeavenEvent;
import me.juancarloscp52.entropy.events.db.TeleportNearbyEntitiesEvent;
import me.juancarloscp52.entropy.events.db.TimelapseEvent;
import me.juancarloscp52.entropy.events.db.TimerSpeed2Event;
import me.juancarloscp52.entropy.events.db.TimerSpeed5Event;
import me.juancarloscp52.entropy.events.db.TimerSpeedHalfEvent;
import me.juancarloscp52.entropy.events.db.TntEvent;
import me.juancarloscp52.entropy.events.db.TopDownViewEvent;
import me.juancarloscp52.entropy.events.db.TrueFrostWalkerEvent;
import me.juancarloscp52.entropy.events.db.UltraFovEvent;
import me.juancarloscp52.entropy.events.db.UltraLowFovEvent;
import me.juancarloscp52.entropy.events.db.UpgradeRandomGearEvent;
import me.juancarloscp52.entropy.events.db.UpsideDownEvent;
import me.juancarloscp52.entropy.events.db.VerticalScreenEvent;
import me.juancarloscp52.entropy.events.db.VexAttackEvent;
import me.juancarloscp52.entropy.events.db.VitalsEvent;
import me.juancarloscp52.entropy.events.db.VoidSightEvent;
import me.juancarloscp52.entropy.events.db.WardenEvent;
import me.juancarloscp52.entropy.events.db.XRayEvent;
import me.juancarloscp52.entropy.events.db.XpRainEvent;
import me.juancarloscp52.entropy.events.db.ZeusUltEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EventRegistry {
    private static final Random random = new Random();
    public static final ResourceKey<Registry<EventType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("entropy", "events"));
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
        register(registry, "random_tp", RandomTPEvent.TYPE);
        register(registry, "creeper", CreeperEvent.TYPE);
        register(registry, "crt", CRTEvent.TYPE);
        register(registry, "drop_hand_item", DropHandItemEvent.TYPE);
        register(registry, "drop_inventory", DropInventoryEvent.TYPE);
        register(registry, "dvd", DVDEvent.TYPE);
        register(registry, "explode_nearby_entities", ExplodeNearbyEntitiesEvent.TYPE);
        register(registry, "extreme_explosion", ExtremeExplosionEvent.TYPE);
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
        register(registry, "spawn_pet", SpawnPetEvent.TYPE);
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
        register(registry, "bouncy_blocks", BouncyBlocksEvent.TYPE);
        register(registry, "balloon_race", BalloonRaceEvent.TYPE);
        register(registry, "rainbow_fog", RainbowFogEvent.TYPE);
        return FabricRegistryBuilder.from(registry).buildAndRegister();
    }

    private static void register(Registry<EventType<?>> registry, String id, EventType<?> type) {
        Registry.register(registry, Identifier.fromNamespaceAndPath("entropy", id), type);
    }

    public static Optional<Holder.Reference<EventType<?>>> getRandomDifferentEvent(List<Event> notThese, List<Event> andAlsoNotThese) {
        return getRandomDifferentEvent(Stream.concat(notThese.stream(), andAlsoNotThese.stream()).toList());
    }

    public static Optional<Holder.Reference<EventType<?>>> getRandomDifferentEvent(List<Event> currentEvents) {

        List<Holder.Reference<EventType<?>>> eventCandidates = EVENTS.listElements().collect(Collectors.toList());
        Set<ResourceKey<EventType<?>>> eventsToRemove = new HashSet<>(Entropy.getInstance().settings.disabledEventTypes);
        Set<EventCategory> ignoredEventCategories = new HashSet<>();

        currentEvents.forEach(event -> {
            EventType<?> type = event.getType();
            eventsToRemove.add(getEventId(type));

            if (event.getTickCount() > 0 && !event.hasEnded() && type.category() != EventCategory.NONE)
                ignoredEventCategories.add(type.category());
        });

        Level overworld = Entropy.getInstance().eventHandler.server.overworld();
        eventCandidates.forEach(typeReference -> {
            EventType<?> type = typeReference.value();
            if (!type.doesWorldHaveRequiredFeatures(overworld)
                || !type.isEnabled()
                || ignoredEventCategories.contains(type.category())) {
                eventsToRemove.add(typeReference.key());
            }
        });

        //Only enable the stuttering event on a dedicated server, because otherwise worldgen will be all wrong.
        //The MathHelper mixin turning off linear interpolation is only applied on the client, but if this is a singleplayer environment,
        //the integrated server has the modified MathHelper, too, causing incorrect worldgen.
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            eventsToRemove.add(getEventId(StutteringEvent.TYPE));

        Set<Identifier> ids = eventsToRemove.stream().map(ResourceKey::identifier).collect(Collectors.toSet());
        eventCandidates.removeIf(candidate -> ids.contains(candidate.key().identifier()));
        return getRandomEvent(eventCandidates);
    }

    private static Optional<Holder.Reference<EventType<?>>> getRandomEvent(List<Holder.Reference<EventType<?>>> eventTypes) {
        if(eventTypes.isEmpty())
            return Optional.empty();

        return Optional.of(eventTypes.get(random.nextInt(eventTypes.size())));
    }

    public static ResourceKey<EventType<?>> getEventId(EventType<?> eventType) {
        return EVENTS.getResourceKey(eventType).get();
    }
}
