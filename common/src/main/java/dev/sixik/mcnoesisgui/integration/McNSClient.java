package dev.sixik.mcnoesisgui.integration;

import dev.sixik.mcnoesisgui.McNoesisGui;
import dev.sixik.mcnoesisgui.loaders.McNamespacedXamlLoader;
import dev.sixik.mcnoesisgui.wrappers.NSIViewWrapper;
import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSGui_Key;
import dev.sixik.noesisgui.nsgui.NSGui_MouseButton;
import dev.sixik.noesisgui.nsgui.NSGui_Visibility;
import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui.nsrenderer.NSRenderDevice;
import dev.sixik.noesisgui_impl.NSThemes;
import dev.sixik.noesisgui_ini.NoesisGuiJava;
import dev.sixik.noesisgui_render.gl.NSOpenGl;
import dev.sixik.noesisgui_render.lwgl.NoesisGlfwKeyMap;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL11.*;

public class McNSClient {

    private static final Map<Long, NSIViewWrapper> ViewMap = new HashMap<>();

    private static NSIViewWrapper currentView;
    public static NSRenderDevice renderDevice;
    public static double appStartTime;

    public static void initialize() {
        if (NoesisGuiJava.IsLoaded()) return;

        NoesisGuiJava.Initialize();

        NoesisGui.setLogHandler((file, line, level, channel, message) -> {
            switch (NoesisGui.getLogLevelName((int) level)) {
                case "TRACE" ->   McNoesisGui.LOGGER.trace("[NOESIS] {}:{} {} - {}", file, line, channel, message);
                case "DEBUG" ->   McNoesisGui.LOGGER.debug("[NOESIS] {}:{} {} - {}", file, line, channel, message);
                case "INFO" ->    McNoesisGui.LOGGER.info( "[NOESIS] {}:{} {} - {}", file, line, channel, message);
                case "WARNING" -> McNoesisGui.LOGGER.warn( "[NOESIS] {}:{} {} - {}", file, line, channel, message);
                case "ERROR" ->   McNoesisGui.LOGGER.error("[NOESIS] {}:{} {} - {}", file, line, channel, message);
            }
        });

        NoesisGui.init();

        NoesisGui.setXamlProvider(new McNamespacedXamlLoader("mcnoesisgui", ""));
        NoesisGui.setThemeProviders();
        NoesisGui.loadApplicationResources(NSThemes.darkBlue());

        renderDevice = NSOpenGl.createDevice(false);
        appStartTime = glfwGetTime();
    }

    public static void openScreen(final NSIView view) {
        if (currentView != null && currentView.getView() == view) return;


        closeScreen();

        final NSIViewWrapper find = ViewMap.get(view.getPtr());
        if(find == null) {
            currentView = new NSIViewWrapper(view);

            ViewMap.put(view.getPtr(), currentView);
        } else {
            currentView = find;
        }

        currentView.getView().getContent().setVisibility(NSGui_Visibility.Visible);
    }

    public static void closeScreen() {
        if (currentView == null) return;
        currentView.getView().getContent().setVisibility(NSGui_Visibility.Hidden);
        currentView = null;
    }

    public static boolean canRender() {
        return currentView != null;
    }

    public static void noesisUpdate() {
        noesisUpdate(glfwGetTime() - appStartTime);
    }

    public static void noesisUpdate(double timeSeconds) {
        if(!canRender()) return;
        currentView.getView().update(timeSeconds);
    }

    public static void noesisRenderOffscreen() {
        if(!canRender()) return;
        currentView.getView().getRenderer().updateRenderTree();
        currentView.getView().getRenderer().renderOffscreen();
    }

    public static void noesisRenderOnscreen() {
        if (!canRender()) return;

        long t0 = System.nanoTime();
        try(GlStateGuard glStateGuard = new GlStateGuard()) {
            currentView.getView().getRenderer().render();
        }

        final long time = System.nanoTime() - t0;
        McNoesisGui.NoesisRender_Time = time;
        McNoesisGui.NoesisRender_Time_MS = (time / 1e6);

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.err.println("GL error after Noesis render: " + error);
        }
    }

    public static void resize(int w, int h) {
        if (!canRender()) return;
        currentView.getView().setSize(w, h);
    }

    public static void mouseMove(final int x, final int y) {
        if(!canRender()) return;
        currentView.getView().mouseMove(x, y);
    }

    public static void mouseWheel(int x, int y, int wheel) {
        if(!canRender()) return;
        currentView.getView().mouseWheel(x, y, wheel);
    }

    public static void mouseHWheel(int x, int y, int wheel) {
        if(!canRender()) return;
        currentView.getView().mouseHWheel(x, y, wheel);
    }

    public static void mouseButtonDown(final int x, final int y, final int button) {
        if(!canRender()) return;
        final var t = NSGui_MouseButton.values()[button];
        currentView.getView().mouseButtonDown(x, y, t);
    }

    public static void mouseButtonUp(final int x, final int y, final int button) {
        if(!canRender()) return;
        currentView.getView().mouseButtonUp(x, y, NSGui_MouseButton.values()[button]);
    }

    public static void keyDown(final NSGui_Key key) {
        if(!canRender()) return;
        currentView.getView().keyDown(key);
    }

    public static void keyUp(final NSGui_Key key) {
        if(!canRender()) return;
        currentView.getView().keyUp(key);
    }

    public static void keyHandler(final int key, final int action) {
        if(!canRender()) return;
        final var noesis_key = NoesisGlfwKeyMap.toNoesisKey(key);
        final var _view = currentView.getView();

        if (action == GLFW_PRESS) {
            _view.keyDown(noesis_key);
        } else if (action == GLFW_RELEASE) {
            _view.keyUp(noesis_key);
        } else if (action == GLFW_REPEAT) {
            _view.keyDown(noesis_key);
        }
    }

    public static void charEvent(final int codepoint) {
        if(!canRender()) return;
        currentView.getView().charEvent(codepoint);
    }
}