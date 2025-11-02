package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPStats;
import dev.fastpath.core.FPSectionCache;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RenderShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {

    @Inject(method = "compile", at = @At("HEAD"), cancellable = true)
    private void fastpath$maybeSmartSkip(SectionPos sectionPos,
                                         RenderSectionRegion region,
                                         VertexSorting sorting,
                                         SectionBufferBuilderPack buffers,
                                         CallbackInfoReturnable<SectionCompiler.Results> cir) {
        if (!FPConfig.enabled) return;

        // Throttle: limit compiles per tick
        if (FPStats.compilesThisTick.get() >= FPConfig.maxCompilesPerTick) {
            // Too many compiles already → skip this one until next tick
            FPStats.skippedDueToThrottleTotal.incrementAndGet();
            FPStats.skippedDueToThrottleThisTick.incrementAndGet();

            SectionCompiler.Results results = new SectionCompiler.Results();
            results.visibilitySet = new VisibilitySet();
            cir.setReturnValue(results);
            return;
        }

        FPStats.compilesStartedThisTick.incrementAndGet();
        FPStats.compilesThisTick.incrementAndGet();
        long startNs = System.nanoTime();

        // Skip empty sections if enabled
        if (FPConfig.skipEmptySections && isRegionEmpty(sectionPos, region)) {
            FPStats.skippedCompilesTotal.incrementAndGet();
            FPStats.skippedCompilesThisTick.incrementAndGet();
            SectionCompiler.Results results = new SectionCompiler.Results();
            results.visibilitySet = new VisibilitySet();
            cir.setReturnValue(results);
            long endNs = System.nanoTime();
            FPStats.emaUpdateAvgCompile((endNs - startNs) / 1_000_000.0, 0.2);
            return;
        }

        // Skip light-only updates if enabled and geometry hasn't changed
        if (FPConfig.skipLightOnly && !FPSectionCache.hasGeometryChanged(sectionPos)) {
            FPStats.skippedLightOnlyTotal.incrementAndGet();
            FPStats.skippedLightOnlyThisTick.incrementAndGet();

            SectionCompiler.Results results = new SectionCompiler.Results();
            results.visibilitySet = new VisibilitySet();
            cir.setReturnValue(results);

            long endNs = System.nanoTime();
            FPStats.emaUpdateAvgCompile((endNs - startNs) / 1_000_000.0, 0.2);
            return;
        }
    }

    @Inject(method = "compile", at = @At("RETURN"))
    private void fastpath$recordCompileTime(SectionPos sectionPos,
                                            RenderSectionRegion region,
                                            VertexSorting sorting,
                                            SectionBufferBuilderPack buffers,
                                            CallbackInfoReturnable<SectionCompiler.Results> cir) {
        FPStats.compilesFinishedThisTick.incrementAndGet();

        // After a real compile, clear the geometry-changed flag
        FPSectionCache.clearGeometryChanged(sectionPos);
    }

    private boolean isRegionEmpty(SectionPos sectionPos, RenderSectionRegion region) {
        BlockPos origin = sectionPos.origin();
        BlockPos end = origin.offset(15, 15, 15);
        for (BlockPos pos : BlockPos.betweenClosed(origin, end)) {
            BlockState state = region.getBlockState(pos);
            if (state.isSolidRender()) return false;
            if (!state.getFluidState().isEmpty()) return false;
            if (state.getRenderShape() == RenderShape.MODEL) return false; // catch plants, torches, etc.
        }
        return true;
    }
}