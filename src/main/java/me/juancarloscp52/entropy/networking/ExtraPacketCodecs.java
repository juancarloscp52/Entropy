package me.juancarloscp52.entropy.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class ExtraPacketCodecs {
    static PacketCodec<PacketByteBuf, int[]> intArray() {
        return new PacketCodec<>() {
            public int[] decode(PacketByteBuf buf) {
                return buf.readIntArray();
            }

            public void encode(PacketByteBuf byteBuf, int[] data) {
                byteBuf.writeIntArray(data);
            }
        };
    }
}
