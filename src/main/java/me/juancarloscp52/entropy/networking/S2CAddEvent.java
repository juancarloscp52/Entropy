package me.juancarloscp52.entropy.networking;

import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

// This sub event id should really be event data, dispatched over types, but too much change for now
public record S2CAddEvent(String id, Optional<String> subEventId) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CAddEvent> CODEC = StreamCodec.composite(
        ByteBufCodecs.stringUtf8(256), S2CAddEvent::id,
        ByteBufCodecs.optional(ByteBufCodecs.stringUtf8(256)), S2CAddEvent::subEventId,
        S2CAddEvent::new
    );

    @Override
    public Type<S2CAddEvent> type() {
        return NetworkingConstants.ADD_EVENT;
    }
}
