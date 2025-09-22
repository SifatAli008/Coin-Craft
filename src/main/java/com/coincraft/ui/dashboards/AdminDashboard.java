package com.coincraft.ui.dashboards;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.coincraft.models.User;
import com.coincraft.ui.components.CentralizedMusicController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Admin Dashboard - Full system administration interface
 * Features user management, content management, and system analytics
 * Follows the established game theme with administrative styling
 */
public class AdminDashboard extends BaseDashboard {
    private BorderPane mainContent;
    private TabPane mainTabPane;
    
    // Statistics
    private Label totalUsersLabel;
    private Label activeUsersLabel;
    private Label totalTasksLabel;
    private Label completedTasksLabel;
    private Label revenueLabel;
    private Label growthLabel;
    
    // Charts
    private PieChart userRoleChart;
    private LineChart<String, Number> activityChart;
    private BarChart<String, Number> taskCompletionChart;
    
    // Tables
    private TableView<UserData> userTable;
    private TableView<TaskData> taskTable;
    private TableView<SystemLogData> logTable;
    
    public AdminDashboard(User user) {
        super(user);
        System.out.println("Creating AdminDashboard for user: " + user.getName() + " with role: " + user.getRole());
        System.out.println("AdminDashboard created successfully");
    }
    
    @Override
    protected void initializeUI() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("admin-dashboard");
        root = borderPane;
        
        System.out.println("Initializing clean admin dashboard UI");
        
        // Initialize main content
        mainContent = new BorderPane();
        mainTabPane = new TabPane();
        
        // Initialize statistics labels with clean state
        totalUsersLabel = new Label("0");
        activeUsersLabel = new Label("0");
        totalTasksLabel = new Label("0");
        completedTasksLabel = new Label("0");
        revenueLabel = new Label("$0.00");
        growthLabel = new Label("0.0%");
        
        // Initialize charts
        setupCharts();
        
        // Initialize tables
        setupTables();
        
        // Setup layout
        setupLayout();
        
        // Load clean data
        loadCleanData();
        
        // Apply admin theme
        applyAdminTheme();
    }
    
    private void setupCharts() {
        // User Role Distribution Chart
        userRoleChart = new PieChart();
        userRoleChart.setTitle("User Role Distribution");
        userRoleChart.setPrefSize(300, 250);
        
        // User Activity Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Active Users");
        activityChart = new LineChart<>(xAxis, yAxis);
        activityChart.setTitle("Daily Active Users (Last 7 Days)");
        activityChart.setPrefSize(400, 250);
        
        // Task Completion Chart
        CategoryAxis taskXAxis = new CategoryAxis();
        NumberAxis taskYAxis = new NumberAxis();
        taskXAxis.setLabel("Task Type");
        taskYAxis.setLabel("Completions");
        taskCompletionChart = new BarChart<>(taskXAxis, taskYAxis);
        taskCompletionChart.setTitle("Task Completion by Type");
        taskCompletionChart.setPrefSize(400, 250);
    }
    
    private void setupTables() {
        // User Management Table
        userTable = new TableView<>();
        TableColumn<UserData, String> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userIdCol.setPrefWidth(100);
        
        TableColumn<UserData, String> userNameCol = new TableColumn<>("Name");
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        userNameCol.setPrefWidth(150);
        
        TableColumn<UserData, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);
        
        TableColumn<UserData, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);
        
        TableColumn<UserData, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        
        TableColumn<UserData, String> lastActiveCol = new TableColumn<>("Last Active");
        lastActiveCol.setCellValueFactory(new PropertyValueFactory<>("lastActive"));
        lastActiveCol.setPrefWidth(150);
        
        userTable.getColumns().addAll(userIdCol, userNameCol, emailCol, roleCol, statusCol, lastActiveCol);
        
        // Task Management Table
        taskTable = new TableView<>();
        TableColumn<TaskData, String> taskIdCol = new TableColumn<>("Task ID");
        taskIdCol.setCellValueFactory(new PropertyValueFactory<>("taskId"));
        taskIdCol.setPrefWidth(100);
        
        TableColumn<TaskData, String> taskNameCol = new TableColumn<>("Task Name");
        taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        taskNameCol.setPrefWidth(200);
        
        TableColumn<TaskData, String> taskTypeCol = new TableColumn<>("Type");
        taskTypeCol.setCellValueFactory(new PropertyValueFactory<>("taskType"));
        taskTypeCol.setPrefWidth(120);
        
        TableColumn<TaskData, Integer> completionsCol = new TableColumn<>("Completions");
        completionsCol.setCellValueFactory(new PropertyValueFactory<>("completions"));
        completionsCol.setPrefWidth(100);
        
        TableColumn<TaskData, String> difficultyCol = new TableColumn<>("Difficulty");
        difficultyCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        difficultyCol.setPrefWidth(100);
        
        taskTable.getColumns().addAll(taskIdCol, taskNameCol, taskTypeCol, completionsCol, difficultyCol);
        
        // System Log Table
        logTable = new TableView<>();
        TableColumn<SystemLogData, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        timestampCol.setPrefWidth(150);
        
        TableColumn<SystemLogData, String> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        levelCol.setPrefWidth(80);
        
        TableColumn<SystemLogData, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageCol.setPrefWidth(400);
        
        TableColumn<SystemLogData, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));
        sourceCol.setPrefWidth(150);
        
        logTable.getColumns().addAll(timestampCol, levelCol, messageCol, sourceCol);
    }
    
    private void setupLayout() {
        // Header
        HBox header = createHeader();
        mainContent.setTop(header);
        
        // Main content with tabs
        setupMainTabs();
        mainContent.setCenter(mainTabPane);
        
        // Footer
        HBox footer = createFooter();
        mainContent.setBottom(footer);
        
        // Add to root
        ((BorderPane) root).setCenter(mainContent);
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #2c3e50, #34495e);" +
            "-fx-border-color: #bdc3c7;" +
            "-fx-border-width: 0 0 1 0;"
        );
        
        // Title
        Text title = new Text("CoinCraft Admin Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);
        
        // Welcome message
        Text welcome = new Text("Welcome, " + currentUser.getName());
        welcome.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        welcome.setFill(Color.LIGHTGRAY);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Refresh button
        Button refreshButton = new Button("Refresh Data");
        refreshButton.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        refreshButton.setOnAction(e -> refreshData());
        
        // Logout button
        Button logoutButton = new Button("🚪 Logout");
        logoutButton.setStyle(
            "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        logoutButton.setOnMouseEntered(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #c0392b;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        logoutButton.setOnMouseExited(e -> {
            logoutButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 8 15 8 15;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            );
        });
        logoutButton.setOnAction(e -> handleAdminLogout());
        
        VBox titleBox = new VBox(5);
        titleBox.getChildren().addAll(title, welcome);
        
        // Music Controller for Admin
        CentralizedMusicController musicController = new CentralizedMusicController();
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(musicController.getRoot(), refreshButton, logoutButton);
        
        header.getChildren().addAll(titleBox, spacer, buttonBox);
        return header;
    }
    
    private void setupMainTabs() {
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Overview Tab
        Tab overviewTab = new Tab("Overview");
        overviewTab.setContent(createOverviewContent());
        
        // User Management Tab
        Tab userTab = new Tab("User Management");
        userTab.setContent(createUserManagementContent());
        
        // Task Management Tab
        Tab taskTab = new Tab("Task Management");
        taskTab.setContent(createTaskManagementContent());
        
        // Analytics Tab
        Tab analyticsTab = new Tab("Analytics");
        analyticsTab.setContent(createAnalyticsContent());
        
        // System Logs Tab
        Tab logsTab = new Tab("System Logs");
        logsTab.setContent(createSystemLogsContent());
        
        mainTabPane.getTabs().addAll(overviewTab, userTab, taskTab, analyticsTab, logsTab);
    }
    
    private ScrollPane createOverviewContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Statistics Cards - Enhanced with more metrics
        VBox statsContainer = new VBox(15);
        
        // Top row stats
        HBox topStatsRow = new HBox(15);
        topStatsRow.getChildren().addAll(
            createEnhancedStatCard("Total Users", totalUsersLabel, "#3498db", "👥", "Ready for users"),
            createEnhancedStatCard("Active Today", activeUsersLabel, "#2ecc71", "🟢", "No users yet"),
            createEnhancedStatCard("Total Tasks", totalTasksLabel, "#f39c12", "📋", "Ready for tasks")
        );
        
        // Bottom row stats
        HBox bottomStatsRow = new HBox(15);
        bottomStatsRow.getChildren().addAll(
            createEnhancedStatCard("Completed Tasks", completedTasksLabel, "#9b59b6", "✅", "No completions yet"),
            createEnhancedStatCard("Revenue", revenueLabel, "#e74c3c", "💰", "Ready to track"),
            createEnhancedStatCard("Growth Rate", growthLabel, "#1abc9c", "📈", "Awaiting data")
        );
        
        statsContainer.getChildren().addAll(topStatsRow, bottomStatsRow);
        
        // Quick Actions
        VBox quickActions = new VBox(10);
        Text quickActionsTitle = new Text("Quick Actions");
        quickActionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        quickActionsTitle.setFill(Color.WHITE);
        
        HBox actionButtons = new HBox(15);
        actionButtons.getChildren().addAll(
            createActionButton("Manage Users", "#3498db", e -> navigateToSection("users")),
            createActionButton("Manage Tasks", "#e67e22", e -> navigateToSection("tasks")),
            createActionButton("View Analytics", "#95a5a6", e -> navigateToSection("analytics")),
            createActionButton("Export Data", "#27ae60", e -> exportData())
        );
        
        quickActions.getChildren().addAll(quickActionsTitle, actionButtons);
        
        // Recent Activity
        VBox recentActivity = createRecentActivitySection();
        
        content.getChildren().addAll(statsContainer, quickActions, recentActivity);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }
    
    private VBox createEnhancedStatCard(String title, Label valueLabel, String color, String icon, String trend) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(250);
        card.setPrefHeight(140);
        card.setStyle(
            "-fx-background-color: linear-gradient(135deg, white 0%, #f8f9fa 100%);" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );
        
        // Header with icon and title
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Text iconText = new Text(icon);
        iconText.setFont(Font.font("Arial", 24));
        
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        titleText.setFill(Color.web("#6c757d"));
        
        header.getChildren().addAll(iconText, titleText);
        
        // Value
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web(color));
        
        // Trend indicator
        Text trendText = new Text(trend);
        trendText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        trendText.setFill(Color.web("#6c757d"));
        
        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        card.getChildren().addAll(header, valueLabel, spacer, trendText);
        
        return card;
    }
    
    private Button createActionButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setPrefHeight(40);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        button.setOnAction(handler);
        return button;
    }
    
    private VBox createRecentActivitySection() {
        VBox section = new VBox(10);
        
        Text title = new Text("Recent System Activity");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);
        
        ListView<String> activityList = new ListView<>();
        activityList.setPrefHeight(200);
        
        // Real recent activities - start with admin access only
        ObservableList<String> activities = FXCollections.observableArrayList(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " - Admin dashboard accessed by " + currentUser.getName(),
            LocalDateTime.now().minusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm")) + " - System initialized successfully",
            LocalDateTime.now().minusMinutes(2).format(DateTimeFormatter.ofPattern("HH:mm")) + " - Admin authentication successful"
        );
        
        activityList.setItems(activities);
        
        section.getChildren().addAll(title, activityList);
        return section;
    }
    
    private ScrollPane createUserManagementContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Text title = new Text("User Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        
        // Search and filter controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        searchField.setPrefWidth(200);
        
        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("All Roles", "STUDENT", "PARENT", "TEACHER", "ADMIN");
        roleFilter.setValue("All Roles");
        
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Active", "Inactive");
        statusFilter.setValue("All Status");
        
        Button addUserButton = new Button("Add New User");
        addUserButton.setStyle(
            "-fx-background-color: #27ae60;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 5;"
        );
        
        controls.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Role:"), roleFilter,
            new Label("Status:"), statusFilter,
            addUserButton
        );
        
        userTable.setPrefHeight(400);
        
        content.getChildren().addAll(title, controls, userTable);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private ScrollPane createTaskManagementContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Text title = new Text("Task Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        
        // Task controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        TextField taskSearchField = new TextField();
        taskSearchField.setPromptText("Search tasks...");
        taskSearchField.setPrefWidth(200);
        
        ComboBox<String> taskTypeFilter = new ComboBox<>();
        taskTypeFilter.getItems().addAll("All Types", "DIGITAL", "REAL_WORLD", "QUIZ", "CHALLENGE");
        taskTypeFilter.setValue("All Types");
        
        Button addTaskButton = new Button("Create New Task");
        addTaskButton.setStyle(
            "-fx-background-color: #e67e22;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 5;"
        );
        
        controls.getChildren().addAll(
            new Label("Search:"), taskSearchField,
            new Label("Type:"), taskTypeFilter,
            addTaskButton
        );
        
        taskTable.setPrefHeight(400);
        
        content.getChildren().addAll(title, controls, taskTable);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private ScrollPane createAnalyticsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Text title = new Text("Analytics Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        
        // Charts row 1
        HBox chartsRow1 = new HBox(20);
        chartsRow1.getChildren().addAll(userRoleChart, activityChart);
        
        // Charts row 2
        HBox chartsRow2 = new HBox(20);
        chartsRow2.getChildren().add(taskCompletionChart);
        
        content.getChildren().addAll(title, chartsRow1, chartsRow2);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private ScrollPane createSystemLogsContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Text title = new Text("System Logs");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.WHITE);
        
        // Log controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        ComboBox<String> logLevelFilter = new ComboBox<>();
        logLevelFilter.getItems().addAll("All Levels", "INFO", "WARN", "ERROR", "DEBUG");
        logLevelFilter.setValue("All Levels");
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Filter by date");
        
        Button clearLogsButton = new Button("Clear Logs");
        clearLogsButton.setStyle(
            "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 15 8 15;" +
            "-fx-background-radius: 5;"
        );
        
        controls.getChildren().addAll(
            new Label("Level:"), logLevelFilter,
            new Label("Date:"), datePicker,
            clearLogsButton
        );
        
        logTable.setPrefHeight(400);
        
        content.getChildren().addAll(title, controls, logTable);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setStyle(
            "-fx-background-color: #ecf0f1;" +
            "-fx-border-color: #bdc3c7;" +
            "-fx-border-width: 1 0 0 0;"
        );
        
        Label statusLabel = new Label("System Status: Online | Last Updated: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setTextFill(Color.GRAY);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label versionLabel = new Label("CoinCraft Admin v1.0.0");
        versionLabel.setFont(Font.font("Arial", 12));
        versionLabel.setTextFill(Color.GRAY);
        
        footer.getChildren().addAll(statusLabel, spacer, versionLabel);
        return footer;
    }
    
    private void loadCleanData() {
        // Initialize with clean, empty data
        System.out.println("Loading clean admin dashboard data");
        
        // Empty user table
        userTable.setItems(FXCollections.observableArrayList());
        
        // Empty task table
        taskTable.setItems(FXCollections.observableArrayList());
        
        // System logs with just admin access
        ObservableList<SystemLogData> logData = FXCollections.observableArrayList();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logData.add(new SystemLogData(timestamp, "INFO", 
            "Admin dashboard accessed by " + currentUser.getName(), "AdminService"));
        logTable.setItems(logData);
        
        // Clean charts
        updateCleanCharts();
        
        System.out.println("Clean admin dashboard initialized successfully");
    }
    
    private void updateCleanCharts() {
        // User role chart - empty initially
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("No Users Yet", 1)
        );
        userRoleChart.setData(pieChartData);
        
        // Activity chart - empty initially
        XYChart.Series<String, Number> activitySeries = new XYChart.Series<>();
        activitySeries.setName("Active Users");
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            activitySeries.getData().add(new XYChart.Data<>(day, 0));
        }
        activityChart.getData().clear();
        activityChart.getData().add(activitySeries);
        
        // Task completion chart - empty initially
        XYChart.Series<String, Number> taskSeries = new XYChart.Series<>();
        taskSeries.setName("Completions");
        taskSeries.getData().add(new XYChart.Data<>("Digital", 0));
        taskSeries.getData().add(new XYChart.Data<>("Real World", 0));
        taskSeries.getData().add(new XYChart.Data<>("Quiz", 0));
        taskSeries.getData().add(new XYChart.Data<>("Challenge", 0));
        taskCompletionChart.getData().clear();
        taskCompletionChart.getData().add(taskSeries);
        
        System.out.println("Charts initialized with clean state");
    }
    
    private void refreshData() {
        System.out.println("Refreshing admin dashboard data...");
        loadCleanData();
    }
    
    /**
     * Handle admin logout
     */
    private void handleAdminLogout() {
        System.out.println("🚪 Admin logout requested");
        
        // Get the current stage
        javafx.stage.Stage currentStage = (javafx.stage.Stage) root.getScene().getWindow();
        
        // Use NavigationUtil for clean logout handling
        com.coincraft.ui.NavigationUtil.handleLogout(currentStage);
    }
    
    private void exportData() {
        // Data export functionality
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Data");
        alert.setHeaderText("Export CoinCraft Data");
        alert.setContentText("Current data: " + 
            userTable.getItems().size() + " users, " + 
            taskTable.getItems().size() + " tasks, " + 
            logTable.getItems().size() + " log entries.\n\n" +
            "Export functionality ready for implementation.");
        alert.showAndWait();
    }
    
    private void applyAdminTheme() {
        root.setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: linear-gradient(to bottom right, #263238, #37474F, #455A64);"
        );
    }
    
    @Override
    public void navigateToSection(String section) {
        switch (section.toLowerCase()) {
            case "users":
                mainTabPane.getSelectionModel().select(1); // User Management tab
                break;
            case "tasks":
                mainTabPane.getSelectionModel().select(2); // Task Management tab
                break;
            case "analytics":
                mainTabPane.getSelectionModel().select(3); // Analytics tab
                break;
            case "logs":
                mainTabPane.getSelectionModel().select(4); // System Logs tab
                break;
            default:
                mainTabPane.getSelectionModel().select(0); // Overview tab
                break;
        }
        System.out.println("Admin Dashboard - Navigate to: " + section);
    }
    
    // Data classes for table views
    public static class UserData {
        private final String userId;
        private final String name;
        private final String email;
        private final String role;
        private final String status;
        private final String lastActive;
        
        public UserData(String userId, String name, String email, String role, String status, String lastActive) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
            this.status = status;
            this.lastActive = lastActive;
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
        public String getLastActive() { return lastActive; }
    }
    
    public static class TaskData {
        private final String taskId;
        private final String taskName;
        private final String taskType;
        private final Integer completions;
        private final String difficulty;
        
        public TaskData(String taskId, String taskName, String taskType, Integer completions, String difficulty) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.taskType = taskType;
            this.completions = completions;
            this.difficulty = difficulty;
        }
        
        // Getters
        public String getTaskId() { return taskId; }
        public String getTaskName() { return taskName; }
        public String getTaskType() { return taskType; }
        public Integer getCompletions() { return completions; }
        public String getDifficulty() { return difficulty; }
    }
    
    public static class SystemLogData {
        private final String timestamp;
        private final String level;
        private final String message;
        private final String source;
        
        public SystemLogData(String timestamp, String level, String message, String source) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
            this.source = source;
        }
        
        // Getters
        public String getTimestamp() { return timestamp; }
        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public String getSource() { return source; }
    }
}