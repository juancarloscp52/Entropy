package me.juancarloscp52.entropy;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EntropyTags {
    public static final TagKey<EntityType<?>> IGNORED_BY_FORCEFIELD_AND_ENTITY_MAGNET = TagKey.of(Registries.ENTITY_TYPE.getKey(), new Identifier("entropy", "ignored_by_forcefield_and_entity_magnet"));
}
