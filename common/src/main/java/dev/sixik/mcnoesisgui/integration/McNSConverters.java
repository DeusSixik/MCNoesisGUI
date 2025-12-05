package dev.sixik.mcnoesisgui.integration;

import dev.sixik.noesisgui.nsmath.NSMatrix4;
import org.joml.Matrix4f;

public class McNSConverters {

    public static NSMatrix4 convert(final Matrix4f mat) {
        return new NSMatrix4(
                mat.m00(),
                mat.m01(),
                mat.m02(),
                mat.m03(),
                mat.m10(),
                mat.m11(),
                mat.m12(),
                mat.m13(),
                mat.m20(),
                mat.m21(),
                mat.m22(),
                mat.m23(),
                mat.m30(),
                mat.m31(),
                mat.m32(),
                mat.m33()
        );
    }
}
