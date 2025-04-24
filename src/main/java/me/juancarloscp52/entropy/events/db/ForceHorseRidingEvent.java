package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ForceHorseRidingEvent extends AbstractTimedEvent {
    public static final EventType<ForceHorseRidingEvent> TYPE = EventType.builder(ForceHorseRidingEvent::new).build();
    private List<Horse> spawnedHorses = new ArrayList<>();

    @Override
    public void initClient() {
        Variables.forceRiding = true;
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            spawnedHorses.add(EntityType.HORSE.spawn(player.serverLevel(), horse -> {
                horse.tameWithName(player);
                horse.setItemSlot(EquipmentSlot.SADDLE, Items.SADDLE.getDefaultInstance());
                horse.setInvulnerable(true);
                player.startRiding(horse);
            }, player.blockPosition(), EntitySpawnReason.EVENT, false, false));
        });
        Variables.forceRiding = true;
    }

    @Override
    public void endClient() {
        super.endClient();
        Variables.forceRiding = false;
    }

    @Override
    public void end() {
        super.end();
        Variables.forceRiding = false;
        spawnedHorses.forEach(Entity::discard);
    }

    @Override
    public EventType<ForceHorseRidingEvent> getType() {
        return TYPE;
    }
}
