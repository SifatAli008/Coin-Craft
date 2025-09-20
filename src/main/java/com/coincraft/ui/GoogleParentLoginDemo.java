package com.coincraft.ui;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.ui.dashboards.ParentDashboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Demo application specifically for testing Google Parent Login
 * Demonstrates the complete flow from Google authentication to Parent Dashboard
 */
public class GoogleParentLoginDemo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create the login screen with callback
        LoginScreen loginScreen = new LoginScreen(new LoginScreen.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                System.out.println("Login successful for user: " + user.getName() + " with role: " + user.getRole());
                
                // If it's a parent user, show parent dashboard
                if (user.getRole() == UserRole.PARENT) {
                    showParentDashboard(primaryStage, user);
                } else {
                    // For other roles, you could show appropriate dashboards
                    System.out.println("Non-parent user logged in. Role: " + user.getRole());
                    // For demo purposes, still show parent dashboard
                    showParentDashboard(primaryStage, user);
                }
            }
            
            @Override
            public void onLoginFailed(String error) {
                System.err.println("Login failed: " + error);
            }
            
            @Override
            public void onNavigateToSignUp() {
                System.out.println("Navigate to sign up requested");
            }
        });
        
        // Set up the scene
        Scene scene = new Scene(loginScreen.getRoot(), 1200, 800);
        
        // Load CSS styles
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        // Configure the stage
        primaryStage.setTitle("CoinCraft - Google Parent Login Demo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        
        primaryStage.show();
        
        System.out.println("=".repeat(60));
        System.out.println("Google Parent Login Demo Instructions:");
        System.out.println("=".repeat(60));
        System.out.println("1. Select 'Parent/Guardian' from the role dropdown");
        System.out.println("2. Click 'SIGN IN WITH GOOGLE' button");
        System.out.println("3. Wait for authentication (simulated 2-second delay)");
        System.out.println("4. You'll be redirected to the Parent Dashboard");
        System.out.println();
        System.out.println("Alternative: Use email/password with Parent role selected");
        System.out.println("Email: any@email.com | Password: any password");
        System.out.println("=".repeat(60));
    }
    
    private void showParentDashboard(Stage stage, User user) {
        // Create parent dashboard
        ParentDashboard parentDashboard = new ParentDashboard(user);
        
        // Create new scene with parent dashboard
        Scene dashboardScene = new Scene(parentDashboard.getRoot(), 1400, 900);
        
        // Load CSS styles
        try {
            dashboardScene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        // Update stage
        stage.setScene(dashboardScene);
        stage.setTitle("CoinCraft - Parent Dashboard (" + user.getName() + ")");
        stage.setMaximized(true);
        
        System.out.println("✅ Parent Dashboard loaded successfully!");
        System.out.println("Parent: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Balance: " + user.getSmartCoinBalance() + " SmartCoins");
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Google Parent Login Demo...");
        launch(args);
    }
}
