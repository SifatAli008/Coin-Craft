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
import javafx.scene.shape.Circle;

/**
 * Top bar for Child Dashboard
 * Displays avatar, personalized welcome message, and SmartCoin balance
 * Follows game theme with cheerful colors and gaming fonts
 */
public class ChildTopBar {
    private HBox root;
    private final User currentUser;
    private Label welcomeLabel;
    
    public ChildTopBar(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new HBox(8);
        root.setPadding(new Insets(16, 16, 16, 16));
        root.setMinHeight(60);
        root.setPrefHeight(60);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 0 0 16 16;" +
            "-fx-border-radius: 0 0 16 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.3);" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );
        
        // Compact avatar with just image and level
        HBox avatarSection = createCompactAvatar();
        
        // Compact welcome message
        Label welcomeMessage = createCompactWelcome();
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Compact coin display
        HBox coinSection = createCompactCoinDisplay();
        
        root.getChildren().addAll(
            avatarSection,
            welcomeMessage,
            spacer,
            coinSection
        );
    }
    
    /**
     * Create a compact avatar section with just image and level badge
     */
    private HBox createCompactAvatar() {
        HBox avatarSection = new HBox(6);
        avatarSection.setAlignment(Pos.CENTER_LEFT);
        
        // Small circular avatar
        ImageView avatarImage = new ImageView();
        avatarImage.setFitWidth(32);
        avatarImage.setFitHeight(32);
        avatarImage.setPreserveRatio(true);
        
        // Create circular clip
        Circle clip = new Circle(16, 16, 16);
        avatarImage.setClip(clip);
        
        // Load avatar or use placeholder
        try {
            Image defaultAvatar = new Image(
                getClass().getResourceAsStream("/images/avatars/default_explorer.png"));
            avatarImage.setImage(defaultAvatar);
        } catch (Exception e) {
            avatarImage.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4CAF50, #388E3C);" +
                "-fx-background-radius: 16;"
            );
        }
        
        // Small level badge
        Label levelBadge = new Label("L" + currentUser.getLevel());
        levelBadge.setStyle(
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 9px;" +
            "-fx-font-weight: 700;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 2 6;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );
        
        avatarSection.getChildren().addAll(avatarImage, levelBadge);
        return avatarSection;
    }
    
    /**
     * Create a compact welcome message
     */
    private Label createCompactWelcome() {
        String greeting = getTimeBasedGreeting();
        String userName = currentUser.getName();
        
        welcomeLabel = new Label(greeting + ", " + userName + "! 🚀");
        welcomeLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        return welcomeLabel;
    }
    
    /**
     * Create a compact coin display
     */
    private HBox createCompactCoinDisplay() {
        HBox coinSection = new HBox(4);
        coinSection.setAlignment(Pos.CENTER);
        coinSection.setStyle(
            "-fx-background-color: #FFF8DC;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #EAB308;" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 4 8;"
        );
        
        // Small coin icon
        Label coinIcon = new Label("💰");
        coinIcon.setStyle("-fx-font-size: 12px;");
        
        // Balance
        Label balanceLabel = new Label(String.valueOf(currentUser.getSmartCoinBalance()));
        balanceLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #B8860B;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        coinSection.getChildren().addAll(coinIcon, balanceLabel);
        return coinSection;
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
