package com.coincraft.game.adventure.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * NPC character for adventure mode
 * Handles dialogue and interaction
 */
public class AdventureNPC {
    private final String name;
    private final String emoji;
    private final String greeting;
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    
    public AdventureNPC(String name, String emoji, String greeting, double x, double y) {
        this.name = name;
        this.emoji = emoji;
        this.greeting = greeting;
        this.x = x;
        this.y = y;
        createNPCSprite();
    }
    
    private String getNPCSpritePath() {
        // Map NPC names to their corresponding sprite files
        String lowerName = name.toLowerCase();
        
        if (lowerName.contains("business") || lowerName.contains("venture") || lowerName.contains("profit")) {
            return getClass().getResource("/Assets/NPC/Smart Businessman.png").toExternalForm();
        } else if (lowerName.contains("adventure") || lowerName.contains("thorin") || lowerName.contains("marcus") || lowerName.contains("gareth") || lowerName.contains("finn")) {
            return getClass().getResource("/Assets/NPC/Strong Adventurere.png").toExternalForm();
        } else if (lowerName.contains("wise") || lowerName.contains("sage") || lowerName.contains("luna") || lowerName.contains("aria") || lowerName.contains("elena")) {
            return getClass().getResource("/Assets/NPC/Wise Lady.png").toExternalForm();
        } else {
            // Default to Smart Businessman if no match
            return getClass().getResource("/Assets/NPC/Smart Businessman.png").toExternalForm();
        }
    }
    
    private void createNPCSprite() {
        try {
            // Load appropriate NPC sprite based on name
            String npcImagePath = getNPCSpritePath();
            System.out.println("Loading NPC sprite for " + name + " from: " + npcImagePath);
            Image npcImage = new Image(npcImagePath);
            ImageView npcImageView = new ImageView(npcImage);
            npcImageView.setFitWidth(60);
            npcImageView.setFitHeight(60);
            npcImageView.setPreserveRatio(true);
            npcSprite = npcImageView;
            System.out.println("Successfully loaded NPC sprite for " + name);
        } catch (Exception e) {
            // Fallback to circle sprite
            System.out.println("Failed to load NPC sprite for " + name + ", using fallback: " + e.getMessage());
            npcSprite = new Circle(30, Color.LIGHTGREEN);
            ((Circle) npcSprite).setStroke(Color.DARKGREEN);
            ((Circle) npcSprite).setStrokeWidth(2);
        }
        
        // Add emoji text
        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(24));
        emojiText.setFill(Color.WHITE);
        emojiText.setLayoutX(-8);
        emojiText.setLayoutY(8);
        
        // Add name label
        npcName = new Text(name);
        npcName.setFont(Font.font(12));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(-name.length() * 3);
        npcName.setLayoutY(-40);
    }
    
    public void render(Pane gameWorld) {
        System.out.println("Rendering NPC: " + name + " at position (" + x + ", " + y + ")");
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        npcName.setLayoutX(x - name.length() * 3);
        npcName.setLayoutY(y - 40);
        gameWorld.getChildren().add(npcName);
        System.out.println("NPC " + name + " rendered successfully");
    }
    
    public void update(double deltaTime, AdventurePlayer player) {
        // Simple NPC behavior - could be expanded
        // For now, just ensure the NPC stays visible
    }
    
    public void interact(AdventurePlayer player) {
        if (!hasInteracted) {
            showGreeting();
            hasInteracted = true;
        } else {
            showFollowUpDialogue();
        }
        
        // Play interaction sound
        // SoundManager.getInstance().playSound("button_hover.wav");
        
        // Set player as interacting
        player.setInteracting(true);
    }
    
    private void showGreeting() {
        System.out.println("💬 " + name + ": " + greeting);
        
        // This would show a proper dialog box in the UI
        // For now, we'll use console output
    }
    
    private void showFollowUpDialogue() {
        String[] followUps = {
            "Is there anything else you'd like to know?",
            "Remember what I taught you!",
            "Come back anytime for more advice!",
            "Keep practicing your financial skills!"
        };
        
        String followUp = followUps[(int)(Math.random() * followUps.length)];
        System.out.println("💬 " + name + ": " + followUp);
    }
    
    public void cleanup() {
        // Clean up NPC resources
        if (npcSprite != null && npcSprite.getParent() != null) {
            ((Pane) npcSprite.getParent()).getChildren().remove(npcSprite);
        }
        if (npcName != null && npcName.getParent() != null) {
            ((Pane) npcName.getParent()).getChildren().remove(npcName);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
}
