package com.coincraft.ui.theme;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

/**
 * 8-bit style audio manager for retro sound effects
 */
public class PixelAudio {
    private static final Map<String, AudioClip> sounds = new HashMap<>();
    private static boolean enabled = true;
    
    static {
        // Load retro sound effects if available
        loadSound("click", "/audio/click.wav");
        loadSound("coin", "/audio/coin.wav");
        loadSound("success", "/audio/success.wav");
        loadSound("error", "/audio/error.wav");
    }
    
    private static void loadSound(String name, String path) {
        try {
            var url = PixelAudio.class.getResource(path);
            if (url != null) {
                AudioClip clip = new AudioClip(url.toExternalForm());
                sounds.put(name, clip);
            }
        } catch (Exception ignored) {
            // Gracefully handle missing audio files
        }
    }
    
    public static void play(String soundName) {
        if (enabled && sounds.containsKey(soundName)) {
            try {
                sounds.get(soundName).play();
            } catch (Exception ignored) {}
        }
    }
    
    public static void setEnabled(boolean enabled) {
        PixelAudio.enabled = enabled;
    }
    
    public static boolean isEnabled() {
        return enabled;
    }
    
    // Quick access methods for common sounds
    public static void click() { play("click"); }
    public static void coin() { play("coin"); }
    public static void success() { play("success"); }
    public static void error() { play("error"); }
}
