package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FakeTeleportEvent extends AbstractInstantEvent {
    public static final StreamCodec<RegistryFriendlyByteBuf, FakeTeleportEvent> STREAM_CODEC = StreamCodec.composite(
        EventRegistry.STREAM_CODEC, e -> e.teleportEvent,
        FakeTeleportEvent::new
    );
    public static final EventType<FakeTeleportEvent> TYPE = EventType.builder(FakeTeleportEvent::new).streamCodec(STREAM_CODEC).build();
    private static final List<EventType<?>> TELEPORT_EVENTS = Arrays.asList(
        CloseRandomTPEvent.TYPE,
        FarRandomTPEvent.TYPE,
        SkyBlockEvent.TYPE,
        SkyEvent.TYPE,
        Teleport0Event.TYPE,
        TeleportHeavenEvent.TYPE
    );
    public static final int TICKS_UNTIL_TELEPORT_BACK = 100;
    final Map<ServerPlayer,TeleportInfo> originalPositions = new HashMap<>();
    final Event teleportEvent;
    int ticksAfterFirstTeleport = 0;

    public FakeTeleportEvent() {
        this(TELEPORT_EVENTS.get(new Random().nextInt(TELEPORT_EVENTS.size())).create());
    }

    public FakeTeleportEvent(Event teleportEvent) {
        this.teleportEvent = teleportEvent;
    }

    @Override
    public void init() {
        savePositions(originalPositions);
        teleportEvent.init();
    }

    @Override
    public void tick() {
        if(!teleportEvent.hasEnded())
            teleportEvent.tick();
        else if(++ticksAfterFirstTeleport == TICKS_UNTIL_TELEPORT_BACK)
            loadPositions(originalPositions);
    }

    @Override
    public void tickClient() {
        if(!teleportEvent.hasEnded())
            teleportEvent.tickClient();
        else
            ticksAfterFirstTeleport++;
    }

    @Override
    public Component getDescription() {
        return hasEnded() ? getVoteDescription() : teleportEvent.getDescription();
    }

    @Override
    public Component getVoteDescription() {
        final MutableComponent description = Component.translatable(getType().getLanguageKey() + ".vote", teleportEvent.getDescription());
        if (!getType().isEnabled()) {
            return description.withStyle(ChatFormatting.STRIKETHROUGH);
        }

        return description;
    }

    @Override
    public boolean hasEnded() {
        return teleportEvent.hasEnded() && ticksAfterFirstTeleport > TICKS_UNTIL_TELEPORT_BACK;
    }

    public static void savePositions(Map<ServerPlayer,TeleportInfo> positions) {
        for(ServerPlayer player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            BlockPos pos = player.blockPosition();

            positions.put(player, new TeleportInfo(pos.getX(), pos.getY(), pos.getZ(), player.getYHeadRot(), player.getXRot()));
        }
    }

    public static void loadPositions(Map<ServerPlayer,TeleportInfo> positions) {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            TeleportInfo info = positions.get(player);

            player.fallDistance = 0;
            player.teleportTo(player.level(), info.x + 0.5D, info.y, info.z + 0.5D, Set.of(), info.yaw, info.pitch, true);
        });
    }

    public static record TeleportInfo(int x, int y, int z, float yaw, float pitch) {}

    @Override
    public EventType<FakeTeleportEvent> getType() {
        return TYPE;
    }
}
