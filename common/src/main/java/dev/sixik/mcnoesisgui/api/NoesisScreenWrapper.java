package dev.sixik.mcnoesisgui.api;

import dev.sixik.mcnoesisgui.integration.McNSClient;
import dev.sixik.noesisgui.nsgui.NSIView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface NoesisScreenWrapper {

    NSIView getView();

    default NoesisScreenWrapper openScreen() {
        if(this instanceof Screen screen) {
            Minecraft.getInstance().setScreen(screen);
        }

        return this;
    }

    default void onScreenInit() {
        McNSClient.openScreen(getView());
    }

    default void onScreenClose() {
        McNSClient.closeScreen();
    }
}
