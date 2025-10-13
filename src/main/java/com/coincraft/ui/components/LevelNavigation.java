package com.coincraft.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Level navigation component displaying the adventure map with available levels
 */
public class LevelNavigation {
    private VBox root;
    private GridPane levelGrid;
    private int currentLevel = 1;
    private int maxUnlockedLevel = 1;
    
    // Level information
    private static final String[] LEVEL_NAMES = {
        "Treasure Chest Awakens",
        "Earners' Guild",
        "Goal Garden",
        "Budget Workshop",
        "Bank Vault",
        "Shield of Safety",
        "Investment Island",
        "Inflation Fog",
        "Emergency Fortress",
        "Giving Tree & Village Hall"
    };
    
    private static final String[] LEVEL_DESCRIPTIONS = {
        "Learn the basics of budgeting and money management",
        "Track chores and earn your first SmartCoins",
        "Set saving goals and practice delayed gratification",
        "Master advanced budgeting and expense tracking",
        "Discover banking, deposits, and account management",
        "Learn digital safety and protect your money",
        "Explore investments and compound interest",
        "Navigate inflation and protect your purchasing power",
        "Build emergency funds for financial resilience",
        "Practice social responsibility and community giving"
    };
    
    public LevelNavigation() {
        initializeComponent();
    }
    
    private void initializeComponent() {
        root = new VBox(15);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("level-navigation");
        
        // Title
        Label titleLabel = new Label("Adventure Map - Treasure Town");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.getStyleClass().add("map-title");
        titleLabel.setStyle("-fx-text-fill: #2F4F4F;");
        
        // Create scrollable level grid
        levelGrid = new GridPane();
        levelGrid.setHgap(15);
        levelGrid.setVgap(15);
        levelGrid.setPadding(new Insets(10));
        levelGrid.setAlignment(Pos.CENTER);
        
        ScrollPane scrollPane = new ScrollPane(levelGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Create level buttons
        createLevelButtons();
        
        root.getChildren().addAll(titleLabel, scrollPane);
    }
    
    private void createLevelButtons() {
        int columns = 2;
        
        for (int i = 0; i < LEVEL_NAMES.length; i++) {
            int levelNumber = i + 1;
            int row = i / columns;
            int col = i % columns;
            
            VBox levelBox = createLevelButton(levelNumber, LEVEL_NAMES[i], LEVEL_DESCRIPTIONS[i]);
            levelGrid.add(levelBox, col, row);
        }
    }
    
    private VBox createLevelButton(int levelNumber, String levelName, String description) {
        VBox levelBox = new VBox(8);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setPadding(new Insets(15));
        levelBox.setPrefWidth(200);
        levelBox.setPrefHeight(120);
        
        // Determine if level is unlocked
        boolean isUnlocked = levelNumber <= maxUnlockedLevel;
        boolean isCurrent = levelNumber == currentLevel;
        
        // Apply 8-bit pixel styling based on 8bitcn.com
        levelBox.getStyleClass().add("pixel-level-tile");
        if (isCurrent) {
            levelBox.getStyleClass().add("pixel-level-current");
        } else if (!isUnlocked) {
            levelBox.getStyleClass().add("pixel-level-locked");
        }
        
        // Level number
        Label numberLabel = new Label(String.valueOf(levelNumber));
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        numberLabel.setStyle("-fx-text-fill: #2F4F4F;");
        
        // Level name
        Label nameLabel = new Label(levelName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        nameLabel.setStyle("-fx-text-fill: #2F4F4F;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(180);
        
        // Status indicator
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        
        if (isCurrent) {
            statusLabel.setText("CURRENT");
            statusLabel.setStyle("-fx-text-fill: #FF6B35;");
        } else if (isUnlocked) {
            statusLabel.setText("AVAILABLE");
            statusLabel.setStyle("-fx-text-fill: #228B22;");
        } else {
            statusLabel.setText("LOCKED");
            statusLabel.setStyle("-fx-text-fill: #696969;");
        }
        
        levelBox.getChildren().addAll(numberLabel, nameLabel, statusLabel);
        
        // Add tooltip with description
        Tooltip tooltip = new Tooltip(description);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(250);
        Tooltip.install(levelBox, tooltip);
        
        // Add click handler
        levelBox.setOnMouseClicked(e -> handleLevelClick(levelNumber, levelName, isUnlocked));
        
        // Add hover effects
        levelBox.setOnMouseEntered(e -> {
            if (isUnlocked) {
                levelBox.setStyle(levelBox.getStyle() + "-fx-cursor: hand;");
                levelBox.setScaleX(1.05);
                levelBox.setScaleY(1.05);
            }
        });
        
        levelBox.setOnMouseExited(e -> {
            levelBox.setScaleX(1.0);
            levelBox.setScaleY(1.0);
        });
        
        return levelBox;
    }
    
    private void handleLevelClick(int levelNumber, String levelName, boolean isUnlocked) {
        if (!isUnlocked) {
            // Show locked level message
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Level Locked");
            alert.setHeaderText("Level " + levelNumber + ": " + levelName);
            alert.setContentText("Complete the previous levels to unlock this adventure! Keep earning SmartCoins and badges to progress.");
            alert.showAndWait();
            return;
        }
        
        // For MVP, show level information
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Level " + levelNumber);
        alert.setHeaderText(levelName);
        alert.setContentText("Level " + levelNumber + " activities will be available soon!\n\nFor now, explore the dashboard and complete your daily tasks to earn SmartCoins.");
        alert.showAndWait();
        
        // Feature coming soon - navigate to specific level activities
        System.out.println("Navigating to Level " + levelNumber + ": " + levelName);
    }
    
    public void updateCurrentLevel(int level) {
        this.currentLevel = level;
        refreshLevelButtons();
    }
    
    public void updateMaxUnlockedLevel(int maxLevel) {
        this.maxUnlockedLevel = maxLevel;
        refreshLevelButtons();
    }
    
    private void refreshLevelButtons() {
        levelGrid.getChildren().clear();
        createLevelButtons();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public int getMaxUnlockedLevel() {
        return maxUnlockedLevel;
    }
}
