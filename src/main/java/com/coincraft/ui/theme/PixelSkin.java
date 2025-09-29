package com.coincraft.ui.theme;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

/**
 * CoinPixel - foundation utilities for crisp pixel-art UI in JavaFX.
 */
public final class PixelSkin {
    private PixelSkin() {}

    /**
     * Apply global pixel-art settings to the scene.
     * - Loads pixel.css (nearest-neighbor hints)
     * - Optionally load a pixel font if present (e.g., resources/fonts/pixel.ttf)
     */
    public static void apply(Scene scene) {
        try {
            var url = PixelSkin.class.getResource("/styles/pixel.css");
            if (url != null) {
                scene.getStylesheets().add(url.toExternalForm());
            }
        } catch (Exception ignored) {}

        // Load readable + pixel fonts for global use
        try {
            var pxf = PixelSkin.class.getResourceAsStream("/Fonts/Pixelify_Sans/PixelifySans-VariableFont_wght.ttf");
            if (pxf != null) { Font.loadFont(pxf, 12); }
        } catch (Exception ignored) {}
        try {
            var mc = PixelSkin.class.getResourceAsStream("/Fonts/minecraft/Minecraft.ttf");
            if (mc != null) { Font.loadFont(mc, 12); }
        } catch (Exception ignored) {}

        // Tag root for pixel styles
        try {
            scene.getRoot().getStyleClass().add("pixel-root");
        } catch (Exception ignored) {}
    }

    /** Ensure ImageView uses nearest-neighbor and keeps crispness. */
    public static void crisp(ImageView iv) {
        iv.setSmooth(false);
        iv.setPreserveRatio(true);
        iv.setCache(true);
    }
}


