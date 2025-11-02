package dev.fastpath.render;

import dev.fastpath.core.FPConfig;
import dev.fastpath.util.FPMath;

public final class FPFrustum {
    private static volatile boolean enabled;
    private static final float[] planes = new float[24]; // 6 planes * 4 floats
    private static int revision;

    public static void init() { enabled = FPConfig.enabled; }
    public static void setEnabled(boolean e) { enabled = e; }

    public static void updateFromMatrix(float[] m) {
        // Extract planes from combined view-projection matrix (row-major assumption).
        // Left
        FPMath.extractPlane(planes, 0, m, 3, 0);
        // Right
        FPMath.extractPlane(planes, 4, m, 3, 0, true);
        // Bottom
        FPMath.extractPlane(planes, 8, m, 3, 1);
        // Top
        FPMath.extractPlane(planes, 12, m, 3, 1, true);
        // Near
        FPMath.extractPlane(planes, 16, m, 3, 2);
        // Far
        FPMath.extractPlane(planes, 20, m, 3, 2, true);

        for (int i = 0; i < 24; i += 4) FPMath.normalizePlane(planes, i);
        revision++;
    }

    public static boolean visibleAABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        if (!enabled) return true;
        for (int i = 0; i < 24; i += 4) {
            float a = planes[i], b = planes[i+1], c = planes[i+2], d = planes[i+3];
            // choose vertex the farthest along plane normal
            float x = a > 0 ? maxX : minX;
            float y = b > 0 ? maxY : minY;
            float z = c > 0 ? maxZ : minZ;
            if (a*x + b*y + c*z + d < 0) return false;
        }
        return true;
    }
}