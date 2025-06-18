package me.juancarloscp52.entropy.datagen;

import me.juancarloscp52.entropy.EntropyTags.EnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

public class EntropyEnchantmentTagProvider extends EnchantmentTagsProvider {
    public EntropyEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
        tag(EnchantmentTags.DO_NOT_ENCHANT_WITH).add(Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE);
    }
}
