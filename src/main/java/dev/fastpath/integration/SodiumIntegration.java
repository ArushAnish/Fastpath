package dev.fastpath.integration;

import dev.fastpath.core.FPCompat;
import dev.fastpath.core.FPToggleBus;
import dev.fastpath.core.FPConfig;

public final class SodiumIntegration {
    public static void tryAddToggle() {
        if (!FPCompat.hasSodium) return;
        // If mixin fails, this fallback can be called from ModMenu or another UI to expose toggle.
        FPToggleBus.setEnabled(FPConfig.enabled);
    }
}