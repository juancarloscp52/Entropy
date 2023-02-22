package me.juancarloscp52.entropy.datagen;

import java.util.concurrent.CompletableFuture;

import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class EntropyBlockTagProvider extends BlockTagProvider {
    public EntropyBlockTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.IGNORED_BY_MIDAS_TOUCH).addTag(BlockTags.NOT_REPLACED_BY_EVENTS).add(Blocks.AIR,
                Blocks.GOLD_BLOCK,
                Blocks.GOLD_ORE,
                Blocks.RAW_GOLD_BLOCK,
                Blocks.NETHER_GOLD_ORE);
        getOrCreateTagBuilder(BlockTags.NOT_REPLACED_BY_EVENTS).add(Blocks.BEDROCK, Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME);
        getOrCreateTagBuilder(BlockTags.NOT_REPLACED_BY_ZEUS_ULT).add(Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME);
        getOrCreateTagBuilder(BlockTags.SHOWN_DURING_XRAY).addOptionalTag(ConventionalBlockTags.ORES).add(Blocks.COAL_BLOCK,
                Blocks.IRON_ORE,
                Blocks.RAW_IRON_BLOCK,
                Blocks.IRON_BLOCK,
                Blocks.RAW_COPPER_BLOCK,
                Blocks.COPPER_BLOCK,
                Blocks.RAW_GOLD_BLOCK,
                Blocks.GOLD_BLOCK,
                Blocks.DIAMOND_BLOCK,
                Blocks.ANCIENT_DEBRIS,
                Blocks.NETHERITE_BLOCK,
                Blocks.LAPIS_BLOCK,
                Blocks.EMERALD_BLOCK,
                Blocks.REDSTONE_BLOCK,
                Blocks.BEDROCK,
                Blocks.OBSIDIAN);
        getOrCreateTagBuilder(BlockTags.VOID_SIGHT_BREAKS).addOptionalTag(ConventionalBlockTags.CHESTS).add(Blocks.BARREL,
                Blocks.FURNACE,
                Blocks.BLAST_FURNACE,
                Blocks.SMOKER);
    }
}
