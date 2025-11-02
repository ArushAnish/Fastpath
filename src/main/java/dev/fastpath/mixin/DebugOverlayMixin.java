package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugOverlayMixin {

    @Inject(method = "getGameInformation", at = @At("RETURN"), cancellable = true)
    private void addFastpathInfo(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = new ArrayList<>(cir.getReturnValue());

        // §b = cyan, §a = green, §c = red, §f = white
        list.add("§3Fastpath: " + (FPConfig.enabled ? "§aON" : "§cOFF"));
        list.add("§3Aggressive Culling: " + (FPConfig.aggressiveCulling ? "§aON" : "§cOFF"));
        list.add("§3Overlay: " + (FPConfig.overlayEnabled ? "§aON" : "§cOFF"));
        list.add("§3Max Rebuilds: §f" + FPConfig.maxConcurrentRebuilds);

        cir.setReturnValue(list);
    }
}