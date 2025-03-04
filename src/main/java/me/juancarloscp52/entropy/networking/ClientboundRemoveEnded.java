package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundRemoveEnded() implements CustomPacketPayload {
    public static final ClientboundRemoveEnded INSTANCE = new ClientboundRemoveEnded();
    public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveEnded> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<ClientboundRemoveEnded> type() {
        return NetworkingConstants.REMOVE_ENDED;
    }
}
