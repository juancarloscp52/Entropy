package me.juancarloscp52.entropy.client;

import me.juancarloscp52.entropy.server.ConstantColorDustParticleEffect;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.AbstractDustParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class ConstantColorDustParticle extends AbstractDustParticle<ConstantColorDustParticleEffect>{
    protected ConstantColorDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ConstantColorDustParticleEffect parameters, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, parameters, spriteProvider);
        red = parameters.red();
        green = parameters.green();
        blue = parameters.blue();
    }

    @Override
    protected float darken(float colorComponent, float multiplier) {
        return colorComponent;
    }

    public static class Factory implements ParticleFactory<ConstantColorDustParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(FabricSpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(ConstantColorDustParticleEffect dustParticleEffect, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ConstantColorDustParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, dustParticleEffect, spriteProvider);
        }
    }
}
