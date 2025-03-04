package me.juancarloscp52.entropy.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import me.juancarloscp52.entropy.Entropy;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

public class ConstantColorDustParticleOptions extends ScalableParticleOptionsBase {
    public static final MapCodec<ConstantColorDustParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color").forGetter(effect -> effect.color),
        SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)
    ).apply(instance, ConstantColorDustParticleOptions::new));
    public static final StreamCodec<ByteBuf, ConstantColorDustParticleOptions> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, effect -> effect.color,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        ConstantColorDustParticleOptions::new
    );

    private final int color;

    public ConstantColorDustParticleOptions(int color, float scale) {
        super(scale);
        this.color = color;
    }

    public ConstantColorDustParticleOptions(Vector3f color, float scale) {
        this(FastColor.ARGB32.colorFromFloat(1.0f, color.x(), color.y(), color.z()), scale);
    }

    @Override
    public ParticleType<ConstantColorDustParticleOptions> getType() {
        return Entropy.CONSTANT_COLOR_DUST;
    }

    public float red() {
        return FastColor.ARGB32.red(color) / 255.0F;
    }

    public float green() {
        return FastColor.ARGB32.green(color) / 255.0F;
    }

    public float blue() {
        return FastColor.ARGB32.blue(color) / 255.0F;
    }
}
