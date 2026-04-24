package me.juancarloscp52.entropy.datagen;

import me.juancarloscp52.entropy.EntropyTags.EntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider.EntityTypeTagsProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntropyEntityTypeTagProvider extends EntityTypeTagsProvider {
    public EntropyEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
        valueLookupBuilder(EntityTypeTags.DO_NOT_EXPLODE).add(EntityType.ENDER_DRAGON, EntityType.PLAYER);
        valueLookupBuilder(EntityTypeTags.DO_NOT_IGNITE).add(EntityType.PLAYER);
        valueLookupBuilder(EntityTypeTags.IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET).add(EntityType.AREA_EFFECT_CLOUD,
                EntityType.END_CRYSTAL,
                EntityType.GLOW_ITEM_FRAME,
                EntityType.ITEM_FRAME,
                EntityType.LEASH_KNOT,
                EntityType.LIGHTNING_BOLT,
                EntityType.MARKER,
                EntityType.PAINTING,
                EntityType.PLAYER);
    }
}
