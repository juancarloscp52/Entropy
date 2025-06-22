package me.juancarloscp52.entropy.client.integrations.discord;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class DiscordIntegrationSettings {
    public static final Codec<DiscordIntegrationSettings> CODEC = RecordCodecBuilder.create(i  -> i.group(
        Codec.BOOL.optionalFieldOf("enabled", false).forGetter(s -> s.enabled),
        Codec.STRING.optionalFieldOf("token", "").forGetter(s -> s.token),
        Codec.LONG.optionalFieldOf("channel", -1L).forGetter(s -> s.channel)
    ).apply(i, DiscordIntegrationSettings::new));

    public DiscordIntegrationSettings(final boolean enabled, final String token, final long channel) {
        this.enabled = enabled;
        this.token = token;
        this.channel = channel;
    }

    public DiscordIntegrationSettings() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordIntegrationSettings that = (DiscordIntegrationSettings) o;
        return enabled == that.enabled && channel == that.channel && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, token, channel);
    }

    public boolean enabled = false;
    public String token = "";
    public long channel = -1;
}
