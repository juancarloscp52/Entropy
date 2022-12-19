package me.juancarloscp52.entropy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.juancarloscp52.entropy.Variables;
import me.juancarloscp52.entropy.events.db.XRayEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    // BlockMixin's shouldDrawSide function is enough for vanilla, but in order for the event to work with Sodium, isSideInvisible and isSideSolid need to be modified
    @Inject(at = @At("HEAD"), method = "isSideInvisible", cancellable = true)
    public void isSideInvisible(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(!XRayEvent.BLOCKS_TO_RENDER.contains(this.getBlock()));
            return;
        }
    }

    @Inject(at = @At("HEAD"), method = "isSideSolid", cancellable = true)
    public void isSideSolid(BlockView world, BlockPos pos, Direction direction, SideShapeType shapeType, CallbackInfoReturnable<Boolean> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(XRayEvent.BLOCKS_TO_RENDER.contains(this.getBlock()));
            return;
        }
    }

    // Make interested blocks glow
    @Inject(at = @At("HEAD"), method = "getAmbientOcclusionLightLevel", cancellable = true)
    public void getAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        if (Variables.xrayActive) {
            ci.setReturnValue(1f);
            return;
        }
    }

    // And make these blocks highlight its surroundings
    @Inject(at = @At("HEAD"), method = "getLuminance", cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> ci) {
        if (Variables.xrayActive && XRayEvent.BLOCKS_TO_RENDER.contains(this.getBlock())) {
            ci.setReturnValue(12);
            return;
        }
    }

    // getCullingFace needs to be modified in order to work with Sodium. Without it if block is next to the stone then only face next to the air is rendered.
    @Inject(at = @At("HEAD"), method = "getCullingFace", cancellable = true)
    public void getCullingFace(CallbackInfoReturnable<VoxelShape> ci) {
        if (Variables.xrayActive) {
            if (XRayEvent.BLOCKS_TO_RENDER.contains(this.getBlock()))
                ci.setReturnValue(VoxelShapes.fullCube());
            else
                ci.setReturnValue(VoxelShapes.empty());
            return;
        }
    }
}
