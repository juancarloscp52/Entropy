/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractInstantEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class InvisiblePlayerEvent extends AbstractInstantEvent {

    @Override
    public void init() {
        for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
            var effect = new MobEffectInstance(MobEffects.INVISIBILITY,
                    Entropy.getInstance().settings.baseEventDuration, 1, true, false);
            serverPlayerEntity.addEffect(effect);
        }
    }

}
