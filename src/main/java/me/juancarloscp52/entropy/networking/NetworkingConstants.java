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

package me.juancarloscp52.entropy.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final CustomPayload.Id<C2SJoinHandshake> JOIN_HANDSHAKE = registerC2S("join-handshake", C2SJoinHandshake.CODEC);
    public static final CustomPayload.Id<S2CJoinConfirm> JOIN_CONFIRM = registerS2C("join-confirm", S2CJoinConfirm.CODEC);
    public static final CustomPayload.Id<S2CJoinSync> JOIN_SYNC = registerS2C("join-sync", S2CJoinSync.CODEC);
    public static final CustomPayload.Id<S2CRemoveFirst> REMOVE_FIRST = registerS2C("remove-first", S2CRemoveFirst.CODEC);
    public static final CustomPayload.Id<S2CRemoveEnded> REMOVE_ENDED = registerS2C("remove-ended", S2CRemoveEnded.CODEC);
    public static final CustomPayload.Id<S2CAddEvent> ADD_EVENT = registerS2C("add-event", S2CAddEvent.CODEC);
    public static final CustomPayload.Id<S2CEndEvent> END_EVENT = registerS2C("end-event", S2CEndEvent.CODEC);
    public static final CustomPayload.Id<S2CTick> TICK = registerS2C("tick", S2CTick.CODEC);
    public static final CustomPayload.Id<S2CNewPoll> NEW_POLL = registerS2C("new-poll", S2CNewPoll.CODEC);
    public static final CustomPayload.Id<S2CPollStatus> POLL_STATUS = registerS2C("poll-status", S2CPollStatus.CODEC);
    public static final CustomPayload.Id<C2SVotes> VOTES = registerC2S("votes", C2SVotes.CODEC);

    private static <T extends CustomPayload> CustomPayload.Id<T> registerC2S(String name, PacketCodec<? super RegistryByteBuf, T> codec) {
        final CustomPayload.Id<T> type = register(name);
        PayloadTypeRegistry.playC2S().register(type, codec);
        return type;
    }

    private static <T extends CustomPayload> CustomPayload.Id<T> registerS2C(String name, PacketCodec<? super RegistryByteBuf, T> codec) {
        final CustomPayload.Id<T> type = register(name);
        PayloadTypeRegistry.playS2C().register(type, codec);
        return type;
    }

    private static <T extends CustomPayload> CustomPayload.Id<T> register(final String name) {
        return new CustomPayload.Id<>(new Identifier("entropy", name));
    }
}