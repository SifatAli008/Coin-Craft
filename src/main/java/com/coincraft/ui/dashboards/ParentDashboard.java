package com.coincraft.ui.dashboards;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.models.Product;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.components.parent.AddAdventurerDialog;
import com.coincraft.ui.components.parent.AddProductDialog;
import com.coincraft.ui.components.parent.EditProductDialog;
import com.coincraft.ui.components.parent.ChildMonitorCard;
import com.coincraft.ui.components.parent.FamilyAnalytics;
import com.coincraft.ui.components.parent.ParentSidebar;
import com.coincraft.ui.components.parent.ParentTopBar;
import com.coincraft.ui.components.parent.SettingsPage;
import com.coincraft.ui.components.parent.TaskManagementPage;
import com.coincraft.ui.components.shared.ProductCard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;

/**
 * Parent/Guardian Dashboard - Interface for parent oversight and management
 * Features child account management, task verification, and progress analytics
 * Follows the established game theme with professional styling
 */
public class ParentDashboard extends BaseDashboard {
    
    // Main UI Components
    private ParentTopBar topBar;
    private ParentSidebar sidebar;
    private FamilyAnalytics analytics;
    
    // Content sections
    private VBox mainContent;
    private ScrollPane contentScrollPane;
    private String currentSection = "overview";
    private javafx.animation.Timeline autoRefreshTimeline;
    
    // Real adventurer data from Firebase
    private List<User> children;
    private List<Task> allTasks;
    
    public ParentDashboard(User user) {
        super(user);
        loadRealData();
    }
    
    /**
     * Refresh data after login or when needed
     */
    public void refreshData() {
        loadRealData();
        if (currentSection.equals("overview")) {
            showOverviewContent();
        } else if (currentSection.equals("children")) {
            showChildrenContent();
        }
        if (topBar != null) {
            topBar.updateActiveChildren(children != null ? children.size() : 0);
            // Update notification count based on pending tasks
            updateNotificationCount();
            // Also refresh the displayed values immediately
            int pendingCount = (int) (allTasks == null ? 0 : allTasks.stream()
                .filter(task -> task.getValidationStatus() == com.coincraft.models.ValidationStatus.AWAITING_APPROVAL)
                .count());
            topBar.updatePendingTasks(pendingCount);
        }
    }
    
    private void loadRealData() {
        children = new ArrayList<>();
        allTasks = new ArrayList<>();
        
        // Load real adventurer data from Firebase
        loadAdventurersFromFirebase();
        
        // Load tasks for notification calculation
        loadTasksForNotifications();
    }
    
    private void loadAdventurersFromFirebase() {
        try {
            FirebaseService firebaseService = FirebaseService.getInstance();
            
            System.out.println("🔍 DEBUG: Loading adventurers from Firebase...");
            System.out.println("🔍 DEBUG: Firebase initialized: " + firebaseService.isInitialized());
            
            // Ensure Firebase service is properly initialized
            if (!firebaseService.isInitialized()) {
                System.out.println("⚠️ Firebase service not initialized, attempting to initialize...");
                firebaseService.initialize();
            }
            
            // In a real implementation, this would load children associated with this merchant
            // For now, we'll load all users with CHILD role and filter by merchant ID
            List<User> allUsers = firebaseService.getAllUsers();
            
            System.out.println("🔍 DEBUG: Total users loaded: " + (allUsers != null ? allUsers.size() : "null"));
            
            if (allUsers != null && !allUsers.isEmpty()) {
                for (User user : allUsers) {
                    System.out.println("🔍 DEBUG: Found user: " + user.getName() + " (Role: " + user.getRole() + ")");
                    if (user.getRole() == UserRole.CHILD) {
                        // Only include children created by this parent/merchant
                        boolean belongsToCurrent = false;
                        if (user.getParentId() != null && currentUser != null) {
                            if (user.getParentId().equals(currentUser.getUserId())) {
                                belongsToCurrent = true;
                            } else if (currentUser.getEmail() != null &&
                                       user.getParentId().equals("email:" + currentUser.getEmail().toLowerCase())) {
                                belongsToCurrent = true;
                            }
                        }
                        if (belongsToCurrent) {
                            children.add(user);
                            System.out.println("🔍 DEBUG: Added adventurer for current parent: " + user.getName());
                        } else if (user.getParentId() == null || user.getParentId().isEmpty()) {
                            // Orphaned child created before parent linkage existed; ignore in strict mode
                            System.out.println("ℹ️ Orphaned child found (no parentId), not showing: " + user.getName());
                        }
                    }
                }
                // Strict mode: do not include other merchants' adventurers
                System.out.println("✅ Loaded " + children.size() + " adventurers from Firebase");
            } else {
                System.out.println("ℹ️ No adventurers found in Firebase - this is normal for new accounts");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not load adventurers from Firebase: " + e.getMessage());
            // Keep empty list - no mock data
        }
    }
    
    @Override
    protected void initializeUI() {
        // Create a StackPane as the root to properly handle background
        StackPane backgroundContainer = new StackPane();
        backgroundContainer.setPadding(new Insets(0));
        backgroundContainer.setAlignment(Pos.CENTER);
        
        // Create background ImageView for subtle pattern
        ImageView backgroundImageView = createBackgroundImageView(backgroundContainer);
        
        // Add semi-transparent overlay for better contrast
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.15);");
        overlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setMouseTransparent(true);
        
        // Create the main BorderPane for layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("parent-dashboard");
        mainLayout.setStyle("-fx-background-color: transparent;");
        mainLayout.setPadding(new Insets(4, 4, 4, 4));
        
        // Create main sections
        createTopBar();
        createSidebar();
        createMainContent();
        
        // Set up layout with proper spacing
        VBox topSection = new VBox();
        topSection.getChildren().add(topBar.getRoot());
        topSection.setPadding(new Insets(0, 0, 8, 0)); // Add bottom padding to prevent overlap
        
        // Add margin to sidebar to prevent overlap with header
        VBox sidebarSection = new VBox();
        sidebarSection.getChildren().add(sidebar.getRoot());
        sidebarSection.setPadding(new Insets(8, 0, 0, 0)); // Add top padding
        
        mainLayout.setTop(topSection);
        mainLayout.setLeft(sidebarSection);
        mainLayout.setCenter(contentScrollPane);
        
        // Add all layers to background container
        backgroundContainer.getChildren().addAll(backgroundImageView, overlay, mainLayout);
        
        root = backgroundContainer;
        
        // Apply parent-specific theme
        applyParentTheme();

        // Begin header auto-refresh for pending reviews/notifications
        startAutoRefresh();
    }
    
    private ImageView createBackgroundImageView(StackPane container) {
        try {
            // Use the new parent dashboard background image
            Image backgroundImage = new Image(
                getClass().getResourceAsStream("/images/WOF_DonegalBG.gif"),
                800, 450, true, true
            );
            
            ImageView imageView = new ImageView(backgroundImage);
            imageView.setOpacity(0.3); // More visible for the parent dashboard
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
    
    private void createTopBar() {
        topBar = new ParentTopBar(currentUser, () -> navigateToSection("tasks"));
    }
    
    private void createSidebar() {
        sidebar = new ParentSidebar(currentUser, this::navigateToSection);
    }
    
    private void createMainContent() {
        mainContent = new VBox(24);
        mainContent.setPadding(new Insets(16));
        mainContent.setAlignment(Pos.TOP_CENTER);
        // Remove maxWidth constraint to allow full width usage
        mainContent.setMaxWidth(Double.MAX_VALUE);
        
        // Create scroll pane for content
        contentScrollPane = new ScrollPane(mainContent);
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Remove extra wrapper that might cause width issues
        contentScrollPane.setContent(mainContent);
        
        // Show overview by default
        showOverviewContent();
    }
    
    private void showOverviewContent() {
        mainContent.getChildren().clear();
        
        // Welcome section
        VBox welcomeSection = createWelcomeSection();
        
        // Children monitoring cards
        VBox childrenSection = createChildrenSection();
        
        // Quick stats
        VBox quickStats = createQuickStatsSection();

        // Pending reviews panel
        VBox pendingReviews = createPendingReviewsSection();
        
        mainContent.getChildren().addAll(welcomeSection, childrenSection, quickStats, pendingReviews);
    }
    
    private VBox createWelcomeSection() {
        VBox welcomeSection = new VBox(12);
        welcomeSection.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Merchant's Adventure Hub");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Guide your adventurers' quests, monitor progress, and celebrate achievements together");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        welcomeSection.getChildren().addAll(titleLabel, subtitleLabel);
        return welcomeSection;
    }
    
    private VBox createChildrenSection() {
        VBox childrenSection = new VBox(16);
        
        Label sectionTitle = new Label("Your Adventurers' Progress");
        sectionTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Add New Adventurer button
        HBox headerRow = new HBox(16);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addAdventurerBtn = createAddAdventurerButton();
        headerRow.getChildren().addAll(sectionTitle, spacer, addAdventurerBtn);
        
        // Create grid layout for 3 adventurers per row
        VBox adventurerGrid = new VBox(16);
        adventurerGrid.setAlignment(Pos.CENTER);
        
        if (children != null && !children.isEmpty()) {
            // Group adventurers in rows of 3
            for (int i = 0; i < children.size(); i += 3) {
                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER);
                
                // Add up to 3 adventurers per row
                for (int j = i; j < Math.min(i + 3, children.size()); j++) {
                    User child = children.get(j);
                    ChildMonitorCard card = new ChildMonitorCard(child);
                    row.getChildren().add(card.getRoot());
                }
                
                adventurerGrid.getChildren().add(row);
            }
        }
        
        // If no children, show placeholder
        if (children == null || children.isEmpty()) {
            VBox placeholder = createChildrenPlaceholder();
            adventurerGrid.getChildren().add(placeholder);
        }
        
        childrenSection.getChildren().addAll(headerRow, adventurerGrid);
        return childrenSection;
    }
    
    private VBox createChildrenPlaceholder() {
        VBox placeholder = new VBox(16);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setPadding(new Insets(40));
        placeholder.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        Label iconLabel = new Label("👨‍👩‍👧‍👦");
        iconLabel.setStyle("-fx-font-size: 48px;");
        
        Label titleLabel = new Label("No Adventurers Added Yet");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label descLabel = new Label("Add your adventurers' accounts to start monitoring their quest progress");
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        descLabel.setWrapText(true);
        
        Button addFirstAdventurerBtn = createAddAdventurerButton();
        addFirstAdventurerBtn.setPrefWidth(250);
        addFirstAdventurerBtn.setText("⚔️ Add Your First Adventurer");
        
        placeholder.getChildren().addAll(iconLabel, titleLabel, descLabel, addFirstAdventurerBtn);
        return placeholder;
    }
    
    private VBox createQuickStatsSection() {
        VBox statsSection = new VBox(16);
        
        Label sectionTitle = new Label("Quick Family Stats");
        sectionTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        HBox statsCards = new HBox(16);
        statsCards.setAlignment(Pos.CENTER);
        
        // Calculate real statistics from adventurer data
        int totalEarnings = calculateTotalEarnings();
        int totalTasks = calculateTotalTasks();
        int familyStreak = calculateFamilyStreak();
        int totalAchievements = calculateTotalAchievements();
        
        VBox totalEarningsCard = createStatCard("💰", "Total Adventure Earnings", totalEarnings + " SmartCoins", "#4CAF50");
        VBox tasksCompletedCard = createStatCard("✅", "Quests Completed", totalTasks + " completed", "#2196F3");
        VBox streakCard = createStatCard("🔥", "Adventure Streak", familyStreak + " days", "#FA8A00");
        VBox achievementsCard = createStatCard("🏆", "Achievements", totalAchievements + " unlocked", "#9C27B0");
        
        statsCards.getChildren().addAll(totalEarningsCard, tasksCompletedCard, streakCard, achievementsCard);
        
        statsSection.getChildren().addAll(sectionTitle, statsCards);
        return statsSection;
    }
    
    private VBox createStatCard(String icon, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 32px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        valueLabel.setWrapText(true);
        
        card.getChildren().addAll(iconLabel, titleLabel, valueLabel);
        return card;
    }
    
    /**
     * Apply parent-specific theme styling
     */
    private void applyParentTheme() {
        root.setStyle(
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            // Professional gradient background
            "-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB, #90CAF9);"
        );
    }
    
    @Override
    public void navigateToSection(String section) {
        this.currentSection = section;
        sidebar.setActiveSection(section);
        
        switch (section.toLowerCase()) {
            case "overview" -> showOverviewContent();
            case "children" -> showChildrenContent();
            case "tasks" -> showTasksContent();
            case "shop" -> showShopManagementContent();
            case "analytics" -> showAnalyticsContent();
            case "messaging" -> showMessagingContent();
            case "settings" -> showSettingsContent();
            default -> System.out.println("Unknown parent section: " + section);
        }
    }
    
    private void showChildrenContent() {
        mainContent.getChildren().clear();
        
        Label titleLabel = new Label("Adventurer Management");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Detailed children view with 3 adventurers per row
        VBox childrenGrid = new VBox(16);
        if (children != null && !children.isEmpty()) {
            // Group adventurers in rows of 3
            for (int i = 0; i < children.size(); i += 3) {
                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER);
                
                // Add up to 3 adventurers per row
                for (int j = i; j < Math.min(i + 3, children.size()); j++) {
                    User child = children.get(j);
                    ChildMonitorCard card = new ChildMonitorCard(child);
                    row.getChildren().add(card.getRoot());
                }
                
                childrenGrid.getChildren().add(row);
            }
        } else {
            // Show placeholder if no adventurers
            VBox placeholder = createChildrenPlaceholder();
            childrenGrid.getChildren().add(placeholder);
        }
        
        mainContent.getChildren().addAll(titleLabel, childrenGrid);
    }
    
    private void showTasksContent() {
        mainContent.getChildren().clear();
        
        // Create and display the task management page
        TaskManagementPage taskManagementPage = new TaskManagementPage(currentUser, children);
        mainContent.getChildren().add(taskManagementPage.getRoot());
    }
    
    private void showAnalyticsContent() {
        mainContent.getChildren().clear();
        
        analytics = new FamilyAnalytics(currentUser);
        mainContent.getChildren().add(analytics.getRoot());
    }

    private void showMessagingContent() {
        mainContent.getChildren().clear();
        com.coincraft.ui.components.parent.MessagingHubPage messaging =
            new com.coincraft.ui.components.parent.MessagingHubPage(currentUser, children);
        mainContent.getChildren().add(messaging.getRoot());
    }
    
    private void showSettingsContent() {
        mainContent.getChildren().clear();
        
        // Create and display the comprehensive settings page
        SettingsPage settingsPage = new SettingsPage(currentUser, children);
        mainContent.getChildren().add(settingsPage.getRoot());
    }
    
    private Button createAddAdventurerButton() {
        Button addBtn = new Button("⚔️ Add New Adventurer");
        addBtn.setPrefWidth(200);
        addBtn.setPrefHeight(40);
        addBtn.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
        );
        
        addBtn.setOnMouseEntered(e -> {
            addBtn.setStyle(
                "-fx-background-color: #E67E00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        addBtn.setOnMouseExited(e -> {
            addBtn.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
            );
        });
        
        addBtn.setOnAction(e -> {
            // Get the current stage
            Stage currentStage = (Stage) root.getScene().getWindow();
            
            // Open the Add Adventurer dialog with current parent context
            AddAdventurerDialog dialog = new AddAdventurerDialog(currentStage, this::onAdventurerAdded, currentUser);
            dialog.show();
        });
        
        return addBtn;
    }
    
    private void onAdventurerAdded(User newAdventurer) {
        // Ensure parent linkage is set
        if (newAdventurer != null) {
            if (newAdventurer.getParentId() == null && currentUser != null) {
                newAdventurer.setParentId(currentUser.getUserId());
            }
            if (children == null) {
                children = new ArrayList<>();
            }
            // Add to in-memory list if it belongs to this parent and not already present
            boolean alreadyExists = children.stream()
                    .anyMatch(u -> u.getUserId() != null && u.getUserId().equals(newAdventurer.getUserId()));
            if (!alreadyExists && currentUser != null && newAdventurer.getParentId() != null &&
                newAdventurer.getParentId().equals(currentUser.getUserId())) {
                children.add(newAdventurer);
            }
        }

        // Refresh data from storage to get the latest state
        loadAdventurersFromFirebase();
        
        // Refresh the current view
        if (currentSection.equals("overview")) {
            showOverviewContent();
        } else if (currentSection.equals("children")) {
            showChildrenContent();
        }
        
        // Update top bar stats
        topBar.updateActiveChildren(children != null ? children.size() : 0);
        
        System.out.println("✅ Adventurer added and data refreshed from Firebase: " + 
                          (newAdventurer != null ? newAdventurer.getName() : "Unknown"));
    }
    
    /**
     * Calculate total SmartCoins earned by all adventurers
     */
    private int calculateTotalEarnings() {
        if (children == null || children.isEmpty()) {
            return 0;
        }
        
        return children.stream()
                .mapToInt(User::getSmartCoinBalance)
                .sum();
    }
    
    /**
     * Calculate total tasks completed by all adventurers
     */
    private int calculateTotalTasks() {
        if (children == null || children.isEmpty()) {
            return 0;
        }
        
        // For now, estimate based on SmartCoins (each task gives ~20 coins)
        int totalCoins = calculateTotalEarnings();
        return totalCoins / 20; // Rough estimate
    }
    
    /**
     * Calculate family adventure streak
     */
    private int calculateFamilyStreak() {
        if (children == null || children.isEmpty()) {
            return 0;
        }
        
        // Use the highest streak among all adventurers
        return children.stream()
                .mapToInt(User::getDailyStreaks)
                .max()
                .orElse(0);
    }
    
    /**
     * Calculate total achievements unlocked
     */
    private int calculateTotalAchievements() {
        if (children == null || children.isEmpty()) {
            return 0;
        }
        
        // Estimate achievements based on level and coins
        return children.stream()
                .mapToInt(child -> child.getLevel() + (child.getSmartCoinBalance() / 50))
                .sum();
    }
    
    /**
     * Load tasks for notification calculation
     */
    private void loadTasksForNotifications() {
        try {
            FirebaseService firebaseService = FirebaseService.getInstance();
            allTasks = firebaseService.loadAllTasks();
            // Filter only tasks that belong to this parent's children or ALL_ADVENTURERS
            if (children != null && !children.isEmpty()) {
                java.util.Set<String> childIds = new java.util.HashSet<>();
                for (User c : children) {
                    if (c != null && c.getUserId() != null) childIds.add(c.getUserId());
                }
                java.util.List<Task> filtered = new java.util.ArrayList<>();
                for (Task t : allTasks) {
                    String assignedTo = t.getAssignedTo();
                    if (assignedTo == null || assignedTo.isEmpty() || "ALL_ADVENTURERS".equals(assignedTo) || childIds.contains(assignedTo)) {
                        filtered.add(t);
                    }
                }
                allTasks = filtered;
            }
            System.out.println("📋 Loaded " + allTasks.size() + " tasks for notification calculation");
        } catch (Exception e) {
            System.out.println("⚠️ Could not load tasks for notifications: " + e.getMessage());
            allTasks = new ArrayList<>();
        }
    }
    
    /**
     * Update notification count based on pending tasks
     */
    private void updateNotificationCount() {
        if (allTasks == null || allTasks.isEmpty()) {
            topBar.updatePendingTasks(0);
            return;
        }
        
        // Count tasks that need parent review/approval
        int pendingCount = (int) allTasks.stream()
                .filter(task -> task.getValidationStatus() == com.coincraft.models.ValidationStatus.AWAITING_APPROVAL)
                .count();
        
        // Also count any unread messages or other notifications
        // For now, we'll focus on task-related notifications
        
        topBar.updatePendingTasks(pendingCount);
        System.out.println("🔔 Updated notification count: " + pendingCount + " pending tasks");
    }

    /**
     * Start periodic refresh of tasks/notifications so the header stays up to date
     */
    private void startAutoRefresh() {
        try {
            if (autoRefreshTimeline != null) {
                autoRefreshTimeline.stop();
            }
            autoRefreshTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(5), e -> {
                    try {
                        // Reload tasks and update counts
                        loadTasksForNotifications();
                        updateNotificationCount();
                    } catch (Exception ex) {
                        System.out.println("⚠️ Auto refresh failed: " + ex.getMessage());
                    }
                })
            );
            autoRefreshTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
            autoRefreshTimeline.play();
            // Trigger an immediate refresh so the header updates right away
            loadTasksForNotifications();
            updateNotificationCount();
        } catch (Exception e) {
            System.out.println("⚠️ Could not start auto refresh: " + e.getMessage());
        }
    }

    /**
     * Small overview panel listing pending reviews and recent approvals
     */
    private VBox createPendingReviewsSection() {
        VBox section = new VBox(12);
        section.setAlignment(Pos.TOP_LEFT);
        section.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #e5e7eb;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);"
        );
        Label title = new Label("Pending Task Reviews");
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );

        VBox list = new VBox(8);
        int shown = 0;
        if (allTasks != null) {
            for (Task t : allTasks) {
                if (t.getValidationStatus() == com.coincraft.models.ValidationStatus.AWAITING_APPROVAL) {
                    Label item = new Label("⏳ " + t.getTitle());
                    item.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151;");
                    list.getChildren().add(item);
                    shown++;
                    if (shown >= 5) break;
                }
            }
        }
        if (shown == 0) {
            Label empty = new Label("No tasks pending review.");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #6b7280;");
            list.getChildren().add(empty);
        }

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        Button openTasks = new Button("Review Tasks");
        openTasks.setOnAction(e -> navigateToSection("tasks"));
        openTasks.setStyle(
            "-fx-background-color: #FA8A00; -fx-text-fill: white; -fx-font-weight: 700; -fx-background-radius: 8; -fx-padding: 8 12;"
        );
        actions.getChildren().add(openTasks);

        section.getChildren().addAll(title, list, actions);
        return section;
    }
    
    private void showShopManagementContent() {
        mainContent.getChildren().clear();
        
        Label titleLabel = new Label("🛒 Shop Management");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Create and manage products that children can purchase with SmartCoins");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Header section
        VBox headerSection = new VBox(8);
        headerSection.setAlignment(Pos.CENTER_LEFT);
        headerSection.setPadding(new Insets(0, 0, 20, 0));
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Add Product Button
        Button addProductButton = new Button("➕ Add New Product");
        addProductButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-padding: 12 24;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        addProductButton.setOnAction(e -> openAddProductDialog());
        
        // Products Grid
        VBox productsGrid = createProductsGrid();
        
        // Layout
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(1200);
        content.getChildren().addAll(headerSection, addProductButton, productsGrid);
        
        mainContent.getChildren().add(content);
    }
    
    private VBox createProductsGrid() {
        VBox grid = new VBox(16);
        grid.setAlignment(Pos.TOP_CENTER);
        
        // Load products for this parent
        List<Product> products = FirebaseService.getInstance().loadProductsByParent(currentUser.getId());
        
        if (products.isEmpty()) {
            VBox emptyState = new VBox(16);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(40, 20, 40, 20));
            
            Label emptyIcon = new Label("🛒");
            emptyIcon.setStyle("-fx-font-size: 48px;");
            
            Label emptyTitle = new Label("No Products Created Yet");
            emptyTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #374151;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
            );
            
            Label emptySubtitle = new Label("Create your first product to start the shop!");
            emptySubtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #6B7280;" +
                "-fx-font-family: 'Segoe UI', sans-serif;"
            );
            
            emptyState.getChildren().addAll(emptyIcon, emptyTitle, emptySubtitle);
            grid.getChildren().add(emptyState);
        } else {
            // Create product cards in rows of 3
            for (int i = 0; i < products.size(); i += 3) {
                HBox row = new HBox(20);
                row.setAlignment(Pos.CENTER);
                
                for (int j = i; j < Math.min(i + 3, products.size()); j++) {
                    Product product = products.get(j);
                    ProductCard productCard = new ProductCard(
                        product,
                        "✏️ Edit", this::openEditProductDialog,
                        product.isActive() ? "⏸️ Deactivate" : "▶️ Activate", this::toggleProductStatus,
                        "🗑️ Delete", this::deleteProduct
                    );
                    row.getChildren().add(productCard);
                }
                
                // Fill remaining space if needed
                while (row.getChildren().size() < 3) {
                    Region spacer = new Region();
                    spacer.setPrefWidth(300);
                    row.getChildren().add(spacer);
                }
                
                grid.getChildren().add(row);
            }
        }
        
        return grid;
    }
    
    
    private void openAddProductDialog() {
        AddProductDialog dialog = new AddProductDialog(currentUser.getId());
        dialog.showAndWait().ifPresent(product -> {
            // Product was saved in the dialog
            showShopManagementContent(); // Refresh the view
        });
    }
    
    private void openEditProductDialog(Product product) {
        EditProductDialog dialog = new EditProductDialog(product);
        dialog.showAndWait().ifPresent(updatedProduct -> {
            // Product was updated in the dialog
            showShopManagementContent(); // Refresh the view
        });
    }
    
    private void toggleProductStatus(Product product) {
        product.setActive(!product.isActive());
        FirebaseService.getInstance().updateProduct(product);
        showShopManagementContent(); // Refresh the view
    }
    
    private void deleteProduct(Product product) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Product");
        confirmationAlert.setHeaderText("Are you sure you want to delete this product?");
        confirmationAlert.setContentText(
            "Product: " + product.getName() + "\n\n" +
            "This action cannot be undone. The product will be permanently removed " +
            "and children will no longer be able to purchase it."
        );
        
        // Style the confirmation dialog
        confirmationAlert.getDialogPane().setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Style the buttons
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);
        
        Button deleteButton = (Button) confirmationAlert.getDialogPane().lookupButton(deleteButtonType);
        Button cancelButton = (Button) confirmationAlert.getDialogPane().lookupButton(cancelButtonType);
        
        deleteButton.setStyle(
            "-fx-background-color: #ef4444;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 8 16;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        cancelButton.setStyle(
            "-fx-background-color: #6b7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 8 16;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == deleteButtonType) {
                FirebaseService.getInstance().deleteProduct(product.getId());
                showShopManagementContent(); // Refresh the view
            }
        });
    }
    
}
