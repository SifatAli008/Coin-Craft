package com.coincraft.ui;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.ui.dashboards.ParentDashboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Demo application specifically for testing the Parent Dashboard
 * Shows the comprehensive parent interface with child monitoring and analytics
 */
public class ParentDashboardDemo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create a mock parent user
        User parentUser = new User();
        parentUser.setUserId("parent_demo");
        parentUser.setName("Sarah Johnson");
        parentUser.setRole(UserRole.PARENT);
        parentUser.setSmartCoinBalance(500); // Parent's own balance
        parentUser.setLevel(1); // Parents don't have levels like children
        
        // Create the parent dashboard
        ParentDashboard parentDashboard = new ParentDashboard(parentUser);
        
        // Set up the scene
        Scene scene = new Scene(parentDashboard.getRoot(), 1400, 900);
        
        // Load CSS styles
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        // Configure the stage
        primaryStage.setTitle("CoinCraft - Parent Dashboard Demo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        
        primaryStage.show();
        
        System.out.println("Parent Dashboard Demo launched successfully!");
        System.out.println("Features demonstrated:");
        System.out.println("• Family overview with child monitoring cards");
        System.out.println("• Real-time family statistics");
        System.out.println("• Navigation between different sections");
        System.out.println("• Analytics dashboard with charts");
        System.out.println("• Professional parent-oriented design");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
