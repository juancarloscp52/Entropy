package me.juancarloscp52.entropy.networking;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundJoinSync(List<EventData> events) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundJoinSync> CODEC = StreamCodec.composite(
        EventData.CODEC.apply(ByteBufCodecs.list()), ClientboundJoinSync::events,
        ClientboundJoinSync::new
    );

    public record EventData(
        String id,
        boolean ended,
        short tickCount
    ) {
        public static final StreamCodec<FriendlyByteBuf, EventData> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EventData::id,
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
