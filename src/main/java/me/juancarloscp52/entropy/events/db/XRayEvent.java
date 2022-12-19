package me.juancarloscp52.entropy.events.db;

import java.util.ArrayList;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

public class XRayEvent extends AbstractTimedEvent {

    public static final ArrayList<Block> BLOCKS_TO_RENDER = new ArrayList<Block>() {{
        add(Blocks.COAL_ORE);
        add(Blocks.COAL_BLOCK);
        add(Blocks.DEEPSLATE_COAL_ORE);
        add(Blocks.IRON_ORE);
        add(Blocks.RAW_IRON_BLOCK);
        add(Blocks.IRON_BLOCK);
        add(Blocks.DEEPSLATE_IRON_ORE);
        add(Blocks.COPPER_ORE);
        add(Blocks.RAW_COPPER_BLOCK);
        add(Blocks.COPPER_BLOCK);
        add(Blocks.DEEPSLATE_COPPER_ORE);
        add(Blocks.GOLD_ORE);
        add(Blocks.RAW_GOLD_BLOCK);
        add(Blocks.NETHER_GOLD_ORE);
        add(Blocks.GOLD_BLOCK);
        add(Blocks.DEEPSLATE_GOLD_ORE);
        add(Blocks.DIAMOND_ORE);
        add(Blocks.DIAMOND_BLOCK);
        add(Blocks.DEEPSLATE_DIAMOND_ORE);
        add(Blocks.ANCIENT_DEBRIS);
        add(Blocks.NETHERITE_BLOCK);
        add(Blocks.LAPIS_ORE);
        add(Blocks.LAPIS_BLOCK);
        add(Blocks.DEEPSLATE_LAPIS_ORE);
        add(Blocks.EMERALD_ORE);
        add(Blocks.EMERALD_BLOCK);
        add(Blocks.DEEPSLATE_EMERALD_ORE);
        add(Blocks.REDSTONE_ORE);
        add(Blocks.REDSTONE_BLOCK);
        add(Blocks.DEEPSLATE_REDSTONE_ORE);
        add(Blocks.BEDROCK);
        add(Blocks.OBSIDIAN);
    }};

    @Override
    public void initClient() {
        Variables.xrayActive = true;

        // Rerender the world because of the caching
        var client = MinecraftClient.getInstance();
        client.worldRenderer.reload();
    }

    @Override
    public void endClient() {
        Variables.xrayActive = false;
        
        // Rerender the world because of the caching
        var client = MinecraftClient.getInstance();
        client.worldRenderer.reload();
        
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

}
