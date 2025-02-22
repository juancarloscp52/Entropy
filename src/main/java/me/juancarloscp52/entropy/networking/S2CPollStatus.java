package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record S2CPollStatus(int voteId, int[] totalVotes, int totalVotesCount) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CPollStatus> CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT, S2CPollStatus::voteId,
        ExtraPacketCodecs.intArray(), S2CPollStatus::totalVotes,
        PacketCodecs.VAR_INT, S2CPollStatus::totalVotesCount,
        S2CPollStatus::new
    );

    @Override
    public Id<S2CPollStatus> getId() {
        return NetworkingConstants.POLL_STATUS;
    }
}
