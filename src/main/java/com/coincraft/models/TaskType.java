package com.coincraft.models;

/**
 * Enumeration defining different types of tasks in CoinCraft
 */
public enum TaskType {
    CHORE("Household Chore"),
    CHALLENGE("Personal Challenge"),
    QUEST("Adventure Quest"),
    DONATION("Donation Activity"),
    LEARNING("Learning Module"),
    SOCIAL("Social Activity");
    
    private final String displayName;
    
    TaskType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean requiresPartnerValidation() {
        return this == CHORE || this == DONATION || this == SOCIAL;
    }
    
    public int getBaseRewardCoins() {
        return switch (this) {
            case CHORE -> 10;
            case CHALLENGE -> 15;
            case QUEST -> 20;
            case DONATION -> 25;
            case LEARNING -> 5;
            case SOCIAL -> 30;
        };
    }
}
