package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

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

        var enchantments = EnchantmentHelper.fromNbt(itemStack.getEnchantments());

        enchantments.keySet().removeIf(enchantment -> !Registries.ENCHANTMENT.getEntry(enchantment).isIn(EnchantmentTags.DO_NOT_REMOVE));
        EnchantmentHelper.set(enchantments, itemStack);
    }
}
