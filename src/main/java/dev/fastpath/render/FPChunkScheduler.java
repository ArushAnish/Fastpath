package dev.fastpath.render;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPLog;
import dev.fastpath.core.FPProfiler;
import dev.fastpath.util.FPCollections;

import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FPChunkScheduler {
    private static volatile boolean enabled;
    private static final PriorityBlockingQueue<Task> queue =
            new PriorityBlockingQueue<>(1024, Comparator.comparingDouble(t -> t.priority));
    private static ExecutorService pool;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    public static final class Task {
        public final Object renderRegion; // opaque to keep compat
        public final double priority;
        public Task(Object region, double prio) { this.renderRegion = region; this.priority = prio; }
    }

    public static void init() {
        enabled = FPConfig.enabled;
        int threads = FPConfig.maxConcurrentRebuilds;
        pool = Executors.newFixedThreadPool(threads, r -> {
            Thread t = new Thread(r, "Fastpath-Rebuild");
            t.setDaemon(true);
            return t;
        });
        running.set(true);
        for (int i = 0; i < threads; i++) {
            pool.submit(FPChunkScheduler::worker);
        }
        FPLog.info("Chunk scheduler online with " + threads + " threads.");
    }

    public static void setEnabled(boolean e) { enabled = e; }

    public static void enqueue(Object region, double distance, boolean likelyVisible) {
        if (!enabled) return;
        double prio = (likelyVisible ? 0.0 : 100.0) + distance;
        queue.offer(new Task(region, prio));
    }

    private static void worker() {
        while (running.get()) {
            try {
                Task t = queue.take();
                long frameStart = System.nanoTime();
                if (!FPFrameBudget.allowMoreWork(frameStart)) {
                    // Defer; reinsert with slight penalty to avoid starvation
                    queue.offer(new Task(t.renderRegion, t.priority + 1.0));
                    Thread.onSpinWait();
                    continue;
                }
                FPProfiler.CHUNK_REBUILD.start();
                // Call vanilla rebuild via reflection to avoid hard coupling; safe if vanilla changes.
                performRebuild(t.renderRegion);
                FPProfiler.CHUNK_REBUILD.stop();
            } catch (InterruptedException ignored) {
            } catch (Throwable th) {
                FPLog.warn("Rebuild task failed: " + th);
            }
        }
    }

    private static void performRebuild(Object renderRegion) {
        // This placeholder should call the game's chunk rebuild method.
        // In practice, the mixin will redirect schedule() to enqueue(),
        // and our worker should invoke the original schedule path or direct builder call via saved handle.
    }

    public static void shutdown() {
        running.set(false);
        FPCollections.drain(queue);
        pool.shutdownNow();
    }
}