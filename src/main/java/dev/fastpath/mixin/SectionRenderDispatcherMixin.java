package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPThreading;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.util.thread.ConsecutiveExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Redirects the default chunk rebuild executor in SectionRenderDispatcher
 * to Fastpath's adaptive thread pool system.
 */
@Mixin(SectionRenderDispatcher.class)
public class SectionRenderDispatcherMixin {

    @Redirect(
            method = "<init>",
            at = @At(value = "NEW", target = "net/minecraft/util/thread/ConsecutiveExecutor")
    )
    private ConsecutiveExecutor fastpath$useAdaptiveExecutor(Executor base, String name) {

        // Fastpath disabled → vanilla executor
        if (!FPConfig.enabled) {
            return new ConsecutiveExecutor(base, name);
        }

        // Decide initial core thread count
        int coreThreads = FPConfig.adaptiveThreads
                ? FPConfig.minThreads
                : FPConfig.maxConcurrentRebuilds;

        // Create Fastpath pool in legal location (outside mixins)
        ThreadPoolExecutor pool = FPThreading.createPool(
                coreThreads,
                FPConfig.maxThreads
        );

        // Vanilla still wants a ConsecutiveExecutor wrapper
        return new ConsecutiveExecutor(pool, name);
    }
}
