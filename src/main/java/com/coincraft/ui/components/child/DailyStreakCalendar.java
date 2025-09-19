package com.coincraft.ui.components.child;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Daily streak calendar widget for Child Dashboard
 * Shows visual calendar with glowing days for streak tracking
 */
public class DailyStreakCalendar {
    private VBox root;
    private User currentUser;
    
    public DailyStreakCalendar(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        
        // Compact streak counter
        Label streakLabel = new Label("🔥 " + currentUser.getDailyStreaks() + " Days");
        streakLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF5722;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Perfect calendar grid for 452px card (7 days)
        GridPane calendar = new GridPane();
        calendar.setHgap(20);
        calendar.setVgap(10);
        calendar.setAlignment(Pos.CENTER);
        
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            VBox dayBox = new VBox(4);
            dayBox.setAlignment(Pos.CENTER);
            dayBox.setPrefSize(50, 55);
            
            boolean isActive = i < currentUser.getDailyStreaks();
            dayBox.setStyle(
                "-fx-background-color: " + (isActive ? "#4CAF50" : "#E0E0E0") + ";" +
                "-fx-background-radius: 4;" +
                "-fx-border-radius: 4;" +
                (isActive ? "-fx-effect: dropshadow(gaussian, #4CAF50, 6, 0, 0, 0);" : "")
            );
            
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: " + (isActive ? "white" : "#666666") + ";" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            
            Label iconLabel = new Label(isActive ? "✓" : "○");
            iconLabel.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-text-fill: " + (isActive ? "white" : "#CCCCCC") + ";"
            );
            
            dayBox.getChildren().addAll(dayLabel, iconLabel);
            calendar.add(dayBox, i, 0);
        }
        
        Label motivationLabel = new Label("Keep going! 🌟");
        motivationLabel.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        root.getChildren().addAll(streakLabel, calendar, motivationLabel);
    }
    
    public VBox getRoot() {
        return root;
    }
}
