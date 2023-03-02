package me.juancarloscp52.entropy.client.websocket;

import com.google.gson.Gson;
import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropySettings;
import me.juancarloscp52.entropy.client.EntropyClient;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class OverlayWebsocket extends WebSocketServer {

    public Gson gson;
    public OverlayWebsocket(InetSocketAddress address) {
        super(address);
        gson=new Gson();
    }
    public void EndVoting() {
        Request("END", new ArrayList<>());
    }

    public void NewVoting(List<OverlayVoteOption> options) {
        Request("CREATE",options);
    }

    public void UpdateVoting(List<OverlayVoteOption> options) {
        Request("UPDATE", options);
    }

    public void Message(String message) {
        // Note, Chaos Mod V websocket protocol does not currently code for
        // a string message. This is therefore deliberately left blank for now.
    }


    private void Request(String request, List<OverlayVoteOption> voteOptions) {
        int totalVotes = 0;
        for (OverlayVoteOption option: voteOptions) {
            totalVotes+=option.value();
        }
        OverlayVoteOption[] options=new OverlayVoteOption[voteOptions.size()];
        voteOptions.toArray(options);
        OverlayMessage msg=new OverlayMessage(request,totalVotes, Entropy.getInstance().settings.votingMode == EntropySettings.VotingMode.MAJORITY ? "MAJORITY" : "PERCENTAGE",options);
        String broadcastString = gson.toJson(msg);
        Entropy.LOGGER.debug("Broadcasting: "+broadcastString);
        this.broadcast(broadcastString);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {

    }
}
