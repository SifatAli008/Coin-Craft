package com.coincraft.ui.components.child;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Event banner carousel for Child Dashboard
 * Shows special events, challenges, and seasonal content
 */
public class EventBanner {
    private VBox root;
    private final User currentUser;
    
    public EventBanner(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(6);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        // Attach current user context for diagnostics/accessibility
        if (currentUser != null) {
            root.setUserData(currentUser.getUserId());
            root.setAccessibleHelp("user:" + currentUser.getUserId());
        }
        root.setStyle(
            "-fx-background-color: rgba(233, 30, 99, 0.9);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );
        
        Label eventTitle = new Label("🎊 WEEKEND CHALLENGE");
        eventTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);" +
            "-fx-text-alignment: center;"
        );
        eventTitle.setMaxWidth(432);
        eventTitle.setWrapText(true);
        
        Label eventDesc = new Label("Complete 3 quests this weekend\nfor DOUBLE coins!");
        eventDesc.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        eventDesc.setMaxWidth(432);
        
        Label timeLeft = new Label("⏳ 2 days left");
        timeLeft.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: rgba(255,255,255,0.8);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        root.getChildren().addAll(eventTitle, eventDesc, timeLeft);
    }
    
    public VBox getRoot() {
        return root;
    }
}
