package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.Optional;

// This sub event id should really be event data, dispatched over types, but too much change for now
public record S2CAddEvent(String id, Optional<String> subEventId) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CAddEvent> CODEC = PacketCodec.tuple(
        PacketCodecs.string(256), S2CAddEvent::id,
        PacketCodecs.optional(PacketCodecs.string(256)), S2CAddEvent::subEventId,
        S2CAddEvent::new
    );

    @Override
    public Id<S2CAddEvent> getId() {
        return NetworkingConstants.ADD_EVENT;
    }
}
