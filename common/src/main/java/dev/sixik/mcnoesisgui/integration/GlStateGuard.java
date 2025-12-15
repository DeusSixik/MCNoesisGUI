package dev.sixik.mcnoesisgui.integration;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public final class GlStateGuard implements AutoCloseable {

    private final int prevVao;
    private final int prevProgram;
    private final int[] viewport = new int[4];

    private final boolean blend, depthTest, cull, stencil, framebufferSrgb;
    private final boolean depthMask;

    private final boolean[] colorMask = new boolean[4];

    private final int blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha;
    private final int blendEqRgb, blendEqAlpha;
    private final float[] blendColor = new float[4];

    private final int stencilFunc, stencilRef, stencilValueMask;
    private final int stencilFail, stencilZFail, stencilZPass, stencilWriteMask;

    private final int prevDrawFb;
    private final int prevReadFb;

    // FAST texture restore (active unit only)
    private final int prevActiveTexture;
    private final int prevTex2D;

    public GlStateGuard() {
        prevVao     = glGetInteger(GL_VERTEX_ARRAY_BINDING);
        prevProgram = glGetInteger(GL_CURRENT_PROGRAM);
        glGetIntegerv(GL_VIEWPORT, viewport);

        blend      = glIsEnabled(GL_BLEND);
        depthTest  = glIsEnabled(GL_DEPTH_TEST);
        cull       = glIsEnabled(GL_CULL_FACE);
        stencil    = glIsEnabled(GL_STENCIL_TEST);
        framebufferSrgb = glIsEnabled(GL_FRAMEBUFFER_SRGB);

        depthMask  = glGetBoolean(GL_DEPTH_WRITEMASK);

        // color mask
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buf = stack.malloc(4);
            glGetBooleanv(GL_COLOR_WRITEMASK, buf);
            colorMask[0] = buf.get(0) != 0;
            colorMask[1] = buf.get(1) != 0;
            colorMask[2] = buf.get(2) != 0;
            colorMask[3] = buf.get(3) != 0;
        }

        blendSrcRgb    = glGetInteger(GL_BLEND_SRC_RGB);
        blendDstRgb    = glGetInteger(GL_BLEND_DST_RGB);
        blendSrcAlpha  = glGetInteger(GL_BLEND_SRC_ALPHA);
        blendDstAlpha  = glGetInteger(GL_BLEND_DST_ALPHA);
        blendEqRgb     = glGetInteger(GL_BLEND_EQUATION_RGB);
        blendEqAlpha   = glGetInteger(GL_BLEND_EQUATION_ALPHA);

        glGetFloatv(GL_BLEND_COLOR, blendColor);

        stencilFunc    = glGetInteger(GL_STENCIL_FUNC);
        stencilRef     = glGetInteger(GL_STENCIL_REF);
        stencilValueMask = glGetInteger(GL_STENCIL_VALUE_MASK);
        stencilFail    = glGetInteger(GL_STENCIL_FAIL);
        stencilZFail   = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL);
        stencilZPass   = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS);
        stencilWriteMask = glGetInteger(GL_STENCIL_WRITEMASK);

        prevDrawFb = glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING);
        prevReadFb = glGetInteger(GL_READ_FRAMEBUFFER_BINDING);

        prevActiveTexture = glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        prevTex2D = glGetInteger(GL_TEXTURE_BINDING_2D);
    }

    @Override
    public void close() {
        glUseProgram(prevProgram);
        glBindVertexArray(prevVao);

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, prevDrawFb);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, prevReadFb);

        glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);

        setEnabled(GL_BLEND, blend);
        setEnabled(GL_DEPTH_TEST, depthTest);
        setEnabled(GL_CULL_FACE, cull);

        glDisable(GL_SCISSOR_TEST);

        glDepthMask(depthMask);
        glColorMask(colorMask[0], colorMask[1], colorMask[2], colorMask[3]);

        glBlendFuncSeparate(blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha);
        glBlendEquationSeparate(blendEqRgb, blendEqAlpha);
        glBlendColor(blendColor[0], blendColor[1], blendColor[2], blendColor[3]);

        setEnabled(GL_STENCIL_TEST, stencil);
        glStencilFunc(stencilFunc, stencilRef, stencilValueMask);
        glStencilOp(stencilFail, stencilZFail, stencilZPass);
        glStencilMask(stencilWriteMask);

        setEnabled(GL_FRAMEBUFFER_SRGB, framebufferSrgb);

        glActiveTexture(prevActiveTexture);
        glBindTexture(GL_TEXTURE_2D, prevTex2D);
    }

    private static void setEnabled(int cap, boolean enable) {
        if (enable) glEnable(cap); else glDisable(cap);
    }
}
