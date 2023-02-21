package me.juancarloscp52.entropy.datagen;

import java.util.concurrent.CompletableFuture;

import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EntityTypeTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class EntropyEntityTypeTagProvider extends EntityTypeTagProvider {
    public EntropyEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(EntityTypeTags.IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET).add(EntityType.AREA_EFFECT_CLOUD,
                EntityType.END_CRYSTAL,
                EntityType.GLOW_ITEM_FRAME,
                EntityType.ITEM_FRAME,
                EntityType.LEASH_KNOT,
                EntityType.LIGHTNING_BOLT,
                EntityType.MARKER,
                EntityType.PAINTING,
                EntityType.PLAYER);
        getOrCreateTagBuilder(EntityTypeTags.DO_NOT_IGNITE).add(EntityType.PLAYER);
        getOrCreateTagBuilder(EntityTypeTags.DO_NOT_EXPLODE).add(EntityType.ENDER_DRAGON, EntityType.PLAYER);
    }
}
