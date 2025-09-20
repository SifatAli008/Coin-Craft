package com.coincraft.ui.components.child;

import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Bottom navigation bar for Child Dashboard
 * Features tab-style navigation with badges and gaming aesthetics
 * Provides access to Home, Tasks, Messages, Shop, and Profile sections
 */
public class ChildBottomNav {
    private HBox root;
    private User currentUser;
    private Consumer<String> navigationCallback;
    private String activeSection = "home";
    
    // Navigation buttons
    private VBox homeButton;
    private VBox tasksButton;
    private VBox messagesButton;
    private VBox shopButton;
    private VBox profileButton;
    
    public ChildBottomNav(User user, Consumer<String> navigationCallback) {
        this.currentUser = user;
        this.navigationCallback = navigationCallback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15, 20, 15, 20));
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 20 20 0 0;" +
            "-fx-border-radius: 20 20 0 0;" +
            "-fx-border-color: rgba(255, 255, 255, 0.6);" +
            "-fx-border-width: 2 2 0 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, -8);"
        );
        
        // Create navigation buttons
        homeButton = createNavButton("🏠", "Home", "home");
        tasksButton = createNavButton("📋", "Tasks", "tasks");
        messagesButton = createNavButtonWithBadge("💬", "Messages", "messages", 2); // 2 unread messages
        shopButton = createNavButton("🛒", "Shop", "shop");
        profileButton = createNavButton("👤", "Profile", "profile");
        
        // Create spacers
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();
        Region spacer4 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        HBox.setHgrow(spacer4, Priority.ALWAYS);
        
        root.getChildren().addAll(
            homeButton, spacer1,
            tasksButton, spacer2,
            messagesButton, spacer3,
            shopButton, spacer4,
            profileButton
        );
        
        // Set initial active state
        setActiveSection("home");
    }
    
    /**
     * Create a navigation button
     */
    private VBox createNavButton(String icon, String label, String section) {
        VBox button = new VBox(4);
        button.setAlignment(Pos.CENTER);
        button.setPrefWidth(60);
        button.setStyle(
            "-fx-cursor: hand;" +
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 8 12;"
        );
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        // Label
        Label textLabel = new Label(label);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
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
                    "-fx-padding: 8 12;" +
                    "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-cursor: hand;" +
                    "-fx-background-color: transparent;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 8 12;"
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
            // Add badge overlay
            Label iconLabel = (Label) button.getChildren().get(0);
            
            // Create badge
            Label badge = new Label(String.valueOf(badgeCount));
            badge.setStyle(
                "-fx-background-color: #F44336;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-min-width: 16;" +
                "-fx-min-height: 16;" +
                "-fx-alignment: center;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            
            // Position badge (this is a simplified approach)
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
            "-fx-padding: 8 12;"
        );
        
        // Reset text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-padding: 8 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 4);"
        );
        
        // Set active text color
        Label textLabel = (Label) button.getChildren().get(1);
        textLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);"
        );
    }
    
    /**
     * Update message badge count
     */
    public void updateMessageBadge(int count) {
        // In a real implementation, this would update the badge display
        // For now, we'll just recreate the messages button
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
    public HBox getRoot() {
        return root;
    }
}
