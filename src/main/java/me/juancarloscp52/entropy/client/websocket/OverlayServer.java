package me.juancarloscp52.entropy.client.websocket;

import me.juancarloscp52.entropy.client.EntropyClient;
import net.minecraft.network.chat.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OverlayServer {

    public OverlayWebsocket overlayWebsocket;
    private ExecutorService botExecutor;

    public OverlayServer() {
        this.start();
    }

    public void start() {
        this.overlayWebsocket = new OverlayWebsocket(new InetSocketAddress("localhost",9091));

        botExecutor = Executors.newCachedThreadPool();
        botExecutor.execute(() -> {
            try {
                this.overlayWebsocket.start();
            } catch (Exception e) {
                EntropyClient.LOGGER.error("Exception while starting bot: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        try {
            this.overlayWebsocket.stop();
        } catch (InterruptedException e) {
            EntropyClient.LOGGER.error("Exception while stopping bot: " + e.getMessage());
            e.printStackTrace();
        }
        botExecutor.shutdown();
    }

    public void onNewVote(int voteID, List<Component> events) {

        int altOffset = voteID % 2 == 0 ? 5 : 1;

        if(EntropyClient.getInstance().integrationsSettings.integrationType==2)
            altOffset=1;

        List<OverlayVoteOption> options=new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            options.add(new OverlayVoteOption(events.get(i), new String[]{Integer.toString(i+altOffset)},0));
        }
        this.overlayWebsocket.NewVoting(options);
    }


    public void updateVote(int voteID, List<Component> events, int[] votes) {
        boolean showVotes = EntropyClient.getInstance().integrationsSettings.showCurrentPercentage;
        int altOffset = voteID % 2 == 0 ? 5 : 1;
        if(EntropyClient.getInstance().integrationsSettings.integrationType==2)
            altOffset=1;
        List<OverlayVoteOption> options=new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            options.add(new OverlayVoteOption(events.get(i), new String[]{Integer.toString(i+altOffset)}, showVotes ? votes[i]:0));
        }
        this.overlayWebsocket.UpdateVoting(options);

    }
    public void onVoteEnd() {
        this.overlayWebsocket.EndVoting();
    }


}
