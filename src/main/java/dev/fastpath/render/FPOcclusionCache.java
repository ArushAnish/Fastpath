package dev.fastpath.render;

import java.util.concurrent.ConcurrentHashMap;

public final class FPOcclusionCache {
    private static final ConcurrentHashMap<Long, Byte> cache = new ConcurrentHashMap<>();

    public static long key(int cx, int cz, int layer) {
        return ((((long)cx) & 0xFFFF_FFFFL) << 24) | ((((long)cz) & 0xFFFF_FFFFL) << 0) | ((long)(layer & 0xFF) << 48);
    }

    public static void setVisible(int cx, int cz, int layer, boolean visible) {
        cache.put(key(cx, cz, layer), (byte)(visible ? 1 : 0));
    }

    public static boolean isVisible(int cx, int cz, int layer) {
        Byte b = cache.get(key(cx, cz, layer));
        return b == null || b == 1;
    }

    public static void clear() { cache.clear(); }
}