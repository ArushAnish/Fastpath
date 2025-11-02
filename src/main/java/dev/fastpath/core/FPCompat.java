package dev.fastpath.core;

public final class FPCompat {
    public static boolean hasSodium;
    public static boolean hasModMenu;

    public static void detect() {
        try {
            Class.forName("me.jellysquid.mods.sodium.client.SodiumClientMod");
            hasSodium = true;
            FPLog.info("Sodium detected: integrating toggle.");
        } catch (Throwable ignored) {}

        try {
            Class.forName("com.terraformersmc.modmenu.ModMenu");
            hasModMenu = true;
        } catch (Throwable ignored) {}
    }
}