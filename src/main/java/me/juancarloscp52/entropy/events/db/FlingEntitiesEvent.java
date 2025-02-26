package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class FlingEntitiesEvent extends AbstractInstantEvent {
    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        fling(MinecraftClient.getInstance().player);
    }

    @Override
    public void init() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerWorld> worlds = new ArrayList<>();

        eventHandler.getActivePlayers().forEach(player -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140));
            ServerWorld playerWorld = player.getServerWorld();
            if(!worlds.contains(playerWorld))
                worlds.add(playerWorld);
        });
        worlds.forEach(world -> {
            world.iterateEntities().forEach(entity -> {
                if(entity instanceof LivingEntity livingEntity && !livingEntity.getType().isIn(EntityTypeTags.DO_NOT_FLING)) {
                    fling(livingEntity);
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140));
                }
            });
        });
    }

    private void fling(LivingEntity entity) {
        Random random = entity.getRandom();

        entity.setVelocity(random.nextBetween(-10, 9) + random.nextDouble(), random.nextInt(3) + random.nextDouble(), random.nextBetween(-10, 9) + random.nextDouble());
    }
}
