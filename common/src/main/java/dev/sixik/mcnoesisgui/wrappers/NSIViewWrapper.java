package dev.sixik.mcnoesisgui.wrappers;

import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui.nsrenderer.NSRenderDevice;
import dev.sixik.noesisgui_render.gl.NSOpenGl;

public class NSIViewWrapper {

    private boolean isInitialized = false;
    private final NSIView view;

    public NSIViewWrapper(NSIView view) {
        this.view = view;
    }

    public void initialize(final NSRenderDevice renderDevice) {
        if(!isInitialized) {

            isInitialized = true;
        }
    }

    public NSIView getView() {
        return view;
    }
}
