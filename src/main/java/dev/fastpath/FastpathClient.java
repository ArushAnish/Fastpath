package dev.fastpath;

import net.fabricmc.api.ClientModInitializer;
import dev.fastpath.core.FPLog;
import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPProfiler;
import dev.fastpath.core.FPCompat;
import dev.fastpath.core.FPToggleBus;
import dev.fastpath.render.*;

public final class FastpathClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FPLog.info("Fastpath initializing...");
        FPConfig.load();
        FPCompat.detect();
        FPProfiler.install();
        FPBufferPool.init();
        FPFrustum.init();
        FPChunkScheduler.init();
        FPDrawBatcher.init();
        FPFrameBudget.init();
        FPOverlay.init();

        // Respect initial toggle; bind runtime switch
        FPToggleBus.onToggle(FPConfig.enabled, () -> {
            FPLog.info("Fastpath toggled " + (FPConfig.enabled ? "ON" : "OFF"));
            FPChunkScheduler.setEnabled(FPConfig.enabled);
            FPDrawBatcher.setEnabled(FPConfig.enabled);
            FPFrustum.setEnabled(FPConfig.enabled);
            FPFrameBudget.setEnabled(FPConfig.enabled);
            FPOverlay.setEnabled(FPConfig.enabled);
        });
    }
}