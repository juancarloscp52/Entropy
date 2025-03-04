package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record C2SVotes(int voteId, int[] votes) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, C2SVotes> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, C2SVotes::voteId,
        ExtraPacketCodecs.intArray(), C2SVotes::votes,
        C2SVotes::new
    );

    @Override
    public Type<C2SVotes> type() {
        return NetworkingConstants.VOTES;
    }
}
