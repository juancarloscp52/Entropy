package me.juancarloscp52.entropy.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import me.juancarloscp52.entropy.Variables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("RETURN"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var _this = (LivingEntity) (Object) this;
        var attacker = source.getAttacker();

        if (attacker instanceof PlayerEntity) {
            if (Variables.isOnePunchActivated > 0) {
                var pos = _this.getPos();
                _this.getWorld().addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y + 1, pos.z, 0, 0, 0);
                _this.kill();
            }

            if (Variables.shouldLounchEntity > 0) {
                var direction = attacker.getRotationVector().normalize().multiply(4).add(0, 1.75d, 0);
                _this.addVelocity(direction);
            }
        }
    }
}
