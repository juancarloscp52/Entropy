package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CRemoveEnded() implements CustomPacketPayload {
    public static final S2CRemoveEnded INSTANCE = new S2CRemoveEnded();
    public static final StreamCodec<FriendlyByteBuf, S2CRemoveEnded> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<S2CRemoveEnded> type() {
        return NetworkingConstants.REMOVE_ENDED;
    }
}
