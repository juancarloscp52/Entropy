package me.juancarloscp52.entropy;

import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier JOIN_HANDSHAKE = new Identifier("entropy", "join-handshake");
    public static final Identifier JOIN_CONFIRM = new Identifier("entropy", "join-confirm");
    public static final Identifier JOIN_SYNC = new Identifier("entropy", "join-sync");
    public static final Identifier REMOVE_FIRST = new Identifier("entropy", "remove-first");
    public static final Identifier ADD_EVENT = new Identifier("entropy", "add-event");
    public static final Identifier END_EVENT = new Identifier("entropy", "end-event");
    public static final Identifier TICK = new Identifier("entropy", "tick");
    public static final Identifier NEW_POLL = new Identifier("entropy", "new-poll");
    public static final Identifier POLL_STATUS = new Identifier("entropy", "poll-status");

}
