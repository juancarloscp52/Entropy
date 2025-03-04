package me.juancarloscp52.entropy.client;

import me.juancarloscp52.entropy.server.ConstantColorDustParticleEffect;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticleBase;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ConstantColorDustParticle extends DustParticleBase<ConstantColorDustParticleEffect>{
    protected ConstantColorDustParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ConstantColorDustParticleEffect parameters, SpriteSet spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, parameters, spriteProvider);
        rCol = parameters.red();
        gCol = parameters.green();
        bCol = parameters.blue();
    }

    @Override
    protected float randomizeColor(float colorComponent, float multiplier) {
        return colorComponent;
    }

    public static class Factory implements ParticleProvider<ConstantColorDustParticleEffect> {
        private final SpriteSet spriteProvider;

        public Factory(FabricSpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(ConstantColorDustParticleEffect dustParticleEffect, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ConstantColorDustParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, dustParticleEffect, spriteProvider);
        }
    }
}
