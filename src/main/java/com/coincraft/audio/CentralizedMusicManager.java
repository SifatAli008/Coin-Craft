package com.coincraft.audio;

import java.util.logging.Logger;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Centralized Music Manager for CoinCraft
 * Provides a single point of control for all music functionality
 */
public class CentralizedMusicManager {
    private static final Logger LOGGER = Logger.getLogger(CentralizedMusicManager.class.getName());
    private static CentralizedMusicManager instance;
    
    private MediaPlayer musicPlayer;
    private boolean isPlaying = false;
    private boolean isMuted = false;
    private double volume = 0.5; // 50% volume by default
    private String currentTrack = null;
    
    // Sound effects
    private AudioClip sfxButtonClick;
    private AudioClip sfxInputSelect;
    
    // Music state listeners
    private MusicStateListener stateListener;
    
    public interface MusicStateListener {
        void onMusicStateChanged(boolean isPlaying, boolean isMuted, double volume);
        void onTrackChanged(String trackName);
    }
    
    private CentralizedMusicManager() {
        LOGGER.info("🎵 Initializing Centralized Music Manager");
        loadDefaultTrack();
    }
    
    public static synchronized CentralizedMusicManager getInstance() {
        if (instance == null) {
            instance = new CentralizedMusicManager();
        }
        return instance;
    }
    
    /**
     * Load the default background music track
     */
    private void loadDefaultTrack() {
        try {
            var resource = getClass().getResource("/sounds/adventure-319767.mp3");
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setVolume(volume);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                currentTrack = "adventure-319767.mp3";
                
                // Set up event handlers
                musicPlayer.setOnReady(() -> {
                    LOGGER.info("🎵 Music player ready - " + currentTrack);
                });
                
                musicPlayer.setOnPlaying(() -> {
                    isPlaying = true;
                    notifyStateChanged();
                    LOGGER.info("🎵 Music started playing");
                });
                
                musicPlayer.setOnPaused(() -> {
                    isPlaying = false;
                    notifyStateChanged();
                    LOGGER.info("🎵 Music paused");
                });
                
                musicPlayer.setOnStopped(() -> {
                    isPlaying = false;
                    notifyStateChanged();
                    LOGGER.info("🎵 Music stopped");
                });
                
                musicPlayer.setOnError(() -> {
                    LOGGER.severe("🎵 Music player error: " + musicPlayer.getError().getMessage());
                });
                
                LOGGER.info("🎵 Default track loaded: " + currentTrack);
            } else {
                LOGGER.warning("🎵 Default music file not found: /sounds/adventure-319767.mp3");
            }
        } catch (Exception e) {
            LOGGER.severe("🎵 Failed to load default music track: " + e.getMessage());
        }
    }

    // ============================
    // Sound Effects (SFX)
    // ============================
    /**
     * Play the global button click sound effect.
     * Uses /sounds/clicking-interface-select-201946.mp3
     */
    public void playButtonClick() {
        try {
            ensureButtonClickClipLoaded();
            playClipIfAllowed(sfxButtonClick);
        } catch (Exception e) {
            LOGGER.warning("🔊 Failed to play button click: " + e.getMessage());
        }
    }

    /**
     * Play the global input selection sound effect.
     * Uses /sounds/beep-313342.mp3
     */
    public void playInputSelect() {
        try {
            ensureInputSelectClipLoaded();
            playClipIfAllowed(sfxInputSelect);
        } catch (Exception e) {
            LOGGER.warning("🔊 Failed to play input select: " + e.getMessage());
        }
    }

    private void ensureButtonClickClipLoaded() {
        if (sfxButtonClick == null) {
            var resource = getClass().getResource("/sounds/clicking-interface-select-201946.mp3");
            if (resource != null) {
                sfxButtonClick = new AudioClip(resource.toExternalForm());
            } else {
                LOGGER.warning("🔊 Button click SFX not found: /sounds/clicking-interface-select-201946.mp3");
            }
        }
    }

    private void ensureInputSelectClipLoaded() {
        if (sfxInputSelect == null) {
            var resource = getClass().getResource("/sounds/beep-313342.mp3");
            if (resource != null) {
                sfxInputSelect = new AudioClip(resource.toExternalForm());
            } else {
                LOGGER.warning("🔊 Input select SFX not found: /sounds/beep-313342.mp3");
            }
        }
    }

    private void playClipIfAllowed(AudioClip clip) {
        if (clip == null) {
            return;
        }
        if (isMuted) {
            return; // respect mute state for SFX as well
        }
        try {
            clip.setVolume(Math.max(0.0, Math.min(1.0, volume)));
            clip.play();
        } catch (Exception e) {
            LOGGER.warning("🔊 Error playing SFX: " + e.getMessage());
        }
    }
    
    /**
     * Start playing music
     */
    public void play() {
        if (musicPlayer != null && !isPlaying) {
            try {
                musicPlayer.play();
                LOGGER.info("🎵 Music playback started");
            } catch (Exception e) {
                LOGGER.warning("🎵 Error starting music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Pause music
     */
    public void pause() {
        if (musicPlayer != null && isPlaying) {
            try {
                musicPlayer.pause();
                LOGGER.info("🎵 Music paused");
            } catch (Exception e) {
                LOGGER.warning("🎵 Error pausing music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Stop music
     */
    public void stop() {
        if (musicPlayer != null) {
            try {
                musicPlayer.stop();
                LOGGER.info("🎵 Music stopped");
            } catch (Exception e) {
                LOGGER.warning("🎵 Error stopping music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Toggle play/pause
     */
    public void togglePlayPause() {
        if (isPlaying) {
            pause();
        } else {
            play();
        }
    }
    
    /**
     * Set volume (0.0 to 1.0)
     */
    public void setVolume(double newVolume) {
        this.volume = Math.max(0.0, Math.min(1.0, newVolume));
        
        if (musicPlayer != null) {
            musicPlayer.setVolume(this.volume);
        }
        
        notifyStateChanged();
        LOGGER.info("🎵 Volume set to: " + (this.volume * 100) + "%");
    }
    
    /**
     * Get current volume
     */
    public double getVolume() {
        return volume;
    }
    
    /**
     * Mute/unmute music
     */
    public void toggleMute() {
        if (isMuted) {
            unmute();
        } else {
            mute();
        }
    }
    
    /**
     * Mute music
     */
    public void mute() {
        if (musicPlayer != null && !isMuted) {
            musicPlayer.setMute(true);
            isMuted = true;
            notifyStateChanged();
            LOGGER.info("🎵 Music muted");
        }
    }
    
    /**
     * Unmute music
     */
    public void unmute() {
        if (musicPlayer != null && isMuted) {
            musicPlayer.setMute(false);
            isMuted = false;
            notifyStateChanged();
            LOGGER.info("🎵 Music unmuted");
        }
    }
    
    /**
     * Check if music is currently playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Check if music is muted
     */
    public boolean isMuted() {
        return isMuted;
    }
    
    /**
     * Get current track name
     */
    public String getCurrentTrack() {
        return currentTrack;
    }
    
    /**
     * Set music state listener
     */
    public void setMusicStateListener(MusicStateListener listener) {
        this.stateListener = listener;
    }
    
    /**
     * Notify state listener of changes
     */
    private void notifyStateChanged() {
        if (stateListener != null) {
            try {
                stateListener.onMusicStateChanged(isPlaying, isMuted, volume);
            } catch (Exception e) {
                LOGGER.warning("🎵 Error notifying state listener: " + e.getMessage());
            }
        }
    }
    
    /**
     * Shutdown music manager
     */
    public void shutdown() {
        if (musicPlayer != null) {
            try {
                musicPlayer.stop();
                musicPlayer.dispose();
                musicPlayer = null;
                LOGGER.info("🎵 Music manager shutdown complete");
            } catch (Exception e) {
                LOGGER.warning("🎵 Error during music manager shutdown: " + e.getMessage());
            }
        }
        // Release SFX references
        sfxButtonClick = null;
        sfxInputSelect = null;
    }
}
