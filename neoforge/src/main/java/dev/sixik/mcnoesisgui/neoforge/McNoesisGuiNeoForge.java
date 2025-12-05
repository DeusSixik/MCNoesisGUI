package dev.sixik.mcnoesisgui.neoforge;

import dev.sixik.mcnoesisgui.McNoesisGui;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforgespi.Environment;

@Mod(McNoesisGui.MOD_ID)
public final class McNoesisGuiNeoForge {
    public McNoesisGuiNeoForge() {
        // Run our common setup.
        if(Environment.get().getDist().isClient()) {
            McNoesisGui.init();
        }
    }
}
