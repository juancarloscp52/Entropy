package me.juancarloscp52.entropy;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EntropyTags {
    public static final TagKey<EntityType<?>> IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "ignored_by_forcefield_and_entity_magnet"));
    public static final TagKey<Block> NOT_REPLACED_BY_EVENTS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "not_replaced_by_events"));
    public static final TagKey<Block> IGNORED_BY_MIDAS_TOUCH = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "ignored_by_midas_touch"));
    public static final TagKey<Block> VOID_SIGHT_BREAKS = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "void_sight_breaks"));
    public static final TagKey<Block> SHOWN_DURING_XRAY = TagKey.of(Registries.BLOCK.getKey(), new Identifier("entropy", "shown_during_xray"));
    public static final TagKey<Enchantment> DO_NOT_ENCHANT_WITH = TagKey.of(Registries.ENCHANTMENT.getKey(), new Identifier("entropy", "do_not_enchant_with"));
}
