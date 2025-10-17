package com.coincraft.game.adventure.models;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Player character for adventure mode
 * Handles movement, animation, and interaction
 */
public class AdventurePlayer extends Pane {
    private ImageView sprite;
    private final String avatarPath;
    private boolean isMoving = false;
    private boolean isInteracting = false;
    private double targetX, targetY;
    private final double moveSpeed = 2.0;
    private AnimationTimer moveTimer;
    
    // Animation states
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION = 200_000_000; // 200ms in nanoseconds
    
    public AdventurePlayer(String name, double x, double y) {
        this.avatarPath = "/Assets/Sprites/Player/Side animations/spr_player_idle.png";
        initializePlayer();
        setLayoutX(x - getPrefWidth() / 2);
        setLayoutY(y - getPrefHeight() / 2);
    }
    
    public AdventurePlayer(String avatarPath) {
        this.avatarPath = avatarPath;
        initializePlayer();
    }
    
    private void initializePlayer() {
        setPrefSize(64, 64);
        
        // Create player sprite
        sprite = new ImageView();
        sprite.setFitWidth(48);
        sprite.setFitHeight(48);
        sprite.setPreserveRatio(true);
        
        // Load player sprite from Assets
        try {
            String playerSpritePath = getClass().getResource("/Assets/Sprites/Player/Side animations/spr_player_idle.png").toExternalForm();
            Image playerImage = new Image(playerSpritePath);
            sprite.setImage(playerImage);
        } catch (Exception e) {
            // Fallback to avatar image
            try {
                Image avatarImage = new Image(avatarPath);
                sprite.setImage(avatarImage);
            } catch (Exception e2) {
                // Final fallback to default sprite
                createDefaultSprite();
            }
        }
        
        getChildren().add(sprite);
        
        // Center the sprite
        sprite.setLayoutX(8);
        sprite.setLayoutY(8);
        
        // Add interaction indicator
        Circle interactionIndicator = new Circle(32, 32, 20);
        interactionIndicator.setFill(Color.TRANSPARENT);
        interactionIndicator.setStroke(Color.YELLOW);
        interactionIndicator.setStrokeWidth(2);
        interactionIndicator.setVisible(false);
        interactionIndicator.setId("interaction-indicator");
        getChildren().add(interactionIndicator);
    }
    
    private void createDefaultSprite() {
        // Create a simple colored rectangle as fallback
        sprite.setImage(null);
        sprite.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 5;");
    }
    
    public void moveTo(double x, double y) {
        targetX = x;
        targetY = y;
        
        if (moveTimer != null) {
            moveTimer.stop();
        }
        
        moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentX = getLayoutX();
                double currentY = getLayoutY();
                
                double dx = targetX - currentX;
                double dy = targetY - currentY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance < 5) {
                    // Reached target
                    setLayoutX(targetX);
                    setLayoutY(targetY);
                    setMoving(false);
                    moveTimer.stop();
                    return;
                }
                
                // Move towards target
                double moveX = (dx / distance) * moveSpeed;
                double moveY = (dy / distance) * moveSpeed;
                
                setLayoutX(currentX + moveX);
                setLayoutY(currentY + moveY);
                setMoving(true);
            }
        };
        moveTimer.start();
    }
    
    public void update(double deltaTime) {
        // Update animation
        updateAnimation();
        
        // Update interaction indicator
        updateInteractionIndicator();
    }
    
    private void updateAnimation() {
        if (isMoving) {
            long currentTime = System.nanoTime();
            if (currentTime - lastFrameTime >= FRAME_DURATION) {
                currentFrame = (currentFrame + 1) % 4; // 4-frame walk cycle
                lastFrameTime = currentTime;
                updateSpriteFrame();
            }
        } else {
            currentFrame = 0; // Idle frame
            updateSpriteFrame();
        }
    }
    
    private void updateSpriteFrame() {
        // This would update the sprite based on current frame
        // For now, we'll just rotate the sprite slightly for animation effect
        if (isMoving) {
            sprite.setRotate(currentFrame * 5); // Subtle rotation
        } else {
            sprite.setRotate(0);
        }
    }
    
    private void updateInteractionIndicator() {
        Circle indicator = (Circle) lookup("#interaction-indicator");
        if (indicator != null) {
            indicator.setVisible(isInteracting);
        }
    }
    
    public void setMoving(boolean moving) {
        this.isMoving = moving;
        if (!moving) {
            sprite.setRotate(0);
        }
    }
    
    public void setInteracting(boolean interacting) {
        this.isInteracting = interacting;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public boolean isInteracting() {
        return isInteracting;
    }
    
    public double getCenterX() {
        return getLayoutX() + getPrefWidth() / 2;
    }
    
    public double getCenterY() {
        return getLayoutY() + getPrefHeight() / 2;
    }
    
    public void playInteractionSound() {
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    public void playMovementSound() {
        // Play subtle movement sound occasionally
        if (Math.random() < 0.1) { // 10% chance per update
            // SoundManager.getInstance().playSound("beep-313342.mp3");
        }
    }
    
    public void setPosition(double x, double y) {
        setLayoutX(x - getPrefWidth() / 2);
        setLayoutY(y - getPrefHeight() / 2);
    }
    
    public void setPosition(double x, double y, boolean center) {
        if (center) {
            setLayoutX(x - getPrefWidth() / 2);
            setLayoutY(y - getPrefHeight() / 2);
        } else {
            setLayoutX(x);
            setLayoutY(y);
        }
    }
    
    public void render(Pane gameWorld) {
        gameWorld.getChildren().add(this);
    }
    
    public String getName() {
        return "Player";
    }
}
