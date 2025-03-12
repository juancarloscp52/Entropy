package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RemoveHeartEvent extends AbstractInstantEvent {
    public static final EventType<RemoveHeartEvent> TYPE = EventType.builder(RemoveHeartEvent::new).category(EventCategory.HEALTH).build();

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if (player.getMaxHealth() > 2) {
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth() - 2);
                if(player.getHealth() > player.getMaxHealth()) // Set players health to max allowed health if it was previously bigger than the new Max health.
                    player.setHealth(player.getMaxHealth());
            }
        }
    }

    @Override
    public EventType<RemoveHeartEvent> getType() {
        return TYPE;
    }
}
