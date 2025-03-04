package me.juancarloscp52.entropy.networking;

import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

// This sub event id should really be event data, dispatched over types, but too much change for now
public record ClientboundAddEvent(String id, Optional<String> subEventId) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundAddEvent> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(256), ClientboundAddEvent::id,
        ByteBufCodecs.optional(ByteBufCodecs.stringUtf8(256)), ClientboundAddEvent::subEventId,
        ClientboundAddEvent::new
    );

    @Override
    public Type<ClientboundAddEvent> type() {
        return NetworkingConstants.ADD_EVENT;
    }
}
