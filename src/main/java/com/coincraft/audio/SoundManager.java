package com.coincraft.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javafx.scene.media.AudioClip;

/**
 * Sound manager for CoinCraft application
 * Handles loading and playing sound effects
 */
public class SoundManager {
    private static final Logger LOGGER = Logger.getLogger(SoundManager.class.getName());
    private static SoundManager instance;
    
    private Map<String, AudioClip> soundCache;
    private boolean soundEnabled = true;
    private double volume = 0.5; // 50% volume by default
    
    private SoundManager() {
        soundCache = new HashMap<>();
        loadSounds();
    }
    
    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Load all sound effects into memory
     */
    private void loadSounds() {
        try {
            // Button click sounds
            loadSound("button_click", "/sounds/beep-313342.mp3");
            loadSound("button_hover", "/sounds/button_hover.wav");
            loadSound("input_click", "/sounds/clicking-interface-select-201946.mp3");
            loadSound("success", "/sounds/success.wav");
            loadSound("error", "/sounds/error.wav");
            loadSound("adventure_start", "/sounds/adventure_start.wav");
            
            LOGGER.info("Sound effects loaded successfully");
        } catch (Exception e) {
            LOGGER.warning("Could not load all sound effects: " + e.getMessage());
        }
    }
    
    /**
     * Load a single sound effect
     */
    private void loadSound(String name, String resourcePath) {
        try {
            // Try to load the sound file
            var resource = getClass().getResource(resourcePath);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toExternalForm());
                clip.setVolume(volume);
                soundCache.put(name, clip);
                LOGGER.info("Loaded sound: " + name);
            } else {
                // Create a silent placeholder if sound file doesn't exist
                LOGGER.warning("Sound file not found: " + resourcePath + " - creating silent placeholder");
                soundCache.put(name, null);
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load sound " + name + ": " + e.getMessage());
            soundCache.put(name, null); // Silent placeholder
        }
    }
    
    /**
     * Play a sound effect
     */
    public void playSound(String soundName) {
        if (!soundEnabled) return;
        
        try {
            AudioClip clip = soundCache.get(soundName);
            if (clip != null) {
                // Play the sound in a separate thread to avoid blocking UI
                new Thread(() -> {
                    try {
                        clip.play();
                    } catch (Exception e) {
                        LOGGER.warning("Error playing sound " + soundName + ": " + e.getMessage());
                    }
                }).start();
            }
        } catch (Exception e) {
            LOGGER.warning("Error playing sound " + soundName + ": " + e.getMessage());
        }
    }
    
    /**
     * Play button click sound
     */
    public void playButtonClick() {
        playSound("button_click");
    }
    
    /**
     * Play button hover sound
     */
    public void playButtonHover() {
        playSound("button_hover");
    }
    
    /**
     * Play input field click sound
     */
    public void playInputClick() {
        playSound("input_click");
    }
    
    /**
     * Play success sound
     */
    public void playSuccess() {
        playSound("success");
    }
    
    /**
     * Play error sound
     */
    public void playError() {
        playSound("error");
    }
    
    /**
     * Play adventure start sound
     */
    public void playAdventureStart() {
        playSound("adventure_start");
    }
    
    /**
     * Enable or disable sound effects
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        LOGGER.info("Sound effects " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Check if sound is enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Set volume level (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        
        // Update volume for all loaded sounds
        for (AudioClip clip : soundCache.values()) {
            if (clip != null) {
                clip.setVolume(this.volume);
            }
        }
        
        LOGGER.info("Volume set to: " + (this.volume * 100) + "%");
    }
    
    /**
     * Get current volume level
     */
    public double getVolume() {
        return volume;
    }
    
    /**
     * Stop all currently playing sounds
     */
    public void stopAllSounds() {
        for (AudioClip clip : soundCache.values()) {
            if (clip != null) {
                try {
                    clip.stop();
                } catch (Exception e) {
                    // Ignore errors when stopping sounds
                }
            }
        }
    }
    
    /**
     * Cleanup resources
     */
    public void shutdown() {
        stopAllSounds();
        soundCache.clear();
        LOGGER.info("SoundManager shutdown complete");
    }
}
