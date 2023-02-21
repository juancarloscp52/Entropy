package me.juancarloscp52.entropy.datagen;

import java.util.concurrent.CompletableFuture;

import me.juancarloscp52.entropy.EntropyTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class EntropyBlockTagProvider extends BlockTagProvider {
    public EntropyBlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EntropyTags.NOT_REPLACED_BY_EVENTS).add(Blocks.BEDROCK, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME);
    }
}
