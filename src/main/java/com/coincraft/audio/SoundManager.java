package com.coincraft.audio;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Sound manager for CoinCraft application
 * Handles loading and playing sound effects
 */
public class SoundManager {
    private static final Logger LOGGER = Logger.getLogger(SoundManager.class.getName());
    private static SoundManager instance;
    
    private Map<String, AudioClip> soundCache;
    private MediaPlayer backgroundMusicPlayer;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private double volume = 0.5; // 50% volume by default
    private double musicVolume = 0.3; // 30% volume for background music
    
    private SoundManager() {
        soundCache = new HashMap<>();
        loadSounds();
        loadBackgroundMusic();
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
     * Load background music
     */
    private void loadBackgroundMusic() {
        try {
            var resource = getClass().getResource("/sounds/adventure-319767.mp3");
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setVolume(musicVolume);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                LOGGER.info("Background music loaded: adventure-319767.mp3");
            } else {
                LOGGER.warning("Background music file not found: /sounds/adventure-319767.mp3");
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to load background music: " + e.getMessage());
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
     * Start background music (ensures single instance)
     */
    public void startBackgroundMusic() {
        if (backgroundMusicPlayer != null && musicEnabled) {
            try {
                // Always stop any existing music first to prevent overlaps
                if (backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING ||
                    backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    backgroundMusicPlayer.stop();
                    LOGGER.info("Stopped existing background music to prevent overlap");
                }
                
                // Start fresh music instance
                backgroundMusicPlayer.play();
                LOGGER.info("Background music started (single instance)");
            } catch (Exception e) {
                LOGGER.warning("Error starting background music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Stop background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            try {
                backgroundMusicPlayer.stop();
                LOGGER.info("Background music stopped");
            } catch (Exception e) {
                LOGGER.warning("Error stopping background music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Pause background music
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            try {
                backgroundMusicPlayer.pause();
                LOGGER.info("Background music paused");
            } catch (Exception e) {
                LOGGER.warning("Error pausing background music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Resume background music (ensures single instance)
     */
    public void resumeBackgroundMusic() {
        if (backgroundMusicPlayer != null && musicEnabled) {
            try {
                // Always stop any existing music first to prevent overlaps
                if (backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING ||
                    backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    backgroundMusicPlayer.stop();
                    LOGGER.info("Stopped existing background music to prevent overlap");
                }
                
                // Start fresh music instance
                backgroundMusicPlayer.play();
                LOGGER.info("Background music resumed (single instance)");
            } catch (Exception e) {
                LOGGER.warning("Error resuming background music: " + e.getMessage());
            }
        }
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
     * Enable or disable background music
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (enabled) {
            startBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
        LOGGER.info("Background music " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Check if music is enabled
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    /**
     * Check if background music is currently playing
     */
    public boolean isMusicPlaying() {
        return backgroundMusicPlayer != null && 
               backgroundMusicPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    /**
     * Ensure single music instance - stop all and start fresh
     */
    public void ensureSingleMusicInstance() {
        if (backgroundMusicPlayer != null) {
            try {
                // Force stop any existing music
                backgroundMusicPlayer.stop();
                LOGGER.info("Forced stop of background music to ensure single instance");
                
                // Small delay to ensure stop is processed
                Thread.sleep(100);
                
                // Start fresh if music is enabled
                if (musicEnabled) {
                    backgroundMusicPlayer.play();
                    LOGGER.info("Started single background music instance");
                }
            } catch (Exception e) {
                LOGGER.warning("Error ensuring single music instance: " + e.getMessage());
            }
        }
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
     * Set music volume level (0.0 to 1.0)
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        
        // Update volume for background music player
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(this.musicVolume);
        }
        
        LOGGER.info("Music volume set to: " + (this.musicVolume * 100) + "%");
    }
    
    /**
     * Get current music volume level
     */
    public double getMusicVolume() {
        return musicVolume;
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
        stopBackgroundMusic();
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
        }
        soundCache.clear();
        LOGGER.info("SoundManager shutdown complete");
    }
}
