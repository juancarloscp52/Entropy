package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public record S2CNewPoll(int voteId, List<String> events) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CNewPoll> CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT, S2CNewPoll::voteId,
        PacketCodecs.STRING.collect(PacketCodecs.toList()), S2CNewPoll::events,
        S2CNewPoll::new
    );

    @Override
    public Id<S2CNewPoll> getId() {
        return NetworkingConstants.NEW_POLL;
    }
}
