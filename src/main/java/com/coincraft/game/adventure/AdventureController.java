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
import com.coincraft.game.adventure.models.AdventurePlayer;
import com.coincraft.game.adventure.models.AdventureZone;
import com.coincraft.game.adventure.ui.AdventureUI;

/**
 * Main controller for the adventure/exploration mode
 * Similar to Echo Quest's exploration mechanics
 */
public class AdventureController {
    private Scene scene;
    private Pane gameWorld;
    private AdventurePlayer player;
    private AdventureZone currentZone;
    private AdventureUI ui;
    private final User user;
    private final GameState gameState;
    
    // Movement state
    private final boolean[] keysPressed = new boolean[256];
    private final double playerSpeed = 3.0;
    
    // Animation
    private AnimationTimer gameLoop;
    private long lastUpdateTime = 0;
    
    public AdventureController(User user, GameState gameState) {
        this.user = user;
        this.gameState = gameState;
        initializeAdventure();
    }
    
    private void initializeAdventure() {
        // Create the game world
        gameWorld = new Pane();
        gameWorld.setPrefSize(1200, 800);
        gameWorld.setStyle("-fx-background-color: #2c3e50;");
        
        // Initialize player
        player = new AdventurePlayer("/images/coincraft-icon.png"); // Default avatar
        player.setLayoutX(600);
        player.setLayoutY(400);
        gameWorld.getChildren().add(player);
        
        // Load starting zone
        currentZone = new com.coincraft.game.adventure.zones.BudgetBayZone();
        currentZone.renderZone(gameWorld);
        
        // Initialize UI overlay
        ui = new AdventureUI(user, gameState);
        ui.setupUI(gameWorld);
        
        // Create scene first
        scene = new Scene(gameWorld, 1200, 800);
        scene.setFill(Color.DARKBLUE);
        
        // Setup input handling after scene is created
        setupInputHandling();
        
        // Start game loop
        startGameLoop();
    }
    
    private void setupInputHandling() {
        // Keyboard input
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
        
        // Handle special keys
        switch (keyCode) {
            case SPACE -> handleInteraction();
            case ESCAPE -> showPauseMenu();
            case TAB -> showMap();
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
            // Click to move
            double targetX = event.getX();
            double targetY = event.getY();
            player.moveTo(targetX, targetY);
        }
    }
    
    private void handleInteraction() {
        // Check for nearby NPCs or interactables
        if (currentZone != null) {
            currentZone.handlePlayerInteraction(player);
        }
        
        // Play interaction sound
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    private void showPauseMenu() {
        ui.showPauseMenu();
    }
    
    private void showMap() {
        ui.showZoneMap(currentZone);
    }
    
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    return;
                }
                
                double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0; // Convert to seconds
                lastUpdateTime = now;
                
                updateGame(deltaTime);
            }
        };
        gameLoop.start();
    }
    
    private void updateGame(double deltaTime) {
        // Handle movement
        handleMovement(deltaTime);
        
        // Update player animation
        player.update(deltaTime);
        
        // Update zone (NPCs, effects, etc.)
        if (currentZone != null) {
            currentZone.update(deltaTime, player);
        }
        
        // Update UI
        ui.updateUI();
    }
    
    private void handleMovement(double deltaTime) {
        double moveX = 0;
        double moveY = 0;
        
        // WASD movement
        if (keysPressed[KeyCode.W.getCode()] || keysPressed[KeyCode.UP.getCode()]) {
            moveY -= playerSpeed;
        }
        if (keysPressed[KeyCode.S.getCode()] || keysPressed[KeyCode.DOWN.getCode()]) {
            moveY += playerSpeed;
        }
        if (keysPressed[KeyCode.A.getCode()] || keysPressed[KeyCode.LEFT.getCode()]) {
            moveX -= playerSpeed;
        }
        if (keysPressed[KeyCode.D.getCode()] || keysPressed[KeyCode.RIGHT.getCode()]) {
            moveX += playerSpeed;
        }
        
        // Apply movement
        if (moveX != 0 || moveY != 0) {
            double newX = player.getLayoutX() + moveX * deltaTime * 60; // 60 FPS normalization
            double newY = player.getLayoutY() + moveY * deltaTime * 60;
            
            // Boundary checking
            newX = Math.max(0, Math.min(gameWorld.getWidth() - player.getBoundsInLocal().getWidth(), newX));
            newY = Math.max(0, Math.min(gameWorld.getHeight() - player.getBoundsInLocal().getHeight(), newY));
            
            player.setLayoutX(newX);
            player.setLayoutY(newY);
            
            player.setMoving(true);
        } else {
            player.setMoving(false);
        }
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void stopAdventure() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
    
    public void changeZone(AdventureZone newZone) {
        if (currentZone != null) {
            currentZone.cleanup();
        }
        
        currentZone = newZone;
        currentZone.renderZone(gameWorld);
        
        // Update UI
        ui.updateZoneInfo(currentZone);
        
        // Play zone transition sound
        // SoundManager.getInstance().playSound("adventure_start.wav");
    }
}
