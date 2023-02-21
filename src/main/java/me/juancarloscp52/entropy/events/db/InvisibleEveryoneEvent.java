/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;
import java.util.List;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;

public class InvisibleEveryoneEvent extends AbstractTimedEvent {

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
                if(shouldBeInvisible(entity))
                    ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 2));
        super.tick();
    }

    public boolean shouldBeInvisible(Entity entity) {
        return entity instanceof LivingEntity && !entity.getType().isIn(EntityTypeTags.NOT_INVISIBLE);
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

    @Override
    public String type() {
        return "invisibility";
    }

}
