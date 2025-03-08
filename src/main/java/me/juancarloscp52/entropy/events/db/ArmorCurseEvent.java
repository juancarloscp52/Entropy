/**
 * @author Curio-sitas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantments;

public class ArmorCurseEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity ->
                    serverPlayerEntity.getInventory().armor.forEach(item ->
                        item.enchant(serverPlayerEntity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.BINDING_CURSE).get(), 1)
                    )
        );
    }
}
