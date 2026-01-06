package me.juancarloscp52.entropy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class EntropyTags {
    public static class BlockTags {
        public static final TagKey<Block> IGNORED_BY_MIDAS_TOUCH = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("entropy", "ignored_by_midas_touch"));
        public static final TagKey<Block> NOT_REPLACED_BY_EVENTS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("entropy", "not_replaced_by_events"));
        public static final TagKey<Block> NOT_REPLACED_BY_ZEUS_ULT = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("entropy", "not_replaced_by_zeus_ult"));
        public static final TagKey<Block> SHOWN_DURING_XRAY = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("entropy", "shown_during_xray"));
        public static final TagKey<Block> VOID_SIGHT_BREAKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("entropy", "void_sight_breaks"));
    }

    public static class EnchantmentTags {
        public static final TagKey<Enchantment> DO_NOT_ENCHANT_WITH = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("entropy", "do_not_enchant_with"));
        public static final TagKey<Enchantment> DO_NOT_REMOVE = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("entropy", "do_not_remove"));
    }

    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> DO_NOT_DAMAGE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_damage"));
        public static final TagKey<EntityType<?>> DO_NOT_EXPLODE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_explode"));
        public static final TagKey<EntityType<?>> DO_NOT_FLING = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_fling"));
        public static final TagKey<EntityType<?>> DO_NOT_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_highlight"));
        public static final TagKey<EntityType<?>> DO_NOT_IGNITE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_ignite"));
        public static final TagKey<EntityType<?>> DO_NOT_LEVITATE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "do_not_levitate"));
        public static final TagKey<EntityType<?>> IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "ignored_by_forcefield_and_entity_magnet"));
        public static final TagKey<EntityType<?>> IGNORED_BY_MIDAS_TOUCH = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "ignored_by_midas_touch"));
        public static final TagKey<EntityType<?>> NO_RAINBOW_TRAIL = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "no_rainbow_trail"));
        public static final TagKey<EntityType<?>> NOT_INVISIBLE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("entropy", "not_invisible"));
    }

    public static class ItemTags {
        public static final TagKey<Item> BANNED = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "banned"));
        public static final TagKey<Item> DOES_NOT_DROP_RANDOMLY = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "does_not_drop_randomly"));
        public static final TagKey<Item> DOES_NOT_RAIN = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "does_not_rain"));
        public static final TagKey<Item> DO_NOT_CURSE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "do_not_curse"));
        public static final TagKey<Item> DO_NOT_DAMAGE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "do_not_damage"));
        public static final TagKey<Item> IGNORED_BY_MIDAS_TOUCH = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "ignored_by_midas_touch"));
        public static final TagKey<Item> MIDAS_TOUCH_GOLDEN_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "midas_touch_golden_items"));
        public static final TagKey<Item> UNFIXABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("entropy", "unfixable"));
    }
}
