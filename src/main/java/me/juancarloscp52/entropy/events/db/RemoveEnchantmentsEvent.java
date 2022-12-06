package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.item.ItemStack;

public class RemoveEnchantmentsEvent extends AbstractInstantEvent {

    @Override
    public void init() {

        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {

            player.getInventory().main.forEach(itemStack -> {
                removeEnchant(itemStack);
            });

            player.getInventory().offHand.forEach(itemStack -> {
                removeEnchant(itemStack);
            });

            player.getInventory().armor.forEach(itemStack -> {
                removeEnchant(itemStack);
            });
        });
    }

    private void removeEnchant(ItemStack itemStack) {
        var nbt = itemStack.getNbt();

        if(nbt==null){return;}
        if(Math.random() > 1.0/3){return;} // one chance over 3 to remove an enchantment.

        nbt.remove("Enchantments");
        itemStack.setNbt(nbt);
    }
}
