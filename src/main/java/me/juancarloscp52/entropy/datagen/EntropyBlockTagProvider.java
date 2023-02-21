package me.juancarloscp52.entropy.datagen;

import java.util.concurrent.CompletableFuture;

import me.juancarloscp52.entropy.EntropyTags.BlockTags;
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
        getOrCreateTagBuilder(BlockTags.NOT_REPLACED_BY_EVENTS).add(Blocks.BEDROCK, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME);
        getOrCreateTagBuilder(BlockTags.IGNORED_BY_MIDAS_TOUCH).addTag(BlockTags.NOT_REPLACED_BY_EVENTS).add(Blocks.AIR,
                Blocks.GOLD_BLOCK,
                Blocks.GOLD_ORE,
                Blocks.RAW_GOLD_BLOCK,
                Blocks.NETHER_GOLD_ORE);
        getOrCreateTagBuilder(BlockTags.VOID_SIGHT_BREAKS).add(Blocks.CHEST,
                Blocks.TRAPPED_CHEST,
                Blocks.BARREL,
                Blocks.FURNACE,
                Blocks.BLAST_FURNACE,
                Blocks.SMOKER);
        getOrCreateTagBuilder(BlockTags.SHOWN_DURING_XRAY).add(Blocks.COAL_ORE,
                Blocks.COAL_BLOCK,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.IRON_ORE,
                Blocks.RAW_IRON_BLOCK,
                Blocks.IRON_BLOCK,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.COPPER_ORE,
                Blocks.RAW_COPPER_BLOCK,
                Blocks.COPPER_BLOCK,
                Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.GOLD_ORE,
                Blocks.RAW_GOLD_BLOCK,
                Blocks.NETHER_GOLD_ORE,
                Blocks.GOLD_BLOCK,
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.DIAMOND_BLOCK,
                Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.ANCIENT_DEBRIS,
                Blocks.NETHERITE_BLOCK,
                Blocks.LAPIS_ORE,
                Blocks.LAPIS_BLOCK,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.EMERALD_ORE,
                Blocks.EMERALD_BLOCK,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.REDSTONE_BLOCK,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.BEDROCK,
                Blocks.OBSIDIAN);
    }
}
