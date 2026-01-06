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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class NetworkingConstants {
    public static final CustomPacketPayload.Type<ServerboundJoinHandshake> JOIN_HANDSHAKE = registerC2S("join-handshake", ServerboundJoinHandshake.CODEC);
    public static final CustomPacketPayload.Type<ClientboundJoinConfirm> JOIN_CONFIRM = registerS2C("join-confirm", ClientboundJoinConfirm.CODEC);
    public static final CustomPacketPayload.Type<ClientboundJoinSync> JOIN_SYNC = registerS2C("join-sync", ClientboundJoinSync.CODEC);
    public static final CustomPacketPayload.Type<ClientboundRemoveFirst> REMOVE_FIRST = registerS2C("remove-first", ClientboundRemoveFirst.CODEC);
    public static final CustomPacketPayload.Type<ClientboundRemoveEnded> REMOVE_ENDED = registerS2C("remove-ended", ClientboundRemoveEnded.CODEC);
    public static final CustomPacketPayload.Type<ClientboundAddEvent> ADD_EVENT = registerS2C("add-event", ClientboundAddEvent.CODEC);
    public static final CustomPacketPayload.Type<ClientboundEndEvent> END_EVENT = registerS2C("end-event", ClientboundEndEvent.CODEC);
    public static final CustomPacketPayload.Type<ClientboundTick> TICK = registerS2C("tick", ClientboundTick.CODEC);
    public static final CustomPacketPayload.Type<ClientboundNewPoll> NEW_POLL = registerS2C("new-poll", ClientboundNewPoll.CODEC);
    public static final CustomPacketPayload.Type<ClientboundPollStatus> POLL_STATUS = registerS2C("poll-status", ClientboundPollStatus.CODEC);
    public static final CustomPacketPayload.Type<ServerboundVotes> VOTES = registerC2S("votes", ServerboundVotes.CODEC);

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> registerC2S(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        final CustomPacketPayload.Type<T> type = register(name);
        PayloadTypeRegistry.playC2S().register(type, codec);
        return type;
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> registerS2C(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        final CustomPacketPayload.Type<T> type = register(name);
        PayloadTypeRegistry.playS2C().register(type, codec);
        return type;
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> register(final String name) {
        return new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("entropy", name));
    }
}