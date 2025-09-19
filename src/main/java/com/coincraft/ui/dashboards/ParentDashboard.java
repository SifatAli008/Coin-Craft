package com.coincraft.ui.dashboards;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Parent/Guardian Dashboard - Interface for parent oversight and management
 * Features child account management, task verification, and progress analytics
 * Follows the established game theme with professional styling
 */
public class ParentDashboard extends BaseDashboard {
    
    public ParentDashboard(User user) {
        super(user);
    }
    
    @Override
    protected void initializeUI() {
        root = new BorderPane();
        root.getStyleClass().add("parent-dashboard");
        
        // Temporary placeholder content
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));
        
        Label titleLabel = new Label("👨‍👩‍👧‍👦 Parent Dashboard");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label descriptionLabel = new Label(
            "Monitor your children's progress, verify completed tasks, and manage their learning journey.\n\n" +
            "Full Parent Dashboard features coming soon:\n" +
            "• Child account management\n" +
            "• Task verification system\n" +
            "• Progress analytics\n" +
            "• SmartCoin purchasing\n" +
            "• Communication tools"
        );
        descriptionLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        descriptionLabel.setMaxWidth(600);
        descriptionLabel.setWrapText(true);
        
        centerContent.getChildren().addAll(titleLabel, welcomeLabel, descriptionLabel);
        root.setCenter(centerContent);
        
        // Apply parent-specific theme
        applyParentTheme();
    }
    
    /**
     * Apply parent-specific theme styling
     */
    private void applyParentTheme() {
        root.setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            // Professional gradient background
            "-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB, #90CAF9);"
        );
    }
    
    @Override
    public void navigateToSection(String section) {
        // TODO: Implement parent navigation sections
        System.out.println("Parent Dashboard - Navigate to: " + section);
    }
}
