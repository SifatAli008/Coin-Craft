package com.coincraft.engine.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Audio manager for handling sound effects and music
 * Provides centralized audio management for the game engine
 */
public class AudioManager {
    private final Map<String, Media> musicTracks = new HashMap<>();
    private final Map<String, AudioClip> soundEffects = new HashMap<>();
    private final List<MediaPlayer> activePlayers = new CopyOnWriteArrayList<>();
    
    // Audio settings
    private double masterVolume = 1.0;
    private double musicVolume = 0.7;
    private double sfxVolume = 0.8;
    private boolean mute = false;
    
    // Current music
    private MediaPlayer currentMusic = null;
    private String currentTrack = null;
    
    /**
     * Load a music track
     */
    public void loadMusic(String name, String filePath) {
        try {
            Media media = new Media(filePath);
            musicTracks.put(name, media);
            System.out.println("🎵 Loaded music: " + name);
        } catch (Exception e) {
            System.err.println("❌ Failed to load music: " + name + " - " + e.getMessage());
        }
    }
    
    /**
     * Load a sound effect
     */
    public void loadSoundEffect(String name, String filePath) {
        try {
            AudioClip clip = new AudioClip(filePath);
            soundEffects.put(name, clip);
            System.out.println("🔊 Loaded sound effect: " + name);
        } catch (Exception e) {
            System.err.println("❌ Failed to load sound effect: " + name + " - " + e.getMessage());
        }
    }
    
    /**
     * Play music
     */
    public void playMusic(String name, boolean loop) {
        if (mute) return;
        
        Media media = musicTracks.get(name);
        if (media == null) {
            System.err.println("❌ Music not found: " + name);
            return;
        }
        
        // Stop current music
        stopMusic();
        
        try {
            currentMusic = new MediaPlayer(media);
            currentMusic.setVolume(masterVolume * musicVolume);
            currentMusic.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
            currentMusic.play();
            currentTrack = name;
            activePlayers.add(currentMusic);
            
            System.out.println("🎵 Playing music: " + name);
        } catch (Exception e) {
            System.err.println("❌ Failed to play music: " + name + " - " + e.getMessage());
        }
    }
    
    /**
     * Stop current music
     */
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            activePlayers.remove(currentMusic);
            currentMusic = null;
            currentTrack = null;
        }
    }
    
    /**
     * Pause current music
     */
    public void pauseMusic() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }
    
    /**
     * Resume current music
     */
    public void resumeMusic() {
        if (currentMusic != null) {
            currentMusic.play();
        }
    }
    
    /**
     * Play sound effect
     */
    public void playSoundEffect(String name) {
        if (mute) return;
        
        AudioClip clip = soundEffects.get(name);
        if (clip == null) {
            System.err.println("❌ Sound effect not found: " + name);
            return;
        }
        
        try {
            clip.setVolume(masterVolume * sfxVolume);
            clip.play();
        } catch (Exception e) {
            System.err.println("❌ Failed to play sound effect: " + name + " - " + e.getMessage());
        }
    }
    
    /**
     * Play sound effect with volume
     */
    public void playSoundEffect(String name, double volume) {
        if (mute) return;
        
        AudioClip clip = soundEffects.get(name);
        if (clip == null) {
            System.err.println("❌ Sound effect not found: " + name);
            return;
        }
        
        try {
            clip.setVolume(masterVolume * sfxVolume * volume);
            clip.play();
        } catch (Exception e) {
            System.err.println("❌ Failed to play sound effect: " + name + " - " + e.getMessage());
        }
    }
    
    /**
     * Update audio manager
     */
    public void update(double deltaTime) {
        // Clean up finished players
        activePlayers.removeIf(player -> 
            player.getStatus() == MediaPlayer.Status.STOPPED);
    }
    
    /**
     * Set master volume
     */
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
        updateAllVolumes();
    }
    
    /**
     * Set music volume
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(masterVolume * musicVolume);
        }
    }
    
    /**
     * Set sound effects volume
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }
    
    /**
     * Set mute state
     */
    public void setMute(boolean mute) {
        this.mute = mute;
        if (mute) {
            stopAllAudio();
        }
    }
    
    /**
     * Update all volumes
     */
    private void updateAllVolumes() {
        if (currentMusic != null) {
            currentMusic.setVolume(masterVolume * musicVolume);
        }
    }
    
    /**
     * Stop all audio
     */
    public void stopAllAudio() {
        for (MediaPlayer player : activePlayers) {
            player.stop();
        }
        activePlayers.clear();
        currentMusic = null;
        currentTrack = null;
    }
    
    /**
     * Cleanup resources
     */
    public void cleanup() {
        stopAllAudio();
        musicTracks.clear();
        soundEffects.clear();
    }
    
    // Getters
    public double getMasterVolume() { return masterVolume; }
    public double getMusicVolume() { return musicVolume; }
    public double getSfxVolume() { return sfxVolume; }
    public boolean isMute() { return mute; }
    public String getCurrentTrack() { return currentTrack; }
    public boolean isMusicPlaying() { return currentMusic != null && currentMusic.getStatus() == MediaPlayer.Status.PLAYING; }
}
