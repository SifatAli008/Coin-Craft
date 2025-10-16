package com.coincraft.game.adventure.models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a themed zone in the adventure mode
 * Similar to Echo Quest's zones like Whispering Woods
 */
public abstract class AdventureZone {
    protected String zoneName;
    protected String description;
    protected String backgroundStyle;
    protected Color zoneColor;
    protected List<AdventureNPC> npcs;
    protected List<AdventureInteractable> interactables;
    protected boolean isCompleted = false;
    protected int completionReward = 100;
    
    public AdventureZone(String zoneName, String description) {
        this.zoneName = zoneName;
        this.description = description;
        this.npcs = new ArrayList<>();
        this.interactables = new ArrayList<>();
        initializeZone();
    }
    
    protected final void initializeZone() {
        // Template method pattern - subclasses implement setupZone()
        setupZone();
    }
    
    protected abstract void setupZone();
    
    public void renderZone(Pane gameWorld) {
        // Clear existing zone elements
        gameWorld.getChildren().removeIf(node -> 
            node.getStyleClass().contains("zone-element"));
        
        // Add background
        Rectangle background = new Rectangle(0, 0, 1200, 800);
        background.setFill(zoneColor);
        background.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(background);
        
        // Add zone-specific elements
        renderZoneElements(gameWorld);
        
        // Add NPCs
        for (AdventureNPC npc : npcs) {
            npc.render(gameWorld);
        }
        
        // Add interactables
        for (AdventureInteractable interactable : interactables) {
            interactable.render(gameWorld);
        }
    }
    
    protected abstract void renderZoneElements(Pane gameWorld);
    
    public void update(double deltaTime, AdventurePlayer player) {
        // Update NPCs
        for (AdventureNPC npc : npcs) {
            npc.update(deltaTime, player);
        }
        
        // Update interactables
        for (AdventureInteractable interactable : interactables) {
            interactable.update(deltaTime, player);
        }
        
        // Check for zone completion
        checkZoneCompletion(player);
    }
    
    protected abstract void checkZoneCompletion(AdventurePlayer player);
    
    public void handlePlayerInteraction(AdventurePlayer player) {
        // Check for nearby NPCs
        for (AdventureNPC npc : npcs) {
            if (isPlayerNearNPC(player, npc)) {
                npc.interact(player);
                return;
            }
        }
        
        // Check for nearby interactables
        for (AdventureInteractable interactable : interactables) {
            if (isPlayerNearInteractable(player, interactable)) {
                interactable.interact(player);
                return;
            }
        }
    }
    
    private boolean isPlayerNearNPC(AdventurePlayer player, AdventureNPC npc) {
        double distance = Math.sqrt(
            Math.pow(player.getCenterX() - npc.getCenterX(), 2) +
            Math.pow(player.getCenterY() - npc.getCenterY(), 2)
        );
        return distance < 80; // Interaction range
    }
    
    private boolean isPlayerNearInteractable(AdventurePlayer player, AdventureInteractable interactable) {
        double distance = Math.sqrt(
            Math.pow(player.getCenterX() - interactable.getCenterX(), 2) +
            Math.pow(player.getCenterY() - interactable.getCenterY(), 2)
        );
        return distance < 60; // Interaction range
    }
    
    public void cleanup() {
        // Clean up zone resources
        for (AdventureNPC npc : npcs) {
            npc.cleanup();
        }
        for (AdventureInteractable interactable : interactables) {
            interactable.cleanup();
        }
    }
    
    // Getters
    public String getZoneName() { return zoneName; }
    public String getDescription() { return description; }
    public String getBackgroundStyle() { return backgroundStyle; }
    public Color getZoneColor() { return zoneColor; }
    public boolean isCompleted() { return isCompleted; }
    public int getCompletionReward() { return completionReward; }
    
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }
}
