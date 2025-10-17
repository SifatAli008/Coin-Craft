package com.coincraft.game.adventure;

import com.coincraft.game.adventure.models.AdventurePlayer;
import com.coincraft.game.adventure.zones.NPCTestZone;
import com.coincraft.game.models.GameState;
import com.coincraft.models.User;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Main controller for the adventure/exploration mode
 * Similar to Echo Quest's exploration mechanics
 */
public class AdventureController {
    private Scene scene;
    private Pane gameWorld;
    private final User user;
    private final GameState gameState;
    
    // Adventure components
    private AdventurePlayer player;
    private NPCTestZone currentZone;
    
    public AdventureController(User user, GameState gameState) {
        this.user = user;
        this.gameState = gameState;
        initializeAdventure();
    }
    
    private void initializeAdventure() {
        System.out.println("=== STARTING ADVENTURE WITH NPCS ===");
        
        // Create the game world
        gameWorld = new Pane();
        gameWorld.setPrefSize(1200, 800);
        gameWorld.setStyle("-fx-background-color: linear-gradient(135deg, #87CEEB 0%, #4682B4 100%);");
        
        System.out.println("Game world created with gradient background");
        
        // Create player
        player = new AdventurePlayer(user.getUsername(), 600, 400);
        System.out.println("Created player: " + player.getName());
        
        // Use gameState for player progress tracking
        System.out.println("Player progress: " + gameState.getCompletedLevels().size() + " levels completed");
        
        // NPCs are managed by the zone
        System.out.println("NPCs will be managed by the zone");
        
        // Create test zone with NPCs
        currentZone = new NPCTestZone();
        System.out.println("Created NPC Test Zone");
        
        // Render the zone (this will add NPCs to the game world)
        currentZone.renderZone(gameWorld);
        System.out.println("Rendered zone with NPCs");
        
        // Add player to game world
        player.render(gameWorld);
        System.out.println("Added player to game world");
        
        // Create scene
        scene = new Scene(gameWorld, 1200, 800);
        scene.setFill(Color.LIGHTBLUE);
        
        // Make sure the scene can receive focus for keyboard events
        gameWorld.setFocusTraversable(true);
        gameWorld.requestFocus();
        
        System.out.println("=== SCENE CREATED WITH NPCS ===");
        System.out.println("Scene size: " + scene.getWidth() + " x " + scene.getHeight());
        System.out.println("Game world children: " + gameWorld.getChildren().size());
        System.out.println("NPCs in zone: " + currentZone.getNPCManager().getNPCCount());
        
        // Handle mouse clicks for NPC interaction
        scene.setOnMouseClicked(event -> {
            System.out.println("*** MOUSE CLICKED ***");
            System.out.println("Position: " + event.getX() + ", " + event.getY());
            System.out.println("Children count: " + gameWorld.getChildren().size());
            
            // Move player to click position
            player.setPosition(event.getX(), event.getY());
            
            // Check for NPC interactions
            currentZone.handlePlayerInteraction(player);
        });
        
        // Handle keyboard input for player movement
        scene.setOnKeyPressed(event -> {
            double moveSpeed = 50.0;
            double newX = player.getCenterX();
            double newY = player.getCenterY();
            
            switch (event.getCode()) {
                case W, UP -> newY -= moveSpeed;
                case S, DOWN -> newY += moveSpeed;
                case A, LEFT -> newX -= moveSpeed;
                case D, RIGHT -> newX += moveSpeed;
                case SPACE -> {
                    // Interact with nearby NPCs
                    currentZone.handlePlayerInteraction(player);
                }
                default -> {}
            }
            
            // Update player position
            player.setPosition(newX, newY);
            
            // Check for NPC interactions after movement
            currentZone.handlePlayerInteraction(player);
        });
        
        System.out.println("=== ADVENTURE WITH NPCS COMPLETE ===");
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void stopAdventure() {
        System.out.println("Adventure stopped");
        
        // Save any progress made during adventure
        if (currentZone != null && currentZone.isCompleted()) {
            System.out.println("Zone completed! Saving progress...");
            // Here you could save progress to gameState
            // gameState.completeAdventureZone(currentZone.getZoneName());
        }
    }
}