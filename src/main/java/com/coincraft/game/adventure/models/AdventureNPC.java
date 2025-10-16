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
    
    private void createNPCSprite() {
        try {
            // Load chest sprite from Assets
            String chestSpritePath = getClass().getResource("/Assets/Sprites/Objects and buildings/Chest/spr_chest.png").toExternalForm();
            Image chestImage = new Image(chestSpritePath);
            ImageView chestImageView = new ImageView(chestImage);
            chestImageView.setFitWidth(60);
            chestImageView.setFitHeight(60);
            chestImageView.setPreserveRatio(true);
            npcSprite = chestImageView;
        } catch (Exception e) {
            // Fallback to circle sprite
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
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        npcName.setLayoutX(x - name.length() * 3);
        npcName.setLayoutY(y - 40);
        gameWorld.getChildren().add(npcName);
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
