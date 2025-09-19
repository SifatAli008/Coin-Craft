package com.coincraft.ui.components.child;

import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Sidebar navigation for Child Dashboard
 * Features vertical navigation with badges and gaming aesthetics
 * Provides access to Home, Tasks, Messages, Shop, and Profile sections
 */
public class ChildSidebar {
    private VBox root;
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
        root.setPadding(new Insets(15, 12, 15, 12));
        root.setPrefWidth(80);
        root.setMaxWidth(80);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 0 16 16 0;" +
            "-fx-border-radius: 0 16 16 0;" +
            "-fx-border-color: rgba(255, 255, 255, 0.6);" +
            "-fx-border-width: 0 2 0 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 6, 0);"
        );
        
        // Create navigation buttons
        homeButton = createNavButton("🏠", "Home", "home");
        tasksButton = createNavButton("📋", "Tasks", "tasks");
        messagesButton = createNavButtonWithBadge("💬", "Messages", "messages", 2);
        shopButton = createNavButton("🛒", "Shop", "shop");
        profileButton = createNavButton("👤", "Profile", "profile");
        
        root.getChildren().addAll(
            homeButton,
            tasksButton,
            messagesButton,
            shopButton,
            profileButton
        );
        
        // Set initial active state
        setActiveSection("home");
    }
    
    /**
     * Create a vertical navigation button
     */
    private VBox createNavButton(String icon, String label, String section) {
        VBox button = new VBox(4);
        button.setAlignment(Pos.CENTER);
        button.setPrefWidth(60);
        button.setPrefHeight(60);
        button.setStyle(
            "-fx-cursor: hand;" +
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 8;"
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
            "-fx-font-size: 9px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        textLabel.setMaxWidth(55);
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
                    "-fx-background-color: rgba(255, 152, 0, 0.1);" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 8;" +
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-cursor: hand;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 8;"
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
            "-fx-padding: 8;"
        );
        
        // Reset text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 9px;" +
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
            "-fx-padding: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 4);"
        );
        
        // Set active text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 9px;" +
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
    public VBox getRoot() {
        return root;
    }
}
