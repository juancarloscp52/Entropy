package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.Entropy;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

public class DropHandItemEvent extends AbstractInstantEvent {
    public DropHandItemEvent() {
        this.translationKey="entropy.events.dropHandItem";
    }

    @Override
    public void init() {
        PlayerLookup.all(Entropy.getInstance().eventHandler.server).forEach(serverPlayerEntity -> serverPlayerEntity.dropSelectedItem(true));
    }
}
