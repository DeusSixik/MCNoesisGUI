package dev.sixik.mcnoesisgui.loaders;

import dev.sixik.mcnoesisgui.McNoesisGui;
import dev.sixik.noesisgui_impl.loaders.NoesisXamlLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * @param defaultNamespace If uri has no "ns:", use this namespace (optional).
 * @param baseFolder       If not empty -> prefix path with this folder, e.g. "noesis".
 */
public record McNamespacedXamlLoader(String defaultNamespace, String baseFolder) implements NoesisXamlLoader {

    public McNamespacedXamlLoader(String defaultNamespace, String baseFolder) {
        this.defaultNamespace = defaultNamespace;
        this.baseFolder = (baseFolder == null) ? "" : trimSlashes(baseFolder);
    }

    @Override
    public byte[] loadXaml(String uri) {
        try {
            String u = normalizeUri(uri);

            // Parse "namespace:path"
            String ns;
            String path;
            int colon = u.indexOf(':');
            if (colon >= 0) {
                ns = u.substring(0, colon);
                path = u.substring(colon + 1);
            } else {
                ns = defaultNamespace;
                path = u;
            }

            if (ns == null || ns.isEmpty()) return null;

            path = trimSlashes(path);

            // Optional: keep old layout assets/<ns>/noesis/<path>
            if (!baseFolder.isEmpty()) {
                path = baseFolder + "/" + path;
            }

            // Minecraft resource paths must be lower-case; ensure you store accordingly
            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(ns, path);

            try (var in = Minecraft.getInstance().getResourceManager().open(rl)) {
                return in.readAllBytes();
            }
        } catch (Exception e) {
            McNoesisGui.LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private static String normalizeUri(String uri) {
        if (uri == null) return "";

        String s = uri.replace('\\', '/');

        // Strip Noesis/WPF-like pack prefix if it appears
        String pack = "pack://application:,,,/";
        if (s.startsWith(pack)) s = s.substring(pack.length());

        // Remove leading '/'
        while (s.startsWith("/")) s = s.substring(1);

        // Drop query/fragment (just in case)
        int q = s.indexOf('?');
        if (q >= 0) s = s.substring(0, q);
        int h = s.indexOf('#');
        if (h >= 0) s = s.substring(0, h);

        return s;
    }

    private static String trimSlashes(String s) {
        if (s == null) return "";
        while (s.startsWith("/")) s = s.substring(1);
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }
}
