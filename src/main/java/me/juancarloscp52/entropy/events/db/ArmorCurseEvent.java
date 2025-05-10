/**
 * @author Curio-sitas
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyUtils;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantments;

public class ArmorCurseEvent extends AbstractInstantEvent {
    public static final EventType<ArmorCurseEvent> TYPE = EventType.builder(ArmorCurseEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(
                serverPlayerEntity ->
                    EntropyUtils.modifyArmor(serverPlayerEntity, item ->
                        item.enchant(serverPlayerEntity.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.BINDING_CURSE).get(), 1)
                    )
        );
    }

    @Override
    public EventType<ArmorCurseEvent> getType() {
        return TYPE;
    }
}
