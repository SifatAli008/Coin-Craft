package com.coincraft.ui.dashboards;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.models.MessageData;
import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;
import com.coincraft.services.MessagingService;
import com.coincraft.ui.components.child.ChildSidebar;
import com.coincraft.ui.components.child.ChildTopBar;
import com.coincraft.ui.components.child.ShopPage;
import com.coincraft.ui.components.child.TaskCardList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
    private ChildSidebar sidebar;
    private ShopPage shopPage;
    
    // Messaging
    private final MessagingService messagingService;
    private List<MessageData> messageHistory = new ArrayList<>();
    private VBox messagesContainer;
    private ScrollPane messagesScroll;
    private TextArea messageInput;
    
    // Content sections
    private VBox mainContent;
    private String currentSection = "quests";
    
    // Real data from Firebase
    private List<Task> userTasks;
    private int dailyStreak = 0;
    private int totalBadges = 0;
    private int completedTasks = 0;
    private int pendingTasks = 0;
    
    public ChildDashboard(User user) {
        super(user);
        loadRealData();
        messagingService = MessagingService.getInstance();
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
        // Reload the current user to ensure latest SmartCoin balance after approvals/purchases
        try {
            FirebaseService firebaseService = FirebaseService.getInstance();
            User reloaded = firebaseService.loadUser(currentUser.getUserId());
            if (reloaded != null) {
                this.currentUser = reloaded;
            }
        } catch (Exception ignored) {}

        loadRealData();
        
        // Refresh UI components with new data
        if (topBar != null) {
            // Ensure top bar uses the latest user instance for balance display
            topBar.setCurrentUser(currentUser);
            topBar.updateStats(dailyStreak, pendingTasks);
            topBar.refresh();
        }
        
        // Refresh current section
        navigateToSection(currentSection);
        
        System.out.println("🔄 Child dashboard data refreshed");
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
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMouseTransparent(true);
        
        // Create the main BorderPane for layout with medieval theme
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().addAll("child-dashboard", "medieval-theme");
        mainLayout.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
        topBar.updateStats(dailyStreak, pendingTasks);
        topBar.refresh(); // Refresh UI to show real stats
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
        
        // Content will be set by navigation (Quest page shows game launcher)
    }
    
    /**
     * Create action section with primary CTA
     */
    @SuppressWarnings("unused")
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-padding: 12 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 8);"
            );
        } else {
            requestBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FA8A00, #E67E00);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-padding: 12 24;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 20, 0, 0, 10);" +
                "-fx-cursor: hand;"
            );
            
            // Hover effects
            requestBtn.setOnMouseEntered(e -> {
                // Button hover sound can be added via CentralizedMusicManager if desired
                requestBtn.setStyle(requestBtn.getStyle() + 
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.6), 25, 0, 0, 12);"
                );
            });
            
            requestBtn.setOnMouseExited(e -> {
                requestBtn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #FA8A00, #E67E00);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 25;" +
                    "-fx-border-radius: 25;" +
                    "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                    "-fx-padding: 12 24;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 20, 0, 0, 10);" +
                    "-fx-cursor: hand;"
                );
            });
            
            requestBtn.setOnAction(e -> {
                // Button click sound can be added via CentralizedMusicManager if desired
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
        dialog.setContentText("""
            Wow! You've reached a high enough level to request your own missions!
            
            You can now propose new real-world tasks to your parent or guardian. Think of something fun and educational you'd like to do, and they can approve it with a SmartCoin reward!
            
            This feature will be fully available in the next update!
            """);
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        // Error sound can be added via CentralizedMusicManager if desired
        alert.showAndWait();
    }
    
    
    @Override
    public void navigateToSection(String section) {
        this.currentSection = section;
        
        // Update sidebar navigation
        sidebar.setActiveSection(section);
        
        // Handle section-specific logic
        switch (section.toLowerCase()) {
            case "quests" -> {
                // Show quests page with available quests
                showQuestsPage();
            }
            case "tasks" -> {
                // Tasks functionality removed - redirect to quests
                showQuestsPage();
            }
            case "messages" -> {
                // Child-parent chat
                showMessagingInterface();
            }
            case "shop" -> {
                // Navigate to shop (if available)
                showShopPage();
            }
            case "profile" -> {
                // Show dedicated profile page
                showProfilePage();
            }
            default -> System.out.println("Unknown section: " + section);
        }
    }
    
    /**
     * Show dedicated tasks page
     */
    @SuppressWarnings("unused")
    private void showTasksPage() {
        mainContent.getChildren().clear();
        
        // Tasks page header
        VBox headerSection = new VBox(8);
        headerSection.setAlignment(Pos.CENTER_LEFT);
        headerSection.setPadding(new Insets(0, 0, 20, 0));
        
        Label titleLabel = new Label("My Quest Journal");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Track your adventures and complete epic quests!");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
        
        // Create task card list
        TaskCardList taskView = new TaskCardList(currentUser);
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
        
        // Quest tips card
        VBox tipsCard = createQuestTipsCard();
        
        statsSection.getChildren().addAll(progressCard, tipsCard);
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label pendingLabel = new Label("⏳ " + pendingTasks + " In Progress");
        pendingLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        int totalTasks = completedTasks + pendingTasks;
        Label totalLabel = new Label("📊 " + totalTasks + " Total Quests");
        totalLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #3B82F6;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        progressContent.getChildren().addAll(completedLabel, pendingLabel, totalLabel);
        
        return createModernCard("⚔️ Quest Progress", progressContent);
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
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
        
        Label nameLabel = new Label("⚜ Sir " + sanitizeDisplayName(currentUser.getName()) + " ⚜");
        nameLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        Label titleLabel = new Label("🛡 Level " + currentUser.getLevel() + " Knight of the Realm 🛡");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #FFFFFF;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
            "-fx-text-fill: #FF9800;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
        VBox valorStats = createQuickStatCard("🎯", String.valueOf(dailyStreak), "Valor Days", "#E53E3E");
        
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label labelText = new Label(label);
        labelText.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
        
        profileSection.getChildren().addAll(avatarCard, statsCard);
        return profileSection;
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
            "-fx-text-fill: #FF9800;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 3);"
        );
        
        // User name
        Label nameLabel = new Label(sanitizeDisplayName(currentUser.getName()));
        nameLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Level and title
        Label levelLabel = new Label("Level " + currentUser.getLevel() + " Adventurer");
        levelLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
        
        statsContent.getChildren().addAll(coinsRow, levelRow, streakRow);
        
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label currentAvatar = new Label("👨‍🚀");
        currentAvatar.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-text-fill: #FF9800;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Hover effects
        avatarBtn.setOnMouseEntered(e -> {
            // Button hover sound can be added via CentralizedMusicManager if desired
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
            // Button click sound can be added via CentralizedMusicManager if desired
            // Avatar selection logic can be implemented here
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(120);
        
        buttonContent.getChildren().addAll(iconLabel, titleLabel);
        
        button.setGraphic(buttonContent);
        button.setText("");
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            // Button hover sound can be added via CentralizedMusicManager if desired
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
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-text-alignment: center;"
            );
            iconLabel.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-text-fill: " + color + ";"
            );
        });
        
        button.setOnAction(e -> {
            // Button click sound can be added via CentralizedMusicManager if desired
            System.out.println("Opening: " + title);
        });
        
        return button;
    }
    
    
    
    /**
     * Show messaging interface for group communication
     */
    private void showMessagingInterface() {
        try {
            BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
            VBox page = new VBox(12);
            page.setPadding(new Insets(16));
            page.setAlignment(Pos.TOP_CENTER);
            page.setMaxWidth(900);
            
            // Check if child has a valid parent ID
            String parentId = currentUser.getParentId();
            if (parentId == null || parentId.isEmpty()) {
                showNoParentMessage(page);
                mainLayout.setCenter(page);
                return;
            }
            
            // Header
            Label header = new Label("💬 Messages with Parent");
            header.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: #1f2937;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
            );
            
            // Messages container
            messagesContainer = new VBox(8);
            messagesContainer.setPadding(new Insets(12));
            messagesScroll = new ScrollPane(messagesContainer);
            messagesScroll.setFitToWidth(true);
            messagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            messagesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            messagesScroll.setStyle(
                "-fx-background-color: white;" +
                "-fx-background: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #e5e7eb;"
            );
            messagesScroll.setPrefHeight(480);
            
            // Input row
            HBox inputRow = new HBox(10);
            inputRow.setAlignment(Pos.CENTER_LEFT);
            messageInput = new TextArea();
            messageInput.setPromptText("Write a message…  (Enter to send • Shift+Enter for new line)");
            messageInput.setPrefRowCount(2);
            HBox.setHgrow(messageInput, Priority.ALWAYS);
            messageInput.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-width: 1;" +
                "-fx-font-family: 'Segoe UI', sans-serif;"
            );
            messageInput.setOnKeyPressed(e -> {
                if (e.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    if (e.isShiftDown()) {
                        // allow newline
                    } else {
                        e.consume();
                        sendChildMessage();
                    }
                }
            });
            Button sendBtn = new Button("📤 Send");
            sendBtn.setPrefHeight(38);
            sendBtn.setStyle(
                "-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: 700; -fx-background-radius: 10; -fx-padding: 8 16;"
            );
            sendBtn.setOnAction(e -> sendChildMessage());
            inputRow.getChildren().addAll(messageInput, sendBtn);
            
            VBox content = new VBox(12);
            content.setMaxWidth(900);
            content.getChildren().addAll(header, messagesScroll, inputRow);
            ScrollPane pageScroll = new ScrollPane(content);
            pageScroll.setFitToWidth(true);
            pageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            pageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            page.getChildren().add(pageScroll);
            
            mainLayout.setCenter(page);
            
            // Load and subscribe to messages
            String conversationId = parentId + "_" + currentUser.getUserId();
            messageHistory = messagingService.getRecent(conversationId, 100);
            renderMessages();
            messagingService.addListener(conversationId, new MessagingService.Listener() {
                @Override public void onUpdate(List<MessageData> messages) {
                    messageHistory = messages;
                    javafx.application.Platform.runLater(() -> renderMessages());
                }
            });
        } catch (Exception ex) {
            System.out.println("Failed to open messaging UI: " + ex.getMessage());
        }
    }

    private void sendChildMessage() {
        String text = messageInput.getText() != null ? messageInput.getText().trim() : "";
        if (text.isEmpty()) {
            showMessageStatus("Please enter a message to send", false);
            return;
        }
        
        String parentId = currentUser.getParentId();
        if (parentId == null || parentId.isEmpty()) {
            showMessageStatus("Cannot send message: No parent linked to your account", false);
            return;
        }
        
        try {
            String conversationId = parentId + "_" + currentUser.getUserId();
            boolean success = messagingService.sendMessage(
                conversationId,
                currentUser.getUserId(),
                currentUser.getName(),
                parentId,
                "Parent",
                text
            );
            
            if (success) {
                messageInput.clear();
                showMessageStatus("Message sent successfully! 📤", true);
            } else {
                showMessageStatus("Failed to send message. Please try again.", false);
            }
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
            showMessageStatus("Error sending message: " + e.getMessage(), false);
        }
    }

    private void renderMessages() {
        messagesContainer.getChildren().clear();
        
        if (messageHistory == null || messageHistory.isEmpty()) {
            // Show empty state
            VBox emptyState = new VBox(16);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(40));
            
            Label iconLabel = new Label("💬");
            iconLabel.setStyle("-fx-font-size: 48px;");
            
            Label emptyLabel = new Label("No messages yet");
            emptyLabel.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #6b7280;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            
            Label hintLabel = new Label("Start a conversation with your parent!");
            hintLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #9ca3af;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            
            emptyState.getChildren().addAll(iconLabel, emptyLabel, hintLabel);
            messagesContainer.getChildren().add(emptyState);
        } else {
            // Show messages
            for (MessageData m : messageHistory) {
                messagesContainer.getChildren().add(createBubble(m));
            }
        }
        
        javafx.application.Platform.runLater(() -> {
            if (messagesScroll != null) messagesScroll.setVvalue(1.0);
        });
    }

    private HBox createBubble(MessageData message) {
        boolean isChild = currentUser.getUserId().equals(message.getSenderId());
        HBox row = new HBox();
        row.setAlignment(isChild ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        VBox bubble = new VBox(6);
        bubble.setMaxWidth(480);
        bubble.setPadding(new Insets(12, 16, 12, 16));
        
        // Enhanced styling with better colors and effects
        String bubbleStyle = isChild
            ? "-fx-background-color: linear-gradient(to bottom, #22C55E, #16A34A);"
            : "-fx-background-color: linear-gradient(to bottom, #F8FAFC, #F1F5F9);";
        
        bubble.setStyle(bubbleStyle +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-border-color: " + (isChild ? "rgba(34, 197, 94, 0.2)" : "rgba(203, 213, 225, 0.5)") + ";" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, " + (isChild ? "rgba(34, 197, 94, 0.3)" : "rgba(0,0,0,0.08)") + ", 8, 0, 0, 2);"
        );
        
        // Message content with sender name
        HBox contentRow = new HBox(8);
        contentRow.setAlignment(Pos.CENTER_LEFT);
        
        Label senderLabel = new Label(isChild ? "You" : message.getSenderName());
        senderLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + (isChild ? "rgba(255,255,255,0.9)" : "#64748B") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label content = new Label(message.getContent());
        content.setWrapText(true);
        content.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + (isChild ? "white" : "#1E293B") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-line-spacing: 2px;"
        );
        
        contentRow.getChildren().addAll(senderLabel);
        bubble.getChildren().addAll(contentRow, content);
        
        // Timestamp
        if (message.getTimestamp() != null) {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
            Label meta = new Label(message.getTimestamp().format(fmt));
            meta.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + (isChild ? "rgba(255,255,255,0.7)" : "#94A3B8") + ";" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            bubble.getChildren().add(meta);
        }
        
        row.getChildren().add(bubble);
        return row;
    }
    
    /**
     * Show shop page
     */
    private void showShopPage() {
        // Create shop page if not exists
        if (shopPage == null) {
            shopPage = new ShopPage(currentUser, this::handleShopRefresh);
        }
        
        // Replace main content with shop page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(shopPage.getRoot());
        
        System.out.println("Shop page displayed");
    }
    
    /**
     * Handle refresh callback from shop page
     */
    private void handleShopRefresh(String event) {
        if ("balance_updated".equals(event)) {
            // Refresh the top bar to update balance display
            if (topBar != null) {
                topBar.refresh();
            }
            System.out.println("🔄 Balance display refreshed after shop purchase");
        }
    }
    
    
    /**
     * Create individual action card
     */
    @SuppressWarnings("unused")
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.8), 1, 0, 0, 1);"
        );
        
        // Enhanced subtitle with gradient text effect
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 1, 0, 0, 1);"
        );
        
        card.getChildren().addAll(iconLabel, titleLabel, subtitleLabel);
        
        // Add premium hover effects with micro-interactions
        card.setOnMouseEntered(e -> {
            // Button hover sound can be added via CentralizedMusicManager if desired
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
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-text-alignment: center;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.8), 1, 0, 0, 1);"
            );
            subtitleLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + color + ";" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-text-alignment: center;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 1, 0, 0, 1);"
            );
            iconLabel.setStyle(
                "-fx-font-size: 42px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
            );
        });
        
        card.setOnMouseClicked(e -> {
            // Button click sound can be added via CentralizedMusicManager if desired
            action.run();
        });
        
        return card;
    }
    
    /**
     * Create daily motivation section
     */
    @SuppressWarnings("unused")
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
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Dynamic tip based on user progress
        String dailyTip = getDailyTip();
        Label tipContent = new Label(dailyTip);
        tipContent.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #047857;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
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
     * Show quests page with available quests and progress
     */
    private void showQuestsPage() {
        // Create simple content container for game launch
        VBox questsContent = new VBox(40);
        questsContent.setPadding(new Insets(60));
        questsContent.setAlignment(Pos.CENTER);
        
        // Quest page header
        VBox questHeader = createSimpleQuestHeader();
        
        // Large game launch button
        VBox gameCard = createLargeGameCard();
        
        questsContent.getChildren().addAll(questHeader, gameCard);
        
        // Replace content with quests page
        BorderPane mainLayout = (BorderPane) ((StackPane) root).getChildren().get(2);
        mainLayout.setCenter(questsContent);
        
        System.out.println("Quests page displayed - Game ready to launch");
    }
    
    /**
     * Create simple quest page header
     */
    private VBox createSimpleQuestHeader() {
        VBox header = new VBox(12);
        header.setAlignment(Pos.CENTER);
        
        // Quest page title
        Label questTitle = new Label("🎮 Financial Literacy Adventure");
        questTitle.setStyle(
            "-fx-font-size: 36px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Quest description
        Label descLabel = new Label("Learn about money and earn SmartCoins!");
        descLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        header.getChildren().addAll(questTitle, descLabel);
        return header;
    }
    
    /**
     * Create large game launch card
     */
    private VBox createLargeGameCard() {
        VBox gameCard = new VBox(30);
        gameCard.setAlignment(Pos.CENTER);
        gameCard.setPadding(new Insets(60));
        gameCard.setMaxWidth(600);
        gameCard.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);" +
            "-fx-background-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 24, 0, 0, 12);"
        );
        
        // Game icon
        Label gameIcon = new Label("🎮");
        gameIcon.setStyle("-fx-font-size: 80px;");
        
        // Game title
        Label gameTitle = new Label("Financial Adventure Game");
        gameTitle.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Game description
        Label gameDesc = new Label("Play interactive levels to learn about money\nand earn SmartCoins!");
        gameDesc.setWrapText(true);
        gameDesc.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: rgba(255,255,255,0.95);" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Launch button
        Button launchButton = new Button("🚀 Start Adventure");
        launchButton.setPrefWidth(280);
        launchButton.setPrefHeight(70);
        launchButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 35;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.5), 16, 0, 0, 8);"
        );
        
        launchButton.setOnMouseEntered(e -> {
            launchButton.setStyle(
                "-fx-background-color: #059669;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 35;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.7), 20, 0, 0, 10);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        launchButton.setOnMouseExited(e -> {
            launchButton.setStyle(
                "-fx-background-color: #10B981;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 35;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(16,185,129,0.5), 16, 0, 0, 8);"
            );
        });
        
        launchButton.setOnAction(e -> launchFinancialGame());
        
        gameCard.getChildren().addAll(gameIcon, gameTitle, gameDesc, launchButton);
        return gameCard;
    }
    
    /**
     * Launch the Financial Literacy Adventure Game
     */
    private void launchFinancialGame() {
        try {
            com.coincraft.game.ui.GameWindow gameWindow = new com.coincraft.game.ui.GameWindow(currentUser);
            gameWindow.show();
            
            // Add listener to refresh dashboard when game closes
            gameWindow.getStage().setOnHiding(e -> {
                // Reload user data to reflect coin changes
                loadRealData();
                if (topBar != null) {
                    topBar.setCurrentUser(currentUser);
                    topBar.updateStats(dailyStreak, pendingTasks);
                    topBar.refresh();
                }
                System.out.println("🎮 Game closed, dashboard refreshed");
            });
            
            System.out.println("🎮 Financial Adventure Game launched!");
            
        } catch (Exception e) {
            System.err.println("❌ Error launching game: " + e.getMessage());
            // Stack trace suppressed to avoid console noise; log framework can capture details in production
            
            // Show error dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Game Error");
            alert.setHeaderText("Could not launch Financial Adventure");
            alert.setContentText("Please try again later. Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Show message when child has no linked parent
     */
    private void showNoParentMessage(VBox container) {
        container.getChildren().clear();
        
        VBox messageBox = new VBox(20);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(40));
        
        Label iconLabel = new Label("👥");
        iconLabel.setStyle("-fx-font-size: 48px;");
        
        Label titleLabel = new Label("No Parent Linked");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label messageLabel = new Label("""
            You don't have a parent linked to your account yet.
            Ask your parent to create your adventurer account
            or contact support for help.
            """);
        messageLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        
        Button contactSupportBtn = new Button("📞 Contact Support");
        contactSupportBtn.setStyle(
            "-fx-background-color: #3B82F6;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 24;"
        );
        contactSupportBtn.setOnAction(e -> {
            System.out.println("Contact support requested");
            // Contact support can be implemented to open email or support page
        });
        
        messageBox.getChildren().addAll(iconLabel, titleLabel, messageLabel, contactSupportBtn);
        container.getChildren().add(messageBox);
    }
    
    /**
     * Show message status feedback
     */
    private void showMessageStatus(String message, boolean isSuccess) {
        // Create a temporary status label
        Label statusLabel = new Label(message);
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + (isSuccess ? "#10B981" : "#EF4444") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 8 16;" +
            "-fx-background-color: " + (isSuccess ? "rgba(16, 185, 129, 0.1)" : "rgba(239, 68, 68, 0.1)") + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );
        
        // Add to messages container temporarily
        if (messagesContainer != null) {
            messagesContainer.getChildren().add(statusLabel);
            
            // Remove after 3 seconds
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(3000);
                    javafx.application.Platform.runLater(() -> {
                        messagesContainer.getChildren().remove(statusLabel);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    
    /**
     * Remove repeated role suffixes like "(Adventurer)" from the display name
     */
    private String sanitizeDisplayName(String rawName) {
        if (rawName == null) return "";
        String cleaned = rawName.replaceAll("\\s*\\(Adventurer\\)", "");
        return cleaned.trim().replaceAll("\\s{2,}", " ");
    }
}
