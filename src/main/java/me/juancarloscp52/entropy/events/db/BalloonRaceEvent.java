package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.events.Event;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.HappyGhast;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class BalloonRaceEvent extends AbstractTimedEvent {
    public static final EventType<BalloonRaceEvent> TYPE = EventType.builder(BalloonRaceEvent::new).build();
    private final List<HappyGhast> ghasts = new ArrayList<>();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            HappyGhast happyGhast = EntityType.HAPPY_GHAST.spawn(player.level(), player.blockPosition(), EntitySpawnReason.EVENT);
            Item harness = BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.HARNESSES, player.level().getRandom()).map(Holder::value).orElse(Items.WHITE_HARNESS);

            happyGhast.setItemSlot(EquipmentSlot.BODY, harness.getDefaultInstance());
            happyGhast.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.4D);
            player.startRiding(happyGhast);
            ghasts.add(happyGhast);
        });
    }

    @Override
    public void end() {
        ghasts.forEach(happyGhast -> happyGhast.remove(Entity.RemovalReason.DISCARDED));
    }

    @Override
    public EventType<? extends Event> getType() {
        return TYPE;
    }
}
