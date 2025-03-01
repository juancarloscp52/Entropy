package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class HighlightAllMobsEvent extends AbstractTimedEvent {
    @Override
    public void tick() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerWorld> worlds = new ArrayList<>();
        for(var player : eventHandler.getActivePlayers()) {
            ServerWorld playerWorld = player.getServerWorld();
            if(!worlds.contains(playerWorld))
                worlds.add(playerWorld);
        }
        for(var world : worlds)
            for(var entity : world.iterateEntities())
                if(entity instanceof MobEntity && !entity.getType().isIn(EntityTypeTags.DO_NOT_HIGHLIGHT))
                    ((MobEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 2));
        super.tick();
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
