package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

public class HighlightAllMobsEvent extends AbstractTimedEvent {

    @Override
    public void init() {
    }

    @Override
    public void tick() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerWorld> worlds = new ArrayList<>();
        for(var player : eventHandler.getActivePlayers())
            if(!worlds.contains(player.getWorld()))
                worlds.add(player.getWorld());
        for(var world : worlds)
            for(var entity : world.iterateEntities())
                if(entity instanceof MobEntity && !entity.getType().isIn(EntityTypeTags.DO_NOT_HIGHLIGHT))
                    ((MobEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 2));
        super.tick();
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }
}
