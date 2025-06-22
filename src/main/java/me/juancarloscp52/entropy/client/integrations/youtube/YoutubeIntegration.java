/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.client.integrations.youtube;

import me.juancarloscp52.entropy.client.ClientEventHandler;
import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.Integration;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YoutubeIntegration implements Integration {
    public static final Logger LOGGER = LogManager.getLogger();

    private static final int BASE_POLLING_INTERVAL = 4800;
    private static final int THRESHOLD_INTERVAL = 3000;
    private static final int END_POLLING_OFFSET = 300;

    private ClientEventHandler _clientEventHandler;
    private VotingClient _votingClient;
    private ExecutorService _executor;
    private EntropyIntegrationsSettings _settings = EntropyClient.getInstance().integrationsSettings;
    private boolean _isAccessTokenValid = false;
    private String _liveChatId = "";
    private String _nextPageToken = null;
    private boolean _isRunning = false;
    private List<String> _messagesToSend = new ArrayList<String>();

    public YoutubeIntegration(ClientEventHandler clientEventHandler, VotingClient votingClient) {
        _clientEventHandler = clientEventHandler;
        _votingClient = votingClient;
        _executor = Executors.newCachedThreadPool();

        this.start();
    }

    @Override
    public void start() {
        _executor.execute(() -> {
            _isAccessTokenValid = YoutubeApi.validateAccessToken(_settings.youtube.accessToken);
            if (!_isAccessTokenValid)
                _isAccessTokenValid = YoutubeApi.refreshAccessToken(_settings.youtube.clientId, _settings.youtube.secret,
                        _settings.youtube.refreshToken);
            if (!_isAccessTokenValid)
                return;

            var broadcasts = YoutubeApi.getLiveBroadcasts(_settings.youtube.accessToken);
            if (broadcasts == null) {
                LOGGER.warn("[Youtube integration] Failed to fetch live broadcasts");
                return;
            }
            if (broadcasts.items.length == 0) {
                LOGGER.warn("[Youtube integration] Failed to find any live broadcasts");
                return;
            }
            var broadcast = broadcasts.items[broadcasts.items.length - 1];
            _liveChatId = broadcast.snippet.liveChatId;
            LOGGER.info("[Youtube integration] Started listening for chat messages for broadcast with the title \""
                    + broadcast.snippet.title + "\" on the channel \"" + broadcast.snippet.channelId + "\"");

            _nextPageToken = YoutubeApi.getChatMessagesLastPage(_settings.youtube.accessToken, _liveChatId);

            _isRunning = true;
            while (_isRunning && _isAccessTokenValid) {
                try {
                    var messages = YoutubeApi.getChatMessages(_settings.youtube.accessToken, _liveChatId,
                            _nextPageToken);
                    if (messages == null) {
                        int failCounter = 0;
                        do {
                            failCounter++;
                            _isAccessTokenValid = YoutubeApi.refreshAccessToken(_settings.youtube.clientId,
                                    _settings.youtube.secret, _settings.youtube.refreshToken);
                            Thread.sleep(1000);
                        } while (failCounter < 4 && !_isAccessTokenValid);
                    } else {
                        if (messages.items.length != 0) {
                            _nextPageToken = messages.nextPageToken;
                            for (var message : messages.items) {
                                _votingClient.processVote(message.snippet.displayMessage,
                                        message.snippet.authorChannelId);
                            }
                        }

                        while (_messagesToSend.size() > 0) {
                            YoutubeApi.sendChatMessage(_settings.youtube.accessToken, _liveChatId,
                                    _messagesToSend.get(0));
                            _messagesToSend.remove(0);
                        }

                        int sleep = BASE_POLLING_INTERVAL;
                        int timeBeforeEvent = _clientEventHandler.eventCountDown * 50;
                        if (THRESHOLD_INTERVAL < timeBeforeEvent && timeBeforeEvent < sleep)
                            sleep = timeBeforeEvent - END_POLLING_OFFSET;
                        Thread.sleep(Math.max(messages.pollingIntervalMillis + 100, sleep));
                    }

                } catch (Exception ex) {
                    LOGGER.error(ex);
                }
            }

            LOGGER.info("[Youtube integration] Stopped listening for chat messages");
        });
    }

    @Override
    public void stop() {
        _isRunning = false;
        _executor.shutdown();
    }

    @Override
    public void sendPoll(int voteID, List<Component> events) {
        int altOffset = voteID % 2 == 0 ? 4 : 0;
        StringBuilder stringBuilder = new StringBuilder("Current poll:");
        for (int i = 0; i < events.size(); i++)
            stringBuilder.append(String.format("[ %d - %s ] ", 1 + i + altOffset, events.get(i).getString()));

        sendMessage(stringBuilder.toString());
    }

    @Override
    public void sendMessage(String message) {
        _messagesToSend.add("[Entropy Bot] " + message);
    }

    @Override
    public int getColor(int alpha) {
        return ARGB.color(alpha,255, 0, 0);
    }

}
