package com.coincraft.game.adventure;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import com.coincraft.models.User;
import com.coincraft.game.models.GameState;
import com.coincraft.game.adventure.models.EchoQuestPlayer;
import com.coincraft.game.adventure.ui.EchoQuestUI;
import com.coincraft.game.adventure.zones.WhisperingWoodsZone;

/**
 * Echo Quest Style Controller
 * Implements therapeutic gameplay mechanics inspired by https://github.com/SifatAli008/Echo-Quest
 * 
 * Core Features:
 * - Peaceful exploration mechanics
 * - Mind Shard collection system
 * - Therapeutic dialogue and interactions
 * - Emotional healing through gameplay
 * - Mindfulness and self-discovery themes
 */
public class EchoQuestController {
    private Scene scene;
    private Pane gameWorld;
    private EchoQuestPlayer player;
    private WhisperingWoodsZone currentZone;
    private EchoQuestUI ui;
    private final User user;
    private final GameState gameState;
    
    // Movement state
    private final boolean[] keysPressed = new boolean[256];
    private final double playerSpeed = 2.5; // Slower, more peaceful movement
    
    // Echo Quest specific mechanics
    private int mindShardsCollected = 0;
    private int emotionalClarity = 0;
    private boolean isInMeditationMode = false;
    
    // Animation
    private AnimationTimer gameLoop;
    private long lastUpdateTime = 0;
    
    public EchoQuestController(User user, GameState gameState) {
        this.user = user;
        this.gameState = gameState;
        initializeEchoQuest();
    }
    
    private void initializeEchoQuest() {
        // Create the therapeutic game world
        gameWorld = new Pane();
        gameWorld.setPrefSize(1200, 800);
        gameWorld.setStyle("-fx-background-color: linear-gradient(135deg, #87CEEB 0%, #98FB98 50%, #DDA0DD 100%);");
        
        // Initialize Echo (the player character)
        player = new EchoQuestPlayer("/Assets/Sprites/Player/Side animations/spr_player_idle.png");
        player.setLayoutX(600);
        player.setLayoutY(400);
        gameWorld.getChildren().add(player);
        
        // Load starting zone - Whispering Woods (mindfulness theme)
        currentZone = new WhisperingWoodsZone();
        currentZone.renderZone(gameWorld);
        
        // Initialize therapeutic UI overlay
        ui = new EchoQuestUI(user, gameState, this);
        ui.setupUI(gameWorld);
        
        // Create scene first
        scene = new Scene(gameWorld, 1200, 800);
        scene.setFill(Color.LIGHTBLUE);
        
        // Setup input handling after scene is created
        setupInputHandling();
        
        // Start therapeutic game loop
        startGameLoop();
    }
    
    private void setupInputHandling() {
        // Keyboard input for peaceful movement
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
        
        // Mouse input for click-to-move
        scene.setOnMouseClicked(this::handleMouseClick);
        
        // Focus for keyboard input
        scene.setOnMouseEntered(e -> gameWorld.requestFocus());
    }
    
    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        int code = keyCode.getCode();
        if (code >= 0 && code < keysPressed.length) {
            keysPressed[code] = true;
        }
        
        // Handle therapeutic interactions
        switch (keyCode) {
            case SPACE -> handleTherapeuticInteraction();
            case ESCAPE -> showMeditationMenu();
            case TAB -> showMindfulnessMap();
            case M -> toggleMeditationMode();
            default -> {
                // No special action for other keys
            }
        }
    }
    
    private void handleKeyReleased(KeyEvent event) {
        int code = event.getCode().getCode();
        if (code >= 0 && code < keysPressed.length) {
            keysPressed[code] = false;
        }
    }
    
    private void handleMouseClick(MouseEvent event) {
        if (event.getButton().toString().equals("PRIMARY")) {
            // Click to move - peaceful movement
            double targetX = event.getX();
            double targetY = event.getY();
            player.moveTo(targetX, targetY);
        }
    }
    
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    return;
                }
                
                lastUpdateTime = now;
                
                updateGame();
            }
        };
        gameLoop.start();
    }
    
    private void updateGame() {
        // Update player movement
        updatePlayerMovement();
        
        // Update player animation
        player.updateAnimation();
        
        // Update therapeutic elements
        updateTherapeuticElements();
        
        // Update UI
        ui.updateUI();
    }
    
    private void updatePlayerMovement() {
        double moveX = 0;
        double moveY = 0;
        
        // WASD movement with peaceful, slower pace
        if (keysPressed[KeyCode.W.getCode()]) moveY -= playerSpeed * 0.8; // Slower upward movement
        if (keysPressed[KeyCode.S.getCode()]) moveY += playerSpeed * 0.8; // Slower downward movement
        if (keysPressed[KeyCode.A.getCode()]) moveX -= playerSpeed * 0.8; // Slower left movement
        if (keysPressed[KeyCode.D.getCode()]) moveX += playerSpeed * 0.8; // Slower right movement
        
        // Apply movement if any key is pressed
        if (moveX != 0 || moveY != 0) {
            double newX = player.getLayoutX() + moveX;
            double newY = player.getLayoutY() + moveY;
            
            // Keep player within bounds
            newX = Math.max(0, Math.min(gameWorld.getPrefWidth() - player.getWidth(), newX));
            newY = Math.max(0, Math.min(gameWorld.getPrefHeight() - player.getHeight(), newY));
            
            player.setLayoutX(newX);
            player.setLayoutY(newY);
        }
    }
    
    private void updateTherapeuticElements() {
        // Update meditation mode effects
        if (isInMeditationMode) {
            // Add peaceful visual effects
            // This would include breathing exercises, calming animations, etc.
        }
        
        // Update emotional clarity based on interactions
        // This would track the player's emotional journey
    }
    
    private void handleTherapeuticInteraction() {
        // Handle interactions with therapeutic elements
        // This includes talking to NPCs, collecting Mind Shards, etc.
        System.out.println("🌟 Therapeutic interaction triggered");
    }
    
    private void showMeditationMenu() {
        // Show meditation and mindfulness menu
        ui.showMeditationMenu();
    }
    
    private void showMindfulnessMap() {
        // Show map of emotional zones and progress
        ui.showMindfulnessMap();
    }
    
    private void toggleMeditationMode() {
        isInMeditationMode = !isInMeditationMode;
        System.out.println("🧘 Meditation mode: " + (isInMeditationMode ? "ON" : "OFF"));
    }
    
    // Getters for therapeutic progress
    public int getMindShardsCollected() {
        return mindShardsCollected;
    }
    
    public int getEmotionalClarity() {
        return emotionalClarity;
    }
    
    public boolean isInMeditationMode() {
        return isInMeditationMode;
    }
    
    public void collectMindShard() {
        mindShardsCollected++;
        emotionalClarity += 10;
        System.out.println("✨ Mind Shard collected! Total: " + mindShardsCollected);
        System.out.println("🌟 Emotional Clarity: " + emotionalClarity);
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void stopAdventure() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        System.out.println("🌟 Echo Quest adventure stopped. Inner peace achieved.");
    }
}
