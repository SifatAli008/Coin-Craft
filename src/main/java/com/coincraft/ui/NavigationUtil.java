package com.coincraft.ui;

import com.coincraft.models.User;
import com.coincraft.ui.routing.DashboardRouter;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Centralized navigation utility to handle screen transitions
 * Prevents code duplication and circular reference issues
 */
public class NavigationUtil {
    
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    
    /**
     * Navigate to login screen from any context
     */
    public static void navigateToLogin(Stage stage) {
        LoginScreen loginScreen = new LoginScreen(new LoginScreen.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                navigateToDashboard(stage, user);
            }
            
            @Override
            public void onLoginFailed(String error) {
                System.err.println("Login failed: " + error);
            }
            
            @Override
            public void onNavigateToParentRegistration() {
                navigateToRegistration(stage);
            }
        });
        
        Scene loginScene = new Scene(loginScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        loadStyles(loginScene);
        
        stage.setScene(loginScene);
        stage.setTitle("CoinCraft - Login");
    }
    
    /**
     * Navigate to registration screen from any context
     */
    public static void navigateToRegistration(Stage stage) {
        RegistrationScreen registrationScreen = new RegistrationScreen(new RegistrationScreen.RegistrationCallback() {
            @Override
            public void onRegistrationSuccess(User user) {
                navigateToDashboard(stage, user);
            }
            
            @Override
            public void onRegistrationFailed(String error) {
                System.err.println("Registration failed: " + error);
            }
            
            @Override
            public void onBackToLogin() {
                navigateToLogin(stage);
            }
        });
        
        Scene registrationScene = new Scene(registrationScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        loadStyles(registrationScene);
        
        stage.setScene(registrationScene);
        stage.setTitle("CoinCraft - Registration");
    }
    
    /**
     * Navigate to dashboard based on user role
     */
    public static void navigateToDashboard(Stage stage, User user) {
        try {
            DashboardRouter router = DashboardRouter.getInstance();
            Scene dashboardScene = new Scene(router.routeToDashboard(user), WINDOW_WIDTH, WINDOW_HEIGHT);
            loadStyles(dashboardScene);
            
            stage.setScene(dashboardScene);
            stage.setTitle("CoinCraft - " + user.getName() + " (" + user.getRole() + ")");
            
        } catch (Exception e) {
            System.err.println("Error navigating to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load CSS styles for a scene
     */
    private static void loadStyles(Scene scene) {
        try {
            scene.getStylesheets().add(
                NavigationUtil.class.getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
    }
    
    /**
     * Handle logout from any dashboard
     */
    public static void handleLogout(Stage stage) {
        System.out.println("🚪 Logout requested");
        
        // Stop centralized music during logout
        try {
            com.coincraft.audio.CentralizedMusicManager musicManager = com.coincraft.audio.CentralizedMusicManager.getInstance();
            musicManager.stop();
            System.out.println("🎵 Stopped centralized music during logout");
        } catch (Exception e) {
            System.out.println("Warning: Could not stop music during logout: " + e.getMessage());
        }
        
        // Clear authentication state and session data
        DashboardRouter.getInstance().logout();
        
        // Navigate back to login screen
        javafx.application.Platform.runLater(() -> {
            try {
                navigateToLogin(stage);
                System.out.println("✅ Logged out successfully - returned to login");
            } catch (Exception e) {
                System.err.println("Error during logout: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
