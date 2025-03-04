package me.juancarloscp52.entropy.networking;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundNewPoll(int voteId, List<String> events) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundNewPoll> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ClientboundNewPoll::voteId,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), ClientboundNewPoll::events,
        ClientboundNewPoll::new
    );

    @Override
    public Type<ClientboundNewPoll> type() {
        return NetworkingConstants.NEW_POLL;
    }
}
