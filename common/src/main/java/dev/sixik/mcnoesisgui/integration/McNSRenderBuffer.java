package dev.sixik.mcnoesisgui.integration;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class McNSRenderBuffer {

    protected static final List<TextureTarget> ResizeListeners = new ArrayList<>();

    private static TextureTarget MainBuffer;

    public static TextureTarget createBuffer(int w, int h, boolean resizable) {
        final TextureTarget buffer = new TextureTarget(w, h, false, Minecraft.ON_OSX);

        if(resizable) {
            ResizeListeners.add(buffer);
        }

        return buffer;
    }

    public static TextureTarget createBufferWithDepth(int w, int h, boolean resizable) {
        final TextureTarget buffer = new TextureTarget(w, h, true, Minecraft.ON_OSX);

        if(resizable) {
            ResizeListeners.add(buffer);
        }

        return buffer;
    }

    public static void resizeBuffers() {
        final Window window = Minecraft.getInstance().getWindow();
        resizeBuffers(window.getWidth(), window.getHeight());
    }

    public static void resizeBuffers(int wight, int height) {
        synchronized (ResizeListeners) {
            for (TextureTarget resizeListener : ResizeListeners) {
                resizeListener.resize(wight, height, Minecraft.ON_OSX);
            }
        }
    }

    public static void destroyBuffers() {
        synchronized (ResizeListeners) {
            for (TextureTarget resizeListener : ResizeListeners) {
                resizeListener.destroyBuffers();
            }
        }
    }

    public static TextureTarget getMainRenderBuffer() {
        if(MainBuffer == null) {
            final Window window = Minecraft.getInstance().getWindow();
            MainBuffer = createBufferWithDepth(window.getWidth(), window.getHeight(), true);
        }

        return MainBuffer;
    }

    public static void destroyMainBuffer() {
        if(MainBuffer == null) return;

        synchronized (ResizeListeners) {
            ResizeListeners.remove(MainBuffer);
        }

        MainBuffer.destroyBuffers();
        MainBuffer = null;
    }


}
