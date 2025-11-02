package dev.fastpath.core;

import net.minecraft.core.SectionPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FPSectionCache {
    // Track per-section flags instead of hashes
    private static final Map<SectionPos, Boolean> geometryChangedMap = new ConcurrentHashMap<>();

    /**
     * Mark a section as having geometry changes (blockstate changed).
     */
    public static void markGeometryChanged(SectionPos pos) {
        geometryChangedMap.put(pos, true);
    }

    /**
     * Check if geometry has changed since last rebuild.
     */
    public static boolean hasGeometryChanged(SectionPos pos) {
        return geometryChangedMap.getOrDefault(pos, false);
    }

    /**
     * Clear the flag after a rebuild.
     */
    public static void clearGeometryChanged(SectionPos pos) {
        geometryChangedMap.put(pos, false);
    }
}