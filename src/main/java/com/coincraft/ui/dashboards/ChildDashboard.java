package com.coincraft.ui.dashboards;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.components.child.AdventurerTaskView;
import com.coincraft.ui.components.child.ChildLeaderboard;
import com.coincraft.ui.components.child.ChildSidebar;
import com.coincraft.ui.components.child.ChildTopBar;
import com.coincraft.ui.components.child.DailyStreakCalendar;
import com.coincraft.ui.components.child.EventBanner;
import com.coincraft.ui.components.child.ShopPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private AdventurerTaskView taskView;
    private DailyStreakCalendar streakCalendar;
    private ChildLeaderboard leaderboard;
    private EventBanner eventBanner;
    private ChildSidebar sidebar;
    private ShopPage shopPage;
    
    // Content sections
    private VBox mainContent;
    private String currentSection = "home";
    
    // Real data from Firebase
    private List<Task> userTasks;
    private int dailyStreak = 0;
    private int totalBadges = 0;
    private int completedTasks = 0;
    private int pendingTasks = 0;
    
    public ChildDashboard(User user) {
        super(user);
        loadRealData();
    }
    
    /**
     * Load real data from Firebase for this child
     */
    private void loadRealData() {
        userTasks = new ArrayList<>();
        
        try {
            FirebaseService firebaseService = FirebaseService.getInstance();
            
            System.out.println("🔍 Loading real data for child: " + currentUser.getName());
            
            // Load user's tasks
            userTasks = firebaseService.loadUserTasks(currentUser.getUserId());
            
            // Calculate real statistics
            calculateRealStats();
            
            System.out.println("✅ Loaded " + userTasks.size() + " tasks for " + currentUser.getName());
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not load real data: " + e.getMessage());
            // Use fallback data
            loadFallbackData();
        }
    }
    
    /**
     * Calculate real statistics from loaded data
     */
    private void calculateRealStats() {
        completedTasks = 0;
        pendingTasks = 0;
        
        for (Task task : userTasks) {
            if (task.isCompleted()) {
                completedTasks++;
            } else {
                pendingTasks++;
            }
        }
        
        // Calculate daily streak (simplified - in real app, track consecutive days)
        dailyStreak = Math.min(30, completedTasks); // Max 30 day streak
        
        // Calculate badges based on achievements (simplified)
        totalBadges = Math.min(50, completedTasks / 2); // 1 badge per 2 completed tasks
        
        System.out.println("📊 Stats calculated - Completed: " + completedTasks + 
                          ", Pending: " + pendingTasks + ", Streak: " + dailyStreak + 
                          ", Badges: " + totalBadges);
    }
    
    /**
     * Load fallback data when Firebase is unavailable
     */
    private void loadFallbackData() {
        // Use reasonable defaults based on user level
        dailyStreak = Math.max(1, currentUser.getLevel() * 2);
        totalBadges = Math.max(3, currentUser.getLevel() * 3);
        completedTasks = Math.max(5, currentUser.getLevel() * 4);
        pendingTasks = Math.max(1, currentUser.getLevel());
        
        System.out.println("📊 Using fallback data for " + currentUser.getName());
    }
    
    /**
     * Refresh data and update UI components
     */
    public void refreshData() {
        loadRealData();
        
        // Refresh UI components with new data
        if (topBar != null) {
            topBar.updateStats(dailyStreak, totalBadges, pendingTasks);
            topBar.refresh();
        }
        
        if (taskView != null) {
            taskView.refresh();
        }
        
        // Refresh current section
        navigateToSection(currentSection);
        
        System.out.println("🔄 Child dashboard data refreshed");
    }
    
    /**
     * Get latest badge name based on user achievements
     */
    private String getLatestBadgeName() {
        // Determine badge based on user level and achievements
        if (completedTasks >= 10) {
            return "🎯 Quest Master";
        } else if (completedTasks >= 5) {
            return "⚔️ Brave Adventurer";
        } else if (completedTasks >= 2) {
            return "🌟 Rising Star";
        } else if (completedTasks >= 1) {
            return "🏆 First Steps";
        } else {
            return "🚀 New Adventurer";
        }
    }
    
    @Override
    protected void initializeUI() {
        // Create a StackPane as the root to properly handle background
        StackPane backgroundContainer = new StackPane();
        backgroundContainer.setPadding(new Insets(0));
        backgroundContainer.setAlignment(Pos.CENTER);
        
        // Create background ImageView for subtle pattern (same as parent dashboard)
        ImageView backgroundImageView = createBackgroundImageView(backgroundContainer);
        
        // Add semi-transparent overlay for better contrast (same as parent dashboard)
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMouseTransparent(true);
        
        // Create the main BorderPane for layout with medieval theme
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().addAll("child-dashboard", "medieval-theme");
        mainLayout.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: 'Minecraft', 'Courier New', monospace;"
        );
        mainLayout.setPadding(new Insets(12, 12, 12, 12));
        
        // Create main sections
        createTopSection();
        createSidebar();
        createMainContent();
        
        // Set up layout with sidebar (add padding to prevent header overlap)
        VBox topSection = new VBox();
        topSection.getChildren().add(topBar.getRoot());
        topSection.setPadding(new Insets(0, 0, 8, 0)); // Add bottom padding to prevent overlap
        
        // Add margin to sidebar to prevent overlap with header
        VBox sidebarSection = new VBox();
        sidebarSection.getChildren().add(sidebar.getRoot());
        sidebarSection.setPadding(new Insets(8, 0, 0, 0)); // Add top padding
        
        mainLayout.setTop(topSection);
        mainLayout.setLeft(sidebarSection);
        mainLayout.setCenter(mainContent);
        
        // Add everything to the background container (background first, then overlay, then content)
        backgroundContainer.getChildren().addAll(backgroundImageView, overlay, mainLayout);
        
        // Set the StackPane as the root
        root = backgroundContainer;
    }
    
    /**
     * Create the top bar with avatar, welcome message, and coin balance
     */
    private void createTopSection() {
        topBar = new ChildTopBar(currentUser);
        // Update header with real stats
        topBar.updateStats(dailyStreak, totalBadges, pendingTasks);
    }
    
    /**
     * Create properly sized main content area
     */
    private void createMainContent() {
        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(16));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: transparent;");
        mainContent.setMaxWidth(1180);
        mainContent.setPrefWidth(1180);
        
        // Main Grid Layout - Responsive Cards
        VBox mainGrid = createMainGrid();
        
        // Action Section
        HBox actionSection = createActionSection();
        
        mainContent.getChildren().addAll(mainGrid, actionSection);
    }
    
    /**
     * Create properly sized grid layout
     */
    private VBox createMainGrid() {
        VBox mainGrid = new VBox(20);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setMaxWidth(1180);
        
        // Row 1: Primary Content
        HBox primaryRow = new HBox(20);
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
        VBox tasksSection = new VBox(20);
        
        taskView = new AdventurerTaskView(currentUser, task -> {
            // Handle task completion - could refresh user data or show notifications
            System.out.println("Task completed: " + task.getTitle());
        });
        
        // Tasks Card (badges moved to sidebar)
        VBox tasksCard = createModernCard("Active Quests", taskView.getRoot());
        
        tasksSection.getChildren().add(tasksCard);
        return tasksSection;
    }
    
    /**
     * Create side panel with social features
     */
    private VBox createSidePanel() {
        VBox sidePanel = new VBox(20);
        
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
     * Create background ImageView for subtle pattern (same as parent dashboard)
     */
    private ImageView createBackgroundImageView(StackPane container) {
        try {
            // Use the same parent dashboard background image
            Image backgroundImage = new Image(
                getClass().getResourceAsStream("/images/WOF_DonegalBG.gif"),
                800, 450, true, true
            );
            
            ImageView imageView = new ImageView(backgroundImage);
            imageView.setOpacity(0.3); // Same opacity as parent dashboard
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);
            
            // Bind to container size
            imageView.fitWidthProperty().bind(container.widthProperty());
            imageView.fitHeightProperty().bind(container.heightProperty());
            
            return imageView;
            
        } catch (Exception e) {
            System.out.println("Could not load background image: " + e.getMessage());
            return new ImageView(); // Return empty ImageView
        }
    }
    
    /**
     * Create modern card with improved design
     */
    private VBox createModernCard(String title, javafx.scene.Node content) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: linear-gradient(145deg, #1A202C 0%, #2D3748 50%, #4A5568 100%);" +
            "-fx-border-color: linear-gradient(45deg, #63B3ED, #4299E1, #3182CE);" +
            "-fx-border-width: 2;" +
            "-fx-border-style: solid;" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0.3, 0, 8), " +
                      "innershadow(gaussian, rgba(255,255,255,0.1), 2, 0, 0, 1);"
        );
        
        // Medieval-style card header
        Label titleLabel = new Label("⚜ " + title + " ⚜");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: linear-gradient(135deg, #667EEA 0%, #764BA2 50%, #F093FB 100%);" +
            "-fx-padding: 16 24;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0.2, 0, 4), " +
                      "dropshadow(gaussian, rgba(102,126,234,0.4), 12, 0, 0, 0);" +
            "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;"
        );
        
        // Content container with medieval styling
        VBox contentContainer = new VBox();
        contentContainer.setPadding(new Insets(12));
        contentContainer.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FFFFFF 0%, #F8FAFC 100%);" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: rgba(226,232,240,0.8);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-border-style: solid;" +
            "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 2);"
        );
        contentContainer.getChildren().add(content);
        
        card.getChildren().addAll(titleLabel, contentContainer);
        return card;
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
                // Show dedicated tasks page
                showTasksPage();
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
                // Show dedicated profile page
                showProfilePage();
                break;
                
            default:
                System.out.println("Unknown section: " + section);
        }
    }
    
    /**
     * Show dedicated tasks page
     */
    private void showTasksPage() {
        mainContent.getChildren().clear();
        
        // Tasks page header
        VBox headerSection = new VBox(8);
        headerSection.setAlignment(Pos.CENTER_LEFT);
        headerSection.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("⚔️ My Quest Journal");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Track your adventures and complete epic quests!");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Tasks grid layout
        HBox tasksLayout = new HBox(20);
        tasksLayout.setAlignment(Pos.TOP_CENTER);
        tasksLayout.setMaxWidth(1180);
        
        // Left column - Active tasks (70% width)
        VBox activeTasksSection = new VBox(16);
        activeTasksSection.setPrefWidth(750);
        activeTasksSection.setMaxWidth(750);
        
        VBox activeTasksCard = createModernCard("⚔️ Noble Quests", taskView.getRoot());
        activeTasksSection.getChildren().add(activeTasksCard);
        
        // Right column - Task stats and progress (30% width)
        VBox taskStatsSection = createTaskStatsSection();
        taskStatsSection.setPrefWidth(350);
        taskStatsSection.setMaxWidth(350);
        
        tasksLayout.getChildren().addAll(activeTasksSection, taskStatsSection);
        
        // Create scrollable content for tasks page
        VBox tasksPageContent = new VBox(20);
        tasksPageContent.setPadding(new Insets(16));
        tasksPageContent.setAlignment(Pos.TOP_CENTER);
        tasksPageContent.setMaxWidth(1180);
        tasksPageContent.getChildren().addAll(headerSection, tasksLayout);
        
        // Create scroll pane for tasks content
        ScrollPane tasksScrollPane = new ScrollPane(tasksPageContent);
        tasksScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tasksScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tasksScrollPane.setFitToWidth(true);
        tasksScrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Replace content with scrollable tasks page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(tasksScrollPane);
    }
    
    /**
     * Create task statistics section
     */
    private VBox createTaskStatsSection() {
        VBox statsSection = new VBox(16);
        
        // Quest progress card
        VBox progressCard = createTaskProgressCard();
        
        // Recent achievements card
        VBox achievementsCard = createTaskAchievementsCard();
        
        // Quest tips card
        VBox tipsCard = createQuestTipsCard();
        
        statsSection.getChildren().addAll(progressCard, achievementsCard, tipsCard);
        return statsSection;
    }
    
    /**
     * Create task progress card
     */
    private VBox createTaskProgressCard() {
        VBox progressContent = new VBox(12);
        progressContent.setAlignment(Pos.CENTER);
        
        // Progress stats using real data
        Label completedLabel = new Label("✅ " + completedTasks + " Completed");
        completedLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #059669;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label pendingLabel = new Label("⏳ " + pendingTasks + " In Progress");
        pendingLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        int totalTasks = completedTasks + pendingTasks;
        Label totalLabel = new Label("📊 " + totalTasks + " Total Quests");
        totalLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #3B82F6;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        progressContent.getChildren().addAll(completedLabel, pendingLabel, totalLabel);
        
        return createModernCard("⚔️ Quest Progress", progressContent);
    }
    
    /**
     * Create task achievements card
     */
    private VBox createTaskAchievementsCard() {
        VBox achievementsContent = new VBox(8);
        achievementsContent.setAlignment(Pos.CENTER);
        
        Label recentBadgeLabel = new Label("🏆 Latest Badge:");
        recentBadgeLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Show latest badge based on achievements
        String latestBadge = getLatestBadgeName();
        Label badgeLabel = new Label(latestBadge);
        badgeLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(251, 191, 36, 0.1);" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;"
        );
        
        achievementsContent.getChildren().addAll(recentBadgeLabel, badgeLabel);
        
        return createModernCard("🛡️ Latest Honor", achievementsContent);
    }
    
    /**
     * Create quest tips card
     */
    private VBox createQuestTipsCard() {
        VBox tipsContent = new VBox(8);
        tipsContent.setAlignment(Pos.CENTER_LEFT);
        
        String[] tips = {
            "⚔️ Complete thy quests daily for valor bonuses",
            "🏰 Focus on high-reward quests first, noble knight",
            "🛡️ Check difficulty levels before embarking",
            "🔥 Maintain thy daily valor for extra gold coins"
        };
        
        for (String tip : tips) {
            Label tipLabel = new Label(tip);
            tipLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #64748b;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 4 0;"
            );
            tipLabel.setWrapText(true);
            tipsContent.getChildren().add(tipLabel);
        }
        
        return createModernCard("📜 Wisdom of the Realm", tipsContent);
    }
    
    /**
     * Create enhanced profile hero section
     */
    private VBox createProfileHeroSection() {
        VBox heroSection = new VBox(20);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(32));
        heroSection.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667EEA 0%, #764BA2 25%, #F093FB 50%, #F5576C 75%, #4ECDC4 100%);" +
            "-fx-background-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 32, 0.3, 0, 16), " +
                      "dropshadow(gaussian, rgba(102,126,234,0.4), 40, 0.2, 0, 0), " +
                      "innershadow(gaussian, rgba(255,255,255,0.2), 4, 0, 0, 2);"
        );
        
        // Large avatar with level ring
        VBox avatarContainer = createEnhancedAvatarDisplay();
        
        // User info section
        VBox userInfoSection = new VBox(8);
        userInfoSection.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("⚜ Sir " + currentUser.getName() + " ⚜");
        nameLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        Label titleLabel = new Label("🛡 Level " + currentUser.getLevel() + " Knight of the Realm 🛡");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        userInfoSection.getChildren().addAll(nameLabel, titleLabel);
        
        // Quick stats row
        HBox quickStats = createProfileQuickStats();
        
        heroSection.getChildren().addAll(avatarContainer, userInfoSection, quickStats);
        return heroSection;
    }
    
    /**
     * Create enhanced avatar display with level ring
     */
    private VBox createEnhancedAvatarDisplay() {
        VBox avatarContainer = new VBox(12);
        avatarContainer.setAlignment(Pos.CENTER);
        
        // Avatar with decorative ring
        javafx.scene.layout.StackPane avatarStack = new javafx.scene.layout.StackPane();
        avatarStack.setAlignment(Pos.CENTER);
        
        // Level ring background
        javafx.scene.shape.Circle levelRing = new javafx.scene.shape.Circle(50);
        levelRing.setFill(null);
        levelRing.setStroke(javafx.scene.paint.Color.web("#8B5CF6"));
        levelRing.setStrokeWidth(4);
        levelRing.getStrokeDashArray().addAll(10d, 5d);
        levelRing.setStyle("-fx-effect: dropshadow(gaussian, rgba(139,92,246,0.4), 8, 0, 0, 4);");
        
        // Large avatar
        Label avatarLabel = new Label("👨‍🚀");
        avatarLabel.setStyle(
            "-fx-font-size: 72px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
        );
        
        avatarStack.getChildren().addAll(levelRing, avatarLabel);
        
        // Level badge
        Label levelBadge = new Label("LVL " + currentUser.getLevel());
        levelBadge.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #8B5CF6, #7C3AED);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 6 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(139,92,246,0.5), 6, 0, 0, 3);"
        );
        
        avatarContainer.getChildren().addAll(avatarStack, levelBadge);
        return avatarContainer;
    }
    
    /**
     * Create profile quick stats
     */
    private HBox createProfileQuickStats() {
        HBox statsContainer = new HBox(16);
        statsContainer.setAlignment(Pos.CENTER);
        
        // Modern medieval stats with clean colors
        VBox goldStats = createQuickStatCard("🪙", String.valueOf(currentUser.getSmartCoinBalance()), "Gold Coins", "#D69E2E");
        
        // Valor streak stat
        VBox valorStats = createQuickStatCard("⚔️", String.valueOf(dailyStreak), "Valor Days", "#E53E3E");
        
        // Heraldry stat
        VBox heraldryStats = createQuickStatCard("🛡️", String.valueOf(totalBadges), "Heraldry", "#3182CE");
        
        // Completed quests stat
        VBox questStats = createQuickStatCard("🏰", String.valueOf(completedTasks), "Quests Won", "#805AD5");
        
        statsContainer.getChildren().addAll(goldStats, valorStats, heraldryStats, questStats);
        return statsContainer;
    }
    
    /**
     * Create individual quick stat card
     */
    private VBox createQuickStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(130);
        card.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FFFFFF, #F7FAFC);" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-style: solid;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 28px;"
        );
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2D3748;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label labelText = new Label(label);
        labelText.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        card.getChildren().addAll(iconLabel, valueLabel, labelText);
        return card;
    }
    
    /**
     * Show dedicated profile page
     */
    private void showProfilePage() {
        mainContent.getChildren().clear();
        
        // Enhanced profile page header with hero section
        VBox headerSection = createProfileHeroSection();
        
        // Profile layout
        HBox profileLayout = new HBox(20);
        profileLayout.setAlignment(Pos.TOP_CENTER);
        profileLayout.setMaxWidth(1180);
        
        // Left column - Profile info (40% width)
        VBox profileInfoSection = createProfileInfoSection();
        profileInfoSection.setPrefWidth(450);
        profileInfoSection.setMaxWidth(450);
        
        // Right column - Customization options (60% width)
        VBox customizationSection = createCustomizationSection();
        customizationSection.setPrefWidth(650);
        customizationSection.setMaxWidth(650);
        
        profileLayout.getChildren().addAll(profileInfoSection, customizationSection);
        
        // Create scrollable content for profile page
        VBox profilePageContent = new VBox(20);
        profilePageContent.setPadding(new Insets(16));
        profilePageContent.setAlignment(Pos.TOP_CENTER);
        profilePageContent.setMaxWidth(1180);
        profilePageContent.getChildren().addAll(headerSection, profileLayout);
        
        // Create scroll pane for profile content
        ScrollPane profileScrollPane = new ScrollPane(profilePageContent);
        profileScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        profileScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        profileScrollPane.setFitToWidth(true);
        profileScrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Replace content with scrollable profile page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(profileScrollPane);
    }
    
    /**
     * Create enhanced profile info section
     */
    private VBox createProfileInfoSection() {
        VBox profileSection = new VBox(16);
        
        // Avatar and basic info card
        VBox avatarCard = createAvatarInfoCard();
        
        // Stats card
        VBox statsCard = createProfileStatsCard();
        
        // Achievement showcase card
        VBox achievementCard = createAchievementShowcaseCard();
        
        profileSection.getChildren().addAll(avatarCard, statsCard, achievementCard);
        return profileSection;
    }
    
    /**
     * Create achievement showcase card
     */
    private VBox createAchievementShowcaseCard() {
        VBox achievementContent = new VBox(12);
        
        // Recent achievements
        VBox recentSection = new VBox(8);
        recentSection.setAlignment(Pos.CENTER_LEFT);
        
        Label recentLabel = new Label("🏆 Recent Achievements");
        recentLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Achievement badges grid
        HBox badgeGrid = new HBox(8);
        badgeGrid.setAlignment(Pos.CENTER_LEFT);
        
        String[] recentBadges = {"🎯", "🌟", "⚔️", "🏃", "📚"};
        String[] badgeNames = {"Quest Master", "Rising Star", "Brave Heart", "Speed Runner", "Book Worm"};
        
        for (int i = 0; i < Math.min(recentBadges.length, 3); i++) {
            VBox badge = createAchievementBadge(recentBadges[i], badgeNames[i]);
            badgeGrid.getChildren().add(badge);
        }
        
        recentSection.getChildren().addAll(recentLabel, badgeGrid);
        
        // Progress toward next achievement
        VBox progressSection = new VBox(8);
        progressSection.setAlignment(Pos.CENTER_LEFT);
        
        Label progressLabel = new Label("🎯 Next Achievement");
        progressLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Progress toward next badge
        VBox nextBadgeProgress = createNextBadgeProgress();
        
        progressSection.getChildren().addAll(progressLabel, nextBadgeProgress);
        
        achievementContent.getChildren().addAll(recentSection, progressSection);
        
        return createModernCard("🏰 Hall of Honor", achievementContent);
    }
    
    /**
     * Create individual achievement badge
     */
    private VBox createAchievementBadge(String icon, String name) {
        VBox badge = new VBox(4);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(8));
        badge.setPrefWidth(60);
        badge.setStyle(
            "-fx-background-color: rgba(251, 191, 36, 0.15);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: rgba(251, 191, 36, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(251,191,36,0.2), 4, 0, 0, 2);"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");
        
        Label nameLabel = new Label(name);
        nameLabel.setStyle(
            "-fx-font-size: 8px;" +
            "-fx-text-fill: #92400e;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(55);
        
        badge.getChildren().addAll(iconLabel, nameLabel);
        return badge;
    }
    
    /**
     * Create progress toward next badge
     */
    private VBox createNextBadgeProgress() {
        VBox progressContainer = new VBox(6);
        progressContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label nextBadgeLabel = new Label("🏅 Master Achiever");
        nextBadgeLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #8B5CF6;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Progress bar for next achievement
        HBox progressBar = new HBox();
        progressBar.setAlignment(Pos.CENTER_LEFT);
        
        Region progressBg = new Region();
        progressBg.setPrefWidth(150);
        progressBg.setPrefHeight(6);
        progressBg.setStyle(
            "-fx-background-color: rgba(139, 92, 246, 0.2);" +
            "-fx-background-radius: 3;"
        );
        
        Region progressFill = new Region();
        double nextProgress = (completedTasks % 5) / 5.0; // Progress toward next badge
        progressFill.setPrefWidth(150 * Math.max(0.1, nextProgress));
        progressFill.setPrefHeight(6);
        progressFill.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B5CF6, #A855F7);" +
            "-fx-background-radius: 3;"
        );
        
        javafx.scene.layout.StackPane progressStack = new javafx.scene.layout.StackPane();
        progressStack.getChildren().addAll(progressBg, progressFill);
        progressStack.setAlignment(Pos.CENTER_LEFT);
        
        Label progressText = new Label((int)(nextProgress * 100) + "% complete");
        progressText.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        progressContainer.getChildren().addAll(nextBadgeLabel, progressStack, progressText);
        return progressContainer;
    }
    
    /**
     * Create avatar info card
     */
    private VBox createAvatarInfoCard() {
        VBox avatarContent = new VBox(16);
        avatarContent.setAlignment(Pos.CENTER);
        
        // Large avatar display
        VBox avatarContainer = new VBox(8);
        avatarContainer.setAlignment(Pos.CENTER);
        avatarContainer.setPadding(new Insets(20));
        avatarContainer.setStyle(
            "-fx-background-color: rgba(255, 152, 0, 0.1);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 1;"
        );
        
        // Avatar placeholder (larger)
        Label avatarPlaceholder = new Label("👨‍🚀");
        avatarPlaceholder.setStyle(
            "-fx-font-size: 64px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 3);"
        );
        
        // User name
        Label nameLabel = new Label(currentUser.getName());
        nameLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Level and title
        Label levelLabel = new Label("Level " + currentUser.getLevel() + " Adventurer");
        levelLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 152, 0, 0.15);" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 4 12;"
        );
        
        avatarContainer.getChildren().addAll(avatarPlaceholder, nameLabel, levelLabel);
        avatarContent.getChildren().add(avatarContainer);
        
        return createModernCard("🎭 Thy Noble Visage", avatarContent);
    }
    
    /**
     * Create profile stats card
     */
    private VBox createProfileStatsCard() {
        VBox statsContent = new VBox(12);
        
        // Create stat rows using real data
        HBox coinsRow = createStatRow("💰", "SmartCoins", String.valueOf(currentUser.getSmartCoinBalance()));
        HBox levelRow = createStatRow("⭐", "Level", String.valueOf(currentUser.getLevel()));
        HBox streakRow = createStatRow("🔥", "Daily Streak", dailyStreak + " days");
        HBox badgesRow = createStatRow("🏆", "Badges Earned", String.valueOf(totalBadges));
        
        statsContent.getChildren().addAll(coinsRow, levelRow, streakRow, badgesRow);
        
        return createModernCard("📊 Noble Statistics", statsContent);
    }
    
    /**
     * Create stat row for profile
     */
    private HBox createStatRow(String icon, String label, String value) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8));
        row.setStyle(
            "-fx-background-color: rgba(248, 250, 252, 0.7);" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");
        
        Label labelText = new Label(label);
        labelText.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        row.getChildren().addAll(iconLabel, labelText, spacer, valueLabel);
        return row;
    }
    
    /**
     * Create customization section
     */
    private VBox createCustomizationSection() {
        VBox customizationSection = new VBox(16);
        
        // Avatar customization card
        VBox avatarCustomCard = createAvatarCustomizationCard();
        
        // Settings card
        VBox settingsCard = createProfileSettingsCard();
        
        customizationSection.getChildren().addAll(avatarCustomCard, settingsCard);
        return customizationSection;
    }
    
    /**
     * Create enhanced avatar customization card
     */
    private VBox createAvatarCustomizationCard() {
        VBox customContent = new VBox(20);
        
        // Current avatar preview
        VBox currentAvatarSection = new VBox(12);
        currentAvatarSection.setAlignment(Pos.CENTER);
        currentAvatarSection.setPadding(new Insets(20));
        currentAvatarSection.setStyle(
            "-fx-background-color: rgba(139, 92, 246, 0.1);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(139, 92, 246, 0.3);" +
            "-fx-border-width: 1;"
        );
        
        Label currentLabel = new Label("Current Avatar");
        currentLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label currentAvatar = new Label("👨‍🚀");
        currentAvatar.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-effect: dropshadow(gaussian, rgba(139,92,246,0.4), 6, 0, 0, 3);"
        );
        
        currentAvatarSection.getChildren().addAll(currentLabel, currentAvatar);
        
        // Avatar options grid with categories
        VBox avatarOptionsSection = new VBox(16);
        
        Label optionsLabel = new Label("Choose New Avatar:");
        optionsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Professional avatars
        HBox professionalRow = new HBox(12);
        professionalRow.setAlignment(Pos.CENTER);
        
        String[] professionals = {"👨‍🚀", "🧙‍♂️", "🥷", "🦸‍♂️", "🧑‍🎓"};
        String[] profLabels = {"Astronaut", "Wizard", "Ninja", "Hero", "Scholar"};
        
        for (int i = 0; i < professionals.length; i++) {
            VBox avatarOption = createAvatarOption(professionals[i], profLabels[i]);
            professionalRow.getChildren().add(avatarOption);
        }
        
        // Fun avatars
        HBox funRow = new HBox(12);
        funRow.setAlignment(Pos.CENTER);
        
        String[] funAvatars = {"🐉", "🦄", "🎮", "🎨", "🎭"};
        String[] funLabels = {"Dragon", "Unicorn", "Gamer", "Artist", "Actor"};
        
        for (int i = 0; i < funAvatars.length; i++) {
            VBox avatarOption = createAvatarOption(funAvatars[i], funLabels[i]);
            funRow.getChildren().add(avatarOption);
        }
        
        avatarOptionsSection.getChildren().addAll(optionsLabel, professionalRow, funRow);
        
        customContent.getChildren().addAll(currentAvatarSection, avatarOptionsSection);
        
        return createModernCard("🎭 Royal Customization", customContent);
    }
    
    /**
     * Create individual avatar option with label
     */
    private VBox createAvatarOption(String avatar, String label) {
        VBox option = new VBox(6);
        option.setAlignment(Pos.CENTER);
        option.setPrefWidth(70);
        option.setStyle("-fx-cursor: hand;");
        
        Button avatarBtn = new Button(avatar);
        avatarBtn.setPrefSize(50, 50);
        avatarBtn.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(226, 232, 240, 0.8);" +
            "-fx-border-width: 1;" +
            "-fx-font-size: 20px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        
        Label nameLabel = new Label(label);
        nameLabel.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Hover effects
        avatarBtn.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            avatarBtn.setStyle(
                "-fx-background-color: rgba(139, 92, 246, 0.2);" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #8B5CF6;" +
                "-fx-border-width: 2;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(139,92,246,0.4), 8, 0, 0, 4);" +
                "-fx-scale-x: 1.15; -fx-scale-y: 1.15;"
            );
        });
        
        avatarBtn.setOnMouseExited(e -> {
            avatarBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: rgba(226, 232, 240, 0.8);" +
                "-fx-border-width: 1;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
            );
        });
        
        avatarBtn.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            // TODO: Implement avatar selection logic
            System.out.println("Selected avatar: " + avatar + " (" + label + ")");
        });
        
        option.getChildren().addAll(avatarBtn, nameLabel);
        return option;
    }
    
    /**
     * Create enhanced profile settings card with inline layout
     */
    private VBox createProfileSettingsCard() {
        VBox settingsContent = new VBox(20);
        
        // Settings grid - 2 columns layout
        HBox settingsGrid = new HBox(16);
        settingsGrid.setAlignment(Pos.TOP_CENTER);
        
        // Left column - Personal & Game settings
        VBox leftColumn = new VBox(16);
        leftColumn.setPrefWidth(300);
        leftColumn.setMaxWidth(300);
        
        // Personal settings section
        VBox personalSection = new VBox(8);
        personalSection.setAlignment(Pos.CENTER_LEFT);
        
        Label personalHeader = new Label("👤 Personal Settings");
        personalHeader.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 0 0 8 0;"
        );
        
        HBox personalRow = new HBox(8);
        personalRow.setAlignment(Pos.CENTER_LEFT);
        
        Button nameBtn = createCompactSettingButton("✏️", "Change Name", "#3B82F6");
        Button themeBtn = createCompactSettingButton("🎨", "Theme Settings", "#8B5CF6");
        
        personalRow.getChildren().addAll(nameBtn, themeBtn);
        personalSection.getChildren().addAll(personalHeader, personalRow);
        
        // Game settings section
        VBox gameSection = new VBox(8);
        gameSection.setAlignment(Pos.CENTER_LEFT);
        
        Label gameHeader = new Label("🎮 Game Settings");
        gameHeader.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 0 0 8 0;"
        );
        
        HBox gameRow = new HBox(8);
        gameRow.setAlignment(Pos.CENTER_LEFT);
        
        Button notificationsBtn = createCompactSettingButton("🔔", "Notifications", "#10B981");
        Button soundBtn = createCompactSettingButton("🔊", "Sound Settings", "#F59E0B");
        
        gameRow.getChildren().addAll(notificationsBtn, soundBtn);
        gameSection.getChildren().addAll(gameHeader, gameRow);
        
        leftColumn.getChildren().addAll(personalSection, gameSection);
        
        // Right column - Privacy settings
        VBox rightColumn = new VBox(16);
        rightColumn.setPrefWidth(300);
        rightColumn.setMaxWidth(300);
        
        VBox privacySection = new VBox(8);
        privacySection.setAlignment(Pos.CENTER_LEFT);
        
        Label privacyHeader = new Label("🔒 Privacy & Safety");
        privacyHeader.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 0 0 8 0;"
        );
        
        HBox privacyRow = new HBox(8);
        privacyRow.setAlignment(Pos.CENTER_LEFT);
        
        Button privacyBtn = createCompactSettingButton("🛡️", "Privacy Settings", "#6B7280");
        Button parentBtn = createCompactSettingButton("👨‍👩‍👧‍👦", "Parent Controls", "#EC4899");
        
        privacyRow.getChildren().addAll(privacyBtn, parentBtn);
        privacySection.getChildren().addAll(privacyHeader, privacyRow);
        
        rightColumn.getChildren().add(privacySection);
        
        settingsGrid.getChildren().addAll(leftColumn, rightColumn);
        settingsContent.getChildren().add(settingsGrid);
        
        return createModernCard("⚙️ Royal Decree & Laws", settingsContent);
    }
    
    /**
     * Create compact setting button for inline layout
     */
    private Button createCompactSettingButton(String icon, String title, String color) {
        Button button = new Button();
        button.setPrefWidth(140);
        button.setPrefHeight(80);
        button.setAlignment(Pos.CENTER);
        button.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 3);"
        );
        
        // Create button content
        VBox buttonContent = new VBox(8);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setPadding(new Insets(8));
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: " + color + ";"
        );
        
        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(120);
        
        buttonContent.getChildren().addAll(iconLabel, titleLabel);
        
        button.setGraphic(buttonContent);
        button.setText("");
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
            
            // Change text colors on hover
            titleLabel.setStyle(titleLabel.getStyle().replace("#1e293b", "white"));
            iconLabel.setStyle(iconLabel.getStyle().replace(color, "white"));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 3);"
            );
            
            // Reset text colors
            titleLabel.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: #1e293b;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
            iconLabel.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-text-fill: " + color + ";"
            );
        });
        
        button.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            System.out.println("Opening: " + title);
        });
        
        return button;
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
     * Create welcome banner with personalized content
     */
    private VBox createWelcomeBanner() {
        VBox banner = new VBox(16);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(32));
        banner.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667EEA, #764BA2, #F093FB);" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 16, 0, 0, 8);"
        );
        
        // Medieval welcome message with heraldic styling
        Label welcomeTitle = new Label("⚔ Hail, Noble " + currentUser.getName() + "! ⚔");
        welcomeTitle.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        // Medieval status message
        String statusMessage = getMedievalStatusMessage();
        Label statusLabel = new Label(statusMessage);
        statusLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0, 0, 1);"
        );
        statusLabel.setWrapText(true);
        
        // Progress indicator
        HBox progressSection = createLevelProgressIndicator();
        
        banner.getChildren().addAll(welcomeTitle, statusLabel, progressSection);
        return banner;
    }
    
    /**
     * Create quick action cards for common tasks
     */
    private HBox createQuickActionCards() {
        HBox actionCards = new HBox(16);
        actionCards.setAlignment(Pos.CENTER);
        actionCards.setPadding(new Insets(16, 0, 16, 0));
        
        // Premium medieval actions with enhanced colors
        VBox questAction = createActionCard("⚔️", "Noble Quests", 
            pendingTasks + " epic adventures", "#FF6B6B", () -> navigateToSection("tasks"));
        
        // View Achievements action
        VBox heraldryAction = createActionCard("🛡️", "Thy Heraldry", 
            totalBadges + " legendary honors", "#FFD93D", () -> navigateToSection("achievements"));
        
        // Visit Shop action
        VBox marketAction = createActionCard("🏰", "Royal Market", 
            currentUser.getSmartCoinBalance() + " golden coins", "#4ECDC4", () -> navigateToSection("shop"));
        
        // Customize Profile action
        VBox courtAction = createActionCard("👑", "Royal Court", 
            "Sovereign Level " + currentUser.getLevel(), "#A8E6CF", () -> navigateToSection("profile"));
        
        actionCards.getChildren().addAll(questAction, heraldryAction, marketAction, courtAction);
        return actionCards;
    }
    
    /**
     * Create individual action card
     */
    private VBox createActionCard(String icon, String title, String subtitle, String color, Runnable action) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24));
        card.setPrefWidth(220);
        card.setStyle(
            "-fx-background-color: linear-gradient(145deg, #FFFFFF 0%, #F8FAFC 50%, #EDF2F7 100%);" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 3;" +
            "-fx-border-style: solid;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 16, 0.3, 0, 8), " +
                      "innershadow(gaussian, rgba(255,255,255,0.6), 2, 0, 0, 1);"
        );
        
        // Enhanced icon with premium styling
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
            "-fx-font-size: 42px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        // Premium title with enhanced typography
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1A202C;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.8), 1, 0, 0, 1);"
        );
        
        // Enhanced subtitle with gradient text effect
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 1, 0, 0, 1);"
        );
        
        card.getChildren().addAll(iconLabel, titleLabel, subtitleLabel);
        
        // Add premium hover effects with micro-interactions
        card.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            card.setStyle(
                "-fx-background-color: linear-gradient(145deg, " + color + " 0%, derive(" + color + ", -20%) 100%);" +
                "-fx-border-color: derive(" + color + ", 30%);" +
                "-fx-border-width: 4;" +
                "-fx-border-style: solid;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 24, 0.4, 0, 12), " +
                          "dropshadow(gaussian, rgba(255,255,255,0.8), 8, 0, 0, -2);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
            
            // Enhanced text effects on hover
            titleLabel.setStyle(titleLabel.getStyle().replace("#1A202C", "#FFFFFF"));
            subtitleLabel.setStyle(subtitleLabel.getStyle().replace(color, "rgba(255,255,255,0.9)"));
            iconLabel.setStyle(iconLabel.getStyle() + "-fx-scale-x: 1.2; -fx-scale-y: 1.2;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: linear-gradient(145deg, #FFFFFF 0%, #F8FAFC 50%, #EDF2F7 100%);" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 3;" +
                "-fx-border-style: solid;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 16, 0.3, 0, 8), " +
                          "innershadow(gaussian, rgba(255,255,255,0.6), 2, 0, 0, 1);"
            );
            
            // Reset all elements to original state
            titleLabel.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: #1A202C;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.8), 1, 0, 0, 1);"
            );
            subtitleLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + color + ";" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 1, 0, 0, 1);"
            );
            iconLabel.setStyle(
                "-fx-font-size: 42px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
            );
        });
        
        card.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            action.run();
        });
        
        return card;
    }
    
    /**
     * Create medieval-themed status message
     */
    private String getMedievalStatusMessage() {
        if (pendingTasks > 0) {
            return "Thy noble quests await thee, brave knight! " + pendingTasks + " adventures beckon!";
        } else if (completedTasks > 0) {
            return "Huzzah! Thou hast completed " + completedTasks + " quests with great valor! Seek ye more glory?";
        } else {
            return "Thy epic saga begins here! Seek thy lord parent to bestow upon thee noble quests!";
        }
    }
    
    
    /**
     * Create level progress indicator
     */
    private HBox createLevelProgressIndicator() {
        HBox progressContainer = new HBox(12);
        progressContainer.setAlignment(Pos.CENTER);
        
        // Progress bar
        VBox progressSection = new VBox(4);
        progressSection.setAlignment(Pos.CENTER);
        
        Label progressLabel = new Label("Level " + currentUser.getLevel() + " Progress");
        progressLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #475569;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Progress bar background
        Region progressBg = new Region();
        progressBg.setPrefWidth(200);
        progressBg.setPrefHeight(8);
        progressBg.setStyle(
            "-fx-background-color: rgba(226, 232, 240, 0.8);" +
            "-fx-background-radius: 4;"
        );
        
        // Progress bar fill
        Region progressFill = new Region();
        double progress = (currentUser.getSmartCoinBalance() % 100) / 100.0;
        progressFill.setPrefWidth(200 * Math.max(0.1, progress));
        progressFill.setPrefHeight(8);
        progressFill.setStyle(
            "-fx-background-color: linear-gradient(to right, #3B82F6, #8B5CF6);" +
            "-fx-background-radius: 4;"
        );
        
        // Stack progress elements
        javafx.scene.layout.StackPane progressStack = new javafx.scene.layout.StackPane();
        progressStack.getChildren().addAll(progressBg, progressFill);
        progressStack.setAlignment(Pos.CENTER_LEFT);
        
        progressSection.getChildren().addAll(progressLabel, progressStack);
        
        // Next level info
        Label nextLevelLabel = new Label("Next: Level " + (currentUser.getLevel() + 1));
        nextLevelLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        progressContainer.getChildren().addAll(progressSection, nextLevelLabel);
        return progressContainer;
    }
    
    /**
     * Create daily motivation section
     */
    private VBox createDailyMotivationSection() {
        VBox motivationSection = new VBox(12);
        motivationSection.setAlignment(Pos.CENTER);
        motivationSection.setPadding(new Insets(20, 0, 0, 0));
        
        // Motivation card
        VBox motivationCard = new VBox(16);
        motivationCard.setAlignment(Pos.CENTER);
        motivationCard.setPadding(new Insets(24));
        motivationCard.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, rgba(16, 185, 129, 0.1), rgba(5, 150, 105, 0.1));" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(16, 185, 129, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.2), 8, 0, 0, 4);"
        );
        
        // Daily tip icon
        Label tipIcon = new Label("💡");
        tipIcon.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.4), 6, 0, 0, 3);"
        );
        
        // Daily tip title
        Label tipTitle = new Label("💫 Daily Adventure Tip");
        tipTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #065f46;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Dynamic tip based on user progress
        String dailyTip = getDailyTip();
        Label tipContent = new Label(dailyTip);
        tipContent.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #047857;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        tipContent.setWrapText(true);
        tipContent.setMaxWidth(600);
        
        motivationCard.getChildren().addAll(tipIcon, tipTitle, tipContent);
        motivationSection.getChildren().add(motivationCard);
        
        return motivationSection;
    }
    
    /**
     * Get personalized daily tip based on user progress
     */
    private String getDailyTip() {
        if (pendingTasks > 3) {
            return "You have many quests to complete! Try focusing on one at a time for the best results.";
        } else if (dailyStreak > 7) {
            return "Incredible streak! You're building amazing habits. Keep up the fantastic work!";
        } else if (currentUser.getSmartCoinBalance() > 100) {
            return "Great job saving your SmartCoins! Consider spending some in the shop for cool rewards.";
        } else if (completedTasks > 5) {
            return "You're becoming a true quest master! Your dedication is paying off.";
        } else {
            return "Every great adventurer starts with a single quest. You're on the right path!";
        }
    }
    
    /**
     * Show enhanced home page (main dashboard)
     */
    private void showHomePage() {
        // Create scrollable content container
        VBox homeContent = new VBox(20);
        homeContent.setPadding(new Insets(16));
        homeContent.setAlignment(Pos.TOP_CENTER);
        homeContent.setMaxWidth(1180);
        homeContent.setPrefWidth(1180);
        
        // Welcome banner section
        VBox welcomeBanner = createWelcomeBanner();
        
        // Quick action cards
        HBox quickActions = createQuickActionCards();
        
        // Main content grid
        VBox mainGrid = createMainGrid();
        
        // Daily motivation section
        VBox motivationSection = createDailyMotivationSection();
        
        homeContent.getChildren().addAll(welcomeBanner, quickActions, mainGrid, motivationSection);
        
        // Create scroll pane for home content
        ScrollPane homeScrollPane = new ScrollPane(homeContent);
        homeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        homeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        homeScrollPane.setFitToWidth(true);
        homeScrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Replace content with scrollable home page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(homeScrollPane);
        
        System.out.println("Enhanced scrollable home page displayed");
    }
    
}
