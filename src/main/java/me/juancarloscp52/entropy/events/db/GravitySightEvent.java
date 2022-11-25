/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;

public class GravitySightEvent extends AbstractTimedEvent {

    @Override
    public void tick() {
        if(tickCount%2==0){
            for (var serverPlayerEntity : PlayerLookup.all(Entropy.getInstance().eventHandler.server)) {
                var rayVector = serverPlayerEntity.getRotationVector().normalize().multiply(32d);
                var fromVector = serverPlayerEntity.getEyePos();
                var toVector = fromVector.add(rayVector);
                var box = new Box(serverPlayerEntity.getPos().add(32, 32, 32), serverPlayerEntity.getPos().subtract(32, 32, 32));
                var hitRes = ProjectileUtil.raycast(serverPlayerEntity, fromVector, toVector, box, x -> true, 1024);
                if (hitRes != null) {
                    var direction = serverPlayerEntity.getRotationVector().normalize().multiply(-1d);
                    var entity = hitRes.getEntity();
                    entity.setOnGround(false);
                    entity.setVelocity(direction);
                }
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
