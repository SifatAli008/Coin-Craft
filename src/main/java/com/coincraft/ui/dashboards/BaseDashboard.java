package com.coincraft.ui.dashboards;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.User;
import com.coincraft.ui.routing.RoleGuard;
import com.coincraft.ui.util.Fonts;

import animatefx.animation.FadeIn;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * Abstract base class for all role-based dashboards
 * Provides common functionality and maintains game theme consistency
 */
public abstract class BaseDashboard {
    protected Pane root;
    protected User currentUser;
    
    public BaseDashboard(User user) {
        this.currentUser = user;
        
        // Validate user access
        RoleGuard.validateUserAccess(user);
        
        // Load game fonts
        loadGameFonts();
        
        // Initialize UI
        initializeUI();
        
        // Apply game theme
        applyGameTheme();
        
        // Start animations
        startAnimations();
        
        // Initialize centralized music system to prevent overlaps
        // This ensures only one background music track plays across all dashboards
        System.out.println("🎵 BaseDashboard initializing centralized music for " + user.getRole() + " dashboard");
        CentralizedMusicManager.getInstance().play();
    }
    
    /**
     * Initialize the dashboard UI
     * Must be implemented by each role-specific dashboard
     */
    protected abstract void initializeUI();
    
    /**
     * Navigate to a specific section within the dashboard
     * @param section Section identifier
     */
    public abstract void navigateToSection(String section);
    
    /**
     * Get the root UI component
     * @return JavaFX Parent component
     */
    public Parent getRoot() {
        return root;
    }
    
    /**
     * Get the current user
     * @return Current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Load Minecraft game fonts for consistent theming
     */
    private void loadGameFonts() {
        Fonts.ensureLoaded();
    }
    
    /**
     * Apply consistent game theme across all dashboards
     */
    protected void applyGameTheme() {
        if (root != null) {
            root.setStyle(
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6f3ff, #ddeeff);"
            );
        }
    }
    
    /**
     * Start entrance animations
     */
    protected void startAnimations() {
        if (root != null) {
            try {
                new FadeIn(root).play();
            } catch (Throwable ignored) {
                // Animation library not available or failed
            }
        }
    }
    
    /**
     * Check if current user has access to a feature
     * @param feature Feature identifier
     * @return true if user has access
     */
    protected boolean hasAccess(String feature) {
        return RoleGuard.hasFeatureAccess(currentUser, feature);
    }
    
    /**
     * Check if current user has level-based access
     * @param feature Feature identifier
     * @param requiredLevel Required level
     * @return true if user has access
     */
    protected boolean hasLevelAccess(String feature, int requiredLevel) {
        return RoleGuard.hasLevelAccess(currentUser, feature, requiredLevel);
    }
    
    /**
     * Get user-friendly error message for access denial
     * @param feature Feature identifier
     * @return Error message
     */
    protected String getAccessDeniedMessage(String feature) {
        return RoleGuard.getAccessDeniedMessage(currentUser, feature);
    }
    
    /**
     * Create a standardized game-themed button
     * @param text Button text
     * @param primaryColor Primary color
     * @param hoverColor Hover color
     * @return Styled button
     */
    protected javafx.scene.control.Button createGameButton(String text, String primaryColor, String hoverColor) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setStyle(
            "-fx-background-color: " + primaryColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-padding: 8 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);"
        );
        
        button.setOnMouseEntered(e -> {
            // Note: Button hover sounds can be implemented via CentralizedMusicManager if needed
            button.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-padding: 8 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + primaryColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-padding: 8 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);"
            );
        });
        
        button.setOnAction(e -> {
            // Note: Button click sounds can be implemented via CentralizedMusicManager if needed
        });
        
        return button;
    }
    
    /**
     * Create a clean modern card container
     * @return Styled VBox container
     */
    protected javafx.scene.layout.VBox createGameCard() {
        javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(16);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.6);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10);" +
            "-fx-padding: 24;"
        );
        return card;
    }
}
