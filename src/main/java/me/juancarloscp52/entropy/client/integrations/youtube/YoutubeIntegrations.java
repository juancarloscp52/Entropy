/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.client.integrations.youtube;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import me.juancarloscp52.entropy.client.integrations.Integrations;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YoutubeIntegrations implements Integrations {
    public static final Logger LOGGER = LogManager.getLogger();

    private VotingClient _votingClient;
    private ExecutorService _executor;
    private EntropyIntegrationsSettings _settings = EntropyClient.getInstance().integrationsSettings;
    private boolean _isAccessTokenValid = false;
    private String _liveChatId = "";
    private String _nextPageToken = null;
    private boolean _isRunning = false;
    private int _failCount = 0;
    private List<String> _messagesToSend = new ArrayList<String>();

    public YoutubeIntegrations(VotingClient votingClient) {
        _votingClient = votingClient;
        _executor = Executors.newCachedThreadPool();

        this.start();
    }

    @Override
    public void start() {
        _executor.execute(() -> {
            _isAccessTokenValid = YoutubeApi.validateAccessToken(_settings.youtubeAccessToken);
            if (!_isAccessTokenValid)
                _isAccessTokenValid = YoutubeApi.refreshAccessToken(_settings.youtubeClientId, _settings.youtubeSecret,
                        _settings.youtubeRefreshToken);
            if (!_isAccessTokenValid)
                return;

            var broadcasts = YoutubeApi.getLiveBroadcasts(_settings.youtubeAccessToken);
            if (broadcasts == null)
                return;
            if (broadcasts.items.length == 0)
                return;
            _liveChatId = broadcasts.items[broadcasts.items.length - 1].snippet.liveChatId;
            _nextPageToken = YoutubeApi.getChatMessagesLastPage(_settings.youtubeAccessToken, _liveChatId);

            _isRunning = true;
            while (_isRunning && _isAccessTokenValid && _failCount < 2) {
                try {
                    var messages = YoutubeApi.getChatMessages(_settings.youtubeAccessToken, _liveChatId,
                            _nextPageToken);
                    if (messages == null) {
                        _failCount++;
                        _isAccessTokenValid = YoutubeApi.refreshAccessToken(_settings.youtubeClientId,
                                _settings.youtubeSecret, _settings.youtubeRefreshToken);
                        Thread.sleep(1000);
                    } else {
                        _failCount = 0;
                        if (messages.items.length != 0) {
                            _nextPageToken = messages.nextPageToken;
                            for (var message : messages.items) {
                                _votingClient.processVote(message.snippet.displayMessage);
                            }
                        }

                        while (_messagesToSend.size() > 0) {
                            YoutubeApi.sendChatMessage(_settings.youtubeAccessToken, _liveChatId,
                                    _messagesToSend.get(0));
                            _messagesToSend.remove(0);
                        }
                        Thread.sleep(messages.pollingIntervalMillis + 100);
                    }

                } catch (Exception ex) {
                    LOGGER.error(ex);
                }
            }
        });
    }

    @Override
    public void stop() {
        _isRunning = false;
        _executor.shutdown();
    }

    @Override
    public void sendPoll(int voteID, List<String> events) {
        int altOffset = voteID % 2 == 0 ? 4 : 0;
        StringBuilder stringBuilder = new StringBuilder("Current poll:");
        for (int i = 0; i < events.size(); i++)
            stringBuilder.append(String.format("[ %d - %s ] ", 1 + i + altOffset, I18n.translate(events.get(i))));

        sendMessage(stringBuilder.toString());
    }

    @Override
    public void sendMessage(String message) {
        _messagesToSend.add("[Entropy Bot] " + message);
    }

    @Override
    public int getColor() {
        return MathHelper.packRgb(255, 0, 0);
    }

}
