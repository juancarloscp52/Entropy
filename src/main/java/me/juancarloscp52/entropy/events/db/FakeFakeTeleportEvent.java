package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventRegistry;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.events.TypedEvent;
import me.juancarloscp52.entropy.events.db.FakeTeleportEvent.TeleportInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class FakeFakeTeleportEvent extends AbstractInstantEvent {
    public static final StreamCodec<FriendlyByteBuf, FakeFakeTeleportEvent> STREAM_CODEC = StreamCodec.composite(
        TypedEvent.STREAM_CODEC, e -> e.fakeTeleportEvent,
        FakeFakeTeleportEvent::new
    );
    private final Map<ServerPlayer,TeleportInfo> positionsBeforeFakeTeleport = new HashMap<>();
    private final TypedEvent<FakeTeleportEvent> fakeTeleportEvent;
    private int ticksAfterSecondTeleport = 0;

    public FakeFakeTeleportEvent() {
        this((TypedEvent<FakeTeleportEvent>) TypedEvent.fromEventType(EventRegistry.EVENTS.get(ResourceLocation.fromNamespaceAndPath("entropy", "fake_teleport")).get().value()));
    }

    public FakeFakeTeleportEvent(TypedEvent<?> fakeTeleportEvent) {
        this.fakeTeleportEvent = (TypedEvent<FakeTeleportEvent>) fakeTeleportEvent;
    }

    @Override
    public void init() {
        fakeTeleportEvent.event().init();
    }

    @Override
    public void tick() {
        if(!fakeTeleportEvent.event().teleportEvent().hasEnded())
            fakeTeleportEvent.event().teleportEvent().tick();
        else if(++fakeTeleportEvent.event().ticksAfterFirstTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK) {
            FakeTeleportEvent.savePositions(positionsBeforeFakeTeleport);
            FakeTeleportEvent.loadPositions(fakeTeleportEvent.event().originalPositions);
        }
        else if(fakeTeleportEvent.event().ticksAfterFirstTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK && ++ticksAfterSecondTeleport == FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK)
            FakeTeleportEvent.loadPositions(positionsBeforeFakeTeleport);
    }

    @Override
    public void tickClient() {
        if(!fakeTeleportEvent.event().hasEnded())
            fakeTeleportEvent.event().tickClient();
        else
            ticksAfterSecondTeleport++;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderQueueItem(EventType<?> eventType, GuiGraphics drawContext, float tickdelta, int x, int y) {
        if(hasEnded())
            super.renderQueueItem(eventType, drawContext, tickdelta, x, y);
        else
            fakeTeleportEvent.event().renderQueueItem(fakeTeleportEvent.type(), drawContext, tickdelta, x, y);
    }

    @Override
    public boolean hasEnded() {
        return fakeTeleportEvent.event().hasEnded() && ticksAfterSecondTeleport > FakeTeleportEvent.TICKS_UNTIL_TELEPORT_BACK;
    }
}
