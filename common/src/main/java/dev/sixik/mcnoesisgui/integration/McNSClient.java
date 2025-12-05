package dev.sixik.mcnoesisgui.integration;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.mcnoesisgui.wrappers.NSIViewWrapper;
import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSGui_MouseButton;
import dev.sixik.noesisgui.nsgui.NSGui_Visibility;
import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui.nsrenderer.NSRenderDevice;
import dev.sixik.noesisgui_impl.NSThemes;
import dev.sixik.noesisgui_ini.NoesisGuiJava;
import dev.sixik.noesisgui_render.gl.NSOpenGl;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.ARBVertexArrayObject.GL_VERTEX_ARRAY_BINDING;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class McNSClient {

    private static final Map<Long, NSIViewWrapper> ViewMap = new HashMap<>();

    private static NSIViewWrapper currentView;
    public static NSRenderDevice renderDevice;
    public static double appStartTime;

    public static void initialize() {
        if (NoesisGuiJava.IsLoaded()) return;

        NoesisGuiJava.Initialize();
        NoesisGui.init();

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

        try(GlStateGuard glStateGuard = new GlStateGuard()) {
            currentView.getView().getRenderer().render();
        }

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

    public static void mouseButtonDown(final int x, final int y, final int button) {
        if(!canRender()) return;
        currentView.getView().mouseButtonDown(x, y, NSGui_MouseButton.values()[button]);
    }

    public static void mouseButtonUp(final int x, final int y, final int button) {
        if(!canRender()) return;
        currentView.getView().mouseButtonUp(x, y, NSGui_MouseButton.values()[button]);
    }
}