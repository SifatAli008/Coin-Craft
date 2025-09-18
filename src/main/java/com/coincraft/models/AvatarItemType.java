package com.coincraft.models;

/**
 * Enumeration defining different types of avatar items
 */
public enum AvatarItemType {
    HAT("Hat & Headwear"),
    CAPE("Cape & Cloak"),
    SHIRT("Shirt & Top"),
    PANTS("Pants & Bottom"),
    SHOES("Shoes & Footwear"),
    ACCESSORY("Accessory"),
    TOOL("Tool & Equipment"),
    BACKGROUND("Background");
    
    private final String displayName;
    
    AvatarItemType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isWearable() {
        return this != BACKGROUND;
    }
}
