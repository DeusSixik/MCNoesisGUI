package dev.sixik.mcnoesisgui.mixin;

import dev.sixik.mcnoesisgui.integration.McNSClient;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler$inject_callback {

    @Inject(method = "keyPress", at = @At("HEAD"))
    public void NSGui$keyPress(long w, int key, int scancode, int action, int mods, CallbackInfo ci) {
        McNSClient.keyHandler(key, action);
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    public void NSGui$charTyped(long w, int codepoint, int mods, CallbackInfo ci) {
        McNSClient.charEvent(codepoint);
    }
}
