/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Box;

public class InvisibleHostileMobsEvent extends AbstractTimedEvent {

    @Override
    public void init() {
    }

    @Override
    public void tick() {
        for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            var box = new Box(serverPlayerEntity.getBlockPos().add(64, 64, 64), serverPlayerEntity.getBlockPos().add(-64, -64, -64));
            for (var mob : serverPlayerEntity.getEntityWorld().getEntitiesByClass(HostileEntity.class, box, e -> true))
                mob.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 2, 1));
        }
        super.tick();
    }

    @Override
    public void end() {
        this.hasEnded = true;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickdelta) {
    }

    @Override
    public short getDuration() {
        return Entropy.getInstance().settings.baseEventDuration;
    }

    @Override
    public String type() {
        return "invisibility";
    }
    
}
