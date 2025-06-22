package me.juancarloscp52.entropy.client.integrations.twitch;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class TwitchIntegrationSettings {
    public static final Codec<TwitchIntegrationSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.BOOL.optionalFieldOf("enabled", false).forGetter(s -> s.enabled),
        Codec.STRING.optionalFieldOf("token", "").forGetter(s -> s.token),
        Codec.STRING.optionalFieldOf("channel", "").forGetter(s -> s.channel)
    ).apply(i, TwitchIntegrationSettings::new));

    public TwitchIntegrationSettings(final boolean enabled, final String token, final String channel) {
        this.enabled = enabled;
        this.token = token;
        this.channel = channel;
    }

    public TwitchIntegrationSettings() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TwitchIntegrationSettings that = (TwitchIntegrationSettings) o;
        return enabled == that.enabled && Objects.equals(token, that.token) && Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, token, channel);
    }

    public boolean enabled = false;
    public String token = "";
    public String channel = "";
}
