package me.juancarloscp52.entropy.mixin;

import me.juancarloscp52.entropy.EntropyTags.BlockTags;
import me.juancarloscp52.entropy.Variables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    // BlockMixin's shouldRenderFace function is enough for vanilla, but in order for the event to work with Sodium, skipRendering and isFaceSturdy need to be modified
    @Inject(at = @At("HEAD"), method = "skipRendering", cancellable = true)
    public void isSideInvisible(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(!((BlockStateBase) (Object) this).is(BlockTags.SHOWN_DURING_XRAY));
        }
    }

    @Inject(at = @At("HEAD"), method = "isFaceSturdy(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/SupportType;)Z", cancellable = true)
    public void isSideSolid(BlockGetter world, BlockPos pos, Direction direction, SupportType shapeType, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(((BlockStateBase) (Object) this).is(BlockTags.SHOWN_DURING_XRAY));
        }
    }

    // Make interested blocks glow
    @Inject(at = @At("HEAD"), method = "getShadeBrightness", cancellable = true)
    public void getAmbientOcclusionLightLevel(BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(1f);
        }
    }

    // And make these blocks highlight its surroundings
    @Inject(at = @At("HEAD"), method = "getLightEmission", cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> ci) {
        if (Variables.xrayActive && ((BlockStateBase) (Object) this).is(BlockTags.SHOWN_DURING_XRAY)) {
            ci.setReturnValue(12);
        }
    }

    // getFaceOcclusionShape needs to be modified in order to work with Sodium. Without it if block is next to the stone then only face next to the air is rendered.
    @Inject(at = @At("HEAD"), method = "getFaceOcclusionShape", cancellable = true)
    public void getCullingFace(CallbackInfoReturnable<VoxelShape> ci) {
        if (Variables.xrayActive) {
            if (((BlockStateBase) (Object) this).is(BlockTags.SHOWN_DURING_XRAY))
                ci.setReturnValue(Shapes.block());
            else
                ci.setReturnValue(Shapes.empty());
        }
    }
}
