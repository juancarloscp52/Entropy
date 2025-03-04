package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundTick(short eventCountDown) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundTick> CODEC = StreamCodec.composite(
        ByteBufCodecs.SHORT, ClientboundTick::eventCountDown,
        ClientboundTick::new
    );

    @Override
    public Type<ClientboundTick> type() {
        return NetworkingConstants.TICK;
    }
}
