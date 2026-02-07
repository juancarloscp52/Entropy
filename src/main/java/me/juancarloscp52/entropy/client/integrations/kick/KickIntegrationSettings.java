package me.juancarloscp52.entropy.client.integrations.kick;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.juancarloscp52.entropy.client.integrations.IntegrationSettings;

import java.util.Objects;

public class KickIntegrationSettings implements IntegrationSettings {
	public static final Codec<KickIntegrationSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.BOOL.optionalFieldOf("enabled", false).forGetter(s -> s.enabled),
			Codec.STRING.optionalFieldOf("chatroomId", "").forGetter(s -> s.chatroomId),
			Codec.STRING.optionalFieldOf("clientBearerToken", "").forGetter(s -> s.clientBearerToken)
	).apply(i, KickIntegrationSettings::new));

	public KickIntegrationSettings(final boolean enabled, final String chatroomId, final String clientBearerToken) {
		this.enabled = enabled;
		this.chatroomId = chatroomId;
		this.clientBearerToken = clientBearerToken;
	}

	public KickIntegrationSettings() {
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final KickIntegrationSettings that = (KickIntegrationSettings) o;
		return enabled == that.enabled &&
				Objects.equals(chatroomId, that.chatroomId) &&
				Objects.equals(clientBearerToken, that.clientBearerToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(enabled, chatroomId, clientBearerToken);
	}

	public boolean enabled = false;
	public String chatroomId = "";
	public String clientBearerToken = "";

	@Override
	public boolean enabled() {
		return enabled;
	}
}
