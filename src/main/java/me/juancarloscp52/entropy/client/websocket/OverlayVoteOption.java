package me.juancarloscp52.entropy.client.websocket;

import net.minecraft.network.chat.Component;

public record OverlayVoteOption(Component label, String[] matches, int value) {
}
