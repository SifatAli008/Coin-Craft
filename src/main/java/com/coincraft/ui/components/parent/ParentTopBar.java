package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.coincraft.models.User;
import com.coincraft.ui.components.SmartCoinDisplay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Top bar for Parent Dashboard
 * Features welcome message, family balance, and real-time notifications
 * Professional design with family-oriented information display
 */
public class ParentTopBar {
    private HBox root;
    private User currentUser;
    private Label timeLabel;
    private Label notificationLabel;
    private SmartCoinDisplay familyBalanceDisplay;
    
    // Real family data (will be updated from Firebase)
    private int totalFamilyBalance = 0;
    private int activeChildren = 0;
    private int pendingTasks = 0;
    
    public ParentTopBar(User user) {
        this.currentUser = user;
        initializeUI();
        startTimeUpdater();
    }
    
    private void initializeUI() {
        root = new HBox(24);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(16, 24, 16, 24));
        root.setPrefHeight(80);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.98);" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-border-color: rgba(76, 175, 80, 0.2);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);"
        );
        
        createWelcomeSection();
        createCenterSpacer();
        createFamilyStats();
        createNotificationArea();
    }
    
    private void createWelcomeSection() {
        VBox welcomeSection = new VBox(4);
        welcomeSection.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Good " + getTimeOfDay() + ", " + currentUser.getName() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        timeLabel = new Label();
        timeLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        updateTimeLabel();
        
        welcomeSection.getChildren().addAll(welcomeLabel, timeLabel);
        root.getChildren().add(welcomeSection);
    }
    
    private void createCenterSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        root.getChildren().add(spacer);
    }
    
    private void createFamilyStats() {
        HBox statsSection = new HBox(16);
        statsSection.setAlignment(Pos.CENTER);
        
        // Family Balance
        VBox balanceBox = new VBox(6);
        balanceBox.setAlignment(Pos.CENTER);
        
        Label balanceTitle = new Label("Family Balance");
        balanceTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Create mock user for SmartCoinDisplay
        User mockUser = new User();
        mockUser.setSmartCoinBalance(totalFamilyBalance);
        familyBalanceDisplay = new SmartCoinDisplay(mockUser);
        familyBalanceDisplay.getRoot().setStyle(
            familyBalanceDisplay.getRoot().getStyle() + 
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-padding: 8 16;"
        );
        
        balanceBox.getChildren().addAll(balanceTitle, familyBalanceDisplay.getRoot());
        
        // Active Adventurers
        VBox childrenBox = new VBox(6);
        childrenBox.setAlignment(Pos.CENTER);
        
        Label childrenTitle = new Label("Active Adventurers");
        childrenTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label childrenValue = new Label("⚔️ " + activeChildren);
        childrenValue.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        childrenBox.getChildren().addAll(childrenTitle, childrenValue);
        
        // Pending Tasks
        VBox tasksBox = new VBox(6);
        tasksBox.setAlignment(Pos.CENTER);
        
        Label tasksTitle = new Label("Pending Review");
        tasksTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label tasksValue = new Label("📋 " + pendingTasks);
        tasksValue.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        tasksBox.getChildren().addAll(tasksTitle, tasksValue);
        
        statsSection.getChildren().addAll(balanceBox, childrenBox, tasksBox);
        root.getChildren().add(statsSection);
    }
    
    private void createNotificationArea() {
        VBox notificationArea = new VBox(4);
        notificationArea.setAlignment(Pos.CENTER_RIGHT);
        
        notificationLabel = new Label("🔔 " + pendingTasks + " new notifications");
        notificationLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-cursor: hand;" +
            "-fx-background-color: rgba(255, 152, 0, 0.15);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.2), 4, 0, 0, 2);"
        );
        
        notificationLabel.setOnMouseEntered(e -> {
            notificationLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #FF9800;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-cursor: hand;" +
                "-fx-background-color: rgba(255, 152, 0, 0.25);" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: rgba(255, 152, 0, 0.4);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        notificationLabel.setOnMouseExited(e -> {
            notificationLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #FF9800;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-cursor: hand;" +
                "-fx-background-color: rgba(255, 152, 0, 0.15);" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: rgba(255, 152, 0, 0.3);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.2), 4, 0, 0, 2);"
            );
        });
        
        notificationLabel.setOnMouseClicked(e -> {
            // TODO: Open notifications panel
            System.out.println("Opening notifications panel");
        });
        
        notificationArea.getChildren().add(notificationLabel);
        root.getChildren().add(notificationArea);
    }
    
    private String getTimeOfDay() {
        int hour = LocalDateTime.now().getHour();
        if (hour < 12) return "Morning";
        else if (hour < 17) return "Afternoon";
        else return "Evening";
    }
    
    private void updateTimeLabel() {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy • h:mm a"));
        timeLabel.setText(formattedTime);
    }
    
    private void startTimeUpdater() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(60), e -> updateTimeLabel())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    public void updateFamilyBalance(int newBalance) {
        this.totalFamilyBalance = newBalance;
        // Update the mock user and refresh display
        User mockUser = new User();
        mockUser.setSmartCoinBalance(newBalance);
        familyBalanceDisplay = new SmartCoinDisplay(mockUser);
    }
    
    public void updateActiveChildren(int count) {
        this.activeChildren = count;
        // Refresh the stats section if needed
    }
    
    public void updatePendingTasks(int count) {
        this.pendingTasks = count;
        notificationLabel.setText("🔔 " + count + " new notifications");
    }
    
    public HBox getRoot() {
        return root;
    }
}
