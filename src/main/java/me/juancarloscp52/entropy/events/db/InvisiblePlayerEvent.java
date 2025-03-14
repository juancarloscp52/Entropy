/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import me.juancarloscp52.entropy.events.EventCategory;
import me.juancarloscp52.entropy.events.EventType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class InvisiblePlayerEvent extends AbstractInstantEvent {
    public static final EventType<InvisiblePlayerEvent> TYPE = EventType.builder(InvisiblePlayerEvent::new).category(EventCategory.INVISIBILITY).build();

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var effect = new MobEffectInstance(MobEffects.INVISIBILITY,
                    Entropy.getInstance().settings.baseEventDuration, 1, true, false);
            serverPlayerEntity.addEffect(effect);
        }
    }

    @Override
    public EventType<InvisiblePlayerEvent> getType() {
        return TYPE;
    }
}
