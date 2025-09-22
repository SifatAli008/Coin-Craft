package com.coincraft.audio;

/**
 * Temporary stub for SoundManager to avoid compilation errors
 * TODO: Remove all SoundManager references and use CentralizedMusicManager instead
 */
@Deprecated
public class SoundManager {
    private static SoundManager instance = new SoundManager();
    
    public static SoundManager getInstance() {
        return instance;
    }
    
    // Stub methods - do nothing
    public void playButtonHover() {
        // TODO: Implement via CentralizedMusicManager if needed
    }
    
    public void playButtonClick() {
        // TODO: Implement via CentralizedMusicManager if needed
    }
    
    public void playSuccess() {
        // TODO: Implement via CentralizedMusicManager if needed
    }
    
    public void playError() {
        // TODO: Implement via CentralizedMusicManager if needed
    }
    
    public void playNotification() {
        // TODO: Implement via CentralizedMusicManager if needed
    }
    
    public void stopBackgroundMusic() {
        // TODO: Use CentralizedMusicManager.getInstance().stop() instead
    }
    
    public void ensureSingleMusicInstance() {
        // TODO: Use CentralizedMusicManager.getInstance().play() instead
    }
}
