package com.coincraft.ui.components.child;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Badge;
import com.coincraft.models.BadgeCategory;
import com.coincraft.models.BadgeLevel;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Modern badge slider with navigation buttons for Child Dashboard
 * Shows earned badges and "Coming Soon" slots for motivation
 * Features smooth sliding and navigation controls
 */
public class BadgesStrip {
    private StackPane root;
    private ScrollPane scrollPane;
    private HBox badgeContainer;
    private Button leftButton;
    private Button rightButton;
    private final User currentUser;
    private final List<Badge> earnedBadges;
    private final List<Badge> availableBadges;
    
    public BadgesStrip(User user) {
        this.currentUser = user;
        this.earnedBadges = generateEarnedBadges(); // In real app, load from database
        this.availableBadges = generateAvailableBadges();
        initializeUI();
    }
    
    private void initializeUI() {
        // Create main container
        root = new StackPane();
        root.setPrefHeight(100);
        root.setMaxHeight(100);
        root.setStyle("-fx-background-color: transparent;");
        
        // Create horizontal scrolling container
        badgeContainer = new HBox(12);
        badgeContainer.setAlignment(Pos.CENTER_LEFT);
        badgeContainer.setPadding(new Insets(8, 16, 8, 16));
        
        // Create scroll pane (hidden scrollbars)
        scrollPane = new ScrollPane(badgeContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Create navigation buttons
        createNavigationButtons();
        
        // Add everything to root
        root.getChildren().addAll(scrollPane, leftButton, rightButton);
        
        refreshBadges();
    }
    
    /**
     * Create navigation buttons for slider
     */
    private void createNavigationButtons() {
        // Left navigation button
        leftButton = new Button("‹");
        leftButton.setPrefSize(40, 40);
        leftButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );
        leftButton.setOnAction(e -> scrollLeft());
        
        // Right navigation button
        rightButton = new Button("›");
        rightButton.setPrefSize(40, 40);
        rightButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
        );
        rightButton.setOnAction(e -> scrollRight());
        
        // Position buttons
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(leftButton, new Insets(0, 0, 0, 8));
        StackPane.setMargin(rightButton, new Insets(0, 8, 0, 0));
        
        // Add hover effects
        addButtonHoverEffects(leftButton);
        addButtonHoverEffects(rightButton);
    }
    
    /**
     * Add hover effects to navigation buttons
     */
    private void addButtonHoverEffects(Button button) {
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + 
                "-fx-scale-x: 1.1; -fx-scale-y: 1.1;" +
                "-fx-background-color: rgba(255, 152, 0, 0.9);" +
                "-fx-text-fill: white;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-text-fill: #333333;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"
            );
        });
    }
    
    /**
     * Scroll left in the badge slider
     */
    private void scrollLeft() {
        SoundManager.getInstance().playButtonClick();
        double currentValue = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.max(0, currentValue - 0.2));
    }
    
    /**
     * Scroll right in the badge slider
     */
    private void scrollRight() {
        SoundManager.getInstance().playButtonClick();
        double currentValue = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.min(1, currentValue + 0.2));
    }
    
    /**
     * Refresh the badge display
     */
    public void refresh() {
        badgeContainer.getChildren().clear();
        refreshBadges();
    }
    
    /**
     * Create and display badge items
     */
    private void refreshBadges() {
        // Add earned badges first
        for (Badge badge : earnedBadges) {
            VBox badgeItem = createEarnedBadgeItem(badge);
            badgeContainer.getChildren().add(badgeItem);
        }
        
        // Add available/coming soon badges
        for (Badge badge : availableBadges) {
            VBox badgeItem = createComingSoonBadgeItem(badge);
            badgeContainer.getChildren().add(badgeItem);
        }
        
        // Add motivational "More Coming" item
        VBox moreItem = createMoreComingItem();
        badgeContainer.getChildren().add(moreItem);
    }
    
    /**
     * Create a compact earned badge item
     */
    private VBox createEarnedBadgeItem(Badge badge) {
        VBox item = new VBox(4);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(70);
        item.setPrefHeight(70);
        item.setMaxHeight(70);
        item.setStyle(
            "-fx-background-color: rgba(255, 215, 0, 0.85);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 143, 0, 0.8);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.5), 12, 0, 0, 6);" +
            "-fx-cursor: hand;"
        );
        
        // Smaller badge icon
        Label iconLabel = new Label(getBadgeIcon(badge.getCategory()));
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);"
        );
        
        // Compact badge name
        Label nameLabel = new Label(badge.getName());
        nameLabel.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);"
        );
        nameLabel.setMaxWidth(60);
        
        item.getChildren().addAll(iconLabel, nameLabel);
        
        // Click handler
        item.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            showBadgeDetails(badge, true);
        });
        
        // Hover effects
        item.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            item.setStyle(item.getStyle() + "-fx-scale-x: 1.1; -fx-scale-y: 1.1;");
        });
        
        item.setOnMouseExited(e -> {
            item.setStyle(item.getStyle().replace("-fx-scale-x: 1.1; -fx-scale-y: 1.1;", ""));
        });
        
        return item;
    }
    
    /**
     * Create a compact "coming soon" badge item
     */
    private VBox createComingSoonBadgeItem(Badge badge) {
        VBox item = new VBox(4);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(70);
        item.setPrefHeight(70);
        item.setMaxHeight(70);
        item.setStyle(
            "-fx-background-color: rgba(224, 224, 224, 0.75);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(158, 158, 158, 0.8);" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);" +
            "-fx-cursor: hand;"
        );
        
        // Smaller badge icon (grayed out)
        Label iconLabel = new Label(getBadgeIcon(badge.getCategory()));
        iconLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-opacity: 0.5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0, 0, 2);"
        );
        
        // Compact "Coming Soon" text
        Label comingSoonLabel = new Label("Coming\nSoon!");
        comingSoonLabel.setStyle(
            "-fx-font-size: 7px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        item.getChildren().addAll(iconLabel, comingSoonLabel);
        
        // Click handler
        item.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            showBadgeDetails(badge, false);
        });
        
        // Hover effects
        item.setOnMouseEntered(e -> {
            item.setStyle(item.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        item.setOnMouseExited(e -> {
            item.setStyle(item.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        return item;
    }
    
    /**
     * Create compact "More Coming" motivational item
     */
    private VBox createMoreComingItem() {
        VBox item = new VBox(4);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(70);
        item.setPrefHeight(70);
        item.setMaxHeight(70);
        item.setStyle(
            "-fx-background-color: rgba(76, 175, 80, 0.85);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(46, 125, 50, 0.8);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.5), 10, 0, 0, 5);"
        );
        
        // Smaller plus icon
        Label iconLabel = new Label("➕");
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: white;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);"
        );
        
        // Compact motivational text
        Label textLabel = new Label("More\nAwaits!");
        textLabel.setStyle(
            "-fx-font-size: 8px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);"
        );
        
        item.getChildren().addAll(iconLabel, textLabel);
        
        return item;
    }
    
    /**
     * Show badge details dialog
     */
    private void showBadgeDetails(Badge badge, boolean isEarned) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        
        if (isEarned) {
            dialog.setTitle("Badge Earned!");
            dialog.setHeaderText("🏆 " + badge.getName());
            dialog.setContentText(
                "Congratulations! You've earned this badge!\n\n" +
                "Description: " + badge.getDescription() + "\n\n" +
                "Category: " + badge.getCategory().toString() + "\n" +
                "Level: " + badge.getLevel().toString() + "\n\n" +
                "Keep up the amazing work, adventurer!"
            );
        } else {
            dialog.setTitle("Future Badge");
            dialog.setHeaderText("🔮 " + badge.getName());
            dialog.setContentText(
                "This badge is waiting for you to earn it!\n\n" +
                "What you need to do:\n" + badge.getDescription() + "\n\n" +
                "Category: " + badge.getCategory().toString() + "\n" +
                "Level: " + badge.getLevel().toString() + "\n\n" +
                "Keep adventuring to unlock this badge!"
            );
        }
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
    
    /**
     * Get icon for badge category
     */
    private String getBadgeIcon(BadgeCategory category) {
        switch (category) {
            case LEARNING: return "🎓";
            case CREATIVITY: return "🎭";
            case FITNESS: return "🏋️‍♀️";
            case SOCIAL: return "👫";
            case ACHIEVEMENT: return "🏆";
            case SPECIAL: return "✨";
            default: return "🎖️";
        }
    }
    
    /**
     * Generate mock earned badges
     */
    private List<Badge> generateEarnedBadges() {
        List<Badge> badges = new ArrayList<>();
        
        Badge badge1 = new Badge("B001", "First Steps", "Completed your first quest!", 
            BadgeCategory.ACHIEVEMENT, BadgeLevel.BRONZE);
        badges.add(badge1);
        
        Badge badge2 = new Badge("B002", "Math Wizard", "Solved 10 math problems correctly!", 
            BadgeCategory.LEARNING, BadgeLevel.BRONZE);
        badges.add(badge2);
        
        Badge badge3 = new Badge("B003", "Creative Mind", "Completed your first art project!", 
            BadgeCategory.CREATIVITY, BadgeLevel.BRONZE);
        badges.add(badge3);
        
        return badges;
    }
    
    /**
     * Generate mock available badges
     */
    private List<Badge> generateAvailableBadges() {
        List<Badge> badges = new ArrayList<>();
        
        Badge badge1 = new Badge("B004", "Speed Reader", "Read 5 books this month", 
            BadgeCategory.LEARNING, BadgeLevel.SILVER);
        badges.add(badge1);
        
        Badge badge2 = new Badge("B005", "Fitness Champion", "Exercise for 7 days straight", 
            BadgeCategory.FITNESS, BadgeLevel.GOLD);
        badges.add(badge2);
        
        Badge badge3 = new Badge("B006", "Team Player", "Help 3 friends with their quests", 
            BadgeCategory.SOCIAL, BadgeLevel.SILVER);
        badges.add(badge3);
        
        Badge badge4 = new Badge("B007", "Master Saver", "Save 100 SmartCoins", 
            BadgeCategory.ACHIEVEMENT, BadgeLevel.GOLD);
        badges.add(badge4);
        
        return badges;
    }
    
    /**
     * Get the root UI component
     */
    public StackPane getRoot() {
        return root;
    }
}
