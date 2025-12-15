package dev.sixik.mcnoesisgui.mixin;

import dev.sixik.mcnoesisgui.integration.McNSClient;
import dev.sixik.mcnoesisgui.integration.McNSRenderBuffer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft$InjectLoadLibrary {

    @Inject(method = "resizeDisplay", at = @At("RETURN"))
    public void bts$resizeDisplay(CallbackInfo ci) {
        McNSRenderBuffer.resizeBuffers();

        final var w = Minecraft.getInstance().getWindow();
        McNSClient.resize(w.getWidth(), w.getHeight());
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void bts$runTick$start_render(boolean bl, CallbackInfo ci) {
        McNSClient.noesisUpdate();
        McNSClient.noesisRenderOffscreen();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(Lnet/minecraft/client/DeltaTracker;Z)V", shift = At.Shift.AFTER))
    public void bts$runTick$start_render_2(boolean bl, CallbackInfo ci) {
        McNSClient.noesisRenderOnscreen();
    }

}
