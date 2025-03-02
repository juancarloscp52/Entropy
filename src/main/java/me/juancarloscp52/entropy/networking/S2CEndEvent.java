package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record S2CEndEvent(byte index) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CEndEvent> CODEC = PacketCodec.tuple(
        PacketCodecs.BYTE, S2CEndEvent::index,
        S2CEndEvent::new
    );

    @Override
    public Id<S2CEndEvent> getId() {
        return NetworkingConstants.END_EVENT;
    }
}
