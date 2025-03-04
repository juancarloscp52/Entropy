package me.juancarloscp52.entropy.networking;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CNewPoll(int voteId, List<String> events) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CNewPoll> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, S2CNewPoll::voteId,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), S2CNewPoll::events,
        S2CNewPoll::new
    );

    @Override
    public Type<S2CNewPoll> type() {
        return NetworkingConstants.NEW_POLL;
    }
}
