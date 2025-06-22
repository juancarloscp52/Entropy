package me.juancarloscp52.entropy.client.integrations;

import me.juancarloscp52.entropy.client.ClientEventHandler;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.discord.DiscordIntegration;
import me.juancarloscp52.entropy.client.integrations.twitch.TwitchIntegration;
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeIntegration;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum IntegrationType {
    TWITCH((handler, client) -> new TwitchIntegration(client), s -> s.twitch),
    DISCORD((handler, client) -> new DiscordIntegration(client), s -> s.discord),
    YOUTUBE(YoutubeIntegration::new, s -> s.youtube),
    ;

    private final BiFunction<ClientEventHandler, VotingClient, Integration> constructor;
    private final Function<EntropyIntegrationsSettings, IntegrationSettings> settingsGetter;

    IntegrationType(final BiFunction<ClientEventHandler, VotingClient, Integration> constructor, final Function<EntropyIntegrationsSettings, IntegrationSettings> settingsGetter) {
        this.constructor = constructor;
        this.settingsGetter = settingsGetter;
    }

    public Integration create(ClientEventHandler handler, VotingClient client) {
        return constructor.apply(handler, client);
    }

    public IntegrationSettings settings(final EntropyIntegrationsSettings settings) {
        return settingsGetter.apply(settings);
    }
}
