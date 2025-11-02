package dev.fastpath.core;

import java.util.ArrayList;
import java.util.List;

public final class FPToggleBus {
    private static final List<Runnable> listeners = new ArrayList<>();

    public static void onToggle(boolean initial, Runnable listener) {
        listeners.add(listener);
        listener.run();
    }

    public static void setEnabled(boolean enabled) {
        if (FPConfig.enabled != enabled) {
            FPConfig.enabled = enabled;
            FPConfig.save();
            for (Runnable r : listeners) r.run();
        }
    }
}