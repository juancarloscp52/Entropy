package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ArmorTrimEvent extends AbstractInstantEvent {
    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            World world = player.world;
            Random random = world.random;
            DynamicRegistryManager registryManager = player.world.getRegistryManager();
            Registry<ArmorTrimMaterial> trimMaterials = registryManager.get(RegistryKeys.TRIM_MATERIAL);
            Registry<ArmorTrimPattern> trimPatterns = registryManager.get(RegistryKeys.TRIM_PATTERN);

            player.getArmorItems().forEach(stack -> ArmorTrim.apply(registryManager, stack, new ArmorTrim(trimMaterials.getRandom(random).get(), trimPatterns.getRandom(random).get())));
        });
    }
}
