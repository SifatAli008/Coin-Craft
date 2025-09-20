package com.coincraft.ui.dashboards;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;
import com.coincraft.ui.components.child.BadgesStrip;
import com.coincraft.ui.components.child.ChildLeaderboard;
import com.coincraft.ui.components.child.ChildSidebar;
import com.coincraft.ui.components.child.ChildTopBar;
import com.coincraft.ui.components.child.DailyStreakCalendar;
import com.coincraft.ui.components.child.EventBanner;
import com.coincraft.ui.components.child.LevelProgressWidget;
import com.coincraft.ui.components.child.ShopPage;
import com.coincraft.ui.components.child.AdventurerTaskView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Adventurer Dashboard - Main interface for young adventurers
 * Features gamified learning, avatar customization, and progress tracking
 * Follows the established game theme with Minecraft fonts and gaming aesthetics
 */
public class ChildDashboard extends BaseDashboard {
    
    // Main UI Components
    private ChildTopBar topBar;
    private LevelProgressWidget levelProgress;
    private AdventurerTaskView taskView;
    private BadgesStrip badgesStrip;
    private DailyStreakCalendar streakCalendar;
    private ChildLeaderboard leaderboard;
    private EventBanner eventBanner;
    private ChildSidebar sidebar;
    private ShopPage shopPage;
    
    // Content sections
    private VBox mainContent;
    private String currentSection = "home";
    
    public ChildDashboard(User user) {
        super(user);
    }
    
    @Override
    protected void initializeUI() {
        // Create a StackPane as the root to properly handle background
        StackPane backgroundContainer = new StackPane();
        backgroundContainer.setPadding(new Insets(0));
        backgroundContainer.setAlignment(Pos.CENTER);
        
        // Create background ImageView for animated GIF
        ImageView backgroundImageView = createBackgroundImageView(backgroundContainer);
        
        // Add semi-transparent dark overlay for better contrast
        Region darkOverlay = new Region();
        darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
        darkOverlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMouseTransparent(true); // Allow clicks to pass through
        
        // Create the main BorderPane for layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("child-dashboard");
        mainLayout.setStyle("-fx-background-color: transparent;");
        mainLayout.setPadding(new Insets(8, 8, 8, 8));
        
        // Create main sections
        createTopSection();
        createSidebar();
        createMainContent();
        
        // Set up layout with sidebar
        mainLayout.setTop(topBar.getRoot());
        mainLayout.setLeft(sidebar.getRoot());
        mainLayout.setCenter(mainContent);
        
        // Add everything to the background container (background first, then overlay, then content)
        backgroundContainer.getChildren().addAll(backgroundImageView, darkOverlay, mainLayout);
        
        // Set the StackPane as the root
        root = backgroundContainer;
    }
    
    /**
     * Create the top bar with avatar, welcome message, and coin balance
     */
    private void createTopSection() {
        topBar = new ChildTopBar(currentUser);
    }
    
    /**
     * Create properly sized main content area
     */
    private void createMainContent() {
        mainContent = new VBox(16);
        mainContent.setPadding(new Insets(8, 16, 16, 16));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: transparent;");
        mainContent.setMaxWidth(1180);
        mainContent.setPrefWidth(1180);
        
        // Hero Section - Level Progress and Stats
        HBox heroSection = createHeroSection();
        
        // Main Grid Layout - Responsive Cards
        VBox mainGrid = createMainGrid();
        
        // Action Section
        HBox actionSection = createActionSection();
        
        mainContent.getChildren().addAll(heroSection, mainGrid, actionSection);
    }
    
    /**
     * Create hero section with level progress and inline stats
     */
    private HBox createHeroSection() {
        HBox heroSection = new HBox(24);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(8, 0, 16, 0));
        heroSection.setMaxWidth(1180);
        
        // Left side - Level Progress Widget
        levelProgress = new LevelProgressWidget(currentUser);
        
        // Right side - Quick Stats Row
        HBox statsRow = createQuickStatsRow();
        
        heroSection.getChildren().addAll(levelProgress.getRoot(), statsRow);
        HBox.setHgrow(levelProgress.getRoot(), Priority.NEVER);
        HBox.setHgrow(statsRow, Priority.NEVER);
        
        return heroSection;
    }
    
    /**
     * Create quick stats row
     */
    private HBox createQuickStatsRow() {
        HBox statsRow = new HBox(12);
        statsRow.setAlignment(Pos.CENTER_RIGHT);
        statsRow.setPadding(new Insets(0));
        
        // Active Tasks Count
        HBox tasksStatCard = createStatCard("⚔", "3", "Active Quests");
        
        // Earned Badges Count  
        HBox badgesStatCard = createStatCard("🏆", "12", "Badges Earned");
        
        // Current Streak
        HBox streakStatCard = createStatCard("🔥", "7", "Day Streak");
        
        // Coins Balance
        HBox coinsStatCard = createStatCard("💰", "250", "SmartCoins");
        
        statsRow.getChildren().addAll(tasksStatCard, badgesStatCard, streakStatCard, coinsStatCard);
        return statsRow;
    }
    
    /**
     * Create properly sized grid layout
     */
    private VBox createMainGrid() {
        VBox mainGrid = new VBox(16);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setMaxWidth(1180);
        
        // Row 1: Primary Content
        HBox primaryRow = new HBox(16);
        primaryRow.setAlignment(Pos.TOP_CENTER);
        primaryRow.setMaxWidth(1180);
        
        // Main Tasks Section (65% width)
        VBox tasksSection = createTasksSection();
        tasksSection.setPrefWidth(750);
        tasksSection.setMaxWidth(750);
        
        // Side Panel (35% width)  
        VBox sidePanel = createSidePanel();
        sidePanel.setPrefWidth(350);
        sidePanel.setMaxWidth(350);
        
        primaryRow.getChildren().addAll(tasksSection, sidePanel);
        HBox.setHgrow(tasksSection, Priority.NEVER);
        HBox.setHgrow(sidePanel, Priority.NEVER);
        
        mainGrid.getChildren().add(primaryRow);
        return mainGrid;
    }
    
    /**
     * Create modern tasks section
     */
    private VBox createTasksSection() {
        VBox tasksSection = new VBox(16);
        
        taskView = new AdventurerTaskView(currentUser, task -> {
            // Handle task completion - could refresh user data or show notifications
            System.out.println("Task completed: " + task.getTitle());
        });
        badgesStrip = new BadgesStrip(currentUser);
        
        // Tasks Card
        VBox tasksCard = createModernCard("Active Quests", taskView.getRoot());
        
        // Badges Card
        VBox badgesCard = createModernCard("Achievement Collection", badgesStrip.getRoot());
        
        tasksSection.getChildren().addAll(tasksCard, badgesCard);
        return tasksSection;
    }
    
    /**
     * Create side panel with social features
     */
    private VBox createSidePanel() {
        VBox sidePanel = new VBox(16);
        
        streakCalendar = new DailyStreakCalendar(currentUser);
        leaderboard = new ChildLeaderboard(currentUser);
        eventBanner = new EventBanner(currentUser);
        
        // Streak Card
        VBox streakCard = createModernCard("Daily Progress", streakCalendar.getRoot());
        
        // Leaderboard Card
        VBox leaderboardCard = createModernCard("Leaderboard", leaderboard.getRoot());
        
        // Events Card
        VBox eventsCard = createModernCard("Special Events", eventBanner.getRoot());
        
        sidePanel.getChildren().addAll(streakCard, leaderboardCard, eventsCard);
        return sidePanel;
    }
    
    /**
     * Create action section with primary CTA
     */
    private HBox createActionSection() {
        HBox actionSection = new HBox();
        actionSection.setAlignment(Pos.CENTER);
        actionSection.setPadding(new Insets(16, 0, 0, 0));
        actionSection.setMaxWidth(1180);
        
        Button requestMissionBtn = createRequestMissionButton();
        actionSection.getChildren().add(requestMissionBtn);
        
        return actionSection;
    }
    
    /**
     * Create the sidebar navigation
     */
    private void createSidebar() {
        sidebar = new ChildSidebar(currentUser, this::navigateToSection);
    }
    
    /**
     * Create background ImageView for animated GIF
     */
    private ImageView createBackgroundImageView(StackPane container) {
        try {
            // Load the animated GIF as an Image
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/tumblr_5d37ab2aa782462c7aa092f7bd0d27cb_fe094893_1280.gif"));
            
            // Create ImageView
            ImageView imageView = new ImageView(backgroundImage);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);
            imageView.setCache(true);
            
            // Make it fill the entire container
            imageView.fitWidthProperty().bind(container.widthProperty());
            imageView.fitHeightProperty().bind(container.heightProperty());
            
            System.out.println("Child Dashboard background GIF loaded successfully: " + backgroundImage.getWidth() + "x" + backgroundImage.getHeight());
            return imageView;
            
        } catch (Exception e) {
            System.err.println("Could not load background GIF: " + e.getMessage());
            e.printStackTrace();
            
            // Create a fallback ImageView with a solid color
            ImageView fallbackView = new ImageView();
            fallbackView.setStyle("-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98, #F0E68C);");
            return fallbackView;
        }
    }
    
    /**
     * Create modern card with improved design
     */
    private VBox createModernCard(String title, javafx.scene.Node content) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.5);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 25, 0, 0, 12);"
        );
        
        // Modern card header
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1a1a1a;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        card.getChildren().addAll(titleLabel, content);
        return card;
    }
    
    /**
     * Create compact stat card with left-aligned icon
     */
    private HBox createStatCard(String icon, String value, String label) {
        HBox statCard = new HBox(8);
        statCard.setAlignment(Pos.CENTER_LEFT);
        statCard.setPrefWidth(140);
        statCard.setMaxWidth(140);
        statCard.setPadding(new Insets(12, 16, 12, 16));
        statCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 8);"
        );
        
        // Left side - Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;"
        );
        
        // Right side - Value and Label
        VBox textSection = new VBox(2);
        textSection.setAlignment(Pos.CENTER_LEFT);
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Label
        Label descLabel = new Label(label);
        descLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        descLabel.setMaxWidth(80);
        descLabel.setWrapText(true);
        
        textSection.getChildren().addAll(valueLabel, descLabel);
        statCard.getChildren().addAll(iconLabel, textSection);
        return statCard;
    }
    
    /**
     * Create a compact standardized section card (legacy method)
     */
    private VBox createSectionCard(String title, javafx.scene.Node content) {
        return createModernCard(title, content);
    }
    
    /**
     * Create a maximum width section card for right column (modern design)
     */
    private VBox createCompactSectionCard(String title, javafx.scene.Node content) {
        return createModernCard(title, content);
    }
    
    /**
     * Create modern Request Mission button
     */
    private Button createRequestMissionButton() {
        Button requestBtn = new Button("🎯 REQUEST MISSION");
        requestBtn.setPrefWidth(200);
        requestBtn.setPrefHeight(50);
        
        // Check if feature is available based on level
        if (!hasLevelAccess("request_mission", 5)) {
            requestBtn.setDisable(true);
            requestBtn.setText("🔒 UNLOCK AT LEVEL 5");
            requestBtn.setStyle(
                "-fx-background-color: rgba(158, 158, 158, 0.8);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 12 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 8);"
            );
        } else {
            requestBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4CAF50, #388E3C);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 12 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 20, 0, 0, 10);" +
                "-fx-cursor: hand;"
            );
            
            // Hover effects
            requestBtn.setOnMouseEntered(e -> {
                SoundManager.getInstance().playButtonHover();
                requestBtn.setStyle(requestBtn.getStyle() + 
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.6), 25, 0, 0, 12);"
                );
            });
            
            requestBtn.setOnMouseExited(e -> {
                requestBtn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #4CAF50, #388E3C);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 25;" +
                    "-fx-border-radius: 25;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                    "-fx-padding: 12 24;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 20, 0, 0, 10);" +
                    "-fx-cursor: hand;"
                );
            });
            
            requestBtn.setOnAction(e -> {
                SoundManager.getInstance().playButtonClick();
                showRequestMissionDialog();
            });
        }
        
        return requestBtn;
    }
    
    /**
     * Show the Request Mission dialog
     */
    private void showRequestMissionDialog() {
        if (!hasAccess("request_mission")) {
            showAccessDeniedAlert("request_mission");
            return;
        }
        
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Request New Mission");
        dialog.setHeaderText("🚀 Create Your Own Adventure!");
        dialog.setContentText(
            "Wow! You've reached a high enough level to request your own missions!\n\n" +
            "You can now propose new real-world tasks to your parent or guardian. " +
            "Think of something fun and educational you'd like to do, and they can " +
            "approve it with a SmartCoin reward!\n\n" +
            "This feature will be fully available in the next update!"
        );
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
    
    /**
     * Show access denied alert with friendly message
     */
    private void showAccessDeniedAlert(String feature) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Adventure Locked");
        alert.setHeaderText("🔒 Not Yet Available");
        alert.setContentText(getAccessDeniedMessage(feature));
        
        // Style the alert
        alert.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        SoundManager.getInstance().playError();
        alert.showAndWait();
    }
    
    
    @Override
    public void navigateToSection(String section) {
        this.currentSection = section;
        
        // Update sidebar navigation
        sidebar.setActiveSection(section);
        
        // Handle section-specific logic
        switch (section.toLowerCase()) {
            case "home":
                // Show home dashboard content
                showHomePage();
                break;
                
            case "tasks":
                // Highlight tasks section
                taskView.refresh();
                break;
                
            case "messages":
                // Show messaging interface (if available)
                if (hasAccess("group_messaging")) {
                    showMessagingInterface();
                } else {
                    showAccessDeniedAlert("group_messaging");
                }
                break;
                
            case "shop":
                // Navigate to shop (if available)
                showShopPage();
                break;
                
            case "profile":
                // Show profile/avatar customization
                showProfileInterface();
                break;
                
            default:
                System.out.println("Unknown section: " + section);
        }
    }
    
    /**
     * Show messaging interface for group communication
     */
    private void showMessagingInterface() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Adventure Messages");
        dialog.setHeaderText("📨 Group Chat");
        dialog.setContentText(
            "Welcome to the Adventure Chat!\n\n" +
            "Here you can chat with your friends about quests and adventures. " +
            "Remember to be kind and helpful to your fellow adventurers!\n\n" +
            "Full messaging features coming soon!"
        );
        
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
    
    /**
     * Show shop page
     */
    private void showShopPage() {
        // Create shop page if not exists
        if (shopPage == null) {
            shopPage = new ShopPage(currentUser);
        }
        
        // Replace main content with shop page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(shopPage.getRoot());
        
        System.out.println("Shop page displayed");
    }
    
    /**
     * Show home page (main dashboard)
     */
    private void showHomePage() {
        // Replace content with main dashboard
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(mainContent);
        
        System.out.println("Home page displayed");
    }
    
    /**
     * Show profile/avatar customization interface
     */
    private void showProfileInterface() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Adventure Profile");
        dialog.setHeaderText("👤 Your Adventurer Profile");
        dialog.setContentText(
            "Customize your adventurer!\n\n" +
            "• Change your avatar\n" +
            "• Pick new accessories\n" +
            "• View your achievements\n" +
            "• Update your adventure name\n\n" +
            "Profile customization coming soon!"
        );
        
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
}
