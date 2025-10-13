package com.coincraft.ui.components.child;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Task card slider component for Child Dashboard
 * Displays active tasks as horizontal scrollable cards
 * Features navigation buttons and modern card design
 * Supports task completion workflow with parent review
 */
public class TaskCardList {
    private StackPane root;
    private ScrollPane scrollPane;
    private HBox cardContainer;
    private Button leftButton;
    private Button rightButton;
    private final User currentUser;
    private List<Task> activeTasks;
    
    public TaskCardList(User user) {
        this.currentUser = user;
        this.activeTasks = new ArrayList<>();
        loadRealTasks();
        initializeUI();
    }
    
    /**
     * Load real tasks from Firebase for this child
     */
    private void loadRealTasks() {
        try {
            FirebaseService firebaseService = FirebaseService.getInstance();
            List<Task> allTasks = firebaseService.loadUserTasks(currentUser.getUserId());
            
            // Filter to show only active tasks (not yet approved/auto-approved)
            activeTasks = allTasks.stream()
                .filter(task -> task.getValidationStatus() != ValidationStatus.APPROVED 
                             && task.getValidationStatus() != ValidationStatus.AUTO_APPROVED)
                .collect(Collectors.toList());
            
            System.out.println("📋 Loaded " + activeTasks.size() + " active tasks for " + currentUser.getName());
        } catch (Exception e) {
            System.out.println("⚠️ Could not load tasks: " + e.getMessage());
            activeTasks = new ArrayList<>();
        }
    }
    
    private void initializeUI() {
        // Create main container
        root = new StackPane();
        root.setPrefHeight(240);
        root.setMaxHeight(240);
        root.setStyle("-fx-background-color: transparent;");
        
        // Create horizontal scrolling container
        cardContainer = new HBox(16);
        cardContainer.setAlignment(Pos.CENTER_LEFT);
        cardContainer.setPadding(new Insets(16, 60, 16, 60)); // Extra padding for nav buttons
        
        // Create scroll pane (hidden scrollbars)
        scrollPane = new ScrollPane(cardContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        // Create navigation buttons
        createNavigationButtons();
        
        // Add everything to root
        root.getChildren().addAll(scrollPane, leftButton, rightButton);
        
        refreshTaskCards();
    }
    
    /**
     * Create navigation buttons for quest slider
     */
    private void createNavigationButtons() {
        // Left navigation button
        leftButton = new Button("‹");
        leftButton.setPrefSize(45, 45);
        leftButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 22.5;" +
            "-fx-border-radius: 22.5;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 8);"
        );
        leftButton.setOnAction(e -> scrollLeft());
        
        // Right navigation button
        rightButton = new Button("›");
        rightButton.setPrefSize(45, 45);
        rightButton.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-text-fill: #333333;" +
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 22.5;" +
            "-fx-border-radius: 22.5;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 8);"
        );
        rightButton.setOnAction(e -> scrollRight());
        
        // Position buttons
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(leftButton, new Insets(0, 0, 0, 12));
        StackPane.setMargin(rightButton, new Insets(0, 12, 0, 0));
        
        // Add hover effects
        addButtonHoverEffects(leftButton);
        addButtonHoverEffects(rightButton);
    }
    
    /**
     * Add hover effects to navigation buttons
     */
    private void addButtonHoverEffects(Button button) {
        button.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + 
                "-fx-scale-x: 1.1; -fx-scale-y: 1.1;" +
                "-fx-background-color: rgba(33, 150, 243, 0.9);" +
                "-fx-text-fill: white;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-text-fill: #333333;" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 22.5;" +
                "-fx-border-radius: 22.5;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 8);"
            );
        });
    }
    
    /**
     * Scroll left in the quest slider
     */
    private void scrollLeft() {
        CentralizedMusicManager.getInstance().playButtonClick();
        double currentValue = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.max(0, currentValue - 0.3));
    }
    
    /**
     * Scroll right in the quest slider
     */
    private void scrollRight() {
        CentralizedMusicManager.getInstance().playButtonClick();
        double currentValue = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.min(1, currentValue + 0.3));
    }
    
    /**
     * Refresh the task cards display
     */
    public void refresh() {
        loadRealTasks();
        cardContainer.getChildren().clear();
        refreshTaskCards();
    }
    
    /**
     * Create and display task cards in horizontal grid (3 per row)
     */
    private void refreshTaskCards() {
        if (activeTasks.isEmpty()) {
            // Show empty state
            VBox emptyState = new VBox(16);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(40, 20, 40, 20));
            
            Label emptyIcon = new Label("🎯");
            emptyIcon.setStyle("-fx-font-size: 48px;");
            
            Label emptyTitle = new Label("No Tasks Assigned");
            emptyTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #374151;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
            );
            
            Label emptySubtitle = new Label("All tasks completed! New tasks will appear here when assigned by your parents.");
            emptySubtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #6B7280;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
            emptySubtitle.setWrapText(true);
            emptySubtitle.setMaxWidth(400);
            
            emptyState.getChildren().addAll(emptyIcon, emptyTitle, emptySubtitle);
            cardContainer.getChildren().add(emptyState);
            return;
        }
        
        // Create horizontal slider cards
        for (Task task : activeTasks) {
            VBox taskCard = createSliderTaskCard(task);
            cardContainer.getChildren().add(taskCard);
        }
    }
    
    /**
     * Create a slider task card for horizontal scrolling
     */
    private VBox createSliderTaskCard(Task task) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(340);
        card.setMaxWidth(340);
        card.setPrefHeight(200);
        card.setMaxHeight(200);
        
        // Background color based on validation status
        String backgroundColor = getStatusBackgroundColor(task);
        
        card.setStyle(
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 12);"
        );
        
        // Status badge for awaiting approval or rejected tasks
        VBox content = new VBox(6);
        content.setAlignment(Pos.CENTER);
        
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) {
            Label statusBadge = new Label("⏳ UNDER REVIEW");
            statusBadge.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: #FF9800;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 4 8;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            content.getChildren().add(statusBadge);
        } else if (task.getValidationStatus() == ValidationStatus.REJECTED) {
            Label statusBadge = new Label("❌ REJECTED");
            statusBadge.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 700;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: #F44336;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 4 8;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
            content.getChildren().add(statusBadge);
        }
        
        // Task type icon
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 32px; -fx-text-fill: #FA8A00;");
        
        // Task title
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;"
        );
        titleLabel.setMaxWidth(300);
        titleLabel.setPrefHeight(32);
        
        // Reward information
        Label rewardLabel = new Label("💰 " + task.getRewardCoins() + " SmartCoins");
        rewardLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #059669;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Progress bar (only for active tasks, not for those under review)
        if (task.getValidationStatus() != ValidationStatus.AWAITING_APPROVAL) {
            ProgressBar progressBar = new ProgressBar(task.getProgressPercentage() / 100.0);
            progressBar.setPrefWidth(280);
            progressBar.setPrefHeight(8);
            progressBar.setStyle(
                "-fx-accent: " + getTaskTypeColor(task.getType()) + ";" +
                "-fx-background-color: rgba(224, 224, 224, 0.8);" +
                "-fx-background-radius: 4;"
            );
            content.getChildren().add(progressBar);
        }
        
        // Action button
        Button actionButton = createSliderActionButton(task);
        
        content.getChildren().addAll(typeIcon, titleLabel, rewardLabel, actionButton);
        card.getChildren().add(content);
        return card;
    }
    
    /**
     * Get background color based on task validation status
     */
    private String getStatusBackgroundColor(Task task) {
        return switch (task.getValidationStatus()) {
            case AWAITING_APPROVAL -> "rgba(255, 152, 0, 0.1)"; // Light orange for under review
            case REJECTED -> "rgba(244, 67, 54, 0.1)"; // Light red for rejected
            case PENDING -> "rgba(255, 255, 255, 0.95)"; // White for pending
            default -> "rgba(255, 255, 255, 0.95)"; // White default
        };
    }
    
    /**
     * Create action button for slider cards
     */
    private Button createSliderActionButton(Task task) {
        Button button = new Button();
        button.setPrefWidth(280);
        button.setPrefHeight(36);
        
        // Different buttons based on validation status
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) {
            // Task is awaiting parent review
            button.setText("⏳ AWAITING REVIEW");
            button.setStyle(
                "-fx-background-color: #FF9800;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: default;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
                "-fx-opacity: 0.9;"
            );
            button.setDisable(true);
            
        } else if (task.getValidationStatus() == ValidationStatus.REJECTED) {
            // Task was rejected, allow resubmission
            button.setText("🔄 RESUBMIT TASK");
            button.setStyle(
                "-fx-background-color: #F44336;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.3), 15, 0, 0, 5);"
            );
            button.setOnAction(e -> {
                CentralizedMusicManager.getInstance().playButtonClick();
                openTaskCompletionDialog(task);
            });
            addButtonHoverEffect(button);
            
        } else if (!task.isCompleted()) {
            // Task is pending/active - child can complete it
            button.setText("✅ COMPLETE TASK");
            button.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 15, 0, 0, 5);"
            );
            button.setOnAction(e -> {
                CentralizedMusicManager.getInstance().playButtonClick();
                openTaskCompletionDialog(task);
            });
            addButtonHoverEffect(button);
            
        } else {
            // Completed but not yet submitted (edge case)
            button.setText("📋 VIEW DETAILS");
            button.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.3), 15, 0, 0, 5);"
            );
            button.setOnAction(e -> {
                CentralizedMusicManager.getInstance().playButtonClick();
                viewTaskDetails(task);
            });
            addButtonHoverEffect(button);
        }
        
        return button;
    }
    
    /**
     * Add hover effect to button
     */
    private void addButtonHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            String baseStyle = button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "");
            button.setStyle(baseStyle);
        });
    }
    
    /**
     * Create a wide task card for horizontal grid layout with full content visibility (legacy)
     */
    @SuppressWarnings("unused")
    private VBox createCompactTaskCard(Task task) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 8);"
        );
        
        // Task type icon (large)
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #FA8A00;");
        
        // Clean task title
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;"
        );
        titleLabel.setMaxWidth(195);
        titleLabel.setPrefHeight(40);
        
        // Progress bar (maximum width for 205px card)
        ProgressBar progressBar = new ProgressBar(task.getProgressPercentage() / 100.0);
        progressBar.setPrefWidth(185);
        progressBar.setPrefHeight(14);
        progressBar.setStyle(
            "-fx-accent: " + getTaskTypeColor(task.getType()) + ";" +
            "-fx-background-color: #E0E0E0;" +
            "-fx-background-radius: 3;"
        );
        
        // Progress percentage (larger for better visibility)
        Label progressLabel = new Label(String.format("%.0f%%", task.getProgressPercentage()));
        progressLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Action button (compact)
        Button actionButton = createCompactActionButton(task);
        
        card.getChildren().addAll(typeIcon, titleLabel, progressBar, progressLabel, actionButton);
        return card;
    }
    
    /**
     * Create an individual task card (old method - keeping for compatibility)
     */
    @SuppressWarnings("unused")
    private VBox createTaskCard(Task task) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );
        
        // Task header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 20px; -fx-text-fill: #FA8A00;");
        
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label rewardLabel = new Label("💰 " + task.getRewardCoins() + " coins");
        rewardLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: #FFF8DC;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 4 8;"
        );
        
        header.getChildren().addAll(typeIcon, titleLabel, spacer, rewardLabel);
        
        // Task description
        Label descLabel = new Label(task.getDescription());
        descLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-wrap-text: true;"
        );
        
        // Progress section
        VBox progressSection = new VBox(8);
        
        // Progress bar
        ProgressBar progressBar = new ProgressBar(task.getProgressPercentage() / 100.0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(8);
        progressBar.setStyle(
            "-fx-accent: " + getTaskTypeColor(task.getType()) + ";" +
            "-fx-background-color: #E0E0E0;" +
            "-fx-background-radius: 4;"
        );
        
        // Progress info row
        HBox progressInfo = new HBox();
        progressInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label progressLabel = new Label(String.format("%.0f%% Complete", task.getProgressPercentage()));
        progressLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region progressSpacer = new Region();
        HBox.setHgrow(progressSpacer, Priority.ALWAYS);
        
        // Deadline info
        String deadlineText = "";
        if (task.getDeadline() != null) {
            deadlineText = "Due: " + task.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd"));
        }
        Label deadlineLabel = new Label(deadlineText);
        deadlineLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #999999;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        progressInfo.getChildren().addAll(progressLabel, progressSpacer, deadlineLabel);
        progressSection.getChildren().addAll(progressBar, progressInfo);
        
        // Action button
        Button actionButton = createTaskActionButton(task);
        
        card.getChildren().addAll(header, descLabel, progressSection, actionButton);
        
        return card;
    }
    
    /**
     * Create compact action button for horizontal grid
     */
    private Button createCompactActionButton(Task task) {
        Button button = new Button();
        
        if (task.getProgressPercentage() >= 100) {
            button.setText("✅ DONE");
            button.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 25;" +
                "-fx-pref-width: 150;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 4, 0, 0, 2);"
            );
            button.setOnAction(e -> openTaskCompletionDialog(task));
        } else {
            button.setText("📋 VIEW");
            button.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 25;" +
                "-fx-pref-width: 150;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 4, 0, 0, 2);"
            );
            button.setOnAction(e -> viewTaskDetails(task));
        }
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.1; -fx-scale-y: 1.1;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.1; -fx-scale-y: 1.1;", ""));
        });
        
        return button;
    }
    
    /**
     * Create action button for task (old method)
     */
    private Button createTaskActionButton(Task task) {
        Button button = new Button();
        
        if (task.getProgressPercentage() >= 100) {
            button.setText("✅ COMPLETE QUEST");
            button.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 10 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
            );
            
            button.setOnAction(e -> openTaskCompletionDialog(task));
            
        } else {
            button.setText("📋 VIEW DETAILS");
            button.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 10 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 8, 0, 0, 2);"
            );
            
            button.setOnAction(e -> viewTaskDetails(task));
        }
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        return button;
    }
    
    /**
     * Open task completion dialog for child to submit evidence
     */
    private void openTaskCompletionDialog(Task task) {
        try {
            // Get the stage from the root node
            Stage parentStage = (Stage) root.getScene().getWindow();
            
            // Open the task completion dialog
            TaskCompletionDialog dialog = new TaskCompletionDialog(
                parentStage,
                task,
                completedTask -> {
                    // Callback when task is completed
                    CentralizedMusicManager.getInstance().playSuccess();
                    
                    // Refresh the task list to show updated status
                    Platform.runLater(() -> {
                        refresh();
                    });
                }
            );
            
            dialog.show();
            
        } catch (Exception e) {
            System.err.println("❌ Error opening task completion dialog: " + e.getMessage());
            
            // Fallback: Show error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Cannot Open Task Completion Dialog");
            errorAlert.setContentText("An error occurred. Please try again.");
            errorAlert.showAndWait();
        }
    }
    
    /**
     * View task details
     */
    private void viewTaskDetails(Task task) {
        CentralizedMusicManager.getInstance().playButtonClick();
        
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quest Details");
        dialog.setHeaderText(getTaskTypeIcon(task.getType()) + " " + task.getTitle());
        
        StringBuilder content = new StringBuilder();
        content.append(task.getDescription()).append("\n\n");
        
        // Status information
        content.append("Status: ").append(getStatusDisplayText(task)).append("\n");
        
        if (task.getValidationStatus() != ValidationStatus.AWAITING_APPROVAL) {
            content.append("Progress: ").append(String.format("%.0f%%", task.getProgressPercentage())).append(" complete\n");
        }
        
        content.append("Reward: ").append(task.getRewardCoins()).append(" SmartCoins\n");
        
        if (task.getDeadline() != null) {
            content.append("Deadline: ").append(task.getDeadline().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))).append("\n");
        }
        
        // Show rejection feedback if rejected
        if (task.getValidationStatus() == ValidationStatus.REJECTED && task.getCompletionNotes() != null) {
            content.append("\n❌ REJECTION FEEDBACK:\n");
            content.append(task.getCompletionNotes()).append("\n");
            content.append("\nYou can resubmit this task after addressing the feedback.");
        }
        
        // Show awaiting approval message
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) {
            content.append("\n⏳ Your completion is under review by a parent/guardian.");
            content.append("\nYou'll receive your reward once approved!");
        }
        
        if (task.getValidationStatus() == ValidationStatus.PENDING || task.getValidationStatus() == ValidationStatus.REJECTED) {
            content.append("\n\nTip: ").append(getTaskTip(task.getType()));
        }
        
        dialog.setContentText(content.toString());
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
    
    /**
     * Get display text for task status
     */
    private String getStatusDisplayText(Task task) {
        return switch (task.getValidationStatus()) {
            case AWAITING_APPROVAL -> "⏳ Under Review";
            case REJECTED -> "❌ Rejected - Needs Resubmission";
            case APPROVED -> "✅ Approved";
            case AUTO_APPROVED -> "✅ Auto-Approved";
            default -> "📋 Active";
        };
    }
    
    /**
     * Get color for task type
     */
    private String getTaskTypeColor(TaskType type) {
        return switch (type) {
            case LEARNING -> "#2196F3";
            case CHORE -> "#FA8A00";
            case CREATIVE -> "#9C27B0";
            case PHYSICAL -> "#4CAF50";
            case SOCIAL -> "#E91E63";
            default -> "#607D8B";
        };
    }
    
    /**
     * Get icon for task type
     */
    private String getTaskTypeIcon(TaskType type) {
        return switch (type) {
            case LEARNING -> "🎓";
            case CHORE -> "🏡";
            case CREATIVE -> "🎭";
            case PHYSICAL -> "🏃‍♂️";
            case SOCIAL -> "👫";
            default -> "⭐";
        };
    }
    
    /**
     * Get helpful tip for task type
     */
    private String getTaskTip(TaskType type) {
        return switch (type) {
            case LEARNING -> "Take your time and ask questions if you need help!";
            case CHORE -> "A clean space makes for a happy place!";
            case CREATIVE -> "Let your imagination run wild!";
            case PHYSICAL -> "Stay active and have fun!";
            case SOCIAL -> "Be kind and make new friends!";
            default -> "You've got this, adventurer!";
        };
    }
    
    
    /**
     * Get the root UI component
     */
    public StackPane getRoot() {
        return root;
    }
}
