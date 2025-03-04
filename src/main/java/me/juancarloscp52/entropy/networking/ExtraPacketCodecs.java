package me.juancarloscp52.entropy.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ExtraPacketCodecs {
    static StreamCodec<FriendlyByteBuf, int[]> intArray() {
        return new StreamCodec<>() {
            public int[] decode(FriendlyByteBuf buf) {
                return buf.readVarIntArray();
            }

            public void encode(FriendlyByteBuf byteBuf, int[] data) {
                byteBuf.writeVarIntArray(data);
            }
        };
    }
}
