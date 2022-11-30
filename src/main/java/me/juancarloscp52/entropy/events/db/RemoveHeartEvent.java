package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.attribute.EntityAttributes;

public class RemoveHeartEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            if (player.getMaxHealth() > 2) {
                player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(player.getMaxHealth() - 2);
                if(player.getHealth() > player.getMaxHealth()) // Set players health to max allowed health if it was previously bigger than the new Max health.
                    player.setHealth(player.getMaxHealth());
            }
        }
    }

    @Override
    public String type() {
        return "health";
    }

}
