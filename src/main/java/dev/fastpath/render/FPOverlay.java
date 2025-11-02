package dev.fastpath.render;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPProfiler;

public final class FPOverlay {
    private static volatile boolean enabled;

    public static void init() { enabled = FPConfig.overlayEnabled; }
    public static void setEnabled(boolean e) { enabled = e; }

    public static void render() {
        if (!enabled || !FPConfig.enabled) return;
        // Minimal overlay: draw text via existing debug HUD APIs if available.
        // Intentionally lightweight to avoid heavy GUI impact.
        // Example: display FPS, frame time (FPProfiler.FRAME), queue size, draw calls.
    }
}