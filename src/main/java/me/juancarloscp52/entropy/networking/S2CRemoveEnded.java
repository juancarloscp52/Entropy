package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record S2CRemoveEnded() implements CustomPayload {
    public static final S2CRemoveEnded INSTANCE = new S2CRemoveEnded();
    public static final PacketCodec<PacketByteBuf, S2CRemoveEnded> CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<S2CRemoveEnded> getId() {
        return NetworkingConstants.REMOVE_ENDED;
    }
}
