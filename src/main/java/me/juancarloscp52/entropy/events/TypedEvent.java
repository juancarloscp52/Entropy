package me.juancarloscp52.entropy.events;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record TypedEvent<T extends Event>(EventType<T> type, T event) {
    public static final StreamCodec<FriendlyByteBuf, TypedEvent<? extends Event>> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buf, TypedEvent<? extends Event> typedEvent) {
            EventType.STREAM_CODEC.encode(buf, typedEvent.type);
            encodeEvent(buf, typedEvent);
        }

        private static <T extends Event> void encodeEvent(FriendlyByteBuf buf, TypedEvent<T> typedEvent) {
            typedEvent.type.streamCodec().encode(buf, typedEvent.event);
        }

        @Override
        public TypedEvent<? extends Event> decode(FriendlyByteBuf buf) {
            EventType<? extends Event> eventType = EventType.STREAM_CODEC.decode(buf);
            return decodeEvent(buf, eventType);
        }

        private static <T extends Event> TypedEvent<T> decodeEvent(FriendlyByteBuf buf, EventType<T> eventType) {
            return new TypedEvent<>(eventType, eventType.streamCodec().decode(buf));
        }
    };

    public static <T extends Event> TypedEvent<T> fromEventType(EventType<T> eventType) {
        return new TypedEvent<>(eventType, eventType.eventSupplier().create());
    }
}
