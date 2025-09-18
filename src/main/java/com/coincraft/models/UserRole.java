package com.coincraft.models;

/**
 * Enumeration defining different user roles in the CoinCraft system
 */
public enum UserRole {
    CHILD("Child Player"),
    PARENT("Parent/Guardian"),
    TEACHER("Teacher/Educator"),
    ELDER("Elder/Mentor"),
    ADMIN("System Administrator");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canVerifyTasks() {
        return this == PARENT || this == TEACHER || this == ELDER || this == ADMIN;
    }
    
    public boolean canAccessAdminFeatures() {
        return this == ADMIN;
    }
    
    public boolean isChild() {
        return this == CHILD;
    }
}
