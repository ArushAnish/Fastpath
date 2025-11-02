package dev.fastpath.core;

import java.util.concurrent.atomic.AtomicInteger;

public class FPStats {
    // Lifetime and per-tick counters
    public static final AtomicInteger skippedCompilesTotal = new AtomicInteger(0);
    public static final AtomicInteger skippedCompilesThisTick = new AtomicInteger(0);
    public static final AtomicInteger compilesStartedThisTick = new AtomicInteger(0);
    public static final AtomicInteger compilesFinishedThisTick = new AtomicInteger(0);
    public static final AtomicInteger skippedLightOnlyTotal = new AtomicInteger(0);
    public static final AtomicInteger skippedLightOnlyThisTick = new AtomicInteger(0);

    // New: throttle counters
    public static final AtomicInteger compilesThisTick = new AtomicInteger(0);
    public static final AtomicInteger skippedDueToThrottleTotal = new AtomicInteger(0);
    public static final AtomicInteger skippedDueToThrottleThisTick = new AtomicInteger(0);

    // Timing stats (simple moving averages)
    public static volatile double avgCompileMs = 0.0;
    public static volatile double avgQueueDelayMs = 0.0;

    // Thread metrics
    public static volatile int activeRebuildThreads = 0;
    public static volatile int maxRebuildThreadsObserved = 0;

    // Reset per-tick counters (call from a client tick)
    public static void resetFrame() {
        skippedCompilesThisTick.set(0);
        compilesStartedThisTick.set(0);
        compilesFinishedThisTick.set(0);
        skippedLightOnlyThisTick.set(0);

        // Reset throttle counters
        compilesThisTick.set(0);
        skippedDueToThrottleThisTick.set(0);
    }

    // EMA helpers
    public static void emaUpdateAvgCompile(double ms, double alpha) {
        avgCompileMs = (alpha * ms) + (1.0 - alpha) * avgCompileMs;
    }

    public static void emaUpdateQueueDelay(double ms, double alpha) {
        avgQueueDelayMs = (alpha * ms) + (1.0 - alpha) * avgQueueDelayMs;
    }
}