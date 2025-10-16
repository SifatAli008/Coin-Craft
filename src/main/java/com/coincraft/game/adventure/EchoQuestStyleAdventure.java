package com.coincraft.game.adventure;

import com.coincraft.game.models.GameState;
import com.coincraft.models.User;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Echo Quest Style Adventure Mode
 * Inspired by the therapeutic 2D pixel art RPG from https://github.com/SifatAli008/Echo-Quest
 * 
 * Features:
 * - Peaceful exploration mechanics
 * - Therapeutic gameplay focused on mental health awareness
 * - 2D pixel art aesthetic
 * - Mind Shard collection system (adapted as SmartCoins)
 * - Emotional healing through gameplay
 */
public class EchoQuestStyleAdventure {
    private final Stage primaryStage;
    private EchoQuestController echoQuestController;
    private final User user;
    private final GameState gameState;
    
    public EchoQuestStyleAdventure(Stage primaryStage, User user, GameState gameState) {
        this.primaryStage = primaryStage;
        this.user = user;
        this.gameState = gameState;
    }
    
    /**
     * Start the Echo Quest style adventure
     * Journey to Inner Peace - Therapeutic RPG Experience
     */
    public void startEchoQuestAdventure() {
        System.out.println("🌟 Echo Quest Style Adventure Starting...");
        System.out.println("Journey to Inner Peace - Therapeutic RPG Experience");
        
        // Create Echo Quest controller
        echoQuestController = new EchoQuestController(user, gameState);
        
        // Set the scene with therapeutic styling
        Scene echoQuestScene = echoQuestController.getScene();
        primaryStage.setScene(echoQuestScene);
        primaryStage.setTitle("Echo Quest: Journey to Inner Peace - " + user.getUsername());
        primaryStage.setResizable(false);
        
        // Show the stage
        primaryStage.show();
        
        System.out.println("🌟 Echo Quest Adventure Started!");
        System.out.println("Welcome to your journey of self-discovery, " + user.getUsername() + "!");
        System.out.println("Collect Mind Shards to restore balance and inner peace.");
        System.out.println("Use WASD or click to move, SPACE to interact!");
    }
    
    /**
     * Stop the Echo Quest adventure
     */
    public void stopEchoQuestAdventure() {
        if (echoQuestController != null) {
            echoQuestController.stopAdventure();
        }
        
        System.out.println("🌟 Echo Quest Adventure ended. May your inner peace continue.");
    }
}
