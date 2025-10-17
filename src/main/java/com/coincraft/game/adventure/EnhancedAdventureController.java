package com.coincraft.game.adventure;

import com.coincraft.game.models.GameState;
import com.coincraft.models.User;
import com.coincraft.game.adventure.models.*;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced Adventure Controller with integrated conversation systems,
 * personality-based NPCs, and advanced quiz functionality
 */
public class EnhancedAdventureController {
    private final User user;
    private Scene adventureScene;
    private Pane gameWorld;
    private AdventurePlayer player;
    
    // Enhanced NPCs
    private List<AdventureNPC> npcs;
    
    // Game loop
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    private long lastUpdateTime = 0;
    
    // UI Elements
    private Text playerInfo;
    private Text interactionPrompt;
    
    public EnhancedAdventureController(User user, GameState gameState) {
        this.user = user;
        initializeGame();
    }
    
    /**
     * Initialize the enhanced adventure game
     */
    private void initializeGame() {
        System.out.println("🎮 Initializing Enhanced Adventure Controller...");
        
        // Create game world
        gameWorld = new Pane();
        gameWorld.setPrefSize(1200, 800);
        gameWorld.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);");
        
        // Create player
        player = new AdventurePlayer("Player", 600, 400);
        player.render(gameWorld);
        
        // Initialize NPCs
        initializeNPCs();
        
        // Initialize UI
        initializeUI();
        
        // Create scene
        adventureScene = new Scene(gameWorld);
        setupInputHandlers();
        
        // Start game loop
        startGameLoop();
        
        System.out.println("🎮 Enhanced Adventure Controller initialized successfully!");
    }
    
    /**
     * Initialize NPCs
     */
    private void initializeNPCs() {
        System.out.println("👥 Initializing NPCs...");
        
        npcs = new ArrayList<>();
        
        // Create NPCs
        createNPCs();
        
        System.out.println("👥 NPCs initialized: " + npcs.size() + " NPCs");
    }
    
    /**
     * Create NPCs
     */
    private void createNPCs() {
        // Strong Adventurer NPC
        AdventureNPC strongAdventurer = new AdventureNPC("Thorin Ironbeard", "🗡️", 
            "Welcome, brave adventurer! I'll guide you through this financial realm!", 200, 200);
        npcs.add(strongAdventurer);
        
        // Wise Lady NPC
        AdventureNPC wiseLady = new AdventureNPC("Sage Wisdomheart", "🧙‍♀️", 
            "Greetings, young learner. I shall impart the ancient wisdom of financial literacy!", 400, 300);
        npcs.add(wiseLady);
        
        // Smart Businessman NPC
        AdventureNPC smartBusinessman = new AdventureNPC("Alexander Venture", "💼", 
            "Ah, a potential business partner! Test your financial knowledge with my quiz!", 600, 200);
        npcs.add(smartBusinessman);
        
        System.out.println("👥 Created " + npcs.size() + " NPCs");
    }
    
    /**
     * Initialize UI elements
     */
    private void initializeUI() {
        System.out.println("🖥️ Initializing enhanced UI...");
        
        // Player info display
        playerInfo = new Text("Player: " + user.getUsername());
        playerInfo.setFont(Font.font("Arial", 14));
        playerInfo.setFill(Color.WHITE);
        playerInfo.setLayoutX(10);
        playerInfo.setLayoutY(30);
        gameWorld.getChildren().add(playerInfo);
        
        // Interaction prompt
        interactionPrompt = new Text("Press SPACE to interact with NPCs");
        interactionPrompt.setFont(Font.font("Arial", 12));
        interactionPrompt.setFill(Color.LIGHTGRAY);
        interactionPrompt.setLayoutX(10);
        interactionPrompt.setLayoutY(50);
        gameWorld.getChildren().add(interactionPrompt);
        
        System.out.println("🖥️ Enhanced UI initialized!");
    }
    
    /**
     * Setup input handlers for enhanced interactions
     */
    private void setupInputHandlers() {
        adventureScene.setOnKeyPressed(this::handleKeyPress);
        adventureScene.setOnKeyReleased(this::handleKeyRelease);
        adventureScene.setOnMouseClicked(this::handleMouseClick);
        
        // Focus the scene to receive key events
        adventureScene.getRoot().requestFocus();
    }
    
    /**
     * Handle key press events
     */
    private void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        
        switch (keyCode) {
            case W, UP -> {
                // Move player up
                player.moveTo(player.getCenterX(), player.getCenterY() - 20);
            }
            case S, DOWN -> {
                // Move player down
                player.moveTo(player.getCenterX(), player.getCenterY() + 20);
            }
            case A, LEFT -> {
                // Move player left
                player.moveTo(player.getCenterX() - 20, player.getCenterY());
            }
            case D, RIGHT -> {
                // Move player right
                player.moveTo(player.getCenterX() + 20, player.getCenterY());
            }
            case SPACE -> {
                handleInteraction();
            }
            case ESCAPE -> {
                // Handle escape key (could open menu, etc.)
            }
            default -> {
                // Ignore other keys
            }
        }
        
        // Update player position
        player.update(0.016); // 60 FPS
    }
    
    /**
     * Handle key release events
     */
    private void handleKeyRelease(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        
        switch (keyCode) {
            case W, UP, S, DOWN, A, LEFT, D, RIGHT -> {
                // Player stops moving when key is released
            }
            default -> {
                // Ignore other keys
            }
        }
    }
    
    /**
     * Handle mouse click events
     */
    private void handleMouseClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        
        // Move player towards click location
        player.moveTo(mouseX, mouseY);
    }
    
    /**
     * Handle player interactions with NPCs
     */
    private void handleInteraction() {
        System.out.println("🎯 Handling player interaction...");
        
        if (npcs != null && !npcs.isEmpty()) {
            // Find the closest NPC
            AdventureNPC closestNPC = null;
            double closestDistance = Double.MAX_VALUE;
            
            for (AdventureNPC npc : npcs) {
                double distance = Math.sqrt(
                    Math.pow(player.getCenterX() - npc.getCenterX(), 2) + 
                    Math.pow(player.getCenterY() - npc.getCenterY(), 2)
                );
                
                if (distance < closestDistance && distance < 100) { // 100 pixel interaction radius
                    closestDistance = distance;
                    closestNPC = npc;
                }
            }
            
            if (closestNPC != null) {
                System.out.println("💬 Interacting with: " + closestNPC.getName());
                closestNPC.interact(player);
            } else {
                System.out.println("❌ No NPCs nearby to interact with");
            }
        }
    }
    
    /**
     * Start the enhanced game loop
     */
    private void startGameLoop() {
        System.out.println("🔄 Starting enhanced game loop...");
        
        isRunning = true;
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    updateGame(now);
                }
            }
        };
        
        gameLoop.start();
        System.out.println("🔄 Enhanced game loop started!");
    }
    
    /**
     * Update game state
     */
    private void updateGame(long currentTime) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = currentTime;
            return;
        }
        
        double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0; // Convert to seconds
        lastUpdateTime = currentTime;
        
        // Update player
        player.update(deltaTime);
        
        // Update NPCs
        if (npcs != null) {
            for (AdventureNPC npc : npcs) {
                npc.update(deltaTime, player);
            }
        }
        
        // Update UI
        updateUI();
    }
    
    /**
     * Update UI elements
     */
    private void updateUI() {
        // Update player info
        if (playerInfo != null) {
            playerInfo.setText("Player: " + user.getUsername() + 
                             " | Position: (" + (int)player.getCenterX() + ", " + (int)player.getCenterY() + ")");
        }
        
        // Update interaction prompt based on nearby NPCs
        if (interactionPrompt != null && npcs != null) {
            AdventureNPC nearbyNPC = null;
            double closestDistance = Double.MAX_VALUE;
            
            for (AdventureNPC npc : npcs) {
                double distance = Math.sqrt(
                    Math.pow(player.getCenterX() - npc.getCenterX(), 2) + 
                    Math.pow(player.getCenterY() - npc.getCenterY(), 2)
                );
                
                if (distance < closestDistance && distance < 100) {
                    closestDistance = distance;
                    nearbyNPC = npc;
                }
            }
            
            if (nearbyNPC != null) {
                interactionPrompt.setText("Press SPACE to interact with " + nearbyNPC.getName());
                interactionPrompt.setFill(Color.YELLOW);
            } else {
                interactionPrompt.setText("Press SPACE to interact with NPCs");
                interactionPrompt.setFill(Color.LIGHTGRAY);
            }
        }
    }
    
    /**
     * Stop the adventure
     */
    public void stopAdventure() {
        System.out.println("🛑 Stopping enhanced adventure...");
        
        isRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
        // Cleanup NPCs
        if (npcs != null) {
            for (AdventureNPC npc : npcs) {
                npc.cleanup();
            }
        }
        
        System.out.println("🛑 Enhanced adventure stopped!");
    }
    
    /**
     * Get the adventure scene
     */
    public Scene getScene() {
        return adventureScene;
    }
    
    /**
     * Check if adventure is running
     */
    public boolean isAdventureRunning() {
        return isRunning;
    }
    
    /**
     * Get player instance
     */
    public AdventurePlayer getPlayer() {
        return player;
    }
    
    
    /**
     * Get all NPCs
     */
    public List<AdventureNPC> getNPCs() {
        return npcs;
    }
}
