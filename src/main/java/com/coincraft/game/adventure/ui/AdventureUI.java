package com.coincraft.game.adventure.ui;

import com.coincraft.game.adventure.models.AdventureZone;
import com.coincraft.game.models.GameState;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * UI overlay for adventure mode
 * Similar to Echo Quest's UI but with financial education elements
 */
public class AdventureUI {
    private final User user;
    private final GameState gameState;
    private Pane gameWorld;
    
    // UI Components
    private VBox topBar;
    private HBox bottomBar;
    private Label coinLabel;
    private Label levelLabel;
    private ProgressBar progressBar;
    private Label zoneLabel;
    private Button pauseButton;
    private Button mapButton;
    private Button inventoryButton;
    
    public AdventureUI(User user, GameState gameState) {
        this.user = user;
        this.gameState = gameState;
    }
    
    public void setupUI(Pane gameWorld) {
        this.gameWorld = gameWorld;
        createTopBar();
        createBottomBar();
        addUIElements();
    }
    
    private void createTopBar() {
        topBar = new VBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10;");
        
        // Coin and level info
        HBox infoBar = new HBox(20);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        
        coinLabel = new Label("💰 SmartCoins: " + user.getSmartCoins());
        coinLabel.setFont(Font.font("Arial", 16));
        coinLabel.setTextFill(Color.WHITE);
        
        levelLabel = new Label("📊 Level: " + gameState.getCurrentLevel());
        levelLabel.setFont(Font.font("Arial", 16));
        levelLabel.setTextFill(Color.WHITE);
        
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        progressBar.setProgress(0.3); // Example progress
        
        infoBar.getChildren().addAll(coinLabel, levelLabel, progressBar);
        
        // Zone info
        zoneLabel = new Label("🏝️ Current Zone: Budget Bay");
        zoneLabel.setFont(Font.font("Arial", 14));
        zoneLabel.setTextFill(Color.LIGHTBLUE);
        
        topBar.getChildren().addAll(infoBar, zoneLabel);
        
        // Position at top
        topBar.setLayoutX(10);
        topBar.setLayoutY(10);
        gameWorld.getChildren().add(topBar);
    }
    
    private void createBottomBar() {
        bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10;");
        
        // Control buttons with Assets UI sprites
        pauseButton = new Button("⏸️ Pause");
        pauseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");
        pauseButton.setOnAction(e -> showPauseMenu());
        
        mapButton = new Button("🗺️ Map");
        mapButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14;");
        mapButton.setOnAction(e -> showZoneMap(null));
        
        inventoryButton = new Button("🎒 Inventory");
        inventoryButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14;");
        inventoryButton.setOnAction(e -> showInventory());
        
        bottomBar.getChildren().addAll(pauseButton, mapButton, inventoryButton);
        
        // Position at bottom
        bottomBar.setLayoutX(10);
        bottomBar.setLayoutY(gameWorld.getHeight() - 60);
        gameWorld.getChildren().add(bottomBar);
    }
    
    private void addUIElements() {
        // Add interaction prompt
        Text interactionPrompt = new Text("Press SPACE to interact");
        interactionPrompt.setFont(Font.font("Arial", 12));
        interactionPrompt.setFill(Color.YELLOW);
        interactionPrompt.setLayoutX(50);
        interactionPrompt.setLayoutY(gameWorld.getHeight() - 100);
        interactionPrompt.setId("interaction-prompt");
        gameWorld.getChildren().add(interactionPrompt);
        
        // Add movement instructions
        Text movementInstructions = new Text("Use WASD or click to move");
        movementInstructions.setFont(Font.font("Arial", 10));
        movementInstructions.setFill(Color.LIGHTGRAY);
        movementInstructions.setLayoutX(50);
        movementInstructions.setLayoutY(gameWorld.getHeight() - 80);
        gameWorld.getChildren().add(movementInstructions);
    }
    
    public void updateUI() {
        // Update coin display
        coinLabel.setText("💰 SmartCoins: " + user.getSmartCoins());
        
        // Update level display
        levelLabel.setText("📊 Level: " + gameState.getCurrentLevel());
        
        // Update progress bar
        double progress = (double) gameState.getCorrectAnswers() / Math.max(gameState.getTotalQuestionsAnswered(), 1);
        progressBar.setProgress(progress);
    }
    
    public void updateZoneInfo(AdventureZone zone) {
        if (zone != null) {
            zoneLabel.setText("🏝️ Current Zone: " + zone.getZoneName());
        }
    }
    
    public void showPauseMenu() {
        // Create pause menu overlay
        VBox pauseMenu = new VBox(20);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 15;");
        pauseMenu.setPadding(new Insets(30));
        pauseMenu.setPrefSize(400, 300);
        pauseMenu.setLayoutX((gameWorld.getWidth() - 400) / 2);
        pauseMenu.setLayoutY((gameWorld.getHeight() - 300) / 2);
        
        Text title = new Text("⏸️ Game Paused");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        
        Button resumeButton = new Button("▶️ Resume");
        resumeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16;");
        resumeButton.setPrefWidth(150);
        resumeButton.setOnAction(e -> {
            gameWorld.getChildren().remove(pauseMenu);
            // SoundManager.getInstance().playSound("button_hover.wav");
        });
        
        Button settingsButton = new Button("⚙️ Settings");
        settingsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16;");
        settingsButton.setPrefWidth(150);
        settingsButton.setOnAction(e -> showSettings());
        
        Button exitButton = new Button("🚪 Exit Adventure");
        exitButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 16;");
        exitButton.setPrefWidth(150);
        exitButton.setOnAction(e -> exitAdventure());
        
        pauseMenu.getChildren().addAll(title, resumeButton, settingsButton, exitButton);
        gameWorld.getChildren().add(pauseMenu);
        
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    public void showZoneMap(AdventureZone currentZone) {
        // Create map overlay
        VBox mapOverlay = new VBox(20);
        mapOverlay.setAlignment(Pos.CENTER);
        mapOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 15;");
        mapOverlay.setPadding(new Insets(30));
        mapOverlay.setPrefSize(600, 400);
        mapOverlay.setLayoutX((gameWorld.getWidth() - 600) / 2);
        mapOverlay.setLayoutY((gameWorld.getHeight() - 400) / 2);
        
        Text title = new Text("🗺️ Adventure Map");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        
        Text mapInfo = new Text("""
            Available Zones:
            🏝️ Budget Bay (Current)
            🏔️ Savings Summit
            🏝️ Investment Island
            🌊 Banking Bayou
            💻 Digital Safety""");
        mapInfo.setFont(Font.font("Arial", 14));
        mapInfo.setFill(Color.LIGHTBLUE);
        
        Button closeButton = new Button("❌ Close Map");
        closeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16;");
        closeButton.setOnAction(e -> {
            gameWorld.getChildren().remove(mapOverlay);
            // SoundManager.getInstance().playSound("button_hover.wav");
        });
        
        mapOverlay.getChildren().addAll(title, mapInfo, closeButton);
        gameWorld.getChildren().add(mapOverlay);
        
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    public void showInventory() {
        // Create inventory overlay
        VBox inventoryOverlay = new VBox(20);
        inventoryOverlay.setAlignment(Pos.CENTER);
        inventoryOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.9); -fx-background-radius: 15;");
        inventoryOverlay.setPadding(new Insets(30));
        inventoryOverlay.setPrefSize(500, 400);
        inventoryOverlay.setLayoutX((gameWorld.getWidth() - 500) / 2);
        inventoryOverlay.setLayoutY((gameWorld.getHeight() - 400) / 2);
        
        Text title = new Text("🎒 Adventure Inventory");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        
        Text inventoryInfo = new Text("""
            Adventure Items:
            💰 SmartCoins: %d
            🏆 Badges: 0
            📚 Knowledge Points: %d
            🎯 Completed Zones: %d""".formatted(
                user.getSmartCoins(),
                gameState.getTotalCoinsEarned(),
                gameState.getCompletedLevels().size()
            ));
        inventoryInfo.setFont(Font.font("Arial", 14));
        inventoryInfo.setFill(Color.LIGHTGREEN);
        
        Button closeButton = new Button("❌ Close Inventory");
        closeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16;");
        closeButton.setOnAction(e -> {
            gameWorld.getChildren().remove(inventoryOverlay);
            // SoundManager.getInstance().playSound("button_hover.wav");
        });
        
        inventoryOverlay.getChildren().addAll(title, inventoryInfo, closeButton);
        gameWorld.getChildren().add(inventoryOverlay);
        
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    private void showSettings() {
        System.out.println("⚙️ Settings menu would open here");
        // SoundManager.getInstance().playSound("button_hover.wav");
    }
    
    private void exitAdventure() {
        System.out.println("🚪 Exiting adventure mode...");
        // SoundManager.getInstance().playSound("button_hover.wav");
        // This would return to the main dashboard
    }
}
