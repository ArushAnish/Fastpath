package dev.fastpath.render;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class FPBufferPool {
    private static final int DEFAULT_SIZE = 1 << 20; // 1MB per buffer
    private static final ThreadLocal<ArrayDeque<ByteBuffer>> TL = ThreadLocal.withInitial(ArrayDeque::new);

    public static void init() {}

    public static ByteBuffer acquire(int min) {
        ArrayDeque<ByteBuffer> q = TL.get();
        ByteBuffer buf = q.pollFirst();
        if (buf == null || buf.capacity() < min) {
            if (buf != null) memFree(buf);
            buf = memAlloc(Math.max(DEFAULT_SIZE, min));
        }
        buf.clear();
        return buf;
    }

    public static void release(ByteBuffer buf) {
        if (buf == null) return;
        TL.get().offerFirst(buf);
    }

    public static void trim() {
        ArrayDeque<ByteBuffer> q = TL.get();
        while (q.size() > 2) {
            memFree(q.pollLast());
        }
    }
}