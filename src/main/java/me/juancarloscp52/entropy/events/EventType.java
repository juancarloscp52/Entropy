package me.juancarloscp52.entropy.events;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public record EventType<T extends Event>(EventSupplier<T> eventSupplier, StreamCodec<FriendlyByteBuf, T> streamCodec, FeatureFlagSet requiredFeatures, boolean disabledByAccessibilityMode, String category) {
    public static final StreamCodec<FriendlyByteBuf, EventType<? extends Event>> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, type -> EventRegistry.EVENTS.getId(type),
        i -> EventRegistry.EVENTS.get(i).get().value()
    );

    public static <T extends Event> Builder<T> builder(EventSupplier<T> eventSupplier) {
        return new Builder<>(eventSupplier);
    }

    public static class Builder<T extends Event> {
        private final EventSupplier<T> eventSupplier;
        private StreamCodec<FriendlyByteBuf, T> streamCodec;
        private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
        private boolean disabledByAccessibilityMode = false;
        private String category = "none";

        public Builder(EventSupplier<T> eventSupplier) {
            this.eventSupplier = eventSupplier;
        }

        public Builder<T> streamCodec(StreamCodec<FriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public Builder<T> requiredFeatures(FeatureFlagSet requiredFeatures) {
            this.requiredFeatures = requiredFeatures;
            return this;
        }

        public Builder<T> disabledByAccessibilityMode() {
            disabledByAccessibilityMode = true;
            return this;
        }

        public Builder<T> category(String category) {
            this.category = category;
            return this;
        }

        public EventType<T> build() {
            if (streamCodec == null) {
                streamCodec = Event.streamCodec(eventSupplier);
            }
            return new EventType<>(eventSupplier, streamCodec, requiredFeatures, disabledByAccessibilityMode, category);
        }
    }

    @FunctionalInterface
    public interface EventSupplier<T extends Event> {
        T create();
    }
}
