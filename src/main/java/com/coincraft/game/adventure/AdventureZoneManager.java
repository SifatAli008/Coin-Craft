package com.coincraft.game.adventure;

import com.coincraft.game.adventure.models.AdventureZone;
import com.coincraft.game.adventure.zones.BudgetBayZone;
import com.coincraft.game.adventure.zones.SavingsSummitZone;
import com.coincraft.game.adventure.zones.InvestmentIslandZone;
import com.coincraft.game.adventure.zones.BankingBayouZone;
import com.coincraft.game.adventure.zones.DigitalSafetyZone;

/**
 * Manages adventure zones and zone transitions
 * Similar to Echo Quest's zone management system
 */
public class AdventureZoneManager {
    
    /**
     * Get the starting zone for new players
     */
    public static AdventureZone getStartingZone() {
        return new BudgetBayZone();
    }
    
    /**
     * Get a specific zone by name
     */
    public static AdventureZone getZone(String zoneName) {
        return switch (zoneName.toLowerCase()) {
            case "budget bay", "budgetbay" -> new BudgetBayZone();
            case "savings summit", "savingssummit" -> new SavingsSummitZone();
            case "investment island", "investmentisland" -> new InvestmentIslandZone();
            case "banking bayou", "bankingbayou" -> new BankingBayouZone();
            default -> getStartingZone();
        };
    }
    
    /**
     * Get the next zone in the progression
     */
    public static AdventureZone getNextZone(AdventureZone currentZone) {
        String currentName = currentZone.getZoneName().toLowerCase();
        
        return switch (currentName) {
            case "budget bay" -> new SavingsSummitZone();
            case "savings summit" -> new InvestmentIslandZone();
            case "investment island" -> new BankingBayouZone();
            case "banking bayou" -> new DigitalSafetyZone();
            default -> getStartingZone();
        };
    }
    
    /**
     * Check if a zone is unlocked for the player
     */
    public static boolean isZoneUnlocked(String zoneName, int playerLevel) {
        return switch (zoneName.toLowerCase()) {
            case "budget bay" -> true; // Always unlocked
            case "savings summit" -> playerLevel >= 2;
            case "investment island" -> playerLevel >= 3;
            case "banking bayou" -> playerLevel >= 4;
            case "digital safety" -> playerLevel >= 5;
            default -> false;
        };
    }
    
    /**
     * Get all available zones for a player level
     */
    public static String[] getAvailableZones(int playerLevel) {
        java.util.List<String> zones = new java.util.ArrayList<>();
        
        if (isZoneUnlocked("budget bay", playerLevel)) {
            zones.add("Budget Bay");
        }
        if (isZoneUnlocked("savings summit", playerLevel)) {
            zones.add("Savings Summit");
        }
        if (isZoneUnlocked("investment island", playerLevel)) {
            zones.add("Investment Island");
        }
        if (isZoneUnlocked("banking bayou", playerLevel)) {
            zones.add("Banking Bayou");
        }
        if (isZoneUnlocked("digital safety", playerLevel)) {
            zones.add("Digital Safety");
        }
        
        return zones.toArray(String[]::new);
    }
}
