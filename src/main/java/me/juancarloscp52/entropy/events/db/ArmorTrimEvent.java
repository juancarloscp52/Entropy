package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.Level;

public class ArmorTrimEvent extends AbstractInstantEvent {
    public static final EventType<ArmorTrimEvent> TYPE = EventType.builder(ArmorTrimEvent::new).build();

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            Level world = player.level();
            RandomSource random = world.random;
            RegistryAccess registryManager = player.level().registryAccess();
            Registry<TrimMaterial> trimMaterials = registryManager.lookupOrThrow(Registries.TRIM_MATERIAL);
            Registry<TrimPattern> trimPatterns = registryManager.lookupOrThrow(Registries.TRIM_PATTERN);

            player.getArmorSlots().forEach(stack -> stack.set(DataComponents.TRIM, new ArmorTrim(trimMaterials.getRandom(random).get(), trimPatterns.getRandom(random).get())));
        });
    }

    @Override
    public EventType<ArmorTrimEvent> getType() {
        return TYPE;
    }
}
