package com.coincraft.game.adventure;

import com.coincraft.game.models.GameState;
import com.coincraft.models.User;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for Adventure Mode
 * Integrates Echo Quest-style exploration with Coin Craft's financial education
 */
public class AdventureMode {
    private final Stage primaryStage;
    private AdventureController adventureController;
    private final User user;
    private final GameState gameState;
    
    public AdventureMode(Stage primaryStage, User user, GameState gameState) {
        this.primaryStage = primaryStage;
        this.user = user;
        this.gameState = gameState;
    }
    
    /**
     * Start the adventure mode
     */
    public void startAdventure() {
        System.out.println("=== ADVENTURE MODE STARTING ===");
        System.out.println("User: " + user.getUsername());
        System.out.println("Stage: " + primaryStage);
        
        // Play adventure start sound
        // SoundManager.getInstance().playSound("adventure_start.wav");
        
        // Create adventure controller
        System.out.println("Creating AdventureController...");
        adventureController = new AdventureController(user, gameState);
        
        // Set the scene
        System.out.println("Getting scene from controller...");
        Scene adventureScene = adventureController.getScene();
        System.out.println("Scene obtained: " + adventureScene);
        
        System.out.println("Setting scene on stage...");
        primaryStage.setScene(adventureScene);
        primaryStage.setTitle("Coin Craft Adventure - " + user.getUsername());
        primaryStage.setResizable(false);
        
        // Show the stage
        System.out.println("Showing stage...");
        primaryStage.show();
        
        System.out.println("🎮 Adventure Mode Started!");
        System.out.println("Welcome to the Financial Literacy Adventure, " + user.getUsername() + "!");
        System.out.println("Use WASD or click to move, SPACE to interact!");
        System.out.println("=== ADVENTURE MODE COMPLETE ===");
    }
    
    /**
     * Stop the adventure mode
     */
    public void stopAdventure() {
        if (adventureController != null) {
            adventureController.stopAdventure();
        }
        
        // Play exit sound
        // SoundManager.getInstance().playSound("button_hover.wav");
        
        System.out.println("🎮 Adventure Mode Stopped!");
    }
    
    /**
     * Get the current adventure scene
     */
    public Scene getAdventureScene() {
        if (adventureController != null) {
            return adventureController.getScene();
        }
        return null;
    }
    
    /**
     * Check if adventure mode is currently running
     */
    public boolean isAdventureRunning() {
        return adventureController != null;
    }
}
