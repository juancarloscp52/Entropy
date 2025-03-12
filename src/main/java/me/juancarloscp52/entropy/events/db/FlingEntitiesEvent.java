package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class FlingEntitiesEvent extends AbstractInstantEvent {
    public static final EventType<FlingEntitiesEvent> TYPE = EventType.builder(FlingEntitiesEvent::new).build();

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        fling(Minecraft.getInstance().player);
    }

    @Override
    public void init() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerLevel> worlds = new ArrayList<>();

        eventHandler.getActivePlayers().forEach(player -> {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 140));
            ServerLevel playerWorld = player.serverLevel();
            if(!worlds.contains(playerWorld))
                worlds.add(playerWorld);
        });
        worlds.forEach(world -> {
            world.getAllEntities().forEach(entity -> {
                if(entity instanceof LivingEntity livingEntity && !livingEntity.getType().is(EntityTypeTags.DO_NOT_FLING)) {
                    fling(livingEntity);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 140));
                }
            });
        });
    }

    private void fling(LivingEntity entity) {
        RandomSource random = entity.getRandom();

        entity.setDeltaMovement(random.nextIntBetweenInclusive(-10, 9) + random.nextDouble(), random.nextInt(3) + random.nextDouble(), random.nextIntBetweenInclusive(-10, 9) + random.nextDouble());
    }

    @Override
    public EventType<FlingEntitiesEvent> getType() {
        return TYPE;
    }
}
