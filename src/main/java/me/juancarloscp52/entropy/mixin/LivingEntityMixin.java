package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.Variables;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void fallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (Variables.bouncyBlocks && !entity.isSuppressingBounce()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("RETURN"))
    private void damage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var _this = (LivingEntity) (Object) this;
        var attacker = source.getEntity();

        if (attacker instanceof Player) {
            if (Variables.isOnePunchActivated > 0) {
                var pos = _this.position();
                level.addParticle(ParticleTypes.EXPLOSION, pos.x, pos.y + 1, pos.z, 0, 0, 0);
                _this.kill(level);
            }

            if (Variables.shouldLaunchEntity > 0) {
                var direction = attacker.getLookAngle().normalize().scale(4).add(0, 1.75d, 0);
                _this.push(direction);
            }
        }
    }
}
