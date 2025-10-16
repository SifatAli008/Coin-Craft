package com.coincraft.game.adventure.ui;

import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.coincraft.models.User;
import com.coincraft.game.models.GameState;
import com.coincraft.game.adventure.EchoQuestController;

/**
 * Echo Quest Therapeutic UI
 * Provides mindfulness tools, emotional tracking, and therapeutic guidance
 * Inspired by the peaceful, healing-focused interface of Echo Quest
 */
public class EchoQuestUI {
    private final EchoQuestController controller;
    
    // UI Components
    private HBox topBar;
    private HBox bottomBar;
    private Button meditationButton;
    private Button mindfulnessButton;
    private Button emotionalCheckButton;
    
    public EchoQuestUI(User user, GameState gameState, EchoQuestController controller) {
        this.controller = controller;
    }
    
    public void setupUI(Pane gameWorld) {
        createTopBar();
        createBottomBar();
        
        // Add UI to game world
        gameWorld.getChildren().add(topBar);
        gameWorld.getChildren().add(bottomBar);
    }
    
    private void createTopBar() {
        topBar = new HBox(20);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: rgba(135, 206, 235, 0.8); -fx-background-radius: 10;");
        
        // Therapeutic progress display
        Label mindShardsLabel = new Label("Mind Shards: " + controller.getMindShardsCollected());
        mindShardsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");
        
        Label emotionalClarityLabel = new Label("Emotional Clarity: " + controller.getEmotionalClarity());
        emotionalClarityLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");
        
        Label currentZoneLabel = new Label("Current Zone: Whispering Woods");
        currentZoneLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold;");
        
        topBar.getChildren().addAll(mindShardsLabel, emotionalClarityLabel, currentZoneLabel);
        
        // Position at top
        topBar.setLayoutX(10);
        topBar.setLayoutY(10);
    }
    
    private void createBottomBar() {
        bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: rgba(152, 251, 152, 0.8); -fx-background-radius: 10;");
        
        // Therapeutic control buttons
        meditationButton = new Button("🧘 Meditation");
        meditationButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");
        meditationButton.setOnAction(e -> showMeditationMenu());
        
        mindfulnessButton = new Button("🌿 Mindfulness");
        mindfulnessButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14;");
        mindfulnessButton.setOnAction(e -> showMindfulnessMap());
        
        emotionalCheckButton = new Button("💚 Emotional Check");
        emotionalCheckButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14;");
        emotionalCheckButton.setOnAction(e -> showEmotionalCheck());
        
        bottomBar.getChildren().addAll(meditationButton, mindfulnessButton, emotionalCheckButton);
        
        // Position at bottom
        bottomBar.setLayoutX(10);
        bottomBar.setLayoutY(750);
    }
    
    public void updateUI() {
        // Update therapeutic progress display
        if (topBar.getChildren().size() >= 3) {
            Label mindShardsLabel = (Label) topBar.getChildren().get(0);
            mindShardsLabel.setText("Mind Shards: " + controller.getMindShardsCollected());
            
            Label emotionalClarityLabel = (Label) topBar.getChildren().get(1);
            emotionalClarityLabel.setText("Emotional Clarity: " + controller.getEmotionalClarity());
        }
    }
    
    public void showMeditationMenu() {
        System.out.println("🧘 Meditation Menu");
        System.out.println("Choose your meditation practice:");
        System.out.println("1. Breathing Exercise (5 minutes)");
        System.out.println("2. Body Scan (10 minutes)");
        System.out.println("3. Loving-Kindness Meditation (15 minutes)");
        System.out.println("4. Mindful Walking (20 minutes)");
    }
    
    public void showMindfulnessMap() {
        System.out.println("🌿 Mindfulness Map");
        System.out.println("Your journey through emotional healing:");
        System.out.println("✅ Whispering Woods - Mindfulness and awareness");
        System.out.println("🔒 Storm Valley - Managing anxiety and panic");
        System.out.println("🔒 Shifting Sands - Dealing with change");
        System.out.println("🔒 Sanctuary City - Building self-worth");
    }
    
    public void showEmotionalCheck() {
        System.out.println("💚 Emotional Check-in");
        System.out.println("How are you feeling right now?");
        System.out.println("Take a moment to notice your emotions without judgment.");
        System.out.println("Remember: All emotions are valid and temporary.");
    }
    
}
