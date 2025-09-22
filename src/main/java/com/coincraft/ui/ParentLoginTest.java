package com.coincraft.ui;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.ui.dashboards.ParentDashboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Direct test of Parent Dashboard without login flow
 * This bypasses the login screen to test if the parent dashboard works correctly
 */
public class ParentLoginTest extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        VBox testContainer = new VBox(20);
        testContainer.setAlignment(Pos.CENTER);
        testContainer.setPadding(new Insets(40));
        testContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB, #90CAF9);");
        
        Label titleLabel = new Label("🧪 Parent Dashboard Test");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;"
        );
        
        Label instructionLabel = new Label("Click the button below to directly test the Parent Dashboard");
        instructionLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;"
        );
        
        Button testParentButton = new Button("🔐 Test Parent Dashboard");
        testParentButton.setPrefWidth(300);
        testParentButton.setPrefHeight(50);
        testParentButton.setStyle(
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        testParentButton.setOnAction(e -> {
            // Create a parent user directly
            User parentUser = new User("test_parent", "Test Parent", UserRole.PARENT, 1);
            parentUser.setEmail("test.parent@gmail.com");
            parentUser.setSmartCoinBalance(500);
            parentUser.setLevel(1);
            
            System.out.println("=".repeat(50));
            System.out.println("DIRECT PARENT TEST:");
            System.out.println("User: " + parentUser.getName());
            System.out.println("Role: " + parentUser.getRole());
            System.out.println("Email: " + parentUser.getEmail());
            System.out.println("=".repeat(50));
            
            // Create parent dashboard directly
            ParentDashboard parentDashboard = new ParentDashboard(parentUser);
            
            // Create new scene
            Scene dashboardScene = new Scene(parentDashboard.getRoot(), 1400, 900);
            
            // Load styles
            try {
                dashboardScene.getStylesheets().add(
                    getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
                );
            } catch (Exception ex) {
                System.out.println("Could not load CSS: " + ex.getMessage());
            }
            
            // Update stage
            primaryStage.setScene(dashboardScene);
            primaryStage.setTitle("CoinCraft - Parent Dashboard (Direct Test)");
            primaryStage.setMaximized(true);
            
            System.out.println("✅ Parent Dashboard loaded successfully!");
        });
        
        Button testGoogleLoginButton = new Button("🔍 Test Google Login Flow");
        testGoogleLoginButton.setPrefWidth(300);
        testGoogleLoginButton.setPrefHeight(50);
        testGoogleLoginButton.setStyle(
            "-fx-background-color: #4285f4;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        testGoogleLoginButton.setOnAction(e -> {
            // Test the login screen with debug
            LoginScreen loginScreen = new LoginScreen(new LoginScreen.LoginCallback() {
                @Override
                public void onLoginSuccess(User user) {
                    System.out.println("=".repeat(50));
                    System.out.println("LOGIN SUCCESS CALLBACK:");
                    System.out.println("User: " + user.getName());
                    System.out.println("Role: " + user.getRole());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("=".repeat(50));
                    
                    // Show parent dashboard if parent, otherwise show message
                    if (user.getRole() == UserRole.PARENT) {
                        ParentDashboard parentDashboard = new ParentDashboard(user);
                        Scene dashboardScene = new Scene(parentDashboard.getRoot(), 1400, 900);
                        primaryStage.setScene(dashboardScene);
                        primaryStage.setTitle("CoinCraft - Parent Dashboard (" + user.getName() + ")");
                        primaryStage.setMaximized(true);
                    } else {
                        System.out.println("❌ ERROR: Expected PARENT role but got: " + user.getRole());
                    }
                }
                
                @Override
                public void onLoginFailed(String error) {
                    System.out.println("❌ Login failed: " + error);
                }
                
                @Override
                public void onNavigateToParentRegistration() {
                    System.out.println("Parent registration navigation from test");
                }
                
            });
            
            Scene loginScene = new Scene(loginScreen.getRoot(), 1200, 800);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("CoinCraft - Login Test");
        });
        
        testContainer.getChildren().addAll(
            titleLabel, 
            instructionLabel, 
            testParentButton, 
            testGoogleLoginButton
        );
        
        Scene scene = new Scene(testContainer, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CoinCraft - Parent Login Test");
        primaryStage.show();
        
        System.out.println("Parent Login Test started!");
        System.out.println("Use this to test:");
        System.out.println("1. Direct Parent Dashboard (bypasses login)");
        System.out.println("2. Google Login Flow (with debug output)");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
