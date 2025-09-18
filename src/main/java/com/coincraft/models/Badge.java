package com.coincraft.models;

import java.time.LocalDateTime;

/**
 * Badge model representing achievements and milestones in CoinCraft
 */
public class Badge {
    private String badgeId;
    private String name;
    private String description;
    private BadgeLevel level;
    private LocalDateTime awardedDate;
    private String iconPath;
    private BadgeCategory category;
    private int pointsRequired;
    private boolean isVisible;
    
    // Constructors
    public Badge() {
        this.level = BadgeLevel.BRONZE;
        this.awardedDate = LocalDateTime.now();
        this.isVisible = true;
    }
    
    public Badge(String badgeId, String name, String description, BadgeLevel level) {
        this();
        this.badgeId = badgeId;
        this.name = name;
        this.description = description;
        this.level = level;
    }
    
    // Getters and Setters
    public String getBadgeId() { return badgeId; }
    public void setBadgeId(String badgeId) { this.badgeId = badgeId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BadgeLevel getLevel() { return level; }
    public void setLevel(BadgeLevel level) { this.level = level; }
    
    public LocalDateTime getAwardedDate() { return awardedDate; }
    public void setAwardedDate(LocalDateTime awardedDate) { this.awardedDate = awardedDate; }
    
    public String getIconPath() { return iconPath; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }
    
    public BadgeCategory getCategory() { return category; }
    public void setCategory(BadgeCategory category) { this.category = category; }
    
    public int getPointsRequired() { return pointsRequired; }
    public void setPointsRequired(int pointsRequired) { this.pointsRequired = pointsRequired; }
    
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    
    // Business methods
    public int getBadgePoints() {
        return switch (level) {
            case BRONZE -> 10;
            case SILVER -> 25;
            case GOLD -> 50;
            case PLATINUM -> 100;
        };
    }
    
    public String getDisplayTitle() {
        return name + " (" + level.getDisplayName() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Badge badge = (Badge) obj;
        return badgeId != null && badgeId.equals(badge.badgeId);
    }
    
    @Override
    public int hashCode() {
        return badgeId != null ? badgeId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Badge{" +
                "badgeId='" + badgeId + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", awardedDate=" + awardedDate +
                '}';
    }
}
