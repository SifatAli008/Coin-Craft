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
import com.coincraft.ui.components.child.TaskCardList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Child Dashboard - Main interface for young adventurers
 * Features gamified learning, avatar customization, and progress tracking
 * Follows the established game theme with Minecraft fonts and gaming aesthetics
 */
public class ChildDashboard extends BaseDashboard {
    
    // Main UI Components
    private ChildTopBar topBar;
    private LevelProgressWidget levelProgress;
    private TaskCardList taskList;
    private BadgesStrip badgesStrip;
    private DailyStreakCalendar streakCalendar;
    private ChildLeaderboard leaderboard;
    private EventBanner eventBanner;
    private ChildSidebar sidebar;
    
    // Content sections
    private VBox mainContent;
    private String currentSection = "home";
    
    public ChildDashboard(User user) {
        super(user);
    }
    
    @Override
    protected void initializeUI() {
        root = new BorderPane();
        root.getStyleClass().add("child-dashboard");
        
        // Create main sections
        createTopSection();
        createSidebar();
        createMainContent();
        
        // Set up layout with sidebar
        root.setTop(topBar.getRoot());
        root.setLeft(sidebar.getRoot());
        root.setCenter(mainContent);
        
        // Apply child-specific styling
        applyChildTheme();
    }
    
    /**
     * Create the top bar with avatar, welcome message, and coin balance
     */
    private void createTopSection() {
        topBar = new ChildTopBar(currentUser);
    }
    
    /**
     * Create the main content area with perfect space utilization
     */
    private void createMainContent() {
        mainContent = new VBox(6);
        mainContent.setPadding(new Insets(5, 0, 5, 0));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        // Compact Level Progress Section
        levelProgress = new LevelProgressWidget(currentUser);
        HBox levelContainer = new HBox();
        levelContainer.setAlignment(Pos.CENTER);
        levelContainer.setPadding(new Insets(0, 0, 5, 0));
        levelContainer.getChildren().add(levelProgress.getRoot());
        
        // Main dashboard with absolute width control
        HBox mainRow = new HBox(8);
        mainRow.setAlignment(Pos.TOP_CENTER);
        mainRow.setPadding(new Insets(0, 5, 0, 5));
        mainRow.setPrefWidth(1100);
        mainRow.setMaxWidth(1100);
        
        // Left column - Tasks and Progress (fixed width for 3 quest cards)
        VBox leftColumn = new VBox(8);
        leftColumn.setPrefWidth(640);
        leftColumn.setMaxWidth(640);
        leftColumn.setMinWidth(640);
        
        taskList = new TaskCardList(currentUser);
        badgesStrip = new BadgesStrip(currentUser);
        
        leftColumn.getChildren().addAll(
            createSectionCard("🗺️ Active Quests", taskList.getRoot()),
            createSectionCard("🏆 Badge Collection", badgesStrip.getRoot())
        );
        
        // Right column - Social and Events (fixed width to fill remaining space)
        VBox rightColumn = new VBox(8);
        rightColumn.setPrefWidth(452);
        rightColumn.setMaxWidth(452);
        rightColumn.setMinWidth(452);
        rightColumn.setAlignment(Pos.TOP_LEFT);
        
        streakCalendar = new DailyStreakCalendar(currentUser);
        leaderboard = new ChildLeaderboard(currentUser);
        eventBanner = new EventBanner(currentUser);
        
        rightColumn.getChildren().addAll(
            createCompactSectionCard("🔥 Daily Streak", streakCalendar.getRoot()),
            createCompactSectionCard("🏅 Leaderboard", leaderboard.getRoot()),
            createCompactSectionCard("🎉 Special Events", eventBanner.getRoot())
        );
        
        mainRow.getChildren().addAll(leftColumn, rightColumn);
        HBox.setHgrow(leftColumn, Priority.NEVER);
        HBox.setHgrow(rightColumn, Priority.NEVER);
        
        // Centered Request Mission Button
        Button requestMissionBtn = createRequestMissionButton();
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(5, 0, 0, 0));
        buttonContainer.getChildren().add(requestMissionBtn);
        
        mainContent.getChildren().addAll(
            levelContainer,
            mainRow,
            buttonContainer
        );
    }
    
    /**
     * Create the sidebar navigation
     */
    private void createSidebar() {
        sidebar = new ChildSidebar(currentUser, this::navigateToSection);
    }
    
    /**
     * Create a compact standardized section card
     */
    private VBox createSectionCard(String title, javafx.scene.Node content) {
        VBox card = createGameCard();
        
        // Compact title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        card.getChildren().addAll(titleLabel, content);
        return card;
    }
    
    /**
     * Create a maximum width section card for right column (452px)
     */
    private VBox createCompactSectionCard(String title, javafx.scene.Node content) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(452);
        card.setMaxWidth(452);
        card.setMinWidth(452);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 255, 255, 0.7);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 6);" +
            "-fx-padding: 15;"
        );
        
        // Maximum title for 452px card
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        titleLabel.setMaxWidth(432);
        
        card.getChildren().addAll(titleLabel, content);
        return card;
    }
    
    /**
     * Create the compact Request Mission button (level-gated feature)
     */
    private Button createRequestMissionButton() {
        Button requestBtn = createGameButton("🚀 REQUEST MISSION", "#4CAF50", "#388E3C");
        requestBtn.setPrefWidth(160);
        
        // Check if feature is available based on level
        if (!hasLevelAccess("request_mission", 5)) {
            requestBtn.setDisable(true);
            requestBtn.setText("🔒 LEVEL 5 UNLOCK");
            requestBtn.setStyle(
                "-fx-background-color: #9E9E9E;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
            );
        } else {
            requestBtn.setOnAction(e -> showRequestMissionDialog());
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
    
    /**
     * Apply child-specific theme styling
     */
    private void applyChildTheme() {
        root.setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            // Bright, cheerful gradient background
            "-fx-background-color: linear-gradient(to bottom right, #87CEEB, #98FB98, #F0E68C);"
        );
    }
    
    @Override
    public void navigateToSection(String section) {
        this.currentSection = section;
        
        // Update sidebar navigation
        sidebar.setActiveSection(section);
        
        // Handle section-specific logic
        switch (section.toLowerCase()) {
            case "home":
                // Already showing home content
                break;
                
            case "tasks":
                // Highlight tasks section
                taskList.refresh();
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
                showShopInterface();
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
     * Show shop interface
     */
    private void showShopInterface() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Adventure Shop");
        dialog.setHeaderText("🛒 SmartCoin Shop");
        dialog.setContentText(
            "Welcome to the Adventure Shop!\n\n" +
            "Spend your hard-earned SmartCoins on:\n" +
            "• Cool avatar accessories\n" +
            "• Special badges\n" +
            "• Adventure tools\n" +
            "• Learning modules\n\n" +
            "Shop features coming soon!"
        );
        
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
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
