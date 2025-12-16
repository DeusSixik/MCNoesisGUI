package dev.sixik.mcnoesisgui.api;

import dev.sixik.mcnoesisgui.NoesisScreenWrapperImpl;
import dev.sixik.mcnoesisgui.integration.McNSClient;
import dev.sixik.noesisgui.NoesisGui;
import dev.sixik.noesisgui.nsgui.NSFrameworkElement;
import dev.sixik.noesisgui.nsgui.NSIView;
import dev.sixik.noesisgui.nsrenderer.NSRenderDevice;

/**
 * Base class for a Noesis-backed screen.
 * <p>
 * Responsibilities: <br>
 *  - Build a root Noesis UI element (NSFrameworkElement) <br>
 *  - Create an NSIView for that element (NoesisGui.createView) <br>
 *  - Initialize the renderer with the Minecraft render device <br>
 *  - Cache the view so the UI is not recreated on every open
 * </p>
 */
public abstract class NoesisScreen {

    /** Cached view instance (reused between screen openings). */
    private NSIView cache;

    /**
     * Must be implemented by subclasses.
     * Create and return the root UI element that will be rendered inside the view.
     */
    protected abstract NSFrameworkElement createRenderElement();

    /**
     * Lifecycle hook: called right after createRenderElement() returns,
     * before the view is created. Good place to initialize the element,
     * attach handlers, set DataContext, etc.
     */
    protected void onCreatedRenderElement(final NSFrameworkElement element) { }

    /**
     * Lifecycle hook: called after the view is created but BEFORE renderer init().
     * Good place for view-level configuration that doesn't require a render device yet.
     */
    protected void onViewCreating(final NSIView view) { }

    /**
     * Lifecycle hook: called after renderer.init(renderDevice).
     * Good place for things that depend on an initialized renderer/device.
     */
    protected void onViewCreated(final NSIView view) { }

    /**
     * Hook called right before the wrapper is opened.
     * Useful for final wiring between wrapper/view/element (bindings, focus, sizing, etc.).
     */
    protected void preOpenScreen(final NoesisScreenWrapper wrapper, final NSIView view, final NSFrameworkElement element) { }

    /**
     * Factory method for the screen wrapper.
     * Subclasses may override to provide custom wrapper logic.
     *
     * @param view    The cached/created Noesis view.
     * @param element The view root content (same as view.getContent()).
     */
    protected NoesisScreenWrapper createScreen(final NSIView view, final NSFrameworkElement element) {
        return new NoesisScreenWrapperImpl(view);
    }

    /**
     * Opens the screen using the cached view.
     */
    public final NoesisScreenWrapper openScreen() {
        if(cache == null) createViewImpl();
        final var element = cache.getContent();
        final NoesisScreenWrapper wrapper = createScreen(cache, element);
        preOpenScreen(wrapper, cache, element);
        return wrapper.openScreen();
    }

    /**
     * Invalidates the cached view.
     * Next call to createViewImpl() will recreate the view and UI.
     */
    public final void invalidateData() {
        cache = null;
    }

    /**
     * Returns the render device used by Noesis renderer in Minecraft.
     * This is the bridge between Noesis and the actual graphics backend.
     */
    public final NSRenderDevice getRenderDevice() {
        return McNSClient.renderDevice;
    }

    /**
     * <p>
     * Internal helper: <br>
     *  - Returns cached view if available <br>
     *  - Otherwise creates a new view, runs lifecycle hooks, and initializes renderer
     *  </p>
     */
    private NSFrameworkElement createRenderImpl() {
        final NSFrameworkElement element = createRenderElement();
        onCreatedRenderElement(element);
        return element;
    }

    /**
     * <p>
     * Internal helper: <br>
     *  - Returns cached view if available <br>
     *  - Otherwise creates a new view, runs lifecycle hooks, and initializes renderer
     *  </p>
     */
    private NSIView createViewImpl() {
        if(cache != null) return cache;
        cache = NoesisGui.createView(createRenderImpl());
        onViewCreating(cache);
        cache.getRenderer().init(getRenderDevice());
        onViewCreated(cache);
        return cache;
    }
}
