package com.coincraft.audio;

/**
 * Deprecated stub for SoundManager to maintain backward compatibility
 * This class delegates all calls to CentralizedMusicManager.
 * Future refactoring: Remove this class once all SoundManager references are updated to use CentralizedMusicManager directly.
 */
@Deprecated
public class SoundManager {
    private static final SoundManager instance = new SoundManager();
    
    public static SoundManager getInstance() {
        return instance;
    }
    
    // Delegate to CentralizedMusicManager for backward compatibility
    public void playButtonHover() {
        CentralizedMusicManager.getInstance().playInputSelect();
    }
    
    public void playButtonClick() {
        CentralizedMusicManager.getInstance().playButtonClick();
    }
    
    public void playSuccess() {
        // Reuse click for now unless separate asset is provided
        CentralizedMusicManager.getInstance().playButtonClick();
    }
    
    public void playError() {
        // Reuse input select for now unless separate asset is provided
        CentralizedMusicManager.getInstance().playInputSelect();
    }
    
    public void playNotification() {
        CentralizedMusicManager.getInstance().playInputSelect();
    }
    
    public void stopBackgroundMusic() {
        CentralizedMusicManager.getInstance().stop();
    }
    
    public void ensureSingleMusicInstance() {
        CentralizedMusicManager.getInstance().play();
    }
}
