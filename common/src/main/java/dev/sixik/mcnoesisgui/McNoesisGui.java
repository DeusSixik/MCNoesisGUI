package dev.sixik.mcnoesisgui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import dev.sixik.mcnoesisgui.integration.McNSClient;
import org.slf4j.Logger;

public final class McNoesisGui {
    public static final String MOD_ID = "mcnoesisgui";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static long NoesisRender_Time = 0;
    public static double NoesisRender_Time_MS = 0;

    public static void init() {
        RenderSystem.recordRenderCall(McNSClient::initialize);
    }
}
