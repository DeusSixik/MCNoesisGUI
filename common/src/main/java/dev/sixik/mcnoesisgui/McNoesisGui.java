package dev.sixik.mcnoesisgui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.mcnoesisgui.integration.McNSClient;

public final class McNoesisGui {
    public static final String MOD_ID = "mcnoesisgui";

    public static void init() {
        RenderSystem.recordRenderCall(McNSClient::initialize);
    }
}
