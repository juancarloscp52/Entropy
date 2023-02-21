package me.juancarloscp52.entropy.datagen;

import java.util.concurrent.CompletableFuture;

import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EnchantmentTagProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class EntropyEnchantmentTagProvider extends EnchantmentTagProvider{
    public EntropyEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.DO_NOT_ENCHANT_WITH).add(Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE);
    }
}
