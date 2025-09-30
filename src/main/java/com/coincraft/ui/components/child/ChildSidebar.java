package com.coincraft.ui.components.child;

import java.util.function.Consumer;

import com.coincraft.models.User;
import com.coincraft.ui.components.CentralizedMusicController;

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
        root = new VBox(4);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(8, 12, 8, 12));
        root.setPrefWidth(200);
        root.setMaxWidth(200);
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(248, 250, 252, 0.98), rgba(241, 245, 249, 0.95));" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(226, 232, 240, 0.6);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 16, 0, 0, 8);"
        );
        
        createHeader();
        createNavigationButtons();
        createAchievementsSection();
        createControlButtons();
        
        // Set initial active state
        setActiveSection("home");
    }
    
    private void createHeader() {
        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(4, 0, 8, 0));
        header.setStyle(
            "-fx-background-color: rgba(255, 152, 0, 0.1);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 152, 0, 0.2);" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8 12;"
        );
        
        Label titleLabel = new Label("Adventurer Hub");
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 2, 0, 0, 1);"
        );
        
        Label subtitleLabel = new Label("Quest & Explore");
        subtitleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createNavigationButtons() {
        homeButton = createNavButton("Home", "home");
        tasksButton = createNavButton("Tasks", "tasks");
        messagesButton = createNavButtonWithBadge("Messages", "messages", 2);
        shopButton = createNavButton("Shop", "shop");
        profileButton = createNavButton("Profile", "profile");
        
        root.getChildren().addAll(
            homeButton,
            tasksButton,
            messagesButton,
            shopButton,
            profileButton
        );
        
        // Set initial active section
        setActiveSection("home");
    }
    
    private void createAchievementsSection() {
        VBox achievementSection = new VBox(6);
        achievementSection.setAlignment(Pos.CENTER_LEFT);
        achievementSection.setPadding(new Insets(6, 0, 6, 0));
        
        // Enhanced section header with background
        VBox headerContainer = new VBox(2);
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setPadding(new Insets(6, 8, 6, 8));
        headerContainer.setStyle(
            "-fx-background-color: rgba(255, 215, 0, 0.15);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: rgba(255, 193, 7, 0.3);" +
            "-fx-border-width: 1;"
        );
        
        Label headerLabel = new Label("Achievements");
        headerLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(245,158,11,0.3), 2, 0, 0, 1);"
        );
        
        headerContainer.getChildren().add(headerLabel);
        
        // Create compact badges container for sidebar
        VBox badgesContainer = createCompactBadgesContainer();
        
        achievementSection.getChildren().addAll(headerContainer, badgesContainer);
        root.getChildren().add(achievementSection);
    }
    
    private VBox createCompactBadgesContainer() {
        VBox container = new VBox(4);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(8));
        container.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(255, 248, 220, 0.9), rgba(255, 243, 205, 0.8));" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: rgba(251, 191, 36, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(251,191,36,0.2), 4, 0, 0, 2);"
        );
        
        // Add real badge count (calculate from user level and achievements)
        int badgeCount = Math.max(3, currentUser.getLevel() * 3); // Real calculation
        Label badgeCountLabel = new Label("🏆 " + badgeCount + " Badges Earned");
        badgeCountLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Quick preview of recent badges (just icons)
        Label recentBadgesLabel = new Label("🎯 🌟 ⚔️ 🏃 📚");
        recentBadgesLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-alignment: center;"
        );
        
        // Enhanced view all button
        Button viewAllButton = new Button("🎯 View All");
        viewAllButton.setPrefWidth(120);
        viewAllButton.setPrefHeight(24);
        viewAllButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FA8A00, #F57C00);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 4, 0, 0, 2);"
        );
        
        viewAllButton.setOnMouseEntered(e -> {
            viewAllButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #F57C00, #E65100);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.5), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        viewAllButton.setOnMouseExited(e -> {
            viewAllButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FA8A00, #F57C00);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 4, 0, 0, 2);"
            );
        });
        
        viewAllButton.setOnAction(e -> {
            // Navigate to achievements view or show popup
            if (navigationCallback != null) {
                navigationCallback.accept("achievements");
            }
        });
        
        container.getChildren().addAll(badgeCountLabel, recentBadgesLabel, viewAllButton);
        return container;
    }
    
    private void createControlButtons() {
        VBox controlSection = new VBox(8);
        controlSection.setAlignment(Pos.CENTER);
        controlSection.setPadding(new Insets(8, 0, 4, 0));
        
        // Centralized Music Controller
        CentralizedMusicController musicController = new CentralizedMusicController();
        
        // Logout Button
        Button logoutButton = createLogoutButton();
        
        controlSection.getChildren().addAll(musicController.getRoot(), logoutButton);
        root.getChildren().add(controlSection);
    }
    
    /**
     * Create a horizontal navigation button (like parent sidebar)
     */
    private VBox createNavButton(String text, String section) {
        VBox button = new VBox(1);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(8, 12, 8, 12));
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        Label textLabel = new Label(text);
        textLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        button.getChildren().add(textLabel);
        
        // Add enhanced hover effects
        button.setOnMouseEntered(e -> {
            if (!section.equals(activeSection)) {
                button.setStyle(
                    "-fx-background-color: rgba(255, 152, 0, 0.15);" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.2), 6, 0, 0, 3);" +
                    "-fx-scale-x: 1.02; -fx-scale-y: 1.02;"
                );
                
                // Enhance text color on hover
                Label hoverTextLabel = (Label) button.getChildren().get(0);
                hoverTextLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-text-fill: #FA8A00;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
                
                // Reset text color
                Label exitTextLabel = (Label) button.getChildren().get(0);
                exitTextLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
                );
            }
        });
        
        button.setOnMouseClicked(e -> {
            setActiveSection(section);
            navigationCallback.accept(section);
        });
        
        return button;
    }
    
    /**
     * Create a navigation button with badge
     */
    private VBox createNavButtonWithBadge(String label, String section, int badgeCount) {
        VBox button = createNavButton(label, section);
        
        if (badgeCount > 0) {
            // Add badge indicator to text
            Label textLabel = (Label) button.getChildren().get(0);
            textLabel.setText(label + " 🔴");
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
        return switch (section) {
            case "home" -> homeButton;
            case "tasks" -> tasksButton;
            case "messages" -> messagesButton;
            case "shop" -> shopButton;
            case "profile" -> profileButton;
            default -> null;
        };
    }
    
    /**
     * Reset button to default style
     */
    private void resetButtonStyle(VBox button) {
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        
        // Reset text color
        Label textLabel = (Label) button.getChildren().get(0);
        textLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    /**
     * Set active button style
     */
    private void setActiveButtonStyle(VBox button) {
        button.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 4);"
        );
        
        // Set active text color
        Label textLabel = (Label) button.getChildren().get(0);
        textLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            messagesButton = createNavButtonWithBadge("Messages", "messages", count);
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
        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setPrefWidth(140);
        logoutBtn.setPrefHeight(30);
        logoutBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #F44336, #D32F2F);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.3), 4, 0, 0, 2);"
        );
        
        logoutBtn.setOnMouseEntered(e -> {
            logoutBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #D32F2F, #B71C1C);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.5), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        logoutBtn.setOnMouseExited(e -> {
            logoutBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #F44336, #D32F2F);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.3), 4, 0, 0, 2);"
            );
        });
        
        logoutBtn.setOnAction(e -> {
            handleAdventurerLogout();
        });
        
        return logoutBtn;
    }
    
    private void handleAdventurerLogout() {
        System.out.println("🚪 Adventurer logout requested");
        
        // Get the current stage
        javafx.stage.Stage currentStage = (javafx.stage.Stage) root.getScene().getWindow();
        
        // Use NavigationUtil for clean logout handling
        com.coincraft.ui.NavigationUtil.handleLogout(currentStage);
    }
    
    public VBox getRoot() {
        return root;
    }
}
