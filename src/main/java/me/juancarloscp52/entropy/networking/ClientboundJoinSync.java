package me.juancarloscp52.entropy.networking;

import me.juancarloscp52.entropy.events.TypedEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record ClientboundJoinSync(List<EventData> events) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundJoinSync> CODEC = StreamCodec.composite(
        EventData.CODEC.apply(ByteBufCodecs.list()), ClientboundJoinSync::events,
        ClientboundJoinSync::new
    );

    public record EventData(
        TypedEvent<?> typedEvent,
        boolean ended,
        short tickCount
    ) {
        public static final StreamCodec<FriendlyByteBuf, EventData> CODEC = StreamCodec.composite(
            TypedEvent.STREAM_CODEC, EventData::typedEvent,
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
