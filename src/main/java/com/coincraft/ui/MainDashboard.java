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
        this(null); // Default constructor for backward compatibility
    }
    
    public MainDashboard(User user) {
        this.currentUser = user;
        initializeUI();
        if (user == null) {
            loadUserData(); // Load mock data if no user provided
        } else {
            updateUI(); // Use provided user data
        }
        
        // Music is handled by BaseDashboard - no need to start here
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.getStyleClass().addAll("main-dashboard", "retro-bg-dark");
        
        // Load Minecraft font
        try {
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 16);
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 18);
        } catch (Exception e) {
            System.out.println("Could not load Minecraft font: " + e.getMessage());
        }
        
        // Set light gaming background style
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6f3ff, #ddeeff);" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
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
        VBox header = new VBox(15);
        header.setPadding(new Insets(20));
        header.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8);" +
            "-fx-background-radius: 0 0 15 15;" +
            "-fx-border-radius: 0 0 15 15;" +
            "-fx-border-color: rgba(255, 255, 255, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        HBox appBar = new HBox(20);
        appBar.setAlignment(Pos.CENTER_LEFT);

        // Gaming-style welcome message
        welcomeLabel = new Label("🏰 Welcome back, Money Explorer!");
        welcomeLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2E3440;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(46,52,64,0.3), 3, 0, 0, 1);"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Enhanced action buttons with gaming style
        HBox actions = new HBox(12);
        Button helpBtn = createActionButton("❓ Help", "#4CAF50");
        Button settingsBtn = createActionButton("⚙️ Settings", "#FA8A00");
        Button logoutBtn = createActionButton("🚪 Logout", "#F44336");
        
        actions.getChildren().addAll(helpBtn, settingsBtn, logoutBtn);

        appBar.getChildren().addAll(welcomeLabel, spacer, actions);

        // Secondary row with level info
        HBox levelRow = new HBox(12);
        levelRow.setAlignment(Pos.CENTER_LEFT);
        levelRow.setPadding(new Insets(0, 16, 12, 16));

        levelLabel = new Label("📊 Level 1: Treasure Chest Awakens");
        levelLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2E3440;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );

        levelProgressBar = new ProgressBar(0.0);
        levelProgressBar.setPrefWidth(300);
        levelProgressBar.setPrefHeight(20);
        levelProgressBar.setStyle(
            "-fx-accent: #FFD700;" +
            "-fx-background-color: rgba(255, 255, 255, 0.2);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;"
        );

        levelRow.getChildren().addAll(levelLabel, levelProgressBar);

        header.getChildren().addAll(appBar, levelRow);
        return header;
    }

    private VBox createNavRail() {
        VBox rail = new VBox(12);
        rail.setPadding(new Insets(20));
        rail.setAlignment(Pos.TOP_CENTER);
        rail.setPrefWidth(160);
        rail.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 0 15 15 0;" +
            "-fx-border-radius: 0 15 15 0;" +
            "-fx-border-color: rgba(255, 255, 255, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 2, 2);"
        );

        // Role-based navigation
        UserRole role = (currentUser != null) ? currentUser.getRole() : UserRole.CHILD;
        
        switch (role) {
            case CHILD:
                rail.getChildren().addAll(
                    createNavButton("🏠 Home", "#FA8A00", true),
                    createNavButton("🗺️ Adventure", "#2196F3", false),
                    createNavButton("📋 Quests", "#4CAF50", false),
                    createNavButton("🛒 Shop", "#9C27B0", false),
                    createNavButton("🏆 Badges", "#FFD700", false),
                    createNavButton("👤 Profile", "#607D8B", false)
                );
                break;
                
            case PARENT:
                rail.getChildren().addAll(
                    createNavButton("🏠 Overview", "#FA8A00", true),
                    createNavButton("👶 My child", "#2196F3", false),
                    createNavButton("✅ Validate", "#4CAF50", false),
                    createNavButton("📊 Progress", "#9C27B0", false),
                    createNavButton("💰 Allowance", "#FFD700", false),
                    createNavButton("⚙️ Settings", "#607D8B", false)
                );
                break;
                
            case TEACHER:
                rail.getChildren().addAll(
                    createNavButton("🏠 Classroom", "#FA8A00", true),
                    createNavButton("👥 Students", "#2196F3", false),
                    createNavButton("📚 Lessons", "#4CAF50", false),
                    createNavButton("🏆 Challenges", "#9C27B0", false),
                    createNavButton("📈 Analytics", "#FFD700", false),
                    createNavButton("📋 Reports", "#607D8B", false)
                );
                break;
                
            case ADMIN:
                rail.getChildren().addAll(
                    createNavButton("🏠 Dashboard", "#FA8A00", true),
                    createNavButton("👥 Users", "#2196F3", false),
                    createNavButton("📊 Analytics", "#4CAF50", false),
                    createNavButton("🔧 System", "#9C27B0", false),
                    createNavButton("📝 Content", "#FFD700", false),
                    createNavButton("🛡️ Security", "#F44336", false)
                );
                break;
                
            case ELDER:
                rail.getChildren().addAll(
                    createNavButton("🏠 Home", "#FA8A00", true),
                    createNavButton("🗺️ Adventure", "#2196F3", false),
                    createNavButton("📋 Quests", "#4CAF50", false),
                    createNavButton("🛒 Shop", "#9C27B0", false),
                    createNavButton("🏆 Badges", "#FFD700", false),
                    createNavButton("👤 Profile", "#607D8B", false)
                );
                break;
        }

        return rail;
    }
    
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-padding: 8 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        button.setOnAction(e -> {
            handleActionButton(text);
        });
        
        return button;
    }
    
    private Button createNavButton(String text, String color, boolean selected) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(50);
        
        String baseStyle = 
            "-fx-background-color: " + (selected ? color : "rgba(240,248,255,0.8)") + ";" +
            "-fx-text-fill: " + (selected ? "white" : "#2E3440") + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: " + (selected ? "700" : "600") + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-padding: 12 8;" +
            (selected ? "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" : "-fx-border-color: rgba(46,52,64,0.2); -fx-border-width: 1;");
            
        button.setStyle(baseStyle);
        
        button.setOnMouseEntered(e -> {
            if (!selected) {
                button.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.95);" +
                    "-fx-text-fill: #1e293b;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-background-radius: 10;" +
                    "-fx-border-radius: 10;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
                    "-fx-padding: 12 8;" +
                    "-fx-border-color: rgba(46,52,64,0.3); -fx-border-width: 1;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!selected) {
                button.setStyle(baseStyle);
            }
        });
        
        button.setOnAction(e -> {
            handleNavigation(text);
        });
        
        return button;
    }
    
    private HBox createMainContent() {
        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        UserRole role = (currentUser != null) ? currentUser.getRole() : UserRole.CHILD;
        
        switch (role) {
            case CHILD:
                return createChildContent();
            case PARENT:
                return createParentContent();
            case TEACHER:
                return createTeacherContent();
            case ADMIN:
                return createAdminContent();
            case ELDER:
                return createChildContent(); // Elders use child-like interface
            default:
                return createChildContent(); // Default to child view
        }
    }
    
    private HBox createChildContent() {
        HBox childContent = new HBox(20);
        childContent.setPadding(new Insets(20));
        childContent.setAlignment(Pos.TOP_CENTER);
        
        // Left panel - Avatar and user info
        VBox leftPanel = createChildLeftPanel();
        leftPanel.setPrefWidth(300);
        
        // Center panel - Adventure and activities
        VBox centerPanel = createChildCenterPanel();
        centerPanel.setPrefWidth(500);
        
        // Right panel - Quests and leaderboard
        VBox rightPanel = createChildRightPanel();
        rightPanel.setPrefWidth(300);
        
        childContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        return childContent;
    }
    
    private HBox createParentContent() {
        HBox parentContent = new HBox(20);
        parentContent.setPadding(new Insets(20));
        parentContent.setAlignment(Pos.TOP_CENTER);
        
        // Left panel - Child overview
        VBox leftPanel = createParentLeftPanel();
        leftPanel.setPrefWidth(350);
        
        // Center panel - Validation and monitoring
        VBox centerPanel = createParentCenterPanel();
        centerPanel.setPrefWidth(450);
        
        // Right panel - Settings and controls
        VBox rightPanel = createParentRightPanel();
        rightPanel.setPrefWidth(350);
        
        parentContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        return parentContent;
    }
    
    private HBox createTeacherContent() {
        HBox teacherContent = new HBox(20);
        teacherContent.setPadding(new Insets(20));
        teacherContent.setAlignment(Pos.TOP_CENTER);
        
        // Left panel - Class overview
        VBox leftPanel = createTeacherLeftPanel();
        leftPanel.setPrefWidth(350);
        
        // Center panel - Lesson management
        VBox centerPanel = createTeacherCenterPanel();
        centerPanel.setPrefWidth(450);
        
        // Right panel - Student progress
        VBox rightPanel = createTeacherRightPanel();
        rightPanel.setPrefWidth(350);
        
        teacherContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        return teacherContent;
    }
    
    private HBox createAdminContent() {
        HBox adminContent = new HBox(20);
        adminContent.setPadding(new Insets(20));
        adminContent.setAlignment(Pos.TOP_CENTER);
        
        // Left panel - System overview
        VBox leftPanel = createAdminLeftPanel();
        leftPanel.setPrefWidth(350);
        
        // Center panel - User management
        VBox centerPanel = createAdminCenterPanel();
        centerPanel.setPrefWidth(450);
        
        // Right panel - Analytics and reports
        VBox rightPanel = createAdminRightPanel();
        rightPanel.setPrefWidth(350);
        
        adminContent.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        return adminContent;
    }
    
    // CHILD ROLE PANELS
    private VBox createChildLeftPanel() {
        return createLeftPanel(); // Use existing implementation for child
    }
    
    private VBox createChildCenterPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Level navigation
        levelNavigation = new LevelNavigation();
        
        // Create adventure map card
        VBox mapCard = createGameCard("🗺️ Adventure Map", levelNavigation.getRoot());
        
        // Add quick action buttons
        HBox quickActions = createQuickActions();
        VBox actionsCard = createGameCard("⚡ Quick Actions", quickActions);
        
        centerPanel.getChildren().addAll(mapCard, actionsCard);
        
        return centerPanel;
    }
    
    private VBox createChildRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Task panel
        taskPanel = new TaskPanel();
        
        // Leaderboard panel
        leaderboardPanel = new LeaderboardPanel();
        
        // Create gaming-style cards
        VBox questCard = createGameCard("⚔️ Active Quests", taskPanel.getRoot());
        VBox leaderCard = createGameCard("🏆 Leaderboard", leaderboardPanel.getRoot());
        
        rightPanel.getChildren().addAll(questCard, leaderCard);
        
        return rightPanel;
    }
    
    // PARENT ROLE PANELS
    private VBox createParentLeftPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Child overview card
        VBox childOverview = createParentChildOverview();
        VBox childCard = createGameCard("👶 My Child", childOverview);
        
        // Quick actions for parents
        VBox parentActions = createParentQuickActions();
        VBox actionsCard = createGameCard("⚡ Quick Actions", parentActions);
        
        leftPanel.getChildren().addAll(childCard, actionsCard);
        return leftPanel;
    }
    
    private VBox createParentCenterPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Task validation panel
        VBox validationPanel = createTaskValidationPanel();
        VBox validationCard = createGameCard("✅ Pending Validations", validationPanel);
        
        // Progress monitoring
        VBox progressPanel = createProgressMonitoringPanel();
        VBox progressCard = createGameCard("📊 Child's Progress", progressPanel);
        
        centerPanel.getChildren().addAll(validationCard, progressCard);
        return centerPanel;
    }
    
    private VBox createParentRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Allowance management
        VBox allowancePanel = createAllowancePanel();
        VBox allowanceCard = createGameCard("💰 Allowance Manager", allowancePanel);
        
        // Safety settings
        VBox safetyPanel = createSafetyPanel();
        VBox safetyCard = createGameCard("🛡️ Safety Settings", safetyPanel);
        
        rightPanel.getChildren().addAll(allowanceCard, safetyCard);
        return rightPanel;
    }
    
    // TEACHER ROLE PANELS
    private VBox createTeacherLeftPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Class overview
        VBox classOverview = createClassOverviewPanel();
        VBox classCard = createGameCard("🏫 Class Overview", classOverview);
        
        // Today's lessons
        VBox lessonsPanel = createTodaysLessonsPanel();
        VBox lessonsCard = createGameCard("📚 Today's Lessons", lessonsPanel);
        
        leftPanel.getChildren().addAll(classCard, lessonsCard);
        return leftPanel;
    }
    
    private VBox createTeacherCenterPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Student management
        VBox studentPanel = createStudentManagementPanel();
        VBox studentCard = createGameCard("👥 Student Management", studentPanel);
        
        // Lesson planner
        VBox plannerPanel = createLessonPlannerPanel();
        VBox plannerCard = createGameCard("📝 Lesson Planner", plannerPanel);
        
        centerPanel.getChildren().addAll(studentCard, plannerCard);
        return centerPanel;
    }
    
    private VBox createTeacherRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Class analytics
        VBox analyticsPanel = createClassAnalyticsPanel();
        VBox analyticsCard = createGameCard("📈 Class Analytics", analyticsPanel);
        
        // Achievements
        VBox achievementsPanel = createClassAchievementsPanel();
        VBox achievementsCard = createGameCard("🏆 Class Achievements", achievementsPanel);
        
        rightPanel.getChildren().addAll(analyticsCard, achievementsCard);
        return rightPanel;
    }
    
    // ADMIN ROLE PANELS
    private VBox createAdminLeftPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // System status
        VBox systemPanel = createSystemStatusPanel();
        VBox systemCard = createGameCard("🖥️ System Status", systemPanel);
        
        // User statistics
        VBox statsPanel = createUserStatsPanel();
        VBox statsCard = createGameCard("📊 User Statistics", statsPanel);
        
        leftPanel.getChildren().addAll(systemCard, statsCard);
        return leftPanel;
    }
    
    private VBox createAdminCenterPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // User management
        VBox userPanel = createUserManagementPanel();
        VBox userCard = createGameCard("👥 User Management", userPanel);
        
        // Content management
        VBox contentPanel = createContentManagementPanel();
        VBox contentCard = createGameCard("📝 Content Management", contentPanel);
        
        centerPanel.getChildren().addAll(userCard, contentCard);
        return centerPanel;
    }
    
    private VBox createAdminRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Security monitoring
        VBox securityPanel = createSecurityMonitorPanel();
        VBox securityCard = createGameCard("🛡️ Security Monitor", securityPanel);
        
        // System analytics
        VBox analyticsPanel = createSystemAnalyticsPanel();
        VBox analyticsCard = createGameCard("📈 System Analytics", analyticsPanel);
        
        rightPanel.getChildren().addAll(securityCard, analyticsCard);
        return rightPanel;
    }
    
    // ROLE-SPECIFIC CONTENT PANELS
    
    // Parent-specific panels
    private VBox createParentChildOverview() {
        VBox overview = new VBox(10);
        overview.setAlignment(Pos.CENTER_LEFT);
        
        Label childName = new Label("👶 " + (currentUser != null ? "Child: Alex" : "No Child Connected"));
        childName.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label childLevel = new Label("📊 Level: 2");
        childLevel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #d97706;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label childCoins = new Label("💰 SmartCoins: 150");
        childCoins.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        overview.getChildren().addAll(childName, childLevel, childCoins);
        return overview;
    }
    
    private VBox createParentQuickActions() {
        VBox actions = new VBox(8);
        actions.setAlignment(Pos.CENTER);
        
        Button validateBtn = createGameButton("✅ Validate tasks", "#4CAF50");
        Button allowanceBtn = createGameButton("💰 Manage allowance", "#FA8A00");
        Button reportsBtn = createGameButton("📊 View reports", "#2196F3");
        
        actions.getChildren().addAll(validateBtn, allowanceBtn, reportsBtn);
        return actions;
    }
    
    private VBox createTaskValidationPanel() {
        VBox panel = new VBox(10);
        
        Label pendingLabel = new Label("📋 3 tasks awaiting validation");
        pendingLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button validateAllBtn = createGameButton("✅ Validate all", "#4CAF50");
        validateAllBtn.setPrefWidth(200);
        
        panel.getChildren().addAll(pendingLabel, validateAllBtn);
        return panel;
    }
    
    private VBox createProgressMonitoringPanel() {
        VBox panel = new VBox(10);
        
        Label progressLabel = new Label("📈 This Week: +50 SmartCoins");
        progressLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label streakLabel = new Label("🔥 Daily Streak: 5 days");
        streakLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().addAll(progressLabel, streakLabel);
        return panel;
    }
    
    private VBox createAllowancePanel() {
        VBox panel = new VBox(10);
        
        Label allowanceLabel = new Label("💰 Weekly Allowance: $10");
        allowanceLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button adjustBtn = createGameButton("⚙️ ADJUST", "#FA8A00");
        adjustBtn.setPrefWidth(120);
        
        panel.getChildren().addAll(allowanceLabel, adjustBtn);
        return panel;
    }
    
    private VBox createSafetyPanel() {
        VBox panel = new VBox(10);
        
        Label safetyLabel = new Label("🛡️ Safety Mode: ON");
        safetyLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button configBtn = createGameButton("⚙️ CONFIGURE", "#607D8B");
        configBtn.setPrefWidth(120);
        
        panel.getChildren().addAll(safetyLabel, configBtn);
        return panel;
    }
    
    // Teacher-specific panels (simplified for demo)
    private VBox createClassOverviewPanel() {
        VBox panel = new VBox(8);
        
        Label studentsLabel = new Label("👥 25 Active Students");
        studentsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label avgProgressLabel = new Label("📊 Avg Progress: Level 3");
        avgProgressLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #d97706;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().addAll(studentsLabel, avgProgressLabel);
        return panel;
    }
    
    private VBox createTodaysLessonsPanel() {
        VBox panel = new VBox(8);
        
        Label lessonLabel = new Label("📚 Budget Basics");
        lessonLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button startBtn = createGameButton("▶️ START LESSON", "#4CAF50");
        startBtn.setPrefWidth(140);
        
        panel.getChildren().addAll(lessonLabel, startBtn);
        return panel;
    }
    
    private VBox createStudentManagementPanel() {
        VBox panel = new VBox(8);
        
        Label studentsLabel = new Label("👥 Manage 25 Students");
        studentsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button manageBtn = createGameButton("👥 MANAGE", "#2196F3");
        manageBtn.setPrefWidth(120);
        
        panel.getChildren().addAll(studentsLabel, manageBtn);
        return panel;
    }
    
    private VBox createLessonPlannerPanel() {
        VBox panel = new VBox(8);
        
        Label plannerLabel = new Label("📝 Next: Saving Goals");
        plannerLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button planBtn = createGameButton("📝 PLAN", "#9C27B0");
        planBtn.setPrefWidth(120);
        
        panel.getChildren().addAll(plannerLabel, planBtn);
        return panel;
    }
    
    private VBox createClassAnalyticsPanel() {
        VBox panel = new VBox(8);
        
        Label analyticsLabel = new Label("📈 Class Performance: 85%");
        analyticsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().add(analyticsLabel);
        return panel;
    }
    
    private VBox createClassAchievementsPanel() {
        VBox panel = new VBox(8);
        
        Label achievementLabel = new Label("🏆 15 Badges Earned This Week");
        achievementLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #d97706;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().add(achievementLabel);
        return panel;
    }
    
    // Admin-specific panels (simplified for demo)
    private VBox createSystemStatusPanel() {
        VBox panel = new VBox(8);
        
        Label statusLabel = new Label("🟢 System: Online");
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label usersLabel = new Label("👥 Active Users: 1,247");
        usersLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().addAll(statusLabel, usersLabel);
        return panel;
    }
    
    private VBox createUserStatsPanel() {
        VBox panel = new VBox(8);
        
        Label newUsersLabel = new Label("📈 New Users Today: 23");
        newUsersLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().add(newUsersLabel);
        return panel;
    }
    
    private VBox createUserManagementPanel() {
        VBox panel = new VBox(8);
        
        Label usersLabel = new Label("👥 Total Users: 1,247");
        usersLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button manageBtn = createGameButton("👥 MANAGE USERS", "#2196F3");
        manageBtn.setPrefWidth(150);
        
        panel.getChildren().addAll(usersLabel, manageBtn);
        return panel;
    }
    
    private VBox createContentManagementPanel() {
        VBox panel = new VBox(8);
        
        Label contentLabel = new Label("📝 Content Items: 150");
        contentLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Button editBtn = createGameButton("✏️ Edit content", "#FA8A00");
        editBtn.setPrefWidth(150);
        
        panel.getChildren().addAll(contentLabel, editBtn);
        return panel;
    }
    
    private VBox createSystemAnalyticsPanel() {
        VBox panel = new VBox(8);
        
        Label analyticsLabel = new Label("📈 System Load: 23%");
        analyticsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().add(analyticsLabel);
        return panel;
    }
    
    private VBox createSecurityMonitorPanel() {
        VBox panel = new VBox(8);
        
        Label securityLabel = new Label("🛡️ Security: Active");
        securityLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label threatsLabel = new Label("⚠️ Threats Blocked: 0");
        threatsLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        panel.getChildren().addAll(securityLabel, threatsLabel);
        return panel;
    }
    
    private VBox createLeftPanel() {
        VBox centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Level navigation
        levelNavigation = new LevelNavigation();
        
        // Create adventure map card
        VBox mapCard = createGameCard("🗺️ Adventure Map", levelNavigation.getRoot());
        
        // Add quick action buttons
        HBox quickActions = createQuickActions();
        VBox actionsCard = createGameCard("⚡ Quick Actions", quickActions);
        
        centerPanel.getChildren().addAll(mapCard, actionsCard);
        
        return centerPanel;
    }
    
    private VBox createRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        
        // Task panel
        taskPanel = new TaskPanel();
        
        // Leaderboard panel
        leaderboardPanel = new LeaderboardPanel();
        
        // Create gaming-style cards
        VBox questCard = createGameCard("⚔️ Active Quests", taskPanel.getRoot());
        VBox leaderCard = createGameCard("🏆 Leaderboard", leaderboardPanel.getRoot());
        
        rightPanel.getChildren().addAll(questCard, leaderCard);
        
        return rightPanel;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setPadding(new Insets(15, 20, 15, 20));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 15 15 0 0;" +
            "-fx-border-radius: 15 15 0 0;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1 1 0 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, -3);"
        );
        
        // Status indicators with light theme
        Label statusLabel = new Label("🟢 Status: Online & Ready");
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #059669;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-font-weight: 600;"
        );
        
        Label streakLabel = new Label("🔥 Daily Streak: 0 days");
        streakLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #ea580c;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-font-weight: 600;"
        );
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Music control button
        Button musicBtn = createActionButton("🎵 Music", "#9C27B0");
        
        footer.getChildren().addAll(statusLabel, streakLabel, spacer, musicBtn);
        return footer;
    }
    
    private VBox createGameCard(String title, Parent content) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: rgba(248, 250, 252, 0.9);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        card.getChildren().addAll(titleLabel, content);
        return card;
    }
    
    private HBox createQuickActions() {
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER);
        
        Button startQuestBtn = createGameButton("🎯 Start quest", "#4CAF50");
        Button shopBtn = createGameButton("🛒 Shop", "#FA8A00");
        Button achievementsBtn = createGameButton("🏆 Badges", "#9C27B0");
        
        actions.getChildren().addAll(startQuestBtn, shopBtn, achievementsBtn);
        return actions;
    }
    
    private Button createGameButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(140);
        button.setPrefHeight(40);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        button.setOnAction(e -> {
            handleGameAction(text);
        });
        
        return button;
    }
    
    private void handleActionButton(String action) {
        switch (action) {
            case "❓ HELP":
                showInfoDialog("Help", "Welcome to CoinCraft! Use the navigation to explore different areas and complete quests to earn SmartCoins!");
                break;
            case "⚙️ SETTINGS":
                showInfoDialog("Settings", "Settings panel coming soon! You'll be able to adjust sound, music, and game preferences.");
                break;
            case "🚪 LOGOUT":
                showInfoDialog("Logout", "Logout functionality coming soon!");
                break;
            case "🎵 MUSIC":
                toggleMusic();
                break;
        }
    }
    
    private void handleNavigation(String nav) {
        String message = switch (nav) {
            case "🏠 HOME" -> "You're already home, adventurer!";
            case "🗺️ MAP" -> "Adventure map coming soon! Explore different levels and unlock new areas.";
            case "📋 TASKS" -> "Task management system coming soon! Complete quests to earn SmartCoins.";
            case "🛒 STORE" -> "Avatar store coming soon! Spend SmartCoins on cool customizations.";
            case "👤 PROFILE" -> "Profile management coming soon! View your achievements and stats.";
            default -> "Feature coming soon!";
        };
        showInfoDialog("Navigation", message);
    }
    
    private void handleGameAction(String action) {
        String message = switch (action) {
            case "🎯 Start quest" -> "Quest system coming soon! Complete real-world money tasks to earn rewards.";
            case "🛒 Shop" -> "Avatar shop coming soon! Customize your character with earned SmartCoins.";
            case "🏆 Badges" -> "Achievement system coming soon! Unlock badges by mastering financial skills.";
            default -> "Feature coming soon!";
        };
        showInfoDialog("Game Action", message);
    }
    
    private void toggleMusic() {
        // Music toggle functionality moved to CentralizedMusicController
        showInfoDialog("Music", "🎵 Music controls are available in the sidebar");
    }
    
    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void applyStyles() {
        // Styling is driven by inline styles for gaming theme
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
        welcomeLabel.setText("🏰 Welcome back, " + currentUser.getName() + "!");
        
        // Update level information
        levelLabel.setText("📊 Level " + currentUser.getLevel() + ": Treasure Chest Awakens");
        
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
