package me.juancarloscp52.entropy.server;

import org.joml.Vector3f;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.juancarloscp52.entropy.Entropy;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;

public class ConstantColorDustParticleEffect extends AbstractDustParticleEffect {
    public static final Codec<ConstantColorDustParticleEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.VECTOR_3F.fieldOf("color").forGetter(effect -> effect.color), Codec.FLOAT.fieldOf("scale").forGetter(effect -> effect.scale)).apply(instance, ConstantColorDustParticleEffect::new));
    public static final ParticleEffect.Factory<ConstantColorDustParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<ConstantColorDustParticleEffect>() {
        public ConstantColorDustParticleEffect read(ParticleType<ConstantColorDustParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            Vector3f color = AbstractDustParticleEffect.readColor(stringReader);
            stringReader.expect(' ');
            return new ConstantColorDustParticleEffect(color, stringReader.readFloat());
        }

        public ConstantColorDustParticleEffect read(ParticleType<ConstantColorDustParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new ConstantColorDustParticleEffect(AbstractDustParticleEffect.readColor(packetByteBuf), packetByteBuf.readFloat());
        }
    };

    public ConstantColorDustParticleEffect(Vector3f color, float scale) {
        super(color, scale);
    }

    @Override
    public ParticleType<ConstantColorDustParticleEffect> getType() {
        return Entropy.CONSTANT_COLOR_DUST;
    }
}
