package dev.fastpath.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Central threading utilities for Fastpath.
 * All thread pools and dynamic resizing live here,
 * NOT inside mixins (to avoid illegal classload errors).
 */
public class FPThreading {

    public static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);
    public static volatile ThreadPoolExecutor FASTPATH_POOL;

    /**
     * Creates or replaces the global rebuild pool.
     */
    public static ThreadPoolExecutor createPool(int core, int max) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                core,
                max,
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> {
                    Thread t = new Thread(() -> {
                        // Track active rebuild threads
                        THREAD_COUNT.incrementAndGet();
                        FPStats.activeRebuildThreads = THREAD_COUNT.get();
                        FPStats.maxRebuildThreadsObserved =
                                Math.max(FPStats.maxRebuildThreadsObserved, FPStats.activeRebuildThreads);

                        try { runnable.run(); }
                        finally {
                            THREAD_COUNT.decrementAndGet();
                            FPStats.activeRebuildThreads = THREAD_COUNT.get();
                        }
                    }, "Fastpath-Rebuild");

                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy() // backpressure fallback
        );

        FASTPATH_POOL = pool;
        return pool;
    }

    /**
     * Resize pool dynamically (used during runtime).
     */
    public static void resize(int newCore) {
        ThreadPoolExecutor pool = FASTPATH_POOL;
        if (pool == null)
            return;

        int bounded = Math.max(FPConfig.minThreads,
                Math.min(FPConfig.maxThreads, newCore));

        pool.setCorePoolSize(bounded);
    }
}
