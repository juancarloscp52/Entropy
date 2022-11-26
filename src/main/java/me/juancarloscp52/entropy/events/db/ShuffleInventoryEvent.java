/**
 * @author Kanawanagasaki
 */
package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;

import java.util.Collections;

public class ShuffleInventoryEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            Collections.shuffle(player.getInventory().main);
        }
    }

}
