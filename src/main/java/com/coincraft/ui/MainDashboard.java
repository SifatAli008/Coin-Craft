package com.coincraft.ui;

import java.util.logging.Logger;

import com.coincraft.models.Badge;
import com.coincraft.models.BadgeLevel;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.ui.components.AvatarDisplay;
import com.coincraft.ui.components.LeaderboardPanel;
import com.coincraft.ui.components.LevelNavigation;
import com.coincraft.ui.components.ProgressDisplay;
import com.coincraft.ui.components.SmartCoinDisplay;
import com.coincraft.ui.components.TaskPanel;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Main dashboard UI for CoinCraft application
 * Displays user progress, avatar, SmartCoin balance, and level navigation
 */
public class MainDashboard {
    private static final Logger LOGGER = Logger.getLogger(MainDashboard.class.getName());
    
    private BorderPane root;
    private User currentUser;
    private AvatarDisplay avatarDisplay;
    private SmartCoinDisplay coinDisplay;
    private ProgressDisplay progressDisplay;
    private LevelNavigation levelNavigation;
    private TaskPanel taskPanel;
    private LeaderboardPanel leaderboardPanel;
    
    // UI Components
    private Label welcomeLabel;
    private Label levelLabel;
    private ProgressBar levelProgressBar;
    
    // Navigation rail
    private VBox navRail;
    
    public MainDashboard() {
        initializeUI();
        loadUserData();
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.getStyleClass().addAll("main-dashboard", "retro-bg-dark");
        
        // Create header section
        VBox header = createHeader();
        root.setTop(header);
        
        // Create navigation rail
        navRail = createNavRail();
        root.setLeft(navRail);

        // Create main content area
        HBox mainContent = createMainContent();
        root.setCenter(mainContent);
        
        // Create footer/status bar
        HBox footer = createFooter();
        root.setBottom(footer);
        
        // Apply styling
        applyStyles();
        try { new FadeIn(root).play(); } catch (Throwable ignored) {}
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(0));
        header.getStyleClass().add("dashboard-header");

        HBox appBar = new HBox(12);
        appBar.getStyleClass().add("app-bar");
        appBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Welcome back, Money Explorer!");
        title.getStyleClass().add("app-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actions = new HBox(8);
        actions.getStyleClass().add("app-actions");
        Button help = new Button("Help");
        Button settings = new Button("Settings");
        actions.getChildren().addAll(help, settings);

        appBar.getChildren().addAll(title, spacer, actions);

        // Secondary row with level info
        HBox levelRow = new HBox(12);
        levelRow.setAlignment(Pos.CENTER_LEFT);
        levelRow.setPadding(new Insets(0, 16, 12, 16));

        levelLabel = new Label("Level 1: Treasure Chest Awakens");
        levelLabel.getStyleClass().add("level-label");
        levelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        levelProgressBar = new ProgressBar(0.0);
        levelProgressBar.setPrefWidth(240);
        levelProgressBar.getStyleClass().add("level-progress");

        levelRow.getChildren().addAll(levelLabel, levelProgressBar);

        header.getChildren().addAll(appBar, levelRow);
        return header;
    }

    private VBox createNavRail() {
        VBox rail = new VBox(8);
        rail.getStyleClass().add("pixel-nav");
        rail.setPadding(new Insets(12, 8, 12, 8));
        rail.setAlignment(Pos.TOP_CENTER);

        Button homeBtn = new Button("HOME");
        Button mapBtn = new Button("MAP");
        Button tasksBtn = new Button("TASKS");
        Button storeBtn = new Button("STORE");
        Button settingsBtn = new Button("SETTINGS");

        for (Button b : new Button[]{homeBtn, mapBtn, tasksBtn, storeBtn, settingsBtn}) {
            b.getStyleClass().add("pixel-nav-button");
            b.setMaxWidth(Double.MAX_VALUE);
        }
        homeBtn.getStyleClass().add("pixel-nav-selected");

        rail.getChildren().addAll(homeBtn, mapBtn, tasksBtn, storeBtn, settingsBtn);
        return rail;
    }
    
    private HBox createMainContent() {
        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Left panel - Avatar and user info
        VBox leftPanel = createLeftPanel();
        leftPanel.setPrefWidth(300);
        
        // Center panel - Level navigation and activities
        VBox centerPanel = createCenterPanel();
        centerPanel.setPrefWidth(500);
        
        // Right panel - Tasks and leaderboard
        VBox rightPanel = createRightPanel();
        rightPanel.setPrefWidth(300);
        
        mainContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        return mainContent;
    }
    
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(15);
        leftPanel.getStyleClass().add("left-panel");
        leftPanel.setPadding(new Insets(15));
        
        // Avatar display
        avatarDisplay = new AvatarDisplay();
        
        // SmartCoin display
        coinDisplay = new SmartCoinDisplay();
        
        // Progress display
        progressDisplay = new ProgressDisplay();
        
        Label yourChar = createSectionTitle("Your Character");
        yourChar.getStyleClass().add("pixel-card");
        coinDisplay.getRoot().getStyleClass().add("pixel-card");
        progressDisplay.getRoot().getStyleClass().add("pixel-card");

        leftPanel.getChildren().addAll(
            createSectionTitle("Your Character"),
            avatarDisplay.getRoot(),
            createSectionTitle("SmartCoins"),
            coinDisplay.getRoot(),
            createSectionTitle("Progress"),
            progressDisplay.getRoot()
        );
        
        return leftPanel;
    }
    
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(15);
        centerPanel.getStyleClass().add("center-panel");
        centerPanel.setPadding(new Insets(15));
        
        // Level navigation
        levelNavigation = new LevelNavigation();
        
        centerPanel.getChildren().addAll(
            createSectionTitle("Adventure Map"),
            levelNavigation.getRoot()
        );
        
        return centerPanel;
    }
    
    private VBox createRightPanel() {
        VBox rightPanel = new VBox(15);
        rightPanel.getStyleClass().add("right-panel");
        rightPanel.setPadding(new Insets(15));
        
        // Task panel
        taskPanel = new TaskPanel();
        
        // Leaderboard panel
        leaderboardPanel = new LeaderboardPanel();
        
        rightPanel.getChildren().addAll(
            createSectionTitle("Active Quests"),
            taskPanel.getRoot(),
            createSectionTitle("Leaderboard"),
            leaderboardPanel.getRoot()
        );
        
        return rightPanel;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("dashboard-footer");
        
        // Status indicators
        Label statusLabel = new Label("Status: Connected");
        statusLabel.getStyleClass().add("status-label");
        
        Label timeLabel = new Label("Daily Streak: 0 days");
        timeLabel.getStyleClass().add("streak-label");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Action buttons
        Button settingsBtn = new Button("Settings");
        Button helpBtn = new Button("Help");
        Button logoutBtn = new Button("Logout");
        
        settingsBtn.getStyleClass().add("footer-button");
        helpBtn.getStyleClass().add("footer-button");
        logoutBtn.getStyleClass().add("footer-button");
        
        footer.getChildren().addAll(statusLabel, timeLabel, spacer, settingsBtn, helpBtn, logoutBtn);
        return footer;
    }
    
    private Label createSectionTitle(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        return titleLabel;
    }
    
    private void applyStyles() {
        // Styling is driven by external CSS
    }
    
    private void loadUserData() {
        // Load user data asynchronously
        Platform.runLater(() -> {
            try {
                // For MVP, create a mock user
                currentUser = createMockUser();
                updateUI();
                LOGGER.info("User data loaded successfully");
            } catch (Exception e) {
                LOGGER.severe(() -> "Failed to load user data: " + e.getMessage());
                showErrorDialog("Failed to load user data. Please try again.");
            }
        });
    }
    
    private User createMockUser() {
        User user = new User("user_123", "Money Explorer", UserRole.CHILD, 10);
        user.setSmartCoinBalance(75);
        user.setLevel(1);
        user.setDailyStreaks(2);
        user.setLeaderboardRank(5);
        
        // Add some sample badges
        Badge budgetBadge = new Badge("badge_budget", "Budget Beginner", 
                                    "Completed first budget exercise", BadgeLevel.BRONZE);
        user.awardBadge(budgetBadge);
        
        return user;
    }
    
    private void updateUI() {
        if (currentUser == null) return;
        
        // Update welcome message
        welcomeLabel.setText("Welcome back, " + currentUser.getName() + "!");
        
        // Update level information
        levelLabel.setText("Level " + currentUser.getLevel() + ": Treasure Chest Awakens");
        
        // Calculate level progress (example: coins needed for next level)
        int currentCoins = currentUser.getSmartCoinBalance();
        int requiredCoins = currentUser.getLevel() * 100;
        double progress = Math.min(1.0, (double) currentCoins / requiredCoins);
        levelProgressBar.setProgress(progress);
        
        // Update components
        if (avatarDisplay != null) avatarDisplay.updateUser(currentUser);
        if (coinDisplay != null) coinDisplay.updateBalance(currentUser.getSmartCoinBalance());
        if (progressDisplay != null) progressDisplay.updateProgress(currentUser);
        if (taskPanel != null) taskPanel.updateTasks(currentUser.getCurrentTasks());
        if (leaderboardPanel != null) leaderboardPanel.loadLeaderboard();
    }
    
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void refreshData() {
        loadUserData();
    }
}
