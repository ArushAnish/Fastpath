package dev.fastpath.util;

public final class FPEnv {
    public static boolean isOnLaptop() {
        String name = System.getProperty("os.name", "").toLowerCase();
        return name.contains("windows") || name.contains("mac"); // heuristic placeholder
    }
}