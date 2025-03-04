package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundEndEvent(byte index) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundEndEvent> CODEC = StreamCodec.composite(
        ByteBufCodecs.BYTE, ClientboundEndEvent::index,
        ClientboundEndEvent::new
    );

    @Override
    public Type<ClientboundEndEvent> type() {
        return NetworkingConstants.END_EVENT;
    }
}
