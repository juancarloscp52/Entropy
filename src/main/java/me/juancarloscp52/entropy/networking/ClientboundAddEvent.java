package me.juancarloscp52.entropy.networking;

import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundAddEvent(Event event) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAddEvent> CODEC = StreamCodec.composite(
        EventRegistry.STREAM_CODEC, ClientboundAddEvent::event,
        ClientboundAddEvent::new
    );

    @Override
    public Type<ClientboundAddEvent> type() {
        return NetworkingConstants.ADD_EVENT;
    }
}
