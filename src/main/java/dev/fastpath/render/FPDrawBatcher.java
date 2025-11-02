package dev.fastpath.render;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPProfiler;

import java.util.ArrayList;
import java.util.List;

public final class FPDrawBatcher {
    private static volatile boolean enabled;
    private static final List<DrawCmd> cmds = new ArrayList<>();

    private static final class DrawCmd {
        int vaoId; int materialId; int indexCount; int firstIndex;
    }

    public static void init() { enabled = FPConfig.enabled; }
    public static void setEnabled(boolean e) { enabled = e; }

    public static void beginFrame() {
        if (!enabled) return;
        cmds.clear();
    }

    public static void submit(int vaoId, int materialId, int firstIndex, int indexCount) {
        if (!enabled) return;
        DrawCmd c = new DrawCmd();
        c.vaoId = vaoId; c.materialId = materialId; c.firstIndex = firstIndex; c.indexCount = indexCount;
        cmds.add(c);
    }

    public static void flush() {
        if (!enabled) return;
        FPProfiler.DRAW.start();
        // In real code, sort by materialId, minimize binds, glMultiDrawElements if available.
        // Here we represent the batching layer; renderer mixin will call submit/flush around vanilla draws.
        FPProfiler.DRAW.stop();
    }
}