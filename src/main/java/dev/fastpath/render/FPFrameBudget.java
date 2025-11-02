package dev.fastpath.render;

import dev.fastpath.core.FPConfig;

public final class FPFrameBudget {
    private static volatile boolean enabled;
    private static final double budgetMs = 3.2; // limit rebuild work per frame

    public static void init() { enabled = FPConfig.enabled; }
    public static void setEnabled(boolean e) { enabled = e; }

    public static boolean allowMoreWork(long frameStartNanos) {
        if (!enabled) return true;
        double elapsedMs = (System.nanoTime() - frameStartNanos) / 1_000_000.0;
        return elapsedMs < budgetMs;
    }
}