package com.coincraft.models;

/**
 * Enumeration defining different badge achievement levels
 */
public enum BadgeLevel {
    BRONZE("Bronze", "#CD7F32"),
    SILVER("Silver", "#C0C0C0"),
    GOLD("Gold", "#FFD700"),
    PLATINUM("Platinum", "#E5E4E2");
    
    private final String displayName;
    private final String colorCode;
    
    BadgeLevel(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public int getPointValue() {
        return switch (this) {
            case BRONZE -> 10;
            case SILVER -> 25;
            case GOLD -> 50;
            case PLATINUM -> 100;
        };
    }
}
