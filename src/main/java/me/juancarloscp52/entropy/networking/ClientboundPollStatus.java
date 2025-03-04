package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundPollStatus(int voteId, int[] totalVotes, int totalVotesCount) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundPollStatus> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ClientboundPollStatus::voteId,
        ExtraPacketCodecs.intArray(), ClientboundPollStatus::totalVotes,
        ByteBufCodecs.VAR_INT, ClientboundPollStatus::totalVotesCount,
        ClientboundPollStatus::new
    );

    @Override
    public Type<ClientboundPollStatus> type() {
        return NetworkingConstants.POLL_STATUS;
    }
}
