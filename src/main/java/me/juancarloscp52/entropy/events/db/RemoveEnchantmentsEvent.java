package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class RemoveEnchantmentsEvent extends AbstractInstantEvent {
   public static final EventType<RemoveEnchantmentsEvent> TYPE = EventType.builder(RemoveEnchantmentsEvent::new).build();

    @Override
    public void init() {

        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {

            player.getInventory().items.forEach(itemStack -> {
                removeEnchant(itemStack);
            });

            player.getInventory().offhand.forEach(itemStack -> {
                removeEnchant(itemStack);
            });

            player.getInventory().armor.forEach(itemStack -> {
                removeEnchant(itemStack);
            });
        });
    }

    private void removeEnchant(ItemStack itemStack) {
        if (Math.random() > 1.0/3) {
            // one chance over 3 to remove an enchantment.
            return;
        }

        EnchantmentHelper.updateEnchantments(itemStack, enchantments ->
            enchantments.removeIf(enchantment -> !enchantment.is(EnchantmentTags.DO_NOT_REMOVE))
        );
    }

    @Override
    public EventType<RemoveEnchantmentsEvent> getType() {
        return TYPE;
    }
}
