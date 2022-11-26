/**
 * @author Kanawanagasaki
 */
package me.juancarloscp52.entropy.events.db;

import java.util.Collections;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

public class ShuffleInventoryEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            Collections.shuffle(player.getInventory().main);
        }
    }

}
