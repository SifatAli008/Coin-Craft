package com.coincraft.game.adventure.models;

import javafx.scene.layout.Pane;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

// Event system integration removed for now - can be added later

/**
 * NPC Manager - Handles multiple NPCs efficiently
 * Integrates with the game engine's event system and physics
 */
public class NPCManager {
    private final List<NPCCharacter> npcs = new CopyOnWriteArrayList<>();
    private final List<NPCCharacter> activeNPCs = new CopyOnWriteArrayList<>();
    private AdventurePlayer currentPlayer;
    // gameWorld removed as it's not used in current implementation
    
    // Performance tracking
    private int maxNPCs = 50;
    private double updateInterval = 1.0 / 60.0; // 60 FPS
    private double lastUpdateTime = 0.0;
    
    public NPCManager() {
        System.out.println("🎭 NPC Manager initialized");
    }
    
    /**
     * Add an NPC to the manager
     */
    public void addNPC(NPCCharacter npc) {
        if (npcs.size() >= maxNPCs) {
            System.out.println("⚠️ Maximum NPC limit reached (" + maxNPCs + ")");
            return;
        }
        
        npcs.add(npc);
        if (isNPCInRange(npc)) {
            activeNPCs.add(npc);
        }
        
        System.out.println("➕ Added NPC: " + npc.getName() + " (Total: " + npcs.size() + ")");
    }
    
    /**
     * Remove an NPC from the manager
     */
    public void removeNPC(NPCCharacter npc) {
        npcs.remove(npc);
        activeNPCs.remove(npc);
        npc.cleanup();
        
        System.out.println("➖ Removed NPC: " + npc.getName() + " (Total: " + npcs.size() + ")");
    }
    
    /**
     * Remove NPC by name
     */
    public void removeNPC(String name) {
        List<NPCCharacter> toRemove = npcs.stream()
            .filter(npc -> npc.getName().equals(name))
            .collect(Collectors.toList());
        
        for (NPCCharacter npc : toRemove) {
            removeNPC(npc);
        }
    }
    
    /**
     * Get NPC by name
     */
    public NPCCharacter getNPC(String name) {
        return npcs.stream()
            .filter(npc -> npc.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get all NPCs of a specific type
     */
    public List<NPCCharacter> getNPCsByType(NPCCharacter.NPCType type) {
        return npcs.stream()
            .filter(npc -> npc.getType() == type)
            .collect(Collectors.toList());
    }
    
    /**
     * Get NPCs within interaction range of player
     */
    public List<NPCCharacter> getInteractableNPCs(AdventurePlayer player) {
        return npcs.stream()
            .filter(npc -> npc.canInteract(player))
            .collect(Collectors.toList());
    }
    
    /**
     * Update all NPCs
     */
    public void update(double deltaTime) {
        lastUpdateTime += deltaTime;
        
        if (lastUpdateTime >= updateInterval) {
            // Update active NPCs
            for (NPCCharacter npc : activeNPCs) {
                if (npc.isActive()) {
                    npc.update(deltaTime);
                }
            }
            
            // Update NPC visibility based on player position
            updateNPCVisibility();
            
            lastUpdateTime = 0.0;
        }
    }
    
    /**
     * Render all active NPCs
     */
    public void render(Pane gameWorld) {
        for (NPCCharacter npc : activeNPCs) {
            if (npc.isActive()) {
                npc.render(gameWorld);
            }
        }
    }
    
    /**
     * Handle player interaction with nearby NPCs
     */
    public void handlePlayerInteraction(AdventurePlayer player) {
        List<NPCCharacter> interactableNPCs = getInteractableNPCs(player);
        
        if (!interactableNPCs.isEmpty()) {
            // Interact with the closest NPC
            NPCCharacter closestNPC = interactableNPCs.stream()
                .min((npc1, npc2) -> {
                    double dist1 = getDistance(player, npc1);
                    double dist2 = getDistance(player, npc2);
                    return Double.compare(dist1, dist2);
                })
                .orElse(null);
            
            if (closestNPC != null) {
                closestNPC.interact(player);
            }
        }
    }
    
    /**
     * Update which NPCs are visible/active based on player position
     */
    private void updateNPCVisibility() {
        if (currentPlayer == null) return;
        
        // Remove NPCs that are out of range
        activeNPCs.removeIf(npc -> !isNPCInRange(npc));
        
        // Add NPCs that are now in range
        for (NPCCharacter npc : npcs) {
            if (!activeNPCs.contains(npc) && isNPCInRange(npc)) {
                activeNPCs.add(npc);
            }
        }
    }
    
    /**
     * Check if NPC is within rendering range
     */
    private boolean isNPCInRange(NPCCharacter npc) {
        if (currentPlayer == null) return true;
        
        double distance = getDistance(currentPlayer, npc);
        return distance <= 500.0; // 500 pixel render distance
    }
    
    /**
     * Calculate distance between player and NPC
     */
    private double getDistance(AdventurePlayer player, NPCCharacter npc) {
        return Math.sqrt(
            Math.pow(player.getCenterX() - npc.getX(), 2) + 
            Math.pow(player.getCenterY() - npc.getY(), 2)
        );
    }
    
    /**
     * Create a new NPC with default settings
     */
    public NPCCharacter createNPC(String name, String greeting, String[] dialogue, 
                                NPCCharacter.NPCType type, double x, double y) {
        NPCCharacter npc = new NPCCharacter(name, greeting, dialogue, type, x, y);
        addNPC(npc);
        return npc;
    }
    
    /**
     * Create a merchant NPC
     */
    public NPCCharacter createMerchant(String name, String greeting, String[] dialogue, double x, double y) {
        return createNPC(name, greeting, dialogue, NPCCharacter.NPCType.MERCHANT, x, y);
    }
    
    /**
     * Create an adventurer NPC
     */
    public NPCCharacter createAdventurer(String name, String greeting, String[] dialogue, double x, double y) {
        return createNPC(name, greeting, dialogue, NPCCharacter.NPCType.ADVENTURER, x, y);
    }
    
    /**
     * Create a sage NPC
     */
    public NPCCharacter createSage(String name, String greeting, String[] dialogue, double x, double y) {
        return createNPC(name, greeting, dialogue, NPCCharacter.NPCType.SAGE, x, y);
    }
    
    /**
     * Create a teacher NPC
     */
    public NPCCharacter createTeacher(String name, String greeting, String[] dialogue, double x, double y) {
        return createNPC(name, greeting, dialogue, NPCCharacter.NPCType.TEACHER, x, y);
    }
    
    /**
     * Create a guide NPC
     */
    public NPCCharacter createGuide(String name, String greeting, String[] dialogue, double x, double y) {
        return createNPC(name, greeting, dialogue, NPCCharacter.NPCType.GUIDE, x, y);
    }
    
    /**
     * Set the current player for distance calculations
     */
    public void setCurrentPlayer(AdventurePlayer player) {
        this.currentPlayer = player;
    }
    
    /**
     * Clean up all NPCs
     */
    public void cleanup() {
        for (NPCCharacter npc : npcs) {
            npc.cleanup();
        }
        npcs.clear();
        activeNPCs.clear();
        
        // Event system cleanup removed for now
        
        System.out.println("🧹 NPC Manager cleaned up");
    }
    
    // Event handling removed for now - can be added later
    
    // Getters
    public List<NPCCharacter> getAllNPCs() { return new ArrayList<>(npcs); }
    public List<NPCCharacter> getActiveNPCs() { return new ArrayList<>(activeNPCs); }
    public int getNPCCount() { return npcs.size(); }
    public int getActiveNPCCount() { return activeNPCs.size(); }
    public int getMaxNPCs() { return maxNPCs; }
    
    // Setters
    public void setMaxNPCs(int maxNPCs) { this.maxNPCs = maxNPCs; }
    public void setUpdateInterval(double interval) { this.updateInterval = interval; }
}
