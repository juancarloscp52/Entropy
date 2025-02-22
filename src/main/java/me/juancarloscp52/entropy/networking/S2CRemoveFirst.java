package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record S2CRemoveFirst() implements CustomPayload {
    public static final S2CRemoveFirst INSTANCE = new S2CRemoveFirst();
    public static final PacketCodec<PacketByteBuf, S2CRemoveFirst> CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<S2CRemoveFirst> getId() {
        return NetworkingConstants.REMOVE_FIRST;
    }
}
