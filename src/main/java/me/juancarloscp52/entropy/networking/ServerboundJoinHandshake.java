package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ServerboundJoinHandshake(String clientVersion) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ServerboundJoinHandshake> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(32767), ServerboundJoinHandshake::clientVersion,
        ServerboundJoinHandshake::new
    );

    @Override
    public Type<ServerboundJoinHandshake> type() {
        return NetworkingConstants.JOIN_HANDSHAKE;
    }
}
