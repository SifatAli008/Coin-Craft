package com.coincraft.models;

/**
 * Enumeration defining validation states for tasks
 */
public enum ValidationStatus {
    PENDING("Awaiting Validation"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    AUTO_APPROVED("Automatically Approved");
    
    private final String displayName;
    
    ValidationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isFinalized() {
        return this == APPROVED || this == AUTO_APPROVED;
    }
}
