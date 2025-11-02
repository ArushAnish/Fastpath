package dev.fastpath.mixin;

import dev.fastpath.core.FPConfig;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VisGraph.class)
public class VisGraphMixin {

    @Inject(method = "resolve", at = @At("RETURN"), cancellable = true)
    private void fastpath$aggressiveCulling(CallbackInfoReturnable<VisibilitySet> cir) {
        if (FPConfig.enabled && FPConfig.aggressiveCulling) {
            VisibilitySet set = cir.getReturnValue();
            // In your mappings, this is setAll(true) — mark all directions visible
            set.setAll(true);
            cir.setReturnValue(set);
        }
    }
}