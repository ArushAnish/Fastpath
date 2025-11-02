package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import dev.fastpath.core.FPStats;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void fastpath$renderOverlay(GuiGraphics gg, DeltaTracker dt, CallbackInfo ci) {
        if (!FPConfig.enabled || !FPConfig.overlayEnabled) return;

        Minecraft mc = Minecraft.getInstance();
        int y = 5;

        gg.drawString(mc.font, Component.literal("[Fastpath Active]").withStyle(s -> s.withColor(0x00FFFF)), 5, y, 0xFFFFFF, false);

        y += 12;
        gg.drawString(mc.font, Component.literal("Threads active/max: " +
                FPStats.activeRebuildThreads + " / " + FPStats.maxRebuildThreadsObserved), 5, y, 0xFFFFFF, false);

        y += 12;
        gg.drawString(mc.font, Component.literal("Skipped Empty: " +
                FPStats.skippedCompilesThisTick.get() + " (tick) / " + FPStats.skippedCompilesTotal.get() + " (total)"), 5, y, 0xFFFFFF, false);

        y += 12;
        gg.drawString(mc.font, Component.literal("Skipped Light-Only: " +
                FPStats.skippedLightOnlyThisTick.get() + " (tick) / " + FPStats.skippedLightOnlyTotal.get() + " (total)"), 5, y, 0xFFFFFF, false);

        if (FPConfig.hudShowAdvanced) {
            y += 12;
            gg.drawString(mc.font, Component.literal(String.format("Avg compile: %.2f ms", FPStats.avgCompileMs)), 5, y, 0xFFFFFF, false);
        }
    }
}