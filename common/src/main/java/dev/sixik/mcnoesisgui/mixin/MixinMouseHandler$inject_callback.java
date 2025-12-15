package dev.sixik.mcnoesisgui.mixin;

import dev.sixik.mcnoesisgui.integration.McNSClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.*;

@Mixin(MouseHandler.class)
public class MixinMouseHandler$inject_callback {

    @Unique
    private static final float NS$WHEEL_UNIT = 120.0f;


    @Shadow
    private double xpos;

    @Shadow
    private double ypos;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onMove", at = @At("HEAD"))
    public void bts$onMove(long l, double x, double y, CallbackInfo ci) {
        McNSClient.mouseMove((int) x, (int) y);
    }

    @Inject(method = "onPress", at = @At("HEAD"))
    public void bts$onPress(long win, int button, int action, int mods, CallbackInfo ci) {
        if (win != this.minecraft.getWindow().getWindow()) {
            return;
        }

        final double xpos[] = new double[1];
        final double ypos[] = new double[1];
        glfwGetCursorPos(win, xpos, ypos);
        final int x = (int) xpos[0];
        final int y = (int) ypos[0];

        if (action == GLFW_PRESS) {
            McNSClient.mouseButtonDown(x, y, button);
        } else if (action == GLFW_RELEASE) {
            McNSClient.mouseButtonUp(x, y, button);
        }
    }

    @Inject(method = "onScroll", at = @At("HEAD"))
    public void bts$onScroll(long w, double xoff, double yoff, CallbackInfo ci) {
        double[] cx = new double[1];
        double[] cy = new double[1];
        glfwGetCursorPos(w, cx, cy);

        int x = (int) Math.round(cx[0]);
        int y = (int) Math.round(cy[0]);

        int wheel = (int) Math.round(yoff * NS$WHEEL_UNIT);
        if (wheel != 0) {
            McNSClient.mouseWheel(x, y, wheel);
        }

        int hwheel = (int) Math.round(xoff * NS$WHEEL_UNIT);
        if (hwheel != 0) {
            McNSClient.mouseHWheel(x, y, hwheel);
        }
    }
}
