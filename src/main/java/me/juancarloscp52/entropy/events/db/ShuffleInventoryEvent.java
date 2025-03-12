/**
 * @author Kanawanagasaki
 */
package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

public class ShuffleInventoryEvent extends AbstractInstantEvent {
    public static final EventType<ShuffleInventoryEvent> TYPE = EventType.builder(ShuffleInventoryEvent::new).build();

    @Override
    public void init() {
        for (var player : Entropy.getInstance().eventHandler.getActivePlayers()) {

            var inventories = new ArrayList<NonNullList<ItemStack>>();
            var itemStacks = new ArrayList<ItemStack>();

            var main = player.getInventory().items;
            var offhand = player.getInventory().offhand;
            var armor = player.getInventory().armor;

            for(var itemStack : main)
            {
                inventories.add(main);
                itemStacks.add(itemStack);
            }
            for(var itemStack : offhand)
            {
                inventories.add(offhand);
                itemStacks.add(itemStack);
            }
            for(var itemStack : armor)
            {
                inventories.add(armor);
                itemStacks.add(itemStack);
            }

            Collections.shuffle(itemStacks);

            NonNullList<ItemStack> lastInventory = null;
            int index = 0;
            for(int i = 0; i < itemStacks.size(); i++)
            {
                if(lastInventory == null || lastInventory != inventories.get(i))
                {
                    lastInventory = inventories.get(i);
                    index = 0;
                }
                else index++;
                inventories.get(i).set(index, itemStacks.get(i));
            }
        }
    }

    @Override
    public EventType<ShuffleInventoryEvent> getType() {
        return TYPE;
    }
}
