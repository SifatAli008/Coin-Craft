package com.coincraft.ui.components.child;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Badge;
import com.coincraft.models.BadgeCategory;
import com.coincraft.models.BadgeLevel;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Horizontally scrolling badge display for Child Dashboard
 * Shows earned badges and "Coming Soon" slots for motivation
 * Features click interactions and achievement celebrations
 */
public class BadgesStrip {
    private ScrollPane root;
    private HBox badgeContainer;
    private User currentUser;
    private List<Badge> earnedBadges;
    private List<Badge> availableBadges;
    
    public BadgesStrip(User user) {
        this.currentUser = user;
        this.earnedBadges = generateEarnedBadges(); // In real app, load from database
        this.availableBadges = generateAvailableBadges();
        initializeUI();
    }
    
    private void initializeUI() {
        // Create compact horizontal scrolling container
        badgeContainer = new HBox(8);
        badgeContainer.setAlignment(Pos.CENTER_LEFT);
        badgeContainer.setPadding(new Insets(8));
        
        // Create scroll pane
        root = new ScrollPane(badgeContainer);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setFitToHeight(true);
        root.setPrefHeight(100);
        root.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;"
        );
        
        refreshBadges();
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
        VBox item = new VBox(6);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(65);
        item.setPrefHeight(80);
        item.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FFD700, #FFA000);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #FF8F00;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.4), 8, 0, 0, 4);" +
            "-fx-cursor: hand;"
        );
        
        // Smaller badge icon
        Label iconLabel = new Label(getBadgeIcon(badge.getCategory()));
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);"
        );
        
        // Compact badge name
        Label nameLabel = new Label(badge.getName());
        nameLabel.setStyle(
            "-fx-font-size: 8px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 2, 0, 0, 1);"
        );
        nameLabel.setMaxWidth(55);
        
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
        VBox item = new VBox(6);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(65);
        item.setPrefHeight(80);
        item.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #E0E0E0, #BDBDBD);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #9E9E9E;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 3);" +
            "-fx-cursor: hand;"
        );
        
        // Smaller badge icon (grayed out)
        Label iconLabel = new Label(getBadgeIcon(badge.getCategory()));
        iconLabel.setStyle(
            "-fx-font-size: 20px;" +
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
        VBox item = new VBox(6);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(65);
        item.setPrefHeight(80);
        item.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #4CAF50, #388E3C);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #2E7D32;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 6, 0, 0, 3);"
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
            case LEARNING: return "📚";
            case CREATIVITY: return "🎨";
            case FITNESS: return "💪";
            case SOCIAL: return "👥";
            case ACHIEVEMENT: return "🏆";
            case SPECIAL: return "⭐";
            default: return "🏅";
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
    public ScrollPane getRoot() {
        return root;
    }
}
