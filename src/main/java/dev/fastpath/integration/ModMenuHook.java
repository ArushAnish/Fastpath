package dev.fastpath.integration;

import dev.fastpath.core.FPToggleBus;
import dev.fastpath.core.FPConfig;

public final class ModMenuHook {
    public static void openMinimalScreen() {
        // Optional: provide a tiny screen with single toggle if Sodium GUI isn't present.
        FPToggleBus.setEnabled(!FPConfig.enabled);
    }
}