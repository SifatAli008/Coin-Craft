package com.coincraft;

import java.util.logging.Logger;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseDataManager;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.LoginScreen;
import com.coincraft.ui.MainDashboard;
import com.coincraft.ui.RegistrationScreen;
import com.coincraft.ui.routing.DashboardRouter;
import com.coincraft.ui.theme.PixelSkin;
import com.coincraft.ui.util.UiSoundBindings;

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
            // Initialize Firebase service with enhanced logging
            System.out.println("🔥 Initializing Firebase connection...");
            FirebaseService firebaseService = FirebaseService.getInstance();
            firebaseService.initialize();
            
            // Initialize enhanced Firebase Data Manager
            System.out.println("🚀 Initializing Firebase Data Manager...");
            FirebaseDataManager dataManager = FirebaseDataManager.getInstance();
            dataManager.initialize();
            
            // Verify Firebase connection
            if (firebaseService.isInitialized()) {
                System.out.println("✅ Firebase connection established successfully!");
                
                // Run comprehensive Firebase connection test
                System.out.println("🧪 Running comprehensive Firebase connection test...");
                boolean connectionTestPassed = firebaseService.testFirebaseConnection();
                if (connectionTestPassed) {
                    System.out.println("✅ All Firebase connection tests PASSED!");
                } else {
                    System.out.println("Some Firebase connection tests FAILED - using local storage fallback");
                }
                
                // Test enhanced data manager
                System.out.println("🧪 Testing Firebase Data Manager...");
                boolean dataManagerReady = dataManager.isFirebaseAvailable();
                if (dataManagerReady) {
                    System.out.println("✅ Firebase Data Manager is ready!");
                } else {
                    System.out.println("Firebase Data Manager in offline mode");
                }
            } else {
                System.out.println("Firebase connection failed - using local storage fallback");
            }
            
            // Display connection status and configuration
            System.out.println("📊 Firebase Status: " + firebaseService.getConnectionStatus());
            System.out.println("📊 Data Manager Status: " + dataManager.getConnectionStatus());
            System.out.println("📋 Firebase Config: " + firebaseService.getConfigInfo());
            System.out.println("=" .repeat(80));
            
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
            public void onNavigateToParentRegistration() {
                showParentRegistrationScreen(primaryStage);
            }
        });
        
        Scene scene = new Scene(loginScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
        loadStyles(scene);
        PixelSkin.apply(scene);
        // Install UI selection sounds and start background music
        UiSoundBindings.install(loginScreen.getRoot());
        CentralizedMusicManager.getInstance().play();
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE + " - Login");
    }
    
    private void showParentRegistrationScreen(Stage primaryStage) {
        System.out.println("Showing registration screen for parents...");
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
                    System.out.println("Back to login requested from registration");
                    showLoginScreen(primaryStage);
                }
            });
            
            Scene scene = new Scene(registrationScreen.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
            loadStyles(scene);
            PixelSkin.apply(scene);
            UiSoundBindings.install(registrationScreen.getRoot());
            primaryStage.setScene(scene);
            primaryStage.setTitle(APP_TITLE + " - Registration");
            System.out.println("Registration screen loaded successfully");
        } catch (Exception e) {
            System.err.println("Error showing registration screen: " + e.getMessage());
            System.err.println("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
        }
    }

    private void showMainDashboard(Stage primaryStage, User user) {
        Platform.runLater(() -> {
            try {
                // Use the new dashboard router for role-based navigation
                DashboardRouter router = DashboardRouter.getInstance();
                
                // Clean up any existing dashboard and music state before creating new one
                router.cleanupCurrentDashboard();
                
                Scene dashboardScene = new Scene(router.routeToDashboard(user), WINDOW_WIDTH, WINDOW_HEIGHT);
                loadStyles(dashboardScene);
                PixelSkin.apply(dashboardScene);
                // Install UI sound bindings for the dashboard scene root
                UiSoundBindings.install(dashboardScene.getRoot());
                
                primaryStage.setScene(dashboardScene);
                primaryStage.setTitle(APP_TITLE + " - " + user.getName() + " (" + user.getRole() + ")");
                
                LOGGER.info(() -> "Loaded " + user.getRole() + " dashboard for user: " + user.getName());
                
            } catch (Exception e) {
                System.err.println("Error switching to dashboard: " + e.getMessage());
                LOGGER.severe(() -> "Dashboard routing error: " + e.getMessage());
                
                // Stop any existing music before creating fallback dashboard
                CentralizedMusicManager.getInstance().stop();
                
                // Fallback to original dashboard
                try {
                    MainDashboard dashboard = new MainDashboard(user);
                    Scene dashboardScene = new Scene(dashboard.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);
                    loadStyles(dashboardScene);
                    PixelSkin.apply(dashboardScene);
                    primaryStage.setScene(dashboardScene);
                    primaryStage.setTitle(APP_TITLE + " - " + user.getName() + " (Fallback)");
                    
                    LOGGER.info("Fallback MainDashboard created successfully");
                } catch (Exception fallbackError) {
                    System.err.println("Fallback dashboard also failed: " + fallbackError.getMessage());
                    LOGGER.severe(() -> "Fallback dashboard creation failed: " + fallbackError.getMessage());
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
        // Check for Firebase test argument
        boolean testFirebase = false;
        for (String arg : args) {
            if ("--test-firebase".equals(arg)) {
                testFirebase = true;
                break;
            }
        }
        
        if (testFirebase) {
            // Run Firebase tests only
            runFirebaseTests();
            return;
        }
        
        launch(args);
    }
    
    /**
     * Run Firebase tests without starting the GUI
     */
    private static void runFirebaseTests() {
        System.out.println("🧪 Running Firebase tests...");
        
        try {
            // Import the test utility
            Class<?> testUtilityClass = Class.forName("com.coincraft.services.FirebaseTestUtility");
            java.lang.reflect.Method testMethod = testUtilityClass.getMethod("testConnection");
            testMethod.invoke(null);
            
        } catch (ClassNotFoundException | NoSuchMethodException | java.lang.reflect.InvocationTargetException | IllegalAccessException e) {
            System.err.println("Failed to run Firebase tests: " + e.getMessage());
            System.err.println("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
        }
    }
}
