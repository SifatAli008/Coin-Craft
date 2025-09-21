package com.coincraft;

import java.util.logging.Logger;

import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.LoginScreen;
import com.coincraft.ui.MainDashboard;
import com.coincraft.ui.RegistrationScreen;
import com.coincraft.ui.routing.DashboardRouter;
import com.coincraft.ui.theme.PixelSkin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class for CoinCraft - Interactive Gamified Financial Literacy Platform
 * 
 * This application teaches children financial concepts through a fully gamified experience
 * combining interactive digital modules, real-world partner-validated tasks, and game elements.
 */
public class CoinCraftApplication extends Application {
    private static final Logger LOGGER = Logger.getLogger(CoinCraftApplication.class.getName());
    private static final String APP_TITLE = "CoinCraft - Money Explorer Adventure";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize Firebase service
            FirebaseService.getInstance().initialize();
            
            // Set up the main window
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setWidth(WINDOW_WIDTH);
            primaryStage.setHeight(WINDOW_HEIGHT);
            primaryStage.setResizable(true);
            
            // Set application icon
            try {
                primaryStage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/images/coincraft-icon.png")
                ));
            } catch (Exception e) {
                System.out.println("Could not load application icon: " + e.getMessage());
            }
            
            // Create and show login screen first
            showLoginScreen(primaryStage);
            primaryStage.show();
            
            System.out.println("CoinCraft application started successfully!");
            
        } catch (Exception e) {
            System.err.println("Error starting CoinCraft application: " + e.getMessage());
            LOGGER.severe(() -> "Application startup error: " + e.getMessage());
        }
    }
    
    private void showLoginScreen(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen(new LoginScreen.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                showMainDashboard(primaryStage, user);
            }
            
            @Override
            public void onLoginFailed(String error) {
                System.err.println("Login failed: " + error);
            }
            
            @Override
            public void onNavigateToSignUp() {
                showRegistrationScreen(primaryStage);
            }
        });
        
        Scene scene = new Scene(loginScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        loadStyles(scene);
        PixelSkin.apply(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE + " - Login");
    }
    
    private void showRegistrationScreen(Stage primaryStage) {
        System.out.println("Showing registration screen...");
        try {
            RegistrationScreen registrationScreen = new RegistrationScreen(new RegistrationScreen.RegistrationCallback() {
                @Override
                public void onRegistrationSuccess(User user) {
                    showMainDashboard(primaryStage, user);
                }
                
                @Override
                public void onRegistrationFailed(String error) {
                    System.err.println("Registration failed: " + error);
                }
                
                @Override
                public void onBackToLogin() {
                    System.out.println("Back to login requested");
                    showLoginScreen(primaryStage);
                }
            });
            
            Scene scene = new Scene(registrationScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
            loadStyles(scene);
            PixelSkin.apply(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle(APP_TITLE + " - Registration");
            System.out.println("Registration screen loaded successfully");
        } catch (Exception e) {
            System.err.println("Error showing registration screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showMainDashboard(Stage primaryStage, User user) {
        Platform.runLater(() -> {
            try {
                // Use the new dashboard router for role-based navigation
                DashboardRouter router = DashboardRouter.getInstance();
                Scene dashboardScene = new Scene(router.routeToDashboard(user), WINDOW_WIDTH, WINDOW_HEIGHT);
                loadStyles(dashboardScene);
                
                primaryStage.setScene(dashboardScene);
                primaryStage.setTitle(APP_TITLE + " - " + user.getName() + " (" + user.getRole() + ")");
                
                LOGGER.info(() -> "Loaded " + user.getRole() + " dashboard for user: " + user.getName());
                
            } catch (Exception e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                LOGGER.severe(() -> "Dashboard routing error: " + e.getMessage());
                
                // Fallback to original dashboard
                try {
                    MainDashboard dashboard = new MainDashboard(user);
                    Scene dashboardScene = new Scene(dashboard.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
                    loadStyles(dashboardScene);
                    primaryStage.setScene(dashboardScene);
                    primaryStage.setTitle(APP_TITLE + " - " + user.getName() + " (Fallback)");
                } catch (Exception fallbackError) {
                    System.err.println("Fallback dashboard also failed: " + fallbackError.getMessage());
                }
            }
        });
    }
    
    
    private void loadStyles(Scene scene) {
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
    }
    
    @Override
    public void stop() {
        try {
            // Cleanup resources
            FirebaseService.getInstance().shutdown();
            System.out.println("CoinCraft application stopped successfully.");
        } catch (Exception e) {
            System.err.println("Error during application shutdown: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
