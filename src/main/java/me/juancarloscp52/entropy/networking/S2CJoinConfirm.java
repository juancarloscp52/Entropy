package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record S2CJoinConfirm(
    short timerDuration,
    short baseEventDuration,
    boolean integrations
) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, S2CJoinConfirm> CODEC = PacketCodec.tuple(
        PacketCodecs.SHORT, S2CJoinConfirm::timerDuration,
        PacketCodecs.SHORT, S2CJoinConfirm::baseEventDuration,
        PacketCodecs.BOOL, S2CJoinConfirm::integrations,
        S2CJoinConfirm::new
    );

    @Override
    public Id<S2CJoinConfirm> getId() {
        return NetworkingConstants.JOIN_CONFIRM;
    }
}
