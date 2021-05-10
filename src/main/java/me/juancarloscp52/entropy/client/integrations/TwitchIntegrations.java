package me.juancarloscp52.entropy.client.integrations;

import me.juancarloscp52.entropy.client.EntropyClient;
import me.juancarloscp52.entropy.client.EntropyIntegrationsSettings;
import me.juancarloscp52.entropy.client.VotingClient;
import net.minecraft.util.math.MathHelper;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchIntegrations extends ListenerAdapter implements Integration {

    private final Configuration config;
    EntropyIntegrationsSettings settings = EntropyClient.getInstance().integrationsSettings;
    private PircBotX ircChatBot;
    private ExecutorService botExecutor;
    private final VotingClient votingClient;
    private long lastJoinMessage = 0;

    public TwitchIntegrations(VotingClient votingClient) {
        this.votingClient = votingClient;
        config = new Configuration.Builder()
                .setAutoNickChange(false)
                .setOnJoinWhoEnabled(false)
                .setCapEnabled(true)
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .setEncoding(StandardCharsets.UTF_8)
                .addServer("irc.chat.twitch.tv", 6697)
                .setSocketFactory(SSLSocketFactory.getDefault())
                .setName(settings.channel.toLowerCase())
                .setServerPassword(settings.authToken.startsWith("oauth:") ? settings.authToken : "oauth:" + settings.authToken)
                .addAutoJoinChannel("#" + settings.channel.toLowerCase())
                .addListener(this)
                .setAutoSplitMessage(false)
                .buildConfiguration();


        this.start();
    }

    @Override
    public void start() {


        this.ircChatBot = new PircBotX(config);

        botExecutor = Executors.newCachedThreadPool();
        botExecutor.execute(() -> {
            try {
                this.ircChatBot.startBot();
            } catch (IOException e) {
                EntropyClient.LOGGER.error("IO Exception while starting bot: " + e.getMessage());
                e.printStackTrace();
            } catch (IrcException e) {
                EntropyClient.LOGGER.error("IRC Exception while starting bot: " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    @Override
    public void stop() {

        ircChatBot.stopBotReconnect();
        ircChatBot.close();
        botExecutor.shutdown();

    }

    @Override
    public void onMessage(MessageEvent event) {
        EntropyClient.getInstance().clientEventHandler.votingClient.processVote(event.getMessage());
    }

    @Override
    public void onJoin(JoinEvent event) {
        long currentTime = System.currentTimeMillis();
        if(currentTime-lastJoinMessage>30000){
            votingClient.sendMessage("Connected to Entropy Mod");
            lastJoinMessage=currentTime;
        }

    }

    @Override
    public void onPing(PingEvent event) {
        System.out.println("Received Ping from twitch, anwsering...");
        ircChatBot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    @Override
    public void sendChatMessage(String message) {
        ircChatBot.sendIRC().message("#" + settings.channel.toLowerCase(), "/me [Entropy Bot] " + message);
    }

    @Override
    public int getColor() {
        return MathHelper.packRgb(145, 70, 255);
    }
}
