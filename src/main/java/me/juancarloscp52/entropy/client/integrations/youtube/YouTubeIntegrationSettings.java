package me.juancarloscp52.entropy.client.integrations.youtube;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.juancarloscp52.entropy.client.integrations.IntegrationSettings;

import java.util.Objects;

public class YouTubeIntegrationSettings implements IntegrationSettings {
    public static final Codec<YouTubeIntegrationSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.BOOL.optionalFieldOf("enabled", false).forGetter(s -> s.enabled),
        Codec.STRING.optionalFieldOf("client_id", "").forGetter(s -> s.clientId),
        Codec.STRING.optionalFieldOf("secret", "").forGetter(s -> s.secret),
        Codec.STRING.optionalFieldOf("access_token", "").forGetter(s -> s.accessToken),
        Codec.STRING.optionalFieldOf("refresh_token", "").forGetter(s -> s.refreshToken)
    ).apply(i, YouTubeIntegrationSettings::new));

    public YouTubeIntegrationSettings(final boolean enabled, final String clientId, final String secret, final String accessToken, final String refreshToken) {
        this.enabled = enabled;
        this.clientId = clientId;
        this.secret = secret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public YouTubeIntegrationSettings() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final YouTubeIntegrationSettings that = (YouTubeIntegrationSettings) o;
        return enabled == that.enabled && Objects.equals(clientId, that.clientId) && Objects.equals(secret, that.secret) && Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, clientId, secret, accessToken, refreshToken);
    }

    public boolean enabled = false;
    public String clientId = "";
    public String secret = "";
    public String accessToken = "";
    public String refreshToken = "";

    @Override
    public boolean enabled() {
        return enabled;
    }
}
