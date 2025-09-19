package com.coincraft.models;

/**
 * Enumeration defining different categories of badges
 */
public enum BadgeCategory {
    BUDGETING("Budgeting & Planning"),
    EARNING("Earning & Income"),
    SAVING("Saving & Goals"),
    BANKING("Banking & Finance"),
    SAFETY("Digital Safety"),
    INVESTING("Investment & Growth"),
    SOCIAL("Social Responsibility"),
    ACHIEVEMENT("General Achievement"),
    STREAK("Consistency & Streaks"),
    LEARNING("Learning & Education"),
    CREATIVITY("Creative Arts"),
    FITNESS("Physical Fitness"),
    SPECIAL("Special Events");
    
    private final String displayName;
    
    BadgeCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
