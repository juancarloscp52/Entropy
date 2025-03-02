package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record C2SJoinHandshake(String clientVersion) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, C2SJoinHandshake> CODEC = PacketCodec.tuple(
        PacketCodecs.string(32767), C2SJoinHandshake::clientVersion,
        C2SJoinHandshake::new
    );

    @Override
    public Id<C2SJoinHandshake> getId() {
        return NetworkingConstants.JOIN_HANDSHAKE;
    }
}
