package dev.fastpath.core;

import dev.fastpath.util.FPStopwatch;

public final class FPProfiler {
    public static final FPStopwatch FRAME = new FPStopwatch();
    public static final FPStopwatch CHUNK_REBUILD = new FPStopwatch();
    public static final FPStopwatch DRAW = new FPStopwatch();

    public static void install() {
        // lightweight, no JFR dependency; used by overlay
    }
}