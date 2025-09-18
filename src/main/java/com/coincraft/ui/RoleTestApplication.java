package com.coincraft.ui;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;

/**
 * Test application to demonstrate role-based MainDashboard functionality
 */
public class RoleTestApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize Firebase service
            FirebaseService.getInstance().initialize();
            
            // Show role selection dialog
            showRoleSelectionDialog(primaryStage);
            
        } catch (Exception e) {
            System.err.println("Error starting role test application: " + e.getMessage());
        }
    }
    
    private void showRoleSelectionDialog(Stage primaryStage) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("CHILD", 
            Arrays.asList("CHILD", "PARENT", "TEACHER", "ADMIN"));
        dialog.setTitle("CoinCraft Role Test");
        dialog.setHeaderText("Select User Role to Test");
        dialog.setContentText("Choose a role:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(role -> {
            User testUser = createTestUser(UserRole.valueOf(role));
            showDashboard(primaryStage, testUser);
        });
    }
    
    private User createTestUser(UserRole role) {
        User user = switch (role) {
            case CHILD -> {
                User child = new User("child_123", "Alex the Explorer", UserRole.CHILD, 10);
                child.setSmartCoinBalance(150);
                child.setLevel(2);
                child.setDailyStreaks(5);
                yield child;
            }
            case PARENT -> {
                User parent = new User("parent_456", "Sarah Johnson", UserRole.PARENT, 35);
                parent.setSmartCoinBalance(0); // Parents don't have SmartCoins
                parent.setLevel(1);
                parent.setDailyStreaks(0);
                yield parent;
            }
            case TEACHER -> {
                User teacher = new User("teacher_789", "Ms. Rodriguez", UserRole.TEACHER, 28);
                teacher.setSmartCoinBalance(0);
                teacher.setLevel(1);
                teacher.setDailyStreaks(0);
                yield teacher;
            }
            case ADMIN -> {
                User admin = new User("admin_101", "System Administrator", UserRole.ADMIN, 30);
                admin.setSmartCoinBalance(0);
                admin.setLevel(1);
                admin.setDailyStreaks(0);
                yield admin;
            }
            case ELDER -> {
                User elder = new User("elder_202", "Elder Pennywise", UserRole.ELDER, 65);
                elder.setSmartCoinBalance(0);
                elder.setLevel(1);
                elder.setDailyStreaks(0);
                yield elder;
            }
        };
        
        user.setEmail("test@coincraft.com");
        return user;
    }
    
    private void showDashboard(Stage primaryStage, User user) {
        try {
            MainDashboard dashboard = new MainDashboard(user);
            Scene scene = new Scene(dashboard.getRoot(), 1200, 800);
            
            // Load CSS styles
            try {
                scene.getStylesheets().add(
                    getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
                );
            } catch (Exception e) {
                System.out.println("Could not load CSS styles: " + e.getMessage());
            }
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("CoinCraft - " + user.getName() + " (" + user.getRole() + ")");
            primaryStage.show();
            
            System.out.println("Dashboard loaded for " + user.getRole() + " role");
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load dashboard");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    @Override
    public void stop() {
        try {
            FirebaseService.getInstance().shutdown();
            System.out.println("Role test application stopped successfully.");
        } catch (Exception e) {
            System.err.println("Error during application shutdown: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
