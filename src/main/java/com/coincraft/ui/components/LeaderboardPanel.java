package com.coincraft.ui.components;

import java.util.List;
import java.util.logging.Logger;

import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Leaderboard panel component showing top players and rankings
 */
public class LeaderboardPanel {
    private static final Logger LOGGER = Logger.getLogger(LeaderboardPanel.class.getName());
    
    private VBox root;
    private VBox leaderboardContainer;
    private Label loadingLabel;
    private Label emptyLabel;
    
    public LeaderboardPanel() {
        initializeComponent();
        loadLeaderboard();
    }
    
    private void initializeComponent() {
        root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("leaderboard-panel");
        root.getStyleClass().add("pixel-card");
        
        // Leaderboard container with scroll capability
        leaderboardContainer = new VBox(5);
        leaderboardContainer.setPadding(new Insets(5));
        
        ScrollPane scrollPane = new ScrollPane(leaderboardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(180);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Loading label
        loadingLabel = new Label("Loading leaderboard...");
        loadingLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        loadingLabel.setStyle("-fx-text-fill: #8B7355;");
        loadingLabel.setVisible(true);
        
        // Empty state label
        emptyLabel = new Label("No rankings available.\nComplete tasks to join the leaderboard!");
        emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        emptyLabel.setStyle("-fx-text-fill: #8B7355; -fx-text-alignment: center;");
        emptyLabel.setWrapText(true);
        emptyLabel.setVisible(false);
        
        root.getChildren().addAll(scrollPane, loadingLabel, emptyLabel);
    }
    
    public void loadLeaderboard() {
        // Load leaderboard data asynchronously
        Platform.runLater(() -> {
            try {
                loadingLabel.setVisible(true);
                emptyLabel.setVisible(false);
                
                // Get leaderboard data from Firebase service
                List<User> leaderboard = FirebaseService.getInstance().getLeaderboard(10);
                
                Platform.runLater(() -> {
                    loadingLabel.setVisible(false);
                    updateLeaderboard(leaderboard);
                });
                
            } catch (Exception e) {
                LOGGER.severe(() -> "Failed to load leaderboard: " + e.getMessage());
                Platform.runLater(() -> {
                    loadingLabel.setVisible(false);
                    showError();
                });
            }
        });
    }
    
    private void updateLeaderboard(List<User> users) {
        leaderboardContainer.getChildren().clear();
        
        if (users == null || users.isEmpty()) {
            emptyLabel.setVisible(true);
            return;
        }
        
        emptyLabel.setVisible(false);
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            HBox rankItem = createRankItem(i + 1, user);
            leaderboardContainer.getChildren().add(rankItem);
        }
    }
    
    private HBox createRankItem(int position, User user) {
        HBox rankItem = new HBox(10);
        rankItem.setAlignment(Pos.CENTER_LEFT);
        rankItem.setPadding(new Insets(8, 10, 8, 10));
        
        // Style based on position
        String backgroundColor;
        String textColor = "#2F4F4F";
        
        backgroundColor = switch (position) {
            case 1 -> "#FFD700"; // Gold
            case 2 -> "#C0C0C0"; // Silver
            case 3 -> "#CD7F32"; // Bronze
            default -> "white";
        };
        
        rankItem.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 5; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-border-width: 1;");
        
        // Rank number
        Label rankLabel = new Label(String.valueOf(position));
        rankLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        rankLabel.setStyle("-fx-text-fill: " + textColor + ";");
        rankLabel.setMinWidth(25);
        
        // Trophy emoji for top 3
        Label trophyLabel = new Label();
        if (position <= 3) {
            String trophy = position == 1 ? "🏆" : position == 2 ? "🥈" : "🥉";
            trophyLabel.setText(trophy);
            trophyLabel.setFont(Font.font(12));
        }
        
        // User name
        Label nameLabel = new Label(user.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        nameLabel.setStyle("-fx-text-fill: " + textColor + ";");
        nameLabel.setMaxWidth(100);
        
        // Level badge
        Label levelLabel = new Label("L" + user.getLevel());
        levelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        levelLabel.setStyle("-fx-background-color: #4169E1; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 8;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // SmartCoin balance
        Label coinsLabel = new Label(user.getSmartCoinBalance() + " SC");
        coinsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        coinsLabel.setStyle("-fx-text-fill: #DAA520;");
        
        rankItem.getChildren().addAll(rankLabel, trophyLabel, nameLabel, levelLabel, spacer, coinsLabel);
        
        // Add hover effect
        rankItem.setOnMouseEntered(e -> {
            rankItem.setScaleX(1.02);
            rankItem.setScaleY(1.02);
        });
        
        rankItem.setOnMouseExited(e -> {
            rankItem.setScaleX(1.0);
            rankItem.setScaleY(1.0);
        });
        
        return rankItem;
    }
    
    private void showError() {
        leaderboardContainer.getChildren().clear();
        
        Label errorLabel = new Label("Failed to load leaderboard.\nTap to retry.");
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        errorLabel.setStyle("-fx-text-fill: #CD5C5C; -fx-text-alignment: center;");
        errorLabel.setWrapText(true);
        errorLabel.setOnMouseClicked(e -> loadLeaderboard());
        
        leaderboardContainer.getChildren().add(errorLabel);
    }
    
    public void refresh() {
        loadLeaderboard();
    }
    
    public Parent getRoot() {
        return root;
    }
}
