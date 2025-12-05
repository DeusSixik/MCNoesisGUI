package dev.sixik.mcnoesisgui.fabric.client;

import dev.sixik.mcnoesisgui.McNoesisGui;
import net.fabricmc.api.ClientModInitializer;

public final class McNoesisGuiFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        McNoesisGui.init();
    }
}
