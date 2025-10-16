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
 * Interactive objects in adventure mode
 * Similar to Echo Quest's interactable elements
 */
public abstract class AdventureInteractable {
    private final String name;
    private final String emoji;
    private final String description;
    private final double x, y;
    private Node interactableSprite;
    private Text interactableName;
    private boolean hasInteracted = false;
    
    public AdventureInteractable(String name, String emoji, String description, double x, double y) {
        this.name = name;
        this.emoji = emoji;
        this.description = description;
        this.x = x;
        this.y = y;
        createInteractableSprite();
    }
    
    private void createInteractableSprite() {
        try {
            // Load barrel sprite from Assets
            String barrelSpritePath = getClass().getResource("/Assets/Sprites/Objects and buildings/Barrels and crates/spr_barrel1.png").toExternalForm();
            Image barrelImage = new Image(barrelSpritePath);
            ImageView barrelImageView = new ImageView(barrelImage);
            barrelImageView.setFitWidth(50);
            barrelImageView.setFitHeight(50);
            barrelImageView.setPreserveRatio(true);
            interactableSprite = barrelImageView;
        } catch (Exception e) {
            // Fallback to circle sprite
            interactableSprite = new Circle(25, Color.ORANGE);
            ((Circle) interactableSprite).setStroke(Color.DARKORANGE);
            ((Circle) interactableSprite).setStrokeWidth(2);
        }
        
        // Add emoji text
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
    }
    
    public void render(Pane gameWorld) {
        interactableSprite.setLayoutX(x);
        interactableSprite.setLayoutY(y);
        gameWorld.getChildren().add(interactableSprite);
        
        interactableName.setLayoutX(x - name.length() * 2);
        interactableName.setLayoutY(y - 35);
        gameWorld.getChildren().add(interactableName);
    }
    
    public void update(double deltaTime, AdventurePlayer player) {
        // Simple interactable behavior
        // Could add pulsing animation, etc.
    }
    
    public void interact(AdventurePlayer player) {
        if (!hasInteracted) {
            showDescription();
            hasInteracted = true;
        }
        
        // Play interaction sound
        // SoundManager.getInstance().playSound("button_hover.wav");
        
        // Set player as interacting
        player.setInteracting(true);
        
        // Call the specific interaction logic
        onInteract(player);
    }
    
    protected abstract void onInteract(AdventurePlayer player);
    
    private void showDescription() {
        System.out.println("🔍 " + name + ": " + description);
        
        // This would show a proper dialog box in the UI
        // For now, we'll use console output
    }
    
    public void cleanup() {
        // Clean up interactable resources
        if (interactableSprite != null && interactableSprite.getParent() != null) {
            ((Pane) interactableSprite.getParent()).getChildren().remove(interactableSprite);
        }
        if (interactableName != null && interactableName.getParent() != null) {
            ((Pane) interactableName.getParent()).getChildren().remove(interactableName);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getDescription() { return description; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
}
