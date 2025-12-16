
package dev.sixik.mcnoesisgui;

import com.mojang.blaze3d.platform.Window;
import dev.sixik.mcnoesisgui.api.NoesisScreenWrapper;
import dev.sixik.mcnoesisgui.integration.McNSClient;
import dev.sixik.noesisgui.nsgui.NSIView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NoesisScreenWrapperImpl extends Screen implements NoesisScreenWrapper {

    private final NSIView view;
    private Window window;

    public NoesisScreenWrapperImpl(NSIView view) {
        super(Component.empty());
        this.view = view;
    }

    @Override
    public void onClose() {
        onScreenClose();
        super.onClose();
    }

    @Override
    protected void init() {
        super.init();
        onScreenInit();
        window = Minecraft.getInstance().getWindow();
        McNSClient.resize(window.getWidth(), window.getHeight());
    }

    @Override
    public void resize(Minecraft minecraft, int i, int j) {
        super.resize(minecraft, i, j);
        McNSClient.resize(i, j);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) { }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public NSIView getView() {
        return view;
    }
}
