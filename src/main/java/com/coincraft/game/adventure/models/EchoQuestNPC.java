package com.coincraft.game.adventure.models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

/**
 * Echo Quest NPC Character
 * Represents therapeutic guides and emotional support characters
 * Each NPC provides mindfulness lessons and emotional healing guidance
 */
public class EchoQuestNPC {
    private final String name;
    private final String emoji;
    private final String greeting;
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    
    public EchoQuestNPC(String name, String emoji, String greeting, double x, double y) {
        this.name = name;
        this.emoji = emoji;
        this.greeting = greeting;
        this.x = x;
        this.y = y;
        createTherapeuticNPCSprite();
    }
    
    private void createTherapeuticNPCSprite() {
        try {
            // Load therapeutic guide sprite from Assets
            String guideSpritePath = getClass().getResource("/Assets/Sprites/Objects and buildings/Chest/spr_chest.png").toExternalForm();
            Image guideImage = new Image(guideSpritePath);
            ImageView guideImageView = new ImageView(guideImage);
            guideImageView.setFitWidth(60);
            guideImageView.setFitHeight(60);
            guideImageView.setPreserveRatio(true);
            npcSprite = guideImageView;
        } catch (Exception e) {
            // Fallback to therapeutic circle sprite
            npcSprite = new Circle(30, Color.LIGHTBLUE);
            ((Circle) npcSprite).setStroke(Color.DARKBLUE);
            ((Circle) npcSprite).setStrokeWidth(2);
        }
        
        // Add emoji text for therapeutic guidance
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
        
        // Position the NPC
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        emojiText.setLayoutX(x - 8);
        emojiText.setLayoutY(y + 8);
        npcName.setLayoutX(x - name.length() * 3);
        npcName.setLayoutY(y - 40);
    }
    
    public void render(Pane gameWorld) {
        if (!gameWorld.getChildren().contains(npcSprite)) {
            gameWorld.getChildren().add(npcSprite);
            gameWorld.getChildren().add(npcName);
        }
    }
    
    public void interact() {
        if (!hasInteracted) {
            System.out.println("🌟 " + name + ": " + greeting);
            hasInteracted = true;
        } else {
            System.out.println("🌟 " + name + ": How are you feeling today? Remember to breathe and stay present.");
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getGreeting() {
        return greeting;
    }
    
    public boolean hasInteracted() {
        return hasInteracted;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}
