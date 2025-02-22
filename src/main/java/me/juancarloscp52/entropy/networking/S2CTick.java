package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record S2CTick(short eventCountDown) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CTick> CODEC = PacketCodec.tuple(
        PacketCodecs.SHORT, S2CTick::eventCountDown,
        S2CTick::new
    );

    @Override
    public Id<S2CTick> getId() {
        return NetworkingConstants.TICK;
    }
}
