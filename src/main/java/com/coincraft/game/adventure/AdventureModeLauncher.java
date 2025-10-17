package com.coincraft.game.adventure;

import com.coincraft.game.models.GameState;
import com.coincraft.models.User;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Simple launcher for Adventure Mode with NPCs
 * This can be called from the main game to test NPCs
 */
public class AdventureModeLauncher {
    
    public static void launchAdventureMode(User user, GameState gameState) {
        System.out.println("🚀 Launching Adventure Mode with NPCs");
        System.out.println("User: " + user.getUsername());
        System.out.println("GameState: " + gameState);
        
        try {
            // Create a new stage for adventure mode
            Stage adventureStage = new Stage();
            adventureStage.setTitle("Coin Craft Adventure - NPC Test");
            adventureStage.setWidth(1200);
            adventureStage.setHeight(800);
            
            System.out.println("Created adventure stage");
            
            // Create adventure controller
            System.out.println("Creating AdventureController...");
            AdventureController controller = new AdventureController(user, gameState);
            System.out.println("AdventureController created successfully");
            
            // Set the scene
            System.out.println("Setting scene...");
            adventureStage.setScene(controller.getScene());
            System.out.println("Scene set successfully");
            
            // Show the stage
            System.out.println("Showing stage...");
            adventureStage.show();
            
            System.out.println("✅ Adventure Mode launched with NPCs!");
            System.out.println("🎮 Use WASD to move, SPACE to interact with NPCs");
            System.out.println("🖱️ Click to move player, click near NPCs to interact");
            
        } catch (Exception e) {
            System.err.println("❌ Error in AdventureModeLauncher: " + e.getMessage());
            // Log the error details for debugging
            System.err.println("Error details: " + e.getClass().getSimpleName());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("  at " + element);
            }
        }
    }
    
    /**
     * Create a simple test button that launches adventure mode
     */
    public static Button createAdventureButton(User user, GameState gameState) {
        Button adventureButton = new Button("🎮 Test Adventure Mode with NPCs");
        adventureButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        adventureButton.setOnAction(e -> {
            System.out.println("🎮 Launching Adventure Mode from button click");
            launchAdventureMode(user, gameState);
        });
        
        return adventureButton;
    }
}
