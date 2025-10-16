package com.coincraft.game.adventure.models;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Echo Quest Player Character
 * Represents Echo, the traveler on a journey of self-discovery
 * Inspired by the therapeutic RPG mechanics from Echo Quest
 */
public class EchoQuestPlayer extends Pane {
    private ImageView sprite;
    private final String avatarPath;
    private boolean isMoving = false;
    private boolean isInteracting = false;
    private double targetX, targetY;
    private final double moveSpeed = 2.0; // Peaceful, slower movement
    
    // Animation states for therapeutic gameplay
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final long FRAME_DURATION = 300_000_000; // 300ms for peaceful animation
    
    // Echo Quest specific properties
    private int emotionalBalance = 50; // Starts at neutral
    private boolean isInMeditation = false;
    
    public EchoQuestPlayer(String avatarPath) {
        this.avatarPath = avatarPath;
        initializeEcho();
    }
    
    private void initializeEcho() {
        setPrefSize(64, 64);
        
        // Create Echo's sprite
        sprite = new ImageView();
        sprite.setFitWidth(48);
        sprite.setFitHeight(48);
        sprite.setPreserveRatio(true);
        
        // Load Echo's sprite from Assets
        try {
            String echoSpritePath = getClass().getResource("/Assets/Sprites/Player/Side animations/spr_player_idle.png").toExternalForm();
            Image echoImage = new Image(echoSpritePath);
            sprite.setImage(echoImage);
        } catch (Exception e) {
            // Fallback to avatar image
            try {
                Image avatarImage = new Image(avatarPath);
                sprite.setImage(avatarImage);
            } catch (Exception e2) {
                // Final fallback to default sprite
                createDefaultEchoSprite();
            }
        }
        
        getChildren().add(sprite);
        
        // Center the sprite
        sprite.setLayoutX(8);
        sprite.setLayoutY(8);
        
        // Add therapeutic interaction indicator
        Circle interactionIndicator = new Circle(32, 32, 20);
        interactionIndicator.setFill(Color.TRANSPARENT);
        interactionIndicator.setStroke(Color.LIGHTBLUE);
        interactionIndicator.setStrokeWidth(2);
        interactionIndicator.setVisible(false);
        interactionIndicator.setId("therapeutic-interaction-indicator");
        getChildren().add(interactionIndicator);
    }
    
    private void createDefaultEchoSprite() {
        // Create a simple colored circle as fallback for Echo
        Circle echoCircle = new Circle(20, Color.LIGHTBLUE);
        echoCircle.setStroke(Color.DARKBLUE);
        echoCircle.setStrokeWidth(2);
        getChildren().add(echoCircle);
    }
    
    public void moveTo(double x, double y) {
        targetX = x;
        targetY = y;
        isMoving = true;
        
        // Start peaceful movement animation
        startMovementAnimation();
    }
    
    private void startMovementAnimation() {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isMoving) {
                    stop();
                    return;
                }
                
                double currentX = getLayoutX();
                double currentY = getLayoutY();
                
                double dx = targetX - currentX;
                double dy = targetY - currentY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance < 5) {
                    // Reached destination
                    isMoving = false;
                    stop();
                    return;
                }
                
                // Peaceful movement towards target
                double moveX = (dx / distance) * moveSpeed;
                double moveY = (dy / distance) * moveSpeed;
                
                setLayoutX(currentX + moveX);
                setLayoutY(currentY + moveY);
            }
        };
        moveTimer.start();
    }
    
    public void updateAnimation() {
        long currentTime = System.nanoTime();
        if (currentTime - lastFrameTime >= FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % 4; // Simple 4-frame animation
            lastFrameTime = currentTime;
            
            // Update sprite based on movement and emotional state
            updateEchoSprite();
        }
    }
    
    private void updateEchoSprite() {
        // Update Echo's appearance based on emotional state
        if (isInMeditation) {
            // Calm, peaceful appearance
            sprite.setOpacity(0.8);
        } else if (emotionalBalance > 70) {
            // Happy, bright appearance
            sprite.setOpacity(1.0);
        } else if (emotionalBalance < 30) {
            // Sad, subdued appearance
            sprite.setOpacity(0.6);
        } else {
            // Neutral appearance
            sprite.setOpacity(0.9);
        }
    }
    
    public void startMeditation() {
        isInMeditation = true;
        emotionalBalance = Math.min(100, emotionalBalance + 5);
        System.out.println("🧘 Echo begins meditation. Emotional balance: " + emotionalBalance);
    }
    
    public void endMeditation() {
        isInMeditation = false;
        System.out.println("✨ Meditation complete. Inner peace restored.");
    }
    
    public void interactWithTherapeuticElement() {
        // Echo interacts with therapeutic elements
        emotionalBalance = Math.min(100, emotionalBalance + 10);
        System.out.println("🌟 Therapeutic interaction. Emotional balance: " + emotionalBalance);
    }
    
    // Getters and setters
    public int getEmotionalBalance() {
        return emotionalBalance;
    }
    
    public void setEmotionalBalance(int balance) {
        this.emotionalBalance = Math.max(0, Math.min(100, balance));
    }
    
    public boolean isInMeditation() {
        return isInMeditation;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public boolean isInteracting() {
        return isInteracting;
    }
    
    public void setInteracting(boolean interacting) {
        this.isInteracting = interacting;
    }
}
