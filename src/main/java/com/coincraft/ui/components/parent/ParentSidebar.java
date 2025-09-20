package com.coincraft.ui.components.parent;

import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Sidebar navigation for Parent Dashboard
 * Features professional navigation with monitoring and management capabilities
 * Provides access to Overview, Children, Tasks, Analytics, and Settings sections
 */
public class ParentSidebar {
    private VBox root;
    @SuppressWarnings("unused")
    private final User currentUser;
    private final Consumer<String> navigationCallback;
    private String activeSection = "overview";
    
    // Navigation buttons
    private VBox overviewButton;
    private VBox childrenButton;
    private VBox tasksButton;
    private VBox analyticsButton;
    private VBox settingsButton;
    
    // Control buttons
    private Button logoutButton;
    private Button musicToggleButton;
    private boolean musicEnabled = true;
    
    public ParentSidebar(User user, Consumer<String> navigationCallback) {
        this.currentUser = user;
        this.navigationCallback = navigationCallback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(12);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(12, 12, 12, 12));
        root.setPrefWidth(200);
        root.setMaxWidth(200);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        createHeader();
        createNavigationButtons();
        createControlButtons();
        createFooter();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 8, 0));
        
        Label titleLabel = new Label("⚔️ Merchant Hub");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Guide Adventures");
        subtitleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createNavigationButtons() {
        overviewButton = createNavButton("🏠", "Overview", "overview");
        childrenButton = createNavButton("⚔️", "My Adventurers", "children");
        tasksButton = createNavButton("📋", "Task Management", "tasks");
        analyticsButton = createNavButton("📈", "Analytics", "analytics");
        settingsButton = createNavButton("⚙️", "Settings", "settings");
        
        root.getChildren().addAll(
            overviewButton,
            childrenButton,
            tasksButton,
            analyticsButton,
            settingsButton
        );
        
        // Set initial active section
        setActiveSection("overview");
    }
    
    private VBox createNavButton(String icon, String text, String section) {
        VBox button = new VBox(4);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 12, 8, 12));
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #666666;"
        );
        
        Label textLabel = new Label(text);
        textLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        button.getChildren().addAll(iconLabel, textLabel);
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            if (!section.equals(activeSection)) {
                SoundManager.getInstance().playButtonHover();
                button.setStyle(
                    "-fx-background-color: rgba(255, 152, 0, 0.1);" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            setActiveSection(section);
            navigationCallback.accept(section);
        });
        
        return button;
    }
    
    private void createControlButtons() {
        VBox controlSection = new VBox(8);
        controlSection.setAlignment(Pos.CENTER);
        controlSection.setPadding(new Insets(16, 0, 8, 0));
        
        // Music Toggle Button
        musicToggleButton = new Button("🎵 Music ON");
        musicToggleButton.setPrefWidth(160);
        musicToggleButton.setPrefHeight(35);
        musicToggleButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        musicToggleButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            musicToggleButton.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        musicToggleButton.setOnMouseExited(e -> {
            String bgColor = musicEnabled ? "#4CAF50" : "#9E9E9E";
            musicToggleButton.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        musicToggleButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            toggleMusic();
        });
        
        // Logout Button
        logoutButton = new Button("🚪 Logout");
        logoutButton.setPrefWidth(160);
        logoutButton.setPrefHeight(35);
        logoutButton.setStyle(
            "-fx-background-color: #F44336;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        logoutButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            logoutButton.setStyle(
                "-fx-background-color: #D32F2F;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        logoutButton.setOnMouseExited(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #F44336;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        logoutButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleLogout();
        });
        
        controlSection.getChildren().addAll(musicToggleButton, logoutButton);
        root.getChildren().add(controlSection);
    }
    
    private void toggleMusic() {
        musicEnabled = !musicEnabled;
        
        if (musicEnabled) {
            SoundManager.getInstance().resumeBackgroundMusic();
            musicToggleButton.setText("🎵 Music ON");
            musicToggleButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            System.out.println("🎵 Background music enabled");
        } else {
            SoundManager.getInstance().pauseBackgroundMusic();
            musicToggleButton.setText("🔇 Music OFF");
            musicToggleButton.setStyle(
                "-fx-background-color: #9E9E9E;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            System.out.println("🔇 Background music disabled");
        }
    }
    
    private void handleLogout() {
        // Show confirmation and logout
        System.out.println("🚪 Merchant logout requested");
        
        // Clear authentication state and session data
        com.coincraft.ui.routing.DashboardRouter.getInstance().logout();
        
        // Return to login screen
        Platform.runLater(() -> {
            try {
                // Get the current stage
                javafx.stage.Stage currentStage = (javafx.stage.Stage) root.getScene().getWindow();
                
                // Create new login screen
                com.coincraft.ui.LoginScreen loginScreen = new com.coincraft.ui.LoginScreen(new com.coincraft.ui.LoginScreen.LoginCallback() {
                    @Override
                    public void onLoginSuccess(com.coincraft.models.User user) {
                        // Create new dashboard based on user role
                        com.coincraft.ui.routing.DashboardRouter router = com.coincraft.ui.routing.DashboardRouter.getInstance();
                        javafx.scene.Scene dashboardScene = new javafx.scene.Scene(router.routeToDashboard(user), 1200, 800);
                        
                        // Load styles
                        try {
                            dashboardScene.getStylesheets().add(
                                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
                            );
                        } catch (Exception e) {
                            System.out.println("Could not load CSS styles: " + e.getMessage());
                        }
                        
                        currentStage.setScene(dashboardScene);
                        currentStage.setTitle("CoinCraft - " + user.getName() + " (" + user.getRole() + ")");
                    }
                    
                    @Override
                    public void onLoginFailed(String error) {
                        System.err.println("Login failed: " + error);
                    }
                    
                    @Override
                    public void onNavigateToSignUp() {
                        // Handle sign up navigation if needed
                        System.out.println("Navigate to sign up requested");
                    }
                });
                
                // Create login scene
                javafx.scene.Scene loginScene = new javafx.scene.Scene(loginScreen.getRoot(), 1200, 800);
                
                // Load styles
                try {
                    loginScene.getStylesheets().add(
                        getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
                    );
                } catch (Exception e) {
                    System.out.println("Could not load CSS styles: " + e.getMessage());
                }
                
                // Set login scene
                currentStage.setScene(loginScene);
                currentStage.setTitle("CoinCraft - Login");
                
                System.out.println("✅ Merchant logged out successfully - returned to login");
                
            } catch (Exception ex) {
                System.out.println("Error during logout: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
    
    private void createFooter() {
        VBox footer = new VBox(8);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(8, 0, 0, 0));
        
        Label versionLabel = new Label("CoinCraft v1.0");
        versionLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #999999;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        footer.getChildren().add(versionLabel);
        root.getChildren().add(footer);
    }
    
    public void setActiveSection(String section) {
        this.activeSection = section;
        
        // Reset all buttons
        resetButtonStyle(overviewButton);
        resetButtonStyle(childrenButton);
        resetButtonStyle(tasksButton);
        resetButtonStyle(analyticsButton);
        resetButtonStyle(settingsButton);
        
        // Set active button style
        VBox activeButton = getButtonBySection(section);
        if (activeButton != null) {
            activeButton.setStyle(
                "-fx-background-color: rgba(255, 152, 0, 0.2);" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #FF9800;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;"
            );
        }
    }
    
    private void resetButtonStyle(VBox button) {
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
    }
    
    private VBox getButtonBySection(String section) {
        switch (section) {
            case "overview": return overviewButton;
            case "children": return childrenButton;
            case "tasks": return tasksButton;
            case "analytics": return analyticsButton;
            case "settings": return settingsButton;
            default: return null;
        }
    }
    
    public VBox getRoot() {
        return root;
    }
}
