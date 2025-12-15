package dev.sixik.mcnoesisgui.mixin;

import com.mojang.authlib.GameProfile;
import dev.sixik.mcnoesisgui.demo.show_menu.ShowMenuDemo;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class DebugMixin extends AbstractClientPlayer {

    public DebugMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "drop", at = @At("HEAD"))
    public void bts$drop(boolean bl, CallbackInfoReturnable<Boolean> cir) {
//        ItemStack itemStack = this.getInventory().removeFromSelected(bl);
//        if(itemStack.is(Items.BEDROCK))
//            McNSClient.openScreen(McNSDebug.createDebug());
//        else McNSClient.closeScreen();

        ShowMenuDemo.openDemo();
    }
}
