package com.coincraft.models;

/**
 * Enumeration defining different avatar base styles
 */
public enum AvatarStyle {
    EXPLORER("Money Explorer"),
    BANKER("Smart Banker"),
    INVESTOR("Future Investor"),
    SAVER("Super Saver"),
    GUARDIAN("Safety Guardian"),
    HELPER("Community Helper");
    
    private final String displayName;
    
    AvatarStyle(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getBasePath() {
        return "/images/avatars/" + this.name().toLowerCase() + "/";
    }
}
