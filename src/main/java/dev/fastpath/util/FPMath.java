package dev.fastpath.util;

public final class FPMath {
    public static void extractPlane(float[] planes, int off, float[] m, int rowW, int rowI) {
        planes[off]   = m[3]  + m[rowI];
        planes[off+1] = m[7]  + m[rowI+4];
        planes[off+2] = m[11] + m[rowI+8];
        planes[off+3] = m[15] + m[rowI+12];
    }

    public static void extractPlane(float[] planes, int off, float[] m, int rowW, int rowI, boolean negate) {
        extractPlane(planes, off, m, rowW, rowI);
        if (negate) { planes[off] *= -1; planes[off+1] *= -1; planes[off+2] *= -1; planes[off+3] *= -1; }
    }

    public static void normalizePlane(float[] planes, int off) {
        float a = planes[off], b = planes[off+1], c = planes[off+2], d = planes[off+3];
        float inv = (float)(1.0 / Math.sqrt(a*a + b*b + c*c));
        planes[off] = a * inv; planes[off+1] = b * inv; planes[off+2] = c * inv; planes[off+3] = d * inv;
    }
}