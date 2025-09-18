package com.coincraft.models;

/**
 * Avatar item model representing customizable avatar accessories and clothing
 */
public class AvatarItem {
    private String itemId;
    private String name;
    private String description;
    private AvatarItemType type;
    private String imagePath;
    private int unlockLevel;
    private int coinCost;
    private Badge requiredBadge;
    private boolean isUnlocked;
    private String colorVariant;
    
    // Constructors
    public AvatarItem() {
        this.isUnlocked = false;
        this.unlockLevel = 1;
        this.coinCost = 0;
    }
    
    public AvatarItem(String itemId, String name, AvatarItemType type, String imagePath) {
        this();
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }
    
    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public AvatarItemType getType() { return type; }
    public void setType(AvatarItemType type) { this.type = type; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public int getUnlockLevel() { return unlockLevel; }
    public void setUnlockLevel(int unlockLevel) { this.unlockLevel = unlockLevel; }
    
    public int getCoinCost() { return coinCost; }
    public void setCoinCost(int coinCost) { this.coinCost = coinCost; }
    
    public Badge getRequiredBadge() { return requiredBadge; }
    public void setRequiredBadge(Badge requiredBadge) { this.requiredBadge = requiredBadge; }
    
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    
    public String getColorVariant() { return colorVariant; }
    public void setColorVariant(String colorVariant) { this.colorVariant = colorVariant; }
    
    // Business methods
    public boolean canUnlock(User user) {
        if (isUnlocked) return true;
        
        boolean levelMet = user.getLevel() >= unlockLevel;
        boolean coinsMet = user.getSmartCoinBalance() >= coinCost;
        boolean badgeMet = requiredBadge == null || user.getEarnedBadges().contains(requiredBadge);
        
        return levelMet && coinsMet && badgeMet;
    }
    
    public String getDisplayName() {
        String display = name;
        if (colorVariant != null && !colorVariant.isEmpty()) {
            display += " (" + colorVariant + ")";
        }
        return display;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AvatarItem that = (AvatarItem) obj;
        return itemId != null && itemId.equals(that.itemId);
    }
    
    @Override
    public int hashCode() {
        return itemId != null ? itemId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "AvatarItem{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", isUnlocked=" + isUnlocked +
                '}';
    }
}
