package me.juancarloscp52.entropy.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record ClientboundNewPoll(int voteId, List<Component> events) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNewPoll> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ClientboundNewPoll::voteId,
        ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundNewPoll::events,
        ClientboundNewPoll::new
    );

    @Override
    public Type<ClientboundNewPoll> type() {
        return NetworkingConstants.NEW_POLL;
    }
}
