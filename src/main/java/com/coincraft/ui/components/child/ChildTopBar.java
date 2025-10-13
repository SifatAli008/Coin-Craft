package com.coincraft.ui.components.child;

import java.time.LocalDateTime;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * Top bar for Child Dashboard
 * Displays avatar, personalized welcome message, and SmartCoin balance
 * Follows game theme with cheerful colors and gaming fonts
 */
public class ChildTopBar {
    private HBox root;
    private User currentUser;
    private Label welcomeLabel;
    
    // Real data for stats
    private int dailyStreak = 0;
    private int activeTasks = 0;
    
    public ChildTopBar(User user) {
        this.currentUser = user;
        // Stats will be set via updateStats() with real data
        initializeUI();
    }

    /**
     * Update the bound user and rebuild internal stats on next refresh
     */
    public void setCurrentUser(User user) {
        if (user != null) {
            this.currentUser = user;
        }
    }
    
    /**
     * Update stats with real data
     */
    public void updateStats(int streak, int tasks) {
        this.dailyStreak = streak;
        this.activeTasks = tasks;
    }
    
    private void initializeUI() {
        root = new HBox(16);
        root.setPadding(new Insets(20, 24, 20, 24));
        root.setMinHeight(80);
        root.setPrefHeight(80);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle(
            "-fx-background-color: rgba(248, 250, 252, 0.95);" +
            "-fx-background-radius: 0 0 16 16;" +
            "-fx-border-radius: 0 0 16 16;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 12, 0, 0, 4);"
        );
        
        // Enhanced avatar section
        HBox avatarSection = createCompactAvatar();
        
        // Enhanced welcome message with progress
        VBox welcomeSection = createEnhancedWelcomeSection();
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Enhanced stats section with coins and streak
        HBox statsSection = createEnhancedStatsSection();
        
        root.getChildren().addAll(
            avatarSection,
            welcomeSection,
            spacer,
            statsSection
        );
    }
    
    /**
     * Create a compact avatar section with just image and level badge
     */
    private HBox createCompactAvatar() {
        HBox avatarSection = new HBox(10);
        avatarSection.setAlignment(Pos.CENTER_LEFT);
        
        // Enhanced circular avatar with border
        ImageView avatarImage = new ImageView();
        avatarImage.setFitWidth(40);
        avatarImage.setFitHeight(40);
        avatarImage.setPreserveRatio(true);
        
        // Create circular clip
        Circle clip = new Circle(20, 20, 20);
        avatarImage.setClip(clip);
        
        // Add border effect
        avatarImage.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        // Load avatar or use placeholder
        try {
            Image defaultAvatar = new Image(
                getClass().getResourceAsStream("/images/avatars/default_explorer.png"));
            avatarImage.setImage(defaultAvatar);
        } catch (Exception e) {
            avatarImage.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FA8A00, #E67E00);" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
            );
        }
        
        // Enhanced level badge with better styling
        Label levelBadge = new Label("LVL " + currentUser.getLevel());
        levelBadge.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 700;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-padding: 3 8;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 4, 0, 0, 2);"
        );
        
        avatarSection.getChildren().addAll(avatarImage, levelBadge);
        return avatarSection;
    }
    
    /**
     * Create a compact welcome message
     */
    private Label createCompactWelcome() {
        String greeting = getTimeBasedGreeting();
        String userName = sanitizeDisplayName(currentUser.getName());
        
        welcomeLabel = new Label(greeting + ", " + userName + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        return welcomeLabel;
    }

    /**
     * Remove repeated role suffixes like "(Adventurer)" from the display name
     */
    private String sanitizeDisplayName(String rawName) {
        if (rawName == null) return "";
        String cleaned = rawName.replaceAll("\\s*\\(Adventurer\\)", "");
        return cleaned.trim().replaceAll("\\s{2,}", " ");
    }
    
    /**
     * Create enhanced welcome section with progress indicator
     */
    private VBox createEnhancedWelcomeSection() {
        VBox welcomeSection = new VBox(4);
        welcomeSection.setAlignment(Pos.CENTER_LEFT);
        
        // Main welcome message
        Label welcomeMessage = createCompactWelcome();
        
        // Progress indicator for next level
        HBox progressSection = createProgressIndicator();
        
        welcomeSection.getChildren().addAll(welcomeMessage, progressSection);
        return welcomeSection;
    }
    
    /**
     * Create progress indicator for level advancement
     */
    private HBox createProgressIndicator() {
        HBox progressContainer = new HBox(6);
        progressContainer.setAlignment(Pos.CENTER_LEFT);
        
        // Progress bar background
        Region progressBg = new Region();
        progressBg.setPrefWidth(120);
        progressBg.setPrefHeight(4);
        progressBg.setStyle(
            "-fx-background-color: rgba(226, 232, 240, 0.6);" +
            "-fx-background-radius: 2;"
        );
        
        // Progress bar fill (simulated progress)
        Region progressFill = new Region();
        double progressPercent = (currentUser.getSmartCoinBalance() % 100) / 100.0; // Simulate progress based on coins
        progressFill.setPrefWidth(120 * Math.max(0.1, progressPercent)); // At least 10% visible
        progressFill.setPrefHeight(4);
        progressFill.setStyle(
            "-fx-background-color: linear-gradient(to right, #FA8A00, #FFB84D);" +
            "-fx-background-radius: 2;"
        );
        
        // Stack progress elements
        javafx.scene.layout.StackPane progressStack = new javafx.scene.layout.StackPane();
        progressStack.getChildren().addAll(progressBg, progressFill);
        progressStack.setAlignment(Pos.CENTER_LEFT);
        
        // Progress text
        Label progressText = new Label((int)(progressPercent * 100) + "% to Level " + (currentUser.getLevel() + 1));
        progressText.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        progressContainer.getChildren().addAll(progressStack, progressText);
        return progressContainer;
    }
    
    /**
     * Create enhanced stats section with multiple indicators
     */
    private HBox createEnhancedStatsSection() {
        HBox statsSection = new HBox(12);
        statsSection.setAlignment(Pos.CENTER);
        
        // Coins display (real data)
        HBox coinsCard = createStatCard("SmartCoins", String.valueOf(currentUser.getSmartCoinBalance()), "#EAB308");
        
        // Streak display (real data)
        HBox streakCard = createStatCard("Streak", String.valueOf(dailyStreak), "#F97316");
        
        // Active quests (real data)
        HBox questsCard = createStatCard("Tasks", String.valueOf(activeTasks), "#3B82F6");
        
        statsSection.getChildren().addAll(coinsCard, streakCard, questsCard);
        return statsSection;
    }
    
    /**
     * Create individual stat card
     */
    private HBox createStatCard(String label, String value, String accentColor) {
        HBox card = new HBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8);" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + accentColor + ";" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        
        // Label
        Label labelLabel = new Label(label);
        labelLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + accentColor + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        card.getChildren().addAll(labelLabel, valueLabel);
        return card;
    }
    
    /**
     * Get time-based greeting message
     */
    private String getTimeBasedGreeting() {
        int hour = LocalDateTime.now().getHour();
        
        if (hour < 12) {
            return "Good morning";
        } else if (hour < 17) {
            return "Good afternoon";
        } else if (hour < 21) {
            return "Good evening";
        } else {
            return "Good night";
        }
    }
    
    /**
     * Update the display with current user data
     */
    public void refresh() {
        // Recreate the UI with updated data
        root.getChildren().clear();
        initializeUI();
    }
    
    /**
     * Get the root UI component
     */
    public HBox getRoot() {
        return root;
    }
    
    /**
     * Get current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
