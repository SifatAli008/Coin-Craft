package com.coincraft.ui.components.child;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Compact animated level progress widget for Child Dashboard
 * Shows current level, progress to next level, and experience points
 * Features gaming-style animations and visual feedback
 */
public class LevelProgressWidget {
    private VBox root;
    private final User currentUser;
    private Label levelLabel;
    private Label progressLabel;
    private ProgressBar progressBar;
    private Label experienceLabel;
    
    public LevelProgressWidget(User user) {
        this.currentUser = user;
        initializeUI();
        startProgressAnimation();
    }
    
    private void initializeUI() {
        root = new VBox(6);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF9800, #FFC107);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #FF8F00;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 10, 0, 0, 5);"
        );
        
        // Smaller level label
        levelLabel = new Label("LEVEL " + currentUser.getLevel());
        levelLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 800;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 4, 0, 0, 2);"
        );
        
        // Create horizontal layout for progress info
        HBox progressInfo = new HBox(8);
        progressInfo.setAlignment(Pos.CENTER);
        
        // Smaller progress bar
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        progressBar.setPrefHeight(10);
        progressBar.setStyle(
            "-fx-accent: #4CAF50;" +
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;"
        );
        
        // Calculate progress to next level
        double currentProgress = calculateLevelProgress();
        progressBar.setProgress(currentProgress);
        
        // Compact progress text
        progressLabel = new Label(String.format("%.0f%% to Level %d", 
            currentProgress * 100, currentUser.getLevel() + 1));
        progressLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 2, 0, 0, 1);"
        );
        
        progressInfo.getChildren().addAll(progressBar, progressLabel);
        
        // Compact experience points display
        int currentXP = currentUser.getExperiencePoints();
        int nextLevelXP = calculateXPForNextLevel();
        experienceLabel = new Label(String.format("Experience: %d / %d XP", currentXP, nextLevelXP));
        experienceLabel.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: rgba(255,255,255,0.85);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        root.getChildren().addAll(levelLabel, progressInfo, experienceLabel);
    }
    
    private double calculateLevelProgress() {
        int currentXP = currentUser.getExperiencePoints();
        int currentLevelXP = calculateXPForLevel(currentUser.getLevel());
        int nextLevelXP = calculateXPForNextLevel();
        
        if (nextLevelXP <= currentLevelXP) {
            return 1.0; // Max level reached
        }
        
        int progressXP = currentXP - currentLevelXP;
        int requiredXP = nextLevelXP - currentLevelXP;
        
        return Math.max(0.0, Math.min(1.0, (double) progressXP / requiredXP));
    }
    
    private int calculateXPForLevel(int level) {
        return level * 100 + Math.max(0, level - 1) * 50;
    }
    
    private int calculateXPForNextLevel() {
        return calculateXPForLevel(currentUser.getLevel() + 1);
    }
    
    private void startProgressAnimation() {
        double targetProgress = calculateLevelProgress();
        
        Timeline fillAnimation = new Timeline();
        final double step = targetProgress / 30.0; // 30 steps for smooth animation
        
        for (int i = 0; i <= 30; i++) {
            final double progress = Math.min(targetProgress, i * step);
            fillAnimation.getKeyFrames().add(
                new KeyFrame(Duration.millis(i * 50), e -> {
                    progressBar.setProgress(progress);
                })
            );
        }
        
        fillAnimation.play();
    }
    
    public void refresh() {
        levelLabel.setText("LEVEL " + currentUser.getLevel());
        
        double currentProgress = calculateLevelProgress();
        progressBar.setProgress(currentProgress);
        
        progressLabel.setText(String.format("%.0f%% to Level %d", 
            currentProgress * 100, currentUser.getLevel() + 1));
        
        int currentXP = currentUser.getExperiencePoints();
        int nextLevelXP = calculateXPForNextLevel();
        experienceLabel.setText(String.format("Experience: %d / %d XP", currentXP, nextLevelXP));
    }
    
    public VBox getRoot() {
        return root;
    }
}
