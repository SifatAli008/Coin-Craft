package com.coincraft.ui.util;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.text.Font;

/**
 * Fonts utility to ensure custom fonts are loaded once per JVM.
 */
public final class Fonts {
    private static final AtomicBoolean loaded = new AtomicBoolean(false);

    private Fonts() {}

    /** Load all required fonts once. Safe to call multiple times. */
    public static void ensureLoaded() {
        if (loaded.get()) {
            return;
        }
        try {
            var mc = Fonts.class.getResourceAsStream("/Fonts/minecraft/Minecraft.ttf");
            if (mc != null) {
                // Load typical sizes once so JavaFX can derive others efficiently
                Font.loadFont(mc, 14);
            }
        } catch (Exception ignored) {}

        try {
            var px = Fonts.class.getResourceAsStream("/Fonts/Pixelify_Sans/PixelifySans-VariableFont_wght.ttf");
            if (px != null) {
                Font.loadFont(px, 12);
            }
        } catch (Exception ignored) {}

        loaded.set(true);
    }
}


