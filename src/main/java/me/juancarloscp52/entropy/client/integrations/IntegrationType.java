package me.juancarloscp52.entropy.client.integrations;

import me.juancarloscp52.entropy.client.ClientEventHandler;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.discord.DiscordIntegration;
import me.juancarloscp52.entropy.client.integrations.twitch.TwitchIntegration;
import me.juancarloscp52.entropy.client.integrations.youtube.YoutubeIntegration;

import java.util.function.BiFunction;

public enum IntegrationType {
    TWITCH((handler, client) -> new TwitchIntegration(client)),
    DISCORD((handler, client) -> new DiscordIntegration(client)),
    YOUTUBE(YoutubeIntegration::new),
    ;

    private final BiFunction<ClientEventHandler, VotingClient, Integration> constructor;

    IntegrationType(final BiFunction<ClientEventHandler, VotingClient, Integration> constructor) {
        this.constructor = constructor;
    }

    public Integration create(ClientEventHandler handler, VotingClient client) {
        return constructor.apply(handler, client);
    }
}
