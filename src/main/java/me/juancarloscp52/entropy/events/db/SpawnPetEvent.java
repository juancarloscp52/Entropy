package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventType;
import me.juancarloscp52.entropy.mixin.CatInvoker;
import me.juancarloscp52.entropy.mixin.WolfInvoker;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.DyeColor;

import java.util.List;
import java.util.Random;

public class SpawnPetEvent extends AbstractInstantEvent {
    public static final List<EntityType<? extends TamableAnimal>> PET_TYPES = List.of(
        EntityType.CAT,
        EntityType.WOLF,
        EntityType.PARROT
    );

    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnPetEvent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.registry(Registries.ENTITY_TYPE).map(type -> (EntityType<? extends TamableAnimal>) type, type -> type), SpawnPetEvent::getPetType,
        SpawnPetEvent::new
    );

    public static final EventType<SpawnPetEvent> TYPE = EventType.builder(SpawnPetEvent::createRandom).streamCodec(STREAM_CODEC).build();

    private final EntityType<? extends TamableAnimal> petType;

    public static SpawnPetEvent createRandom() {
        Random random = new Random();
        return new SpawnPetEvent(PET_TYPES.get(random.nextInt(PET_TYPES.size())));
    }

    public SpawnPetEvent(EntityType<? extends TamableAnimal> petType) {
        this.petType = petType;
    }

    public EntityType<? extends TamableAnimal> getPetType() {
        return petType;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("events.entropy.spawn_pet.type", petType.getDescription());
    }

    @Override
    public void init() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(player -> {
            RandomSource random = player.getRandom();

            petType.spawn(player.level(), pet -> {
                pet.tame(player);
                // Some stuff that doesn't have an interface in the base game
                if (pet instanceof Cat cat) {
                    ((CatInvoker) cat).invokeSetCollarColor(Util.getRandom(DyeColor.values(), random));
                } else if (pet instanceof Wolf wolf) {
                    ((WolfInvoker) wolf).invokeSetCollarColor(Util.getRandom(DyeColor.values(), random));
                }
            }, player.blockPosition().offset(random.nextIntBetweenInclusive(-4, 4), random.nextInt(2), random.nextIntBetweenInclusive(-4, 4)), EntitySpawnReason.EVENT, false, false);
        });
    }

    @Override
    public EventType<SpawnPetEvent> getType() {
        return TYPE;
    }
}
