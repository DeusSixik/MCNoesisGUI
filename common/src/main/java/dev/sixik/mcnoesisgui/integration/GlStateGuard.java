package dev.sixik.mcnoesisgui.integration;

import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public final class GlStateGuard implements AutoCloseable {

    private static final int MAX_TEXTURE_UNITS = 16;

    private final int prevVao;
    private final int prevProgram;
    private final int[] viewport = new int[4];

    private final boolean blend;
    private final boolean depthTest;
    private final boolean scissor;
    private final boolean cull;
    private final boolean depthMask;
    private final boolean[] colorMask = new boolean[4];

    private final int blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha;
    private final int blendEqRgb, blendEqAlpha;

    private final boolean stencil;
    private final int stencilFunc, stencilRef, stencilValueMask;
    private final int stencilFail, stencilZFail, stencilZPass, stencilWriteMask;

    private final int prevFb;

    private final int prevActiveTexture;
    private final int[] textureBindings2D = new int[MAX_TEXTURE_UNITS];

    public GlStateGuard() {
        prevVao     = glGetInteger(GL_VERTEX_ARRAY_BINDING);
        prevProgram = glGetInteger(GL_CURRENT_PROGRAM);
        glGetIntegerv(GL_VIEWPORT, viewport);

        blend      = glIsEnabled(GL_BLEND);
        depthTest  = glIsEnabled(GL_DEPTH_TEST);
        scissor    = glIsEnabled(GL_SCISSOR_TEST);
        cull       = glIsEnabled(GL_CULL_FACE);
        depthMask  = glGetBoolean(GL_DEPTH_WRITEMASK);

        // color mask RGBA
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

        stencil        = glIsEnabled(GL_STENCIL_TEST);
        stencilFunc    = glGetInteger(GL_STENCIL_FUNC);
        stencilRef     = glGetInteger(GL_STENCIL_REF);
        stencilValueMask = glGetInteger(GL_STENCIL_VALUE_MASK);
        stencilFail    = glGetInteger(GL_STENCIL_FAIL);
        stencilZFail   = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL);
        stencilZPass   = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS);
        stencilWriteMask = glGetInteger(GL_STENCIL_WRITEMASK);

        // save FB binding
        prevFb = glGetInteger(GL_FRAMEBUFFER_BINDING);

        // save texture states
        prevActiveTexture = glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        for (int unit = 0; unit < MAX_TEXTURE_UNITS; unit++) {
            glActiveTexture(GL13.GL_TEXTURE0 + unit);
            textureBindings2D[unit] = glGetInteger(GL_TEXTURE_BINDING_2D);
        }
        glActiveTexture(prevActiveTexture); // Восстанавливаем active сразу, чтобы не сломать конструктор
    }

    @Override
    public void close() {
        // Programs & VAO
        glUseProgram(prevProgram);
        glBindVertexArray(prevVao);

        // Viewport
        glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);

        // Enable/disable flags
        setEnabled(GL_BLEND, blend);
        setEnabled(GL_DEPTH_TEST, depthTest);
        setEnabled(GL_SCISSOR_TEST, scissor);
        setEnabled(GL_CULL_FACE, cull);

        glDepthMask(depthMask);

        glColorMask(colorMask[0], colorMask[1], colorMask[2], colorMask[3]);

        glBlendFuncSeparate(blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha);
        glBlendEquationSeparate(blendEqRgb, blendEqAlpha);

        setEnabled(GL_STENCIL_TEST, stencil);
        glStencilFunc(stencilFunc, stencilRef, stencilValueMask);
        glStencilOp(stencilFail, stencilZFail, stencilZPass);
        glStencilMask(stencilWriteMask);

        // restore FB binding
        glBindFramebuffer(GL_FRAMEBUFFER, prevFb);

        // restore texture states
        glActiveTexture(prevActiveTexture);
        for (int unit = MAX_TEXTURE_UNITS - 1; unit >= 0; unit--) {
            glActiveTexture(GL13.GL_TEXTURE0 + unit);
            glBindTexture(GL_TEXTURE_2D, textureBindings2D[unit]);
        }
        glActiveTexture(prevActiveTexture);
    }

    private static void setEnabled(int cap, boolean enable) {
        if (enable) glEnable(cap); else glDisable(cap);
    }
}