package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CRemoveFirst() implements CustomPacketPayload {
    public static final S2CRemoveFirst INSTANCE = new S2CRemoveFirst();
    public static final StreamCodec<FriendlyByteBuf, S2CRemoveFirst> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<S2CRemoveFirst> type() {
        return NetworkingConstants.REMOVE_FIRST;
    }
}
