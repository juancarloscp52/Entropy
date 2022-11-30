package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.entity.attribute.EntityAttributes;

public class AddHeartEvent extends AbstractInstantEvent {
    
    @Override
    public void init() {
        for(var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(player.getHealth() + 2);
            player.setHealth(player.getHealth() + 2);
        }
    }
    

    @Override
    public String type() {
        return "health";
    }

}
