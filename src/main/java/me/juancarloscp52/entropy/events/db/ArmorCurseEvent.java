/**
 * @author Curio-sitas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.enchantment.Enchantments;

public class ArmorCurseEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity -> {
                    serverPlayerEntity.getInventory().armor.forEach(item -> {
                        item.addEnchantment(Enchantments.BINDING_CURSE, 1);
                    });
                }
        );
    }
}
