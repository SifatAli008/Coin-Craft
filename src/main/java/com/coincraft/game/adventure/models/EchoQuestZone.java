package com.coincraft.game.adventure.models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Echo Quest therapeutic zones
 * Each zone represents a different aspect of emotional healing and self-discovery
 */
public abstract class EchoQuestZone {
    protected final String zoneName;
    protected final String description;
    protected Color zoneColor;
    protected String backgroundStyle;
    protected List<EchoQuestNPC> npcs;
    protected List<EchoQuestInteractable> interactables;
    
    public EchoQuestZone(String zoneName, String description) {
        this.zoneName = zoneName;
        this.description = description;
        this.npcs = new ArrayList<>();
        this.interactables = new ArrayList<>();
        setupZone();
    }
    
    protected final void setupZone() {
        // Template method pattern - subclasses implement setupZone()
        setupTherapeuticElements();
    }
    
    protected abstract void setupTherapeuticElements();
    
    public void renderZone(Pane gameWorld) {
        // Clear existing zone elements
        gameWorld.getChildren().removeIf(node -> 
            node.getStyleClass().contains("therapeutic-zone-element"));
        
        // Set therapeutic background
        gameWorld.setStyle(backgroundStyle);
        
        // Render NPCs
        for (EchoQuestNPC npc : npcs) {
            npc.render(gameWorld);
        }
        
        // Render interactables
        for (EchoQuestInteractable interactable : interactables) {
            interactable.render(gameWorld);
        }
    }
    
    protected void addNPC(EchoQuestNPC npc) {
        npcs.add(npc);
    }
    
    protected void addInteractable(EchoQuestInteractable interactable) {
        interactables.add(interactable);
    }
    
    public String getZoneName() {
        return zoneName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Color getZoneColor() {
        return zoneColor;
    }
}
