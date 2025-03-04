package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record C2SJoinHandshake(String clientVersion) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, C2SJoinHandshake> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(32767), C2SJoinHandshake::clientVersion,
        C2SJoinHandshake::new
    );

    @Override
    public Type<C2SJoinHandshake> type() {
        return NetworkingConstants.JOIN_HANDSHAKE;
    }
}
