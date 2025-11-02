package dev.fastpath.mixin;

import dev.fastpath.core.FPSectionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    // Inject into the 3-arg overload
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("HEAD"))
    private void onSetBlock3(BlockPos pos,
                             BlockState state,
                             int flags,
                             CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(pos);
        FPSectionCache.markGeometryChanged(sectionPos);
    }

    // Inject into the 4-arg overload
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At("HEAD"))
    private void onSetBlock4(BlockPos pos,
                             BlockState state,
                             int flags,
                             int recursionLeft,
                             CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(pos);
        FPSectionCache.markGeometryChanged(sectionPos);
    }
}