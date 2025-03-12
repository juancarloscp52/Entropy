package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AddHeartEvent extends AbstractInstantEvent {
    public static final EventType<AddHeartEvent> TYPE = EventType.builder(AddHeartEvent::new).category(EventCategory.HEALTH).build();

    @Override
    public void init() {
        for(var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth() + 2);
            player.setHealth(player.getHealth() + 2);
        }
    }

    @Override
    public EventType<AddHeartEvent> getType() {
        return TYPE;
    }
}
