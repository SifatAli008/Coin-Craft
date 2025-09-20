package com.coincraft.ui.dashboards;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.components.parent.AddAdventurerDialog;
import com.coincraft.ui.components.parent.ChildMonitorCard;
import com.coincraft.ui.components.parent.FamilyAnalytics;
import com.coincraft.ui.components.parent.ParentSidebar;
import com.coincraft.ui.components.parent.ParentTopBar;
import com.coincraft.ui.components.parent.SettingsPage;
import com.coincraft.ui.components.parent.TaskManagementPage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    
    // Real adventurer data from Firebase
    private List<User> children;
    
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
        }
    }
    
    private void loadRealData() {
        children = new ArrayList<>();
        
        // Load real adventurer data from Firebase
        loadAdventurersFromFirebase();
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
                        // In a real app, check if this child belongs to current merchant
                        // For now, add all children (you can implement parent-child relationship later)
                        children.add(user);
                        System.out.println("🔍 DEBUG: Added adventurer: " + user.getName());
                    }
                }
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
        overlay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05);");
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
        topBar = new ParentTopBar(currentUser);
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
        
        mainContent.getChildren().addAll(welcomeSection, childrenSection, quickStats);
    }
    
    private VBox createWelcomeSection() {
        VBox welcomeSection = new VBox(12);
        welcomeSection.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("⚔️ Merchant's Adventure Hub");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Guide your adventurers' quests, monitor progress, and celebrate achievements together");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label descLabel = new Label("Add your adventurers' accounts to start monitoring their quest progress");
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
        VBox streakCard = createStatCard("🔥", "Adventure Streak", familyStreak + " days", "#FF9800");
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
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
            case "analytics" -> showAnalyticsContent();
            case "settings" -> showSettingsContent();
            default -> System.out.println("Unknown parent section: " + section);
        }
    }
    
    private void showChildrenContent() {
        mainContent.getChildren().clear();
        
        Label titleLabel = new Label("⚔️ Adventurer Management");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
        );
        
        addBtn.setOnMouseEntered(e -> {
            addBtn.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        addBtn.setOnMouseExited(e -> {
            addBtn.setStyle(
                "-fx-background-color: #4CAF50;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
            );
        });
        
        addBtn.setOnAction(e -> {
            // Get the current stage
            Stage currentStage = (Stage) root.getScene().getWindow();
            
            // Open the Add Adventurer dialog
            AddAdventurerDialog dialog = new AddAdventurerDialog(currentStage, this::onAdventurerAdded);
            dialog.show();
        });
        
        return addBtn;
    }
    
    private void onAdventurerAdded(User newAdventurer) {
        // Refresh data from Firebase to get the latest state
        loadAdventurersFromFirebase();
        
        // Refresh the current view
        if (currentSection.equals("overview")) {
            showOverviewContent();
        } else if (currentSection.equals("children")) {
            showChildrenContent();
        }
        
        // Update top bar stats
        topBar.updateActiveChildren(children.size());
        
        System.out.println("✅ Adventurer added and data refreshed from Firebase: " + newAdventurer.getName());
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
}
