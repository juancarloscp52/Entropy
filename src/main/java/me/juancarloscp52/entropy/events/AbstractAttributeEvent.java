package me.juancarloscp52.entropy.events;

import me.juancarloscp52.entropy.Entropy;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public abstract class AbstractAttributeEvent extends AbstractTimedEvent {
    public record ActiveModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
    }

    private List<ActiveModifier> modifiers = List.of();

    protected abstract List<ActiveModifier> getModifiers();

    @Override
    public void init() {
        modifiers = getModifiers();
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::startPlayer);
    }

    private void startPlayer(ServerPlayerEntity player) {
        modifiers.forEach(active -> player.getAttributeInstance(active.attribute()).addTemporaryModifier(active.modifier()));
    }

    @Override
    public void end() {
        Entropy.getInstance().eventHandler.getActivePlayers().forEach(this::endPlayer);
        modifiers = List.of();
        super.end();
    }

    @Override
    public void endPlayer(ServerPlayerEntity player) {
        modifiers.forEach(active -> player.getAttributeInstance(active.attribute()).removeModifier(active.modifier().id()));
    }

    @Override
    public void tick() {
        if (getTickCount() % 20 == 0) {
            for (final ServerPlayerEntity player : Entropy.getInstance().eventHandler.getActivePlayers()) {
                for (final ActiveModifier active : modifiers) {
                    final EntityAttributeInstance attributeInstance = player.getAttributeInstance(active.attribute());
                    if (attributeInstance.getModifier(active.modifier().id()) == null) {
                        attributeInstance.addTemporaryModifier(active.modifier());
                    }
                }
            }
        }
        super.tick();
    }
}
