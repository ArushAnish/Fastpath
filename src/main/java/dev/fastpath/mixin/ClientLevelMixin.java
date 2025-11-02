package dev.fastpath.mixin;

import dev.fastpath.core.FPStats;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void fastpath$resetCounters(CallbackInfo ci) {
        FPStats.resetFrame();
    }
}