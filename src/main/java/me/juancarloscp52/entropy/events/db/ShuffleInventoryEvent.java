/**
 * @author Kanawanagasaki
 */
package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleInventoryEvent extends AbstractInstantEvent {
    public static final EventType<ShuffleInventoryEvent> TYPE = EventType.builder(ShuffleInventoryEvent::new).build();

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {
            Inventory inv = player.getInventory();
            List<ItemStack> stacks = new ArrayList<>();

            for (int i = 0; i < inv.getContainerSize(); i++) {
                stacks.add(inv.getItem(i));
            }

            Collections.shuffle(stacks);

            for (int i = 0; i < inv.getContainerSize(); i++) {
                inv.setItem(i, stacks.get(i));
            }
        }
    }

    @Override
    public EventType<ShuffleInventoryEvent> getType() {
        return TYPE;
    }
}
