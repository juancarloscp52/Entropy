package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundJoinConfirm(
    short timerDuration,
    short baseEventDuration,
    boolean integrations
) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundJoinConfirm> CODEC = StreamCodec.composite(
        ByteBufCodecs.SHORT, ClientboundJoinConfirm::timerDuration,
        ByteBufCodecs.SHORT, ClientboundJoinConfirm::baseEventDuration,
        ByteBufCodecs.BOOL, ClientboundJoinConfirm::integrations,
        ClientboundJoinConfirm::new
    );

    @Override
    public Type<ClientboundJoinConfirm> type() {
        return NetworkingConstants.JOIN_CONFIRM;
    }
}
