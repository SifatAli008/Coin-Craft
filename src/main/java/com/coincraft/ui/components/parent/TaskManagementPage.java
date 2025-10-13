package com.coincraft.ui.components.parent;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Task Management Page for Parent Dashboard
 * Allows parents to view, create, and manage all tasks for their adventurers
 */
public class TaskManagementPage {
    private VBox root;
    private final User currentParent;
    private final List<User> adventurers;
    private final List<Task> allTasks;
    
    // UI Components
    private VBox taskListContainer;
    private TextField searchField;
    private ComboBox<String> filterCombo;
    private ComboBox<String> adventurerFilterCombo;
    private Label statsLabel;
    private Button createTaskButton;
    
    public TaskManagementPage(User parent, List<User> adventurers) {
        this.currentParent = parent;
        this.adventurers = adventurers;
        this.allTasks = loadAllTasks();
        initializeUI();
        refreshTaskList();
    }
    
    private void initializeUI() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        // Allow root to use full available width
        root.setMaxWidth(Double.MAX_VALUE);
        
        createHeader();
        createToolbar();
        createTaskList();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        // Allow header to use full width
        header.setMaxWidth(Double.MAX_VALUE);
        
        Label titleLabel = new Label("Task Management Center");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Create, assign, and manage tasks for your adventurers");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Statistics
        statsLabel = new Label();
        updateStats();
        statsLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 10 0 0 0;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel, statsLabel);
        root.getChildren().add(header);
    }
    
    private void createToolbar() {
        VBox toolbar = new VBox(16);
        toolbar.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        // Allow toolbar to use full width
        toolbar.setMaxWidth(Double.MAX_VALUE);
        
        // Top row: Create button and search
        HBox topRow = new HBox(16);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        createTaskButton = new Button("📋 CREATE NEW TASK");
        createTaskButton.setPrefHeight(45);
        createTaskButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
        );
        createTaskButton.setOnAction(e -> handleCreateTask());
        addButtonHoverEffects(createTaskButton, "#45A049", "#4CAF50");
        
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search tasks...");
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(40);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 0 15;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTasks());
        
        topRow.getChildren().addAll(createTaskButton, spacer1, searchField);
        
        // Bottom row: Filters
        HBox filterRow = new HBox(16);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        
        Label filterLabel = new Label("Filter by:");
        filterLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All Tasks", "Active", "Completed", "Pending Review", "Overdue");
        filterCombo.setValue("All Tasks");
        filterCombo.setPrefWidth(150);
        filterCombo.setOnAction(e -> filterTasks());
        styleComboBox(filterCombo);
        
        adventurerFilterCombo = new ComboBox<>();
        adventurerFilterCombo.getItems().add("All Adventurers");
        for (User adventurer : adventurers) {
            adventurerFilterCombo.getItems().add(adventurer.getName());
        }
        adventurerFilterCombo.setValue("All Adventurers");
        adventurerFilterCombo.setPrefWidth(200);
        adventurerFilterCombo.setOnAction(e -> filterTasks());
        styleComboBox(adventurerFilterCombo);
        
        filterRow.getChildren().addAll(filterLabel, filterCombo, adventurerFilterCombo);
        
        toolbar.getChildren().addAll(topRow, filterRow);
        root.getChildren().add(toolbar);
    }
    
    private void createTaskList() {
        VBox listContainer = new VBox(16);
        listContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        // Allow list container to use full width
        listContainer.setMaxWidth(Double.MAX_VALUE);
        
        Label listTitle = new Label("Active Tasks");
        listTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        taskListContainer = new VBox(12);
        taskListContainer.setMaxWidth(Double.MAX_VALUE);
        
        ScrollPane scrollPane = new ScrollPane(taskListContainer);
        scrollPane.setFitToWidth(true);
        // Make scroll pane more responsive - use min height instead of pref height
        scrollPane.setMinHeight(300);
        scrollPane.setPrefHeight(500);
        scrollPane.setMaxHeight(700);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        listContainer.getChildren().addAll(listTitle, scrollPane);
        root.getChildren().add(listContainer);
    }
    
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();
        
        if (allTasks.isEmpty()) {
            Label emptyLabel = new Label("📝 No tasks created yet. Click 'CREATE NEW TASK' to get started!");
            emptyLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: #666666;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 40;"
            );
            emptyLabel.setAlignment(Pos.CENTER);
            taskListContainer.getChildren().add(emptyLabel);
            return;
        }
        
        for (Task task : allTasks) {
            VBox taskCard = createTaskCard(task);
            taskListContainer.getChildren().add(taskCard);
        }
        
        updateStats();
    }
    
    private VBox createTaskCard(Task task) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + getTaskBorderColor(task) + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        // Header row
        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 20px;");
        
        VBox titleSection = new VBox(2);
        
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label typeLabel = new Label(task.getType().toString());
        typeLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        titleSection.getChildren().addAll(titleLabel, typeLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusLabel = new Label(getStatusText(task));
        statusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + getStatusColor(task) + ";" +
            "-fx-background-color: " + getStatusBgColor(task) + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 4 8;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        headerRow.getChildren().addAll(typeIcon, titleSection, spacer, statusLabel);
        
        // Description
        Label descLabel = new Label(task.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #555555;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Details row
        HBox detailsRow = new HBox(20);
        detailsRow.setAlignment(Pos.CENTER_LEFT);
        
        Label rewardLabel = new Label("💰 " + task.getRewardCoins() + " SmartCoins");
        rewardLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label difficultyLabel = new Label("⭐ Level " + task.getDifficultyLevel());
        difficultyLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label deadlineLabel = new Label();
        if (task.getDeadline() != null) {
            deadlineLabel.setText("📅 Due: " + task.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy, HH:mm")));
        } else {
            deadlineLabel.setText("📅 No deadline");
        }
        deadlineLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        detailsRow.getChildren().addAll(rewardLabel, difficultyLabel, deadlineLabel);
        
        // Action buttons (if needed)
        HBox actionRow = new HBox(8);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) {
            Button verifyButton = new Button("📋 Review");
            styleActionButton(verifyButton, "#FA8A00");
            verifyButton.setOnAction(e -> handleVerifyTask(task));
            actionRow.getChildren().add(verifyButton);
        }
        
        card.getChildren().addAll(headerRow, descLabel, detailsRow);
        if (!actionRow.getChildren().isEmpty()) {
            card.getChildren().add(actionRow);
        }
        
        return card;
    }
    
    private void handleCreateTask() {
        CentralizedMusicManager.getInstance().playButtonClick();
        
        Stage parentStage = (Stage) root.getScene().getWindow();
        CreateTaskDialog dialog = new CreateTaskDialog(parentStage, currentParent, adventurers, task -> {
            allTasks.add(task);
            refreshTaskList();
        });
        dialog.show();
    }
    
    private void handleVerifyTask(Task task) {
        CentralizedMusicManager.getInstance().playButtonClick();
        
        Stage parentStage = (Stage) root.getScene().getWindow();
        TaskVerificationDialog dialog = new TaskVerificationDialog(parentStage, task, verifiedTask -> {
            // Update the task in our list
            int index = allTasks.indexOf(task);
            if (index >= 0) {
                allTasks.set(index, verifiedTask);
            }
            refreshTaskList();
            
            // Notify parent dashboard to update notification count
            notifyParentDashboard();
        });
        dialog.show();
    }
    
    private void filterTasks() {
        // Implementation for filtering tasks based on search and combo box values
        refreshTaskList();
    }
    
    private void updateStats() {
        int totalTasks = allTasks.size();
        int activeTasks = (int) allTasks.stream().filter(t -> !t.isCompleted()).count();
        int pendingReview = (int) allTasks.stream().filter(t -> t.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL).count();
        int completedTasks = totalTasks - activeTasks;
        
        statsLabel.setText(String.format(
            "📊 Total: %d tasks | ⚔️ Active: %d | ✅ Completed: %d | 📋 Pending Review: %d",
            totalTasks, activeTasks, completedTasks, pendingReview
        ));
    }
    
    private List<Task> loadAllTasks() {
        // Load all tasks from Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        return firebaseService.loadAllTasks();
    }
    
    // Utility methods for styling and colors
    private String getTaskBorderColor(Task task) {
        if (task.isCompleted()) return "#4CAF50";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "#FA8A00";
        return "#2196F3";
    }
    
    private String getStatusText(Task task) {
        if (task.isCompleted()) return "✅ Completed";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "📋 Pending Review";
        return "⚔️ Active";
    }
    
    private String getStatusColor(Task task) {
        if (task.isCompleted()) return "#4CAF50";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "#FA8A00";
        return "#2196F3";
    }
    
    private String getStatusBgColor(Task task) {
        if (task.isCompleted()) return "rgba(76, 175, 80, 0.1)";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "rgba(255, 152, 0, 0.1)";
        return "rgba(33, 150, 243, 0.1)";
    }
    
    private String getTaskTypeIcon(TaskType type) {
        return switch (type) {
            case LEARNING -> "📚";
            case CHALLENGE -> "🎯";
            case CHORE -> "🏠";
            case QUEST -> "🎯";
            case DONATION -> "💝";
            case CREATIVE -> "🎨";
            case PHYSICAL -> "💪";
            case SOCIAL -> "👥";
            default -> "📋";
        };
    }
    
    private void styleComboBox(ComboBox<?> combo) {
        combo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void styleActionButton(Button button, String color) {
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 6 12;"
        );
    }
    
    private void addButtonHoverEffects(Button button, String hoverColor, String normalColor) {
        button.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle.replace("-fx-background-color: " + normalColor, 
                                                 "-fx-background-color: " + hoverColor) + 
                           "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        button.setOnMouseExited(e -> {
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle.replace("-fx-background-color: " + hoverColor, 
                                                 "-fx-background-color: " + normalColor)
                           .replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
    }
    
    public VBox getRoot() {
        return root;
    }
    
    /**
     * Notify parent dashboard to update notification count
     */
    private void notifyParentDashboard() {
        try {
            com.coincraft.ui.routing.DashboardRouter router = com.coincraft.ui.routing.DashboardRouter.getInstance();
            if (router.getCurrentDashboard() instanceof com.coincraft.ui.dashboards.ParentDashboard) {
                com.coincraft.ui.dashboards.ParentDashboard parentDashboard = 
                    (com.coincraft.ui.dashboards.ParentDashboard) router.getCurrentDashboard();
                // Refresh the notification count
                parentDashboard.refreshData();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not notify parent dashboard: " + e.getMessage());
        }
    }
}
