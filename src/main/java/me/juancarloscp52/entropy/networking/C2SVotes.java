package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record C2SVotes(int voteId, int[] votes) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, C2SVotes> CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT, C2SVotes::voteId,
        ExtraPacketCodecs.intArray(), C2SVotes::votes,
        C2SVotes::new
    );

    @Override
    public Id<C2SVotes> getId() {
        return NetworkingConstants.VOTES;
    }
}
