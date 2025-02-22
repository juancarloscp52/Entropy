package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public record S2CJoinSync(List<EventData> events) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CJoinSync> CODEC = PacketCodec.tuple(
        EventData.CODEC.collect(PacketCodecs.toList()), S2CJoinSync::events,
        S2CJoinSync::new
    );

    public record EventData(
        String id,
        boolean ended,
        short tickCount
    ) {
        public static final PacketCodec<PacketByteBuf, EventData> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, EventData::id,
            PacketCodecs.BOOL, EventData::ended,
            PacketCodecs.SHORT, EventData::tickCount,
            EventData::new
        );
    }

    @Override
    public Id<S2CJoinSync> getId() {
        return NetworkingConstants.JOIN_SYNC;
    }
}
