package me.juancarloscp52.entropy.client.websocket;

public record OverlayMessage(String request, int totalVotes, String votingMode, OverlayVoteOption[] voteOptions){
}
