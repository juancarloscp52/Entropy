package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CPollStatus(int voteId, int[] totalVotes, int totalVotesCount) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CPollStatus> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, S2CPollStatus::voteId,
        ExtraPacketCodecs.intArray(), S2CPollStatus::totalVotes,
        ByteBufCodecs.VAR_INT, S2CPollStatus::totalVotesCount,
        S2CPollStatus::new
    );

    @Override
    public Type<S2CPollStatus> type() {
        return NetworkingConstants.POLL_STATUS;
    }
}
