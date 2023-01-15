package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.random.Random;

public class FlingPlayerEvent extends AbstractInstantEvent {
    @Override
    public void initClient() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Random random = player.getRandom();

        player.setVelocity(random.nextInt(10) + random.nextDouble(), random.nextInt(3) + random.nextDouble(), random.nextInt(10) + random.nextDouble());
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140)));
    }
}
