package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPStats;
import dev.fastpath.core.FPThreading;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftTickMixin {

    private static double smoothedFrameMs = 16.67;

    @Inject(method = "tick", at = @At("HEAD"))
    private void fastpath$adaptThreads(CallbackInfo ci) {
        FPStats.resetFrame();

        if (!FPConfig.enabled || !FPConfig.adaptiveThreads) {
            return;
        }

        // Current FPS (mapping may differ depending on Loom)
        double fps = Minecraft.getInstance().getFps();
        if (fps <= 0) return;

        double frameMs = 1000.0 / fps;

        // Exponential smoothing
        double a = FPConfig.frameTimeSmoothAlpha;
        smoothedFrameMs = a * frameMs + (1.0 - a) * smoothedFrameMs;

        double targetMs = 1000.0 / FPConfig.targetFps;

        boolean tooSlow = smoothedFrameMs > targetMs * 1.08;
        boolean verySmooth = smoothedFrameMs < targetMs * 0.92;

        // Current rebuild worker count
        int current = FPStats.activeRebuildThreads;

        // +1 or -1 adjustment
        int desired = current + (tooSlow ? -1 : (verySmooth ? +1 : 0));

        // ✅ Use the correct non-mixin helper
        FPThreading.resize(desired);
    }
}
