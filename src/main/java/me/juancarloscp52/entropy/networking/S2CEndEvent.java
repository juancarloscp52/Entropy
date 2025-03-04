package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CEndEvent(byte index) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CEndEvent> CODEC = StreamCodec.composite(
        ByteBufCodecs.BYTE, S2CEndEvent::index,
        S2CEndEvent::new
    );

    @Override
    public Type<S2CEndEvent> type() {
        return NetworkingConstants.END_EVENT;
    }
}
