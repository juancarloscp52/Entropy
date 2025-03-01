/**
 * @author Curio-sitas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;

public class ArmorCurseEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity ->
                    serverPlayerEntity.getInventory().armor.forEach(item ->
                        item.addEnchantment(serverPlayerEntity.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.BINDING_CURSE).get(), 1)
                    )
        );
    }
}
