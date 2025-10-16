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
 * Echo Quest Interactive Objects
 * Therapeutic elements that provide mindfulness lessons and emotional healing
 * Includes Mind Shards, meditation spots, breathing stations, and awareness tools
 */
public abstract class EchoQuestInteractable {
    private final String name;
    private final String emoji;
    private final String description;
    private final double x, y;
    private Node interactableSprite;
    private Text interactableName;
    private boolean hasInteracted = false;
    
    public EchoQuestInteractable(String name, String emoji, String description, double x, double y) {
        this.name = name;
        this.emoji = emoji;
        this.description = description;
        this.x = x;
        this.y = y;
        createTherapeuticInteractableSprite();
    }
    
    private void createTherapeuticInteractableSprite() {
        try {
            // Load therapeutic object sprite from Assets
            String objectSpritePath = getClass().getResource("/Assets/Sprites/Objects and buildings/Barrels and crates/spr_barrel1.png").toExternalForm();
            Image objectImage = new Image(objectSpritePath);
            ImageView objectImageView = new ImageView(objectImage);
            objectImageView.setFitWidth(50);
            objectImageView.setFitHeight(50);
            objectImageView.setPreserveRatio(true);
            interactableSprite = objectImageView;
        } catch (Exception e) {
            // Fallback to therapeutic circle sprite
            interactableSprite = new Circle(25, Color.LIGHTGREEN);
            ((Circle) interactableSprite).setStroke(Color.DARKGREEN);
            ((Circle) interactableSprite).setStrokeWidth(2);
        }
        
        // Add emoji text for therapeutic guidance
        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(20));
        emojiText.setFill(Color.WHITE);
        emojiText.setLayoutX(-6);
        emojiText.setLayoutY(6);
        
        // Add name label
        interactableName = new Text(name);
        interactableName.setFont(Font.font(10));
        interactableName.setFill(Color.WHITE);
        interactableName.setLayoutX(-name.length() * 2);
        interactableName.setLayoutY(-35);
        
        // Position the interactable
        interactableSprite.setLayoutX(x);
        interactableSprite.setLayoutY(y);
        emojiText.setLayoutX(x - 6);
        emojiText.setLayoutY(y + 6);
        interactableName.setLayoutX(x - name.length() * 2);
        interactableName.setLayoutY(y - 35);
    }
    
    public void render(Pane gameWorld) {
        if (!gameWorld.getChildren().contains(interactableSprite)) {
            gameWorld.getChildren().add(interactableSprite);
            gameWorld.getChildren().add(interactableName);
        }
    }
    
    public void interact() {
        if (!hasInteracted) {
            System.out.println("🌟 " + name + ": " + description);
            onInteract();
            hasInteracted = true;
        } else {
            System.out.println("🌟 " + name + ": " + getRepeatMessage());
        }
    }
    
    public abstract void onInteract();
    
    protected String getRepeatMessage() {
        return "Continue your journey of self-discovery.";
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
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
