/*
 * Copyright (c) 2021 juancarloscp52
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
