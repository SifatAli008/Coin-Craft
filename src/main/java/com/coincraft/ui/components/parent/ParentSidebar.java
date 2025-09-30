package com.coincraft.ui.components.parent;

import java.util.function.Consumer;

import com.coincraft.models.User;
import com.coincraft.ui.components.CentralizedMusicController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private VBox messagingButton;
    private VBox settingsButton;
    
    // Control buttons
    private Button logoutButton;
    
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
        
        Label titleLabel = new Label("Merchant Hub");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Guide Adventures");
        subtitleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createNavigationButtons() {
        overviewButton = createNavButton("Overview", "overview");
        childrenButton = createNavButton("My Adventurers", "children");
        tasksButton = createNavButton("Task Management", "tasks");
        messagingButton = createNavButton("Messaging", "messaging");
        settingsButton = createNavButton("Settings", "settings");
        
        root.getChildren().addAll(
            overviewButton,
            childrenButton,
            tasksButton,
            messagingButton,
            settingsButton
        );
        
        // Set initial active section
        setActiveSection("overview");
    }
    
    private VBox createNavButton(String text, String section) {
        VBox button = new VBox(4);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(12, 16, 12, 16));
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        Label textLabel = new Label(text);
        textLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        button.getChildren().add(textLabel);
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            if (!section.equals(activeSection)) {
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
            setActiveSection(section);
            navigationCallback.accept(section);
        });
        
        return button;
    }
    
    private void createControlButtons() {
        VBox controlSection = new VBox(12);
        controlSection.setAlignment(Pos.CENTER);
        controlSection.setPadding(new Insets(16, 0, 8, 0));
        
        // Music Controller
        CentralizedMusicController musicController = new CentralizedMusicController();
        
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        logoutButton.setOnMouseEntered(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #D32F2F;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
            );
        });
        
        logoutButton.setOnAction(e -> {
            handleLogout();
        });
        
        controlSection.getChildren().addAll(musicController.getRoot(), logoutButton);
        root.getChildren().add(controlSection);
    }
    
    private void handleLogout() {
        System.out.println("🚪 Merchant logout requested");
        
        // Get the current stage
        javafx.stage.Stage currentStage = (javafx.stage.Stage) root.getScene().getWindow();
        
        // Use NavigationUtil for clean logout handling
        com.coincraft.ui.NavigationUtil.handleLogout(currentStage);
    }
    
    private void createFooter() {
        VBox footer = new VBox(8);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(8, 0, 0, 0));
        
        Label versionLabel = new Label("CoinCraft v1.0");
        versionLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #999999;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
        resetButtonStyle(messagingButton);
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
            
            // Update text color for active button
            Label activeTextLabel = (Label) activeButton.getChildren().get(0);
            activeTextLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: #FF9800;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
            );
        }
    }
    
    private void resetButtonStyle(VBox button) {
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        // Reset text color
        Label textLabel = (Label) button.getChildren().get(0);
        textLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
    }
    
    private VBox getButtonBySection(String section) {
        return switch (section) {
            case "overview" -> overviewButton;
            case "children" -> childrenButton;
            case "tasks" -> tasksButton;
            case "messaging" -> messagingButton;
            case "settings" -> settingsButton;
            default -> null;
        };
    }
    
    public VBox getRoot() {
        return root;
    }
}
