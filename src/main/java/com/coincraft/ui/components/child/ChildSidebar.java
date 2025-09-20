package com.coincraft.ui.components.child;

import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Sidebar navigation for Child Dashboard
 * Features vertical navigation with badges and gaming aesthetics
 * Provides access to Home, Tasks, Messages, Shop, and Profile sections
 */
public class ChildSidebar {
    private VBox root;
    @SuppressWarnings("unused")
    private final User currentUser;
    private final Consumer<String> navigationCallback;
    private String activeSection = "home";
    
    // Navigation buttons
    private VBox homeButton;
    private VBox tasksButton;
    private VBox messagesButton;
    private VBox shopButton;
    private VBox profileButton;
    
    public ChildSidebar(User user, Consumer<String> navigationCallback) {
        this.currentUser = user;
        this.navigationCallback = navigationCallback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(8);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(16, 12, 16, 12));
        root.setPrefWidth(120);
        root.setMaxWidth(120);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 0 16 16 0;" +
            "-fx-border-radius: 0 16 16 0;" +
            "-fx-border-color: rgba(255, 255, 255, 0.3);" +
            "-fx-border-width: 0 1 0 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 10, 0);"
        );
        
        // Create navigation buttons
        homeButton = createNavButton("🏡", "Home", "home");
        tasksButton = createNavButton("⚔️", "Tasks", "tasks");
        messagesButton = createNavButtonWithBadge("💬", "Messages", "messages", 2);
        shopButton = createNavButton("🏪", "Shop", "shop");
        profileButton = createNavButton("👨‍🚀", "Profile", "profile");
        
        // Create logout button
        Button logoutButton = createLogoutButton();
        
        root.getChildren().addAll(
            homeButton,
            tasksButton,
            messagesButton,
            shopButton,
            profileButton,
            logoutButton
        );
        
        // Set initial active state
        setActiveSection("home");
    }
    
    /**
     * Create a vertical navigation button
     */
    private VBox createNavButton(String icon, String label, String section) {
        VBox button = new VBox(3);
        button.setAlignment(Pos.CENTER);
        button.setPrefWidth(96);
        button.setPrefHeight(65);
        button.setStyle(
            "-fx-cursor: hand;" +
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 6;"
        );
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        // Label
        Label textLabel = new Label(label);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        textLabel.setMaxWidth(90);
        textLabel.setWrapText(true);
        
        button.getChildren().addAll(iconLabel, textLabel);
        
        // Click handler
        button.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            if (navigationCallback != null) {
                navigationCallback.accept(section);
            }
        });
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-cursor: hand;" +
                    "-fx-background-color: rgba(255, 152, 0, 0.2);" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 6;" +
                    "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-cursor: hand;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 6;"
                );
            }
        });
        
        return button;
    }
    
    /**
     * Create a navigation button with badge
     */
    private VBox createNavButtonWithBadge(String icon, String label, String section, int badgeCount) {
        VBox button = createNavButton(icon, label, section);
        
        if (badgeCount > 0) {
            // Add badge indicator to icon
            Label iconLabel = (Label) button.getChildren().get(0);
            iconLabel.setText(icon + " 🔴");
        }
        
        return button;
    }
    
    /**
     * Set the active section
     */
    public void setActiveSection(String section) {
        // Reset all buttons
        resetButtonStyle(homeButton);
        resetButtonStyle(tasksButton);
        resetButtonStyle(messagesButton);
        resetButtonStyle(shopButton);
        resetButtonStyle(profileButton);
        
        // Set active button
        VBox activeButton = getButtonForSection(section);
        if (activeButton != null) {
            setActiveButtonStyle(activeButton);
        }
        
        this.activeSection = section;
    }
    
    /**
     * Get button for section
     */
    private VBox getButtonForSection(String section) {
        switch (section) {
            case "home": return homeButton;
            case "tasks": return tasksButton;
            case "messages": return messagesButton;
            case "shop": return shopButton;
            case "profile": return profileButton;
            default: return null;
        }
    }
    
    /**
     * Reset button to default style
     */
    private void resetButtonStyle(VBox button) {
        button.setStyle(
            "-fx-cursor: hand;" +
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 6;"
        );
        
        // Reset text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
    }
    
    /**
     * Set active button style
     */
    private void setActiveButtonStyle(VBox button) {
        button.setStyle(
            "-fx-cursor: hand;" +
            "-fx-background-color: #FF9800;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 6;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.5), 12, 0, 0, 6);"
        );
        
        // Set active text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);"
        );
    }
    
    /**
     * Update message badge count
     */
    public void updateMessageBadge(int count) {
        // Update the messages button with new badge count
        int index = root.getChildren().indexOf(messagesButton);
        if (index != -1) {
            root.getChildren().remove(messagesButton);
            messagesButton = createNavButtonWithBadge("💬", "Messages", "messages", count);
            root.getChildren().add(index, messagesButton);
            
            if ("messages".equals(activeSection)) {
                setActiveButtonStyle(messagesButton);
            }
        }
    }
    
    /**
     * Get the root UI component
     */
    private Button createLogoutButton() {
        Button logoutBtn = new Button("🚪");
        logoutBtn.setPrefWidth(80);
        logoutBtn.setPrefHeight(30);
        logoutBtn.setStyle(
            "-fx-background-color: #F44336;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        logoutBtn.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            logoutBtn.setStyle(
                "-fx-background-color: #D32F2F;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
            );
        });
        
        logoutBtn.setOnMouseExited(e -> {
            logoutBtn.setStyle(
                "-fx-background-color: #F44336;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        logoutBtn.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleAdventurerLogout();
        });
        
        return logoutBtn;
    }
    
    private void handleAdventurerLogout() {
        System.out.println("🚪 Adventurer logout requested");
        
        // Clear authentication state and session data
        com.coincraft.ui.routing.DashboardRouter.getInstance().logout();
        
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
                        } catch (Exception ex) {
                            System.out.println("Could not load CSS styles: " + ex.getMessage());
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
                } catch (Exception ex) {
                    System.out.println("Could not load CSS styles: " + ex.getMessage());
                }
                
                // Set login scene
                currentStage.setScene(loginScene);
                currentStage.setTitle("CoinCraft - Login");
                
                System.out.println("✅ Adventurer logged out successfully - returned to login");
                
            } catch (Exception ex) {
                System.out.println("Error during adventurer logout: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
    
    public VBox getRoot() {
        return root;
    }
}
