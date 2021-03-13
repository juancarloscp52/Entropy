package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

public class DropInventoryEvent extends AbstractInstantEvent {

    public DropInventoryEvent() {
        this.translationKey="entropy.events.dropInventory";
    }

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.inventory.dropAll());
    }

}
