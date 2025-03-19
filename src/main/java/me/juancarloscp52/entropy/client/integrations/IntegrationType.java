package me.juancarloscp52.entropy.client.integrations;

import me.juancarloscp52.entropy.client.ClientEventHandler;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.discord.DiscordIntegration;
import me.juancarloscp52.entropy.client.integrations.twitch.TwitchIntegrations;
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeIntegrations;

import java.util.function.BiFunction;

public enum IntegrationType {
    TWITCH((handler, client) -> new TwitchIntegrations(client)),
    DISCORD((handler, client) -> new DiscordIntegration(client)),
    YOUTUBE(YoutubeIntegrations::new),
    ;

    private final BiFunction<ClientEventHandler, VotingClient, Integrations> constructor;

    IntegrationType(final BiFunction<ClientEventHandler, VotingClient, Integrations> constructor) {
        this.constructor = constructor;
    }

    public Integrations create(ClientEventHandler handler, VotingClient client) {
        return constructor.apply(handler, client);
    }
}
