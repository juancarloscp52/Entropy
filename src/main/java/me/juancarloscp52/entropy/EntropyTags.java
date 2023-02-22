package me.juancarloscp52.entropy;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EntropyTags {
    public static class BlockTags {
        public static final TagKey<Block> IGNORED_BY_MIDAS_TOUCH = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "ignored_by_midas_touch"));
        public static final TagKey<Block> NOT_REPLACED_BY_EVENTS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "not_replaced_by_events"));
        public static final TagKey<Block> SHOWN_DURING_XRAY = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "shown_during_xray"));
        public static final TagKey<Block> VOID_SIGHT_BREAKS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "void_sight_breaks"));
    }

    public static class EnchantmentTags {
        public static final TagKey<Enchantment> DO_NOT_ENCHANT_WITH = TagKey.of(Registries.ENCHANTMENT.getKey(), new Identifier("entropy", "do_not_enchant_with"));
        public static final TagKey<Enchantment> DO_NOT_REMOVE = TagKey.of(Registries.ENCHANTMENT.getKey(), new Identifier("entropy", "do_not_remove"));
    }

    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> DO_NOT_DAMAGE = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_damage"));
        public static final TagKey<EntityType<?>> DO_NOT_EXPLODE = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_explode"));
        public static final TagKey<EntityType<?>> DO_NOT_FLING = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_fling"));
        public static final TagKey<EntityType<?>> DO_NOT_HIGHLIGHT = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_highlight"));
        public static final TagKey<EntityType<?>> DO_NOT_IGNITE = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_ignite"));
        public static final TagKey<EntityType<?>> DO_NOT_LEVITATE = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "do_not_levitate"));
        public static final TagKey<EntityType<?>> IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "ignored_by_forcefield_and_entity_magnet"));
        public static final TagKey<EntityType<?>> IGNORED_BY_MIDAS_TOUCH = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "ignored_by_midas_touch"));
        public static final TagKey<EntityType<?>> NOT_INVISIBLE = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "not_invisible"));
    }

    public static class ItemTags {
        public static final TagKey<Item> BANNED = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "banned"));
        public static final TagKey<Item> DOES_NOT_DROP_RANDOMLY = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "does_not_drop_randomly"));
        public static final TagKey<Item> DOES_NOT_RAIN = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "does_not_rain"));
        public static final TagKey<Item> DO_NOT_CURSE = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "do_not_curse"));
        public static final TagKey<Item> DO_NOT_DAMAGE = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "do_not_damage"));
        public static final TagKey<Item> IGNORED_BY_MIDAS_TOUCH = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "ignored_by_midas_touch"));
        public static final TagKey<Item> UNFIXABLE = TagKey.of(Registries.ITEM.getKey(), new Identifier("entropy", "unfixable"));
    }
}
