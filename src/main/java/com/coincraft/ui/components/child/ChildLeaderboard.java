package com.coincraft.ui.components.child;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Leaderboard panel for Child Dashboard
 * Shows current rank among friends with motivational feedback
 */
public class ChildLeaderboard {
    private VBox root;
    private User currentUser;
    
    public ChildLeaderboard(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(12);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(0));
        
        // Compact rank display with better styling
        Label rankLabel = new Label("👑 Rank #3");
        rankLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Compact top players list
        VBox playersBox = new VBox(4);
        playersBox.setAlignment(Pos.CENTER);
        
        String[] players = {"🥇 Alex - 150", "🥈 Sam - 125", "🥉 You - 100"};
        for (String player : players) {
            Label playerLabel = new Label(player);
            playerLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: #333333;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
            playersBox.getChildren().add(playerLabel);
        }
        
        // Compact motivation text
        Label motivationLabel = new Label("Climb higher! 🚀");
        motivationLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        root.getChildren().addAll(rankLabel, playersBox, motivationLabel);
    }
    
    public VBox getRoot() {
        return root;
    }
}
