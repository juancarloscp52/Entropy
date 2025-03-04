package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundRemoveFirst() implements CustomPacketPayload {
    public static final ClientboundRemoveFirst INSTANCE = new ClientboundRemoveFirst();
    public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveFirst> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<ClientboundRemoveFirst> type() {
        return NetworkingConstants.REMOVE_FIRST;
    }
}
