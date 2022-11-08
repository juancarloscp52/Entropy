/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;

public class DeathSightEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
            var rayVector = serverPlayerEntity.getRotationVector().normalize().multiply(32d);
            var fromVector = serverPlayerEntity.getEyePos();
            var toVector = fromVector.add(rayVector);
            var box = new Box(serverPlayerEntity.getPos().add(32, 32, 32), serverPlayerEntity.getPos().subtract(32, 32, 32));
            var hitRes = ProjectileUtil.raycast(serverPlayerEntity, fromVector, toVector, box, x -> true, 1024);
            if (hitRes != null) {
                var entity = hitRes.getEntity();
                if(entity instanceof LivingEntity)
                    entity.damage(DamageSource.player(serverPlayerEntity), 3);
            }
        }
        super.tick();
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
        return "sight";
    }

}
