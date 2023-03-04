/**
 * @author Kanawanagasaki
 */

package me.juancarloscp52.entropy.events.db;

import me.juancarloscp52.entropy.Entropy;
import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.events.AbstractTimedEvent;
import me.juancarloscp52.entropy.mixin.FallingBlockEntityAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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
                var rayVector = serverPlayerEntity.getRotationVector().normalize().multiply(64d);
                var fromVector = serverPlayerEntity.getEyePos();
                var toVector = fromVector.add(rayVector);
                var box = new Box(serverPlayerEntity.getPos().add(64, 64, 64),
                        serverPlayerEntity.getPos().subtract(64, 64, 64));
                var hitRes = ProjectileUtil.raycast(serverPlayerEntity, fromVector, toVector, box, x -> true, 2048);
                if (hitRes != null) {
                    var direction = serverPlayerEntity.getRotationVector().normalize().multiply(-1d);
                    var entity = hitRes.getEntity();
                    entity.setOnGround(false);
                    entity.setVelocity(direction);
                } else if (_lastBlockInSight == null || _stareTimer < 10) {
                    var hitRes2 = serverPlayerEntity.raycast(64, 1, false);
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
                    var world = serverPlayerEntity.getWorld();
                    var blockState = world.getBlockState(_lastBlockInSight);
                    if (!blockState.isIn(BlockTags.NOT_REPLACED_BY_EVENTS)) {

                        world.setBlockState(_lastBlockInSight, Blocks.AIR.getDefaultState());

                        var direction = serverPlayerEntity.getRotationVector().normalize().multiply(-1d);

                        var fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);
                        var fallingBlockAccessor = (FallingBlockEntityAccessor) fallingBlockEntity;

                        fallingBlockAccessor.setBlock(blockState);
                        fallingBlockEntity.intersectionChecked = true;
                        fallingBlockEntity.setPosition(new Vec3d(_lastBlockInSight.getX() + .5d, _lastBlockInSight.getY(), _lastBlockInSight.getZ() + .5d));
                        fallingBlockEntity.setVelocity(direction);
                        fallingBlockEntity.setFallingBlockPos(fallingBlockEntity.getBlockPos());
                        world.spawnEntity(fallingBlockEntity);

                        _lastBlockInSight = null;
                        _stareTimer = 0;
                    }
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
