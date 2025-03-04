package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CTick(short eventCountDown) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CTick> CODEC = StreamCodec.composite(
        ByteBufCodecs.SHORT, S2CTick::eventCountDown,
        S2CTick::new
    );

    @Override
    public Type<S2CTick> type() {
        return NetworkingConstants.TICK;
    }
}
