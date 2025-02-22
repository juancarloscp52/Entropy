package me.juancarloscp52.entropy.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Colors;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector3f;

public class ConstantColorDustParticleEffect extends AbstractDustParticleEffect {
    public static final MapCodec<ConstantColorDustParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color").forGetter(effect -> effect.color),
        Codec.FLOAT.fieldOf("scale").forGetter(AbstractDustParticleEffect::getScale)
    ).apply(instance, ConstantColorDustParticleEffect::new));
    public static final PacketCodec<ByteBuf, ConstantColorDustParticleEffect> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER, effect -> effect.color,
        PacketCodecs.FLOAT, AbstractDustParticleEffect::getScale,
        ConstantColorDustParticleEffect::new
    );

    private final int color;

    public ConstantColorDustParticleEffect(int color, float scale) {
        super(scale);
        this.color = color;
    }

    public ConstantColorDustParticleEffect(Vector3f color, float scale) {
        this(ColorHelper.Argb.fromFloats(1.0f, color.x(), color.y(), color.z()), scale);
    }

    @Override
    public ParticleType<ConstantColorDustParticleEffect> getType() {
        return Entropy.CONSTANT_COLOR_DUST;
    }
}
