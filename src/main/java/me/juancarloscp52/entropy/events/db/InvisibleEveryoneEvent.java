/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.ArrayList;
import java.util.List;

public class InvisibleEveryoneEvent extends AbstractTimedEvent {
    @Override
    public void tick() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerLevel> worlds = new ArrayList<>();
        for(var player : eventHandler.getActivePlayers()) {
            ServerLevel playerWorld = player.serverLevel();
            if(!worlds.contains(playerWorld))
                worlds.add(playerWorld);
        }
        for(var world : worlds)
            for(var entity : world.getAllEntities())
                if(shouldBeInvisible(entity))
                    ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2));
        super.tick();
    }

    public boolean shouldBeInvisible(Entity entity) {
        return entity instanceof LivingEntity && !entity.getType().is(EntityTypeTags.NOT_INVISIBLE);
    }

}
