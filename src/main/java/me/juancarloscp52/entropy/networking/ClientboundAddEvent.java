package me.juancarloscp52.entropy.networking;

import me.juancarloscp52.entropy.events.TypedEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundAddEvent(TypedEvent<?> typedEvent) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundAddEvent> CODEC = StreamCodec.composite(
        TypedEvent.STREAM_CODEC, ClientboundAddEvent::typedEvent,
        ClientboundAddEvent::new
    );

    @Override
    public Type<ClientboundAddEvent> type() {
        return NetworkingConstants.ADD_EVENT;
    }
}
