package me.juancarloscp52.entropy.networking;

import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record ClientboundJoinSync(List<EventData> events) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundJoinSync> CODEC = StreamCodec.composite(
        EventData.CODEC.apply(ByteBufCodecs.list()), ClientboundJoinSync::events,
        ClientboundJoinSync::new
    );

    public record EventData(
        Event event,
        boolean ended,
        short tickCount
    ) {
        public static final StreamCodec<RegistryFriendlyByteBuf, EventData> CODEC = StreamCodec.composite(
            EventRegistry.STREAM_CODEC, EventData::event,
            ByteBufCodecs.BOOL, EventData::ended,
            ByteBufCodecs.SHORT, EventData::tickCount,
            EventData::new
        );
    }

    @Override
    public Type<ClientboundJoinSync> type() {
        return NetworkingConstants.JOIN_SYNC;
    }
}
