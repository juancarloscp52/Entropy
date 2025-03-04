package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ServerboundVotes(int voteId, int[] votes) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ServerboundVotes> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ServerboundVotes::voteId,
        ExtraPacketCodecs.intArray(), ServerboundVotes::votes,
        ServerboundVotes::new
    );

    @Override
    public Type<ServerboundVotes> type() {
        return NetworkingConstants.VOTES;
    }
}
