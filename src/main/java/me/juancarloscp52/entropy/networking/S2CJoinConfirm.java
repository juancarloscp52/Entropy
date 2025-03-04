package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record S2CJoinConfirm(
    short timerDuration,
    short baseEventDuration,
    boolean integrations
) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, S2CJoinConfirm> CODEC = StreamCodec.composite(
        ByteBufCodecs.SHORT, S2CJoinConfirm::timerDuration,
        ByteBufCodecs.SHORT, S2CJoinConfirm::baseEventDuration,
        ByteBufCodecs.BOOL, S2CJoinConfirm::integrations,
        S2CJoinConfirm::new
    );

    @Override
    public Type<S2CJoinConfirm> type() {
        return NetworkingConstants.JOIN_CONFIRM;
    }
}
