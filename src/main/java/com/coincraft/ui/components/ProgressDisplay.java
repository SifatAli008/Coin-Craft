package com.coincraft.ui.components;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Progress display component showing user achievements and streaks
 */
public class ProgressDisplay {
    private VBox root;
    private Label streakLabel;
    private Label badgeCountLabel;
    private Label rankLabel;
    private ProgressBar experienceBar;
    private User currentUser;
    
    public ProgressDisplay() {
        initializeComponent();
    }
    
    private void initializeComponent() {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("progress-display");
        root.getStyleClass().add("pixel-card");
        
        // Daily streak
        HBox streakBox = new HBox(5);
        streakBox.setAlignment(Pos.CENTER_LEFT);
        Label streakIcon = new Label("🔥");
        streakIcon.setFont(Font.font(16));
        streakLabel = new Label("0 day streak");
        streakLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        streakBox.getChildren().addAll(streakIcon, streakLabel);
        
        // Badge count
        HBox badgeBox = new HBox(5);
        badgeBox.setAlignment(Pos.CENTER_LEFT);
        Label badgeIcon = new Label("🏆");
        badgeIcon.setFont(Font.font(16));
        badgeCountLabel = new Label("0 badges earned");
        badgeCountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        badgeBox.getChildren().addAll(badgeIcon, badgeCountLabel);
        
        // Leaderboard rank
        HBox rankBox = new HBox(5);
        rankBox.setAlignment(Pos.CENTER_LEFT);
        Label rankIcon = new Label("📊");
        rankIcon.setFont(Font.font(16));
        rankLabel = new Label("Unranked");
        rankLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        rankBox.getChildren().addAll(rankIcon, rankLabel);
        
        // Experience progress
        Label expLabel = new Label("Experience Progress");
        expLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        expLabel.setStyle("-fx-text-fill: #2F4F4F;");
        
        experienceBar = new ProgressBar(0.0);
        experienceBar.setPrefWidth(180);
        experienceBar.getStyleClass().add("experience-bar");
        experienceBar.setStyle("-fx-accent: #32CD32;");
        
        Tooltip expTooltip = new Tooltip("Complete tasks and challenges to gain experience!");
        Tooltip.install(experienceBar, expTooltip);
        
        root.getChildren().addAll(streakBox, badgeBox, rankBox, expLabel, experienceBar);
    }
    
    public void updateProgress(User user) {
        this.currentUser = user;
        
        if (user != null) {
            // Update streak
            int streak = user.getDailyStreaks();
            String streakText = streak == 1 ? "1 day streak" : streak + " day streak";
            streakLabel.setText(streakText);
            
            // Update badge count
            int badgeCount = user.getEarnedBadges().size();
            String badgeText = badgeCount == 1 ? "1 badge earned" : badgeCount + " badges earned";
            badgeCountLabel.setText(badgeText);
            
            // Update rank
            int rank = user.getLeaderboardRank();
            if (rank > 0) {
                String rankText = "#" + rank + " on leaderboard";
                rankLabel.setText(rankText);
            } else {
                rankLabel.setText("Unranked");
            }
            
            // Update experience bar
            updateExperienceBar(user);
        }
    }
    
    private void updateExperienceBar(User user) {
        // Calculate experience based on level progression
        int currentLevel = user.getLevel();
        int currentCoins = user.getSmartCoinBalance();
        int currentLevelRequirement = (currentLevel - 1) * 100;
        int nextLevelRequirement = currentLevel * 100;
        
        // Calculate progress within current level
        double progress = 0.0;
        if (nextLevelRequirement > currentLevelRequirement) {
            int coinsForCurrentLevel = Math.max(0, currentCoins - currentLevelRequirement);
            int coinsNeededForLevel = nextLevelRequirement - currentLevelRequirement;
            progress = Math.min(1.0, (double) coinsForCurrentLevel / coinsNeededForLevel);
        }
        
        experienceBar.setProgress(progress);
        
        // Update tooltip with detailed info
        String tooltipText = String.format(
            "Level %d Progress: %d/%d SmartCoins\n%d more coins needed for Level %d",
            currentLevel,
            Math.max(0, currentCoins - currentLevelRequirement),
            nextLevelRequirement - currentLevelRequirement,
            Math.max(0, nextLevelRequirement - currentCoins),
            currentLevel + 1
        );
        
        Tooltip newTooltip = new Tooltip(tooltipText);
        Tooltip.install(experienceBar, newTooltip);
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
}
