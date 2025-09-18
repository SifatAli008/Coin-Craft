package com.coincraft.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Avatar model representing the player's customizable character
 */
public class Avatar {
    private String avatarId;
    private String name;
    private String baseImagePath;
    private List<AvatarItem> equippedItems;
    private AvatarStyle style;
    private String backgroundColor;
    private boolean isCustomized;
    
    // Constructors
    public Avatar() {
        this.equippedItems = new ArrayList<>();
        this.style = AvatarStyle.EXPLORER;
        this.backgroundColor = "#87CEEB"; // Sky blue default
        this.isCustomized = false;
        this.name = "Money Explorer";
    }
    
    public Avatar(String avatarId, String name) {
        this();
        this.avatarId = avatarId;
        this.name = name;
    }
    
    // Getters and Setters
    public String getAvatarId() { return avatarId; }
    public void setAvatarId(String avatarId) { this.avatarId = avatarId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBaseImagePath() { return baseImagePath; }
    public void setBaseImagePath(String baseImagePath) { this.baseImagePath = baseImagePath; }
    
    public List<AvatarItem> getEquippedItems() { return equippedItems; }
    public void setEquippedItems(List<AvatarItem> equippedItems) { this.equippedItems = equippedItems; }
    
    public AvatarStyle getStyle() { return style; }
    public void setStyle(AvatarStyle style) { this.style = style; }
    
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    
    public boolean isCustomized() { return isCustomized; }
    public void setCustomized(boolean customized) { isCustomized = customized; }
    
    // Business methods
    public void equipItem(AvatarItem item) {
        // Remove any existing item of the same type
        equippedItems.removeIf(existing -> existing.getType() == item.getType());
        equippedItems.add(item);
        this.isCustomized = true;
    }
    
    public void unequipItem(AvatarItemType type) {
        equippedItems.removeIf(item -> item.getType() == type);
    }
    
    public AvatarItem getEquippedItem(AvatarItemType type) {
        return equippedItems.stream()
                .filter(item -> item.getType() == type)
                .findFirst()
                .orElse(null);
    }
    
    public boolean hasItem(AvatarItemType type) {
        return getEquippedItem(type) != null;
    }
    
    public String getDisplayImagePath() {
        if (baseImagePath != null && !baseImagePath.isEmpty()) {
            return baseImagePath;
        }
        return "/images/avatars/" + style.name().toLowerCase() + "_default.png";
    }
    
    @Override
    public String toString() {
        return "Avatar{" +
                "avatarId='" + avatarId + '\'' +
                ", name='" + name + '\'' +
                ", style=" + style +
                ", equippedItems=" + equippedItems.size() +
                '}';
    }
}
