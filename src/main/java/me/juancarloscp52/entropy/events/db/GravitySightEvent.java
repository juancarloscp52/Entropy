/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.mixin.FallingBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public class GravitySightEvent extends AbstractTimedEvent {

    private BlockPos _lastBlockInSight = null;
    private int _stareTimer = 0;

    @Override
    public void init() {
        _lastBlockInSight = null;
        _stareTimer = 0;
    }

    @Override
    public void tick() {
        if (tickCount % 2 == 0) {
            for (var serverPlayerEntity : Entropy.getInstance().eventHandler.getActivePlayers()) {
                var rayVector = serverPlayerEntity.getLookAngle().normalize().scale(64d);
                var fromVector = serverPlayerEntity.getEyePosition();
                var toVector = fromVector.add(rayVector);
                var box = new AABB(serverPlayerEntity.position().add(64, 64, 64),
                        serverPlayerEntity.position().subtract(64, 64, 64));
                var hitRes = ProjectileUtil.getEntityHitResult(serverPlayerEntity, fromVector, toVector, box, x -> true, 2048);
                if (hitRes != null) {
                    var direction = serverPlayerEntity.getLookAngle().normalize().scale(-1d);
                    var entity = hitRes.getEntity();
                    entity.setOnGround(false);
                    entity.setDeltaMovement(direction);
                } else if (_lastBlockInSight == null || _stareTimer < 10) {
                    var hitRes2 = serverPlayerEntity.pick(64, 1, false);
                    if (hitRes2.getType() == Type.BLOCK) {
                        var blockHitRes = (BlockHitResult) hitRes2;
                        var blockPos = blockHitRes.getBlockPos();
                        if (blockPos.equals(_lastBlockInSight))
                            _stareTimer++;
                        else {
                            _lastBlockInSight = blockPos;
                            _stareTimer = 1;
                        }
                    }
                } else {
                    var world = serverPlayerEntity.level();
                    var blockState = world.getBlockState(_lastBlockInSight);
                    if (!blockState.is(BlockTags.NOT_REPLACED_BY_EVENTS)) {

                        world.setBlockAndUpdate(_lastBlockInSight, Blocks.AIR.defaultBlockState());

                        var direction = serverPlayerEntity.getLookAngle().normalize().scale(-1d);

                        var fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);
                        var fallingBlockAccessor = (FallingBlockEntityAccessor) fallingBlockEntity;

                        fallingBlockAccessor.setBlockState(blockState);
                        fallingBlockEntity.blocksBuilding = true;
                        fallingBlockEntity.setPos(new Vec3(_lastBlockInSight.getX() + .5d, _lastBlockInSight.getY(), _lastBlockInSight.getZ() + .5d));
                        fallingBlockEntity.setDeltaMovement(direction);
                        fallingBlockEntity.setStartPos(fallingBlockEntity.blockPosition());
                        world.addFreshEntity(fallingBlockEntity);

                        _lastBlockInSight = null;
                        _stareTimer = 0;
                    }
                }
            }
        }

        super.tick();
    }
}
