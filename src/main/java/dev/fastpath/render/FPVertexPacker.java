package dev.fastpath.render;

import java.nio.ByteBuffer;

public final class FPVertexPacker {
    public static final int STRIDE = 20; // pos FP16x3 (6), uv FP16x2 (4), color RGBA8 (4), light packed (4), pad (2)

    public static void writeVertex(ByteBuffer buf, float x, float y, float z, float u, float v, int rgba, int light) {
        buf.putShort(fp16(x));
        buf.putShort(fp16(y));
        buf.putShort(fp16(z));
        buf.putShort(fp16(u));
        buf.putShort(fp16(v));
        buf.putInt(rgba);
        buf.putInt(light);
        buf.putShort((short)0);
    }

    private static short fp16(float f) {
        // Simplified float16 packing (round-to-zero). Replace with precise conversion if needed.
        int i = Float.floatToIntBits(f);
        int sign = (i >>> 16) & 0x8000;
        int mant = (i >> 12) & 0x03FF;
        int exp = ((i >>> 23) & 0xFF) - 112;
        if (exp <= 0) return (short)sign;
        if (exp >= 31) return (short)(sign | 0x7C00);
        return (short)(sign | (exp << 10) | mant);
    }
}