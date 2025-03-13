package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;

public record EventType<T extends Event>(EventSupplier<T> eventSupplier, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec, FeatureFlagSet requiredFeatures, boolean disabledByAccessibilityMode, EventCategory category) {
    public boolean isEnabled() {
        return !(Entropy.getInstance().settings.accessibilityMode && disabledByAccessibilityMode());
    }

    public boolean doesWorldHaveRequiredFeatures(Level world) {
        return requiredFeatures().isSubsetOf(world.enabledFeatures());
    }

    public T create() {
        return eventSupplier().create();
    }

    public static <T extends Event> Builder<T> builder(EventSupplier<T> eventSupplier) {
        return new Builder<>(eventSupplier);
    }

    public static class Builder<T extends Event> {
        private final EventSupplier<T> eventSupplier;
        private StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
        private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
        private boolean disabledByAccessibilityMode = false;
        private EventCategory category = EventCategory.NONE;

        public Builder(EventSupplier<T> eventSupplier) {
            this.eventSupplier = eventSupplier;
        }

        public Builder<T> streamCodec(StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
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

        public Builder<T> category(EventCategory category) {
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
