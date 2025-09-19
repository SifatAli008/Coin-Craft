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
        root = new VBox(8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        
        // Perfect rank display for 452px card
        Label rankLabel = new Label("🏅 You're Rank #3 this week!");
        rankLabel.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;"
        );
        rankLabel.setMaxWidth(432);
        
        // Perfect top players for 452px card
        VBox playersBox = new VBox(8);
        playersBox.setAlignment(Pos.CENTER);
        
        String[] players = {"🥇 Alex - 150 coins", "🥈 Sam - 125 coins", "🥉 You - 100 coins"};
        for (String player : players) {
            Label playerLabel = new Label(player);
            playerLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: #333333;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            playersBox.getChildren().add(playerLabel);
        }
        
        Label motivationLabel = new Label("Keep adventuring to climb higher! 🚀");
        motivationLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;"
        );
        motivationLabel.setMaxWidth(432);
        
        root.getChildren().addAll(rankLabel, playersBox, motivationLabel);
    }
    
    public VBox getRoot() {
        return root;
    }
}
