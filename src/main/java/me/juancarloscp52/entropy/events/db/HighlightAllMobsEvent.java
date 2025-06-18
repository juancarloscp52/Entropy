package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.server.ServerEventHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.List;

public class HighlightAllMobsEvent extends AbstractTimedEvent {
    public static final EventType<HighlightAllMobsEvent> TYPE = EventType.builder(HighlightAllMobsEvent::new).build();

    @Override
    public void tick() {
        ServerEventHandler eventHandler = Entropy.getInstance().eventHandler;
        List<ServerLevel> worlds = new ArrayList<>();
        for(var player : eventHandler.getActivePlayers()) {
            ServerLevel playerWorld = player.level();
            if(!worlds.contains(playerWorld))
                worlds.add(playerWorld);
        }
        for(var world : worlds)
            for(var entity : world.getAllEntities())
                if(entity instanceof Mob && !entity.getType().is(EntityTypeTags.DO_NOT_HIGHLIGHT))
                    ((Mob)entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 2));
        super.tick();
    }

    @Override
    public EventType<HighlightAllMobsEvent> getType() {
        return TYPE;
    }
}
