package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.events.db.FakeTeleportEvent.TeleportInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class FakeFakeTeleportEvent extends AbstractInstantEvent {
    public static final StreamCodec<RegistryFriendlyByteBuf, FakeFakeTeleportEvent> STREAM_CODEC = StreamCodec.composite(
        FakeTeleportEvent.STREAM_CODEC, e -> e.fakeTeleportEvent,
        FakeFakeTeleportEvent::new
    );
    public static final EventType<FakeFakeTeleportEvent> TYPE = EventType.builder(FakeFakeTeleportEvent::new).streamCodec(STREAM_CODEC).build();
    private final Map<ServerPlayer,TeleportInfo> positionsBeforeFakeTeleport = new HashMap<>();
    private final FakeTeleportEvent fakeTeleportEvent;
    private int ticksAfterSecondTeleport = 0;

    public FakeFakeTeleportEvent() {
        this(FakeTeleportEvent.TYPE.create());
    }

    public FakeFakeTeleportEvent(FakeTeleportEvent fakeTeleportEvent) {
        this.fakeTeleportEvent = fakeTeleportEvent;
    }

    @Override
    public void init() {
        fakeTeleportEvent.init();
    }

    @Override
    public void tick() {
        if(!fakeTeleportEvent.teleportEvent.hasEnded())
            fakeTeleportEvent.teleportEvent.tick();
        else if(++fakeTeleportEvent.ticksAfterFirstTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK) {
            FakeTeleportEvent.savePositions(positionsBeforeFakeTeleport);
            FakeTeleportEvent.loadPositions(fakeTeleportEvent.originalPositions);
        }
        else if(fakeTeleportEvent.ticksAfterFirstTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK && ++ticksAfterSecondTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK)
            FakeTeleportEvent.loadPositions(positionsBeforeFakeTeleport);
    }

    @Override
    public void tickClient() {
        if(!fakeTeleportEvent.hasEnded())
            fakeTeleportEvent.tickClient();
        else
            ticksAfterSecondTeleport++;
    }

    @Override
    public Component getDescription() {
        return hasEnded() ? super.getDescription() : fakeTeleportEvent.getDescription();
    }

    @Override
    public boolean hasEnded() {
        return fakeTeleportEvent.hasEnded() && ticksAfterSecondTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK;
    }

    @Override
    public EventType<FakeFakeTeleportEvent> getType() {
        return TYPE;
    }
}
