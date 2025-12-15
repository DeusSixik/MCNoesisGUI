package dev.sixik.mcnoesisgui.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.mcnoesisgui.integration.McNSClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {


    @Inject(method = "flipFrame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/Tesselator;clear()V", shift = At.Shift.AFTER))
    private static void bts$flipFrame(long l, CallbackInfo ci) {

    }
}
