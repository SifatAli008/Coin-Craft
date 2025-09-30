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
        root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(0));
        
        // Modern streak header with better styling
        VBox headerSection = new VBox(8);
        headerSection.setAlignment(Pos.CENTER);
        
        Label streakLabel = new Label("🔥 " + currentUser.getDailyStreaks() + " Day Streak");
        streakLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF5722;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,87,34,0.3), 2, 0, 0, 1);"
        );
        
        Label subLabel = new Label("This Week");
        subLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        headerSection.getChildren().addAll(streakLabel, subLabel);
        
        // Modern calendar grid with better design
        GridPane calendar = new GridPane();
        calendar.setHgap(8);
        calendar.setVgap(8);
        calendar.setAlignment(Pos.CENTER);
        
        String[] days = {"M", "T", "W", "T", "F", "S", "S"};
        String[] fullDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        for (int i = 0; i < 7; i++) {
            VBox dayBox = new VBox(2);
            dayBox.setAlignment(Pos.CENTER);
            dayBox.setPrefSize(40, 50);
            
            boolean isActive = i < currentUser.getDailyStreaks();
            dayBox.setStyle(
                "-fx-background-color: " + (isActive ? "rgba(76, 175, 80, 0.9)" : "rgba(224, 224, 224, 0.7)") + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                (isActive ? "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 4);" : "") +
                "-fx-cursor: hand;"
            );
            
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: " + (isActive ? "white" : "#666666") + ";" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            
            Label iconLabel = new Label(isActive ? "✓" : "");
            iconLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 700;"
            );
            
            dayBox.getChildren().addAll(dayLabel, iconLabel);
            
            // Add hover effect for better UX
            final String finalDay = fullDays[i];
            dayBox.setOnMouseEntered(e -> {
                if (!isActive) {
                    dayBox.setStyle(dayBox.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
                }
            });
            
            dayBox.setOnMouseExited(e -> {
                dayBox.setStyle(dayBox.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
            });
            
            calendar.add(dayBox, i, 0);
        }
        
        // Enhanced motivation text
        Label motivationLabel = new Label("Keep going! 🌟");
        motivationLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        root.getChildren().addAll(headerSection, calendar, motivationLabel);
    }
    
    public VBox getRoot() {
        return root;
    }
}
