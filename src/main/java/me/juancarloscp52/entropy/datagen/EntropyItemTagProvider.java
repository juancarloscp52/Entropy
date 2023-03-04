package me.juancarloscp52.entropy.datagen;

import me.juancarloscp52.entropy.EntropyTags.ItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class EntropyItemTagProvider extends ItemTagProvider{
    public EntropyItemTagProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture, BlockTagProvider blockTagProvider) {
        super(output, completableFuture, blockTagProvider);
    }

    @Override
    protected void configure(WrapperLookup wrapperLookup) {
        TagKey<Item> spawnEggsTag = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "spawn_eggs"));

        getOrCreateTagBuilder(ItemTags.BANNED).addTag(spawnEggsTag).add(Items.DEBUG_STICK,
                Items.COMMAND_BLOCK,
                Items.CHAIN_COMMAND_BLOCK,
                Items.REPEATING_COMMAND_BLOCK,
                Items.BARRIER,
                Items.STRUCTURE_BLOCK,
                Items.STRUCTURE_VOID);
        getOrCreateTagBuilder(ItemTags.DOES_NOT_DROP_RANDOMLY).addTag(ItemTags.BANNED);
        getOrCreateTagBuilder(ItemTags.DOES_NOT_RAIN).addTag(ItemTags.BANNED);
        getOrCreateTagBuilder(ItemTags.IGNORED_BY_MIDAS_TOUCH).add(Items.AIR);
        getOrCreateTagBuilder(spawnEggsTag).add(Items.ALLAY_SPAWN_EGG,
                Items.AXOLOTL_SPAWN_EGG,
                Items.BAT_SPAWN_EGG,
                Items.BEE_SPAWN_EGG,
                Items.BLAZE_SPAWN_EGG,
                Items.CAT_SPAWN_EGG,
                Items.CAMEL_SPAWN_EGG,
                Items.CAVE_SPIDER_SPAWN_EGG,
                Items.CHICKEN_SPAWN_EGG,
                Items.COD_SPAWN_EGG,
                Items.COW_SPAWN_EGG,
                Items.CREEPER_SPAWN_EGG,
                Items.DOLPHIN_SPAWN_EGG,
                Items.DONKEY_SPAWN_EGG,
                Items.DROWNED_SPAWN_EGG,
                Items.ELDER_GUARDIAN_SPAWN_EGG,
                Items.ENDER_DRAGON_SPAWN_EGG,
                Items.ENDERMAN_SPAWN_EGG,
                Items.ENDERMITE_SPAWN_EGG,
                Items.EVOKER_SPAWN_EGG,
                Items.FOX_SPAWN_EGG,
                Items.FROG_SPAWN_EGG,
                Items.GHAST_SPAWN_EGG,
                Items.GLOW_SQUID_SPAWN_EGG,
                Items.GOAT_SPAWN_EGG,
                Items.GUARDIAN_SPAWN_EGG,
                Items.HOGLIN_SPAWN_EGG,
                Items.HORSE_SPAWN_EGG,
                Items.HUSK_SPAWN_EGG,
                Items.IRON_GOLEM_SPAWN_EGG,
                Items.LLAMA_SPAWN_EGG,
                Items.MAGMA_CUBE_SPAWN_EGG,
                Items.MOOSHROOM_SPAWN_EGG,
                Items.MULE_SPAWN_EGG,
                Items.OCELOT_SPAWN_EGG,
                Items.PANDA_SPAWN_EGG,
                Items.PARROT_SPAWN_EGG,
                Items.PHANTOM_SPAWN_EGG,
                Items.PIG_SPAWN_EGG,
                Items.PIGLIN_SPAWN_EGG,
                Items.PIGLIN_BRUTE_SPAWN_EGG,
                Items.PILLAGER_SPAWN_EGG,
                Items.POLAR_BEAR_SPAWN_EGG,
                Items.PUFFERFISH_SPAWN_EGG,
                Items.RABBIT_SPAWN_EGG,
                Items.RAVAGER_SPAWN_EGG,
                Items.SALMON_SPAWN_EGG,
                Items.SHEEP_SPAWN_EGG,
                Items.SHULKER_SPAWN_EGG,
                Items.SILVERFISH_SPAWN_EGG,
                Items.SKELETON_SPAWN_EGG,
                Items.SKELETON_HORSE_SPAWN_EGG,
                Items.SLIME_SPAWN_EGG,
                Items.SNOW_GOLEM_SPAWN_EGG,
                Items.SPIDER_SPAWN_EGG,
                Items.SQUID_SPAWN_EGG,
                Items.STRAY_SPAWN_EGG,
                Items.STRIDER_SPAWN_EGG,
                Items.TADPOLE_SPAWN_EGG,
                Items.TRADER_LLAMA_SPAWN_EGG,
                Items.TROPICAL_FISH_SPAWN_EGG,
                Items.TURTLE_SPAWN_EGG,
                Items.VEX_SPAWN_EGG,
                Items.VILLAGER_SPAWN_EGG,
                Items.VINDICATOR_SPAWN_EGG,
                Items.WANDERING_TRADER_SPAWN_EGG,
                Items.WARDEN_SPAWN_EGG,
                Items.WITCH_SPAWN_EGG,
                Items.WITHER_SPAWN_EGG,
                Items.WITHER_SKELETON_SPAWN_EGG,
                Items.WOLF_SPAWN_EGG,
                Items.ZOGLIN_SPAWN_EGG,
                Items.ZOMBIE_SPAWN_EGG,
                Items.ZOMBIE_HORSE_SPAWN_EGG,
                Items.ZOMBIE_VILLAGER_SPAWN_EGG,
                Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
    }
}
