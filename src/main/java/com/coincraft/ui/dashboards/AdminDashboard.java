package com.coincraft.ui.dashboards;

import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Admin Dashboard - Full system administration interface
 * Features user management, content management, and system analytics
 * Follows the established game theme with administrative styling
 */
public class AdminDashboard extends BaseDashboard {
    
    public AdminDashboard(User user) {
        super(user);
    }
    
    @Override
    protected void initializeUI() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("admin-dashboard");
        root = borderPane;
        
        // Temporary placeholder content
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(40));
        
        Label titleLabel = new Label("⚙️ Admin Dashboard");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label welcomeLabel = new Label("Welcome, Administrator " + currentUser.getName() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label descriptionLabel = new Label(
            "Full system administration and management capabilities.\n\n" +
            "Full Admin Dashboard features coming soon:\n" +
            "• User management system\n" +
            "• Content and quest management\n" +
            "• System analytics and reporting\n" +
            "• Economy and coin management\n" +
            "• Security and audit logs\n" +
            "• Support and feedback management"
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
        borderPane.setCenter(centerContent);
        
        // Apply admin-specific theme
        applyAdminTheme();
    }
    
    /**
     * Apply admin-specific theme styling
     */
    private void applyAdminTheme() {
        root.setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            // Professional dark gradient background
            "-fx-background-color: linear-gradient(to bottom right, #263238, #37474F, #455A64);"
        );
        
        // Update text colors for dark theme
        VBox centerContent = (VBox) ((BorderPane) root).getCenter();
        if (centerContent != null && centerContent.getChildren().size() >= 2) {
            Label welcomeLabel = (Label) centerContent.getChildren().get(1);
            welcomeLabel.setStyle(welcomeLabel.getStyle().replace("#333333", "#FFFFFF"));
            
            if (centerContent.getChildren().size() >= 3) {
                Label descLabel = (Label) centerContent.getChildren().get(2);
                descLabel.setStyle(descLabel.getStyle().replace("#666666", "#CCCCCC"));
            }
        }
    }
    
    @Override
    public void navigateToSection(String section) {
        // TODO: Implement admin navigation sections
        System.out.println("Admin Dashboard - Navigate to: " + section);
    }
}
