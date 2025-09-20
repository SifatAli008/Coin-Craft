package com.coincraft.ui.components.child;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Task view component for adventurers to see and complete their assigned tasks
 */
public class AdventurerTaskView {
    private VBox root;
    private User currentAdventurer;
    private List<Task> assignedTasks;
    private Consumer<Task> onTaskCompleted;
    
    // UI Components
    private VBox taskListContainer;
    private Label statsLabel;
    
    public AdventurerTaskView(User adventurer, Consumer<Task> onTaskCompleted) {
        this.currentAdventurer = adventurer;
        this.onTaskCompleted = onTaskCompleted;
        this.assignedTasks = loadAssignedTasks();
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
        
        createHeader();
        createTaskList();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("⚔️ My Quests");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1976D2;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Complete your assigned quests to earn SmartCoins!");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #4CAF50;" +
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
    
    private void createTaskList() {
        VBox listContainer = new VBox(16);
        listContainer.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        
        Label listTitle = new Label("📋 Your Active Quests");
        listTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1976D2;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        taskListContainer = new VBox(12);
        
        ScrollPane scrollPane = new ScrollPane(taskListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;"
        );
        
        listContainer.getChildren().addAll(listTitle, scrollPane);
        root.getChildren().add(listContainer);
    }
    
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();
        
        if (assignedTasks.isEmpty()) {
            Label emptyLabel = new Label("📝 No quests assigned yet. Check back later for new adventures!");
            emptyLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: #666666;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 40;" +
                "-fx-alignment: center;"
            );
            taskListContainer.getChildren().add(emptyLabel);
            return;
        }
        
        for (Task task : assignedTasks) {
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
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label difficultyLabel = new Label("⭐ Level " + task.getDifficultyLevel());
        difficultyLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label deadlineLabel = new Label();
        if (task.getDeadline() != null) {
            deadlineLabel.setText("📅 Due: " + task.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        } else {
            deadlineLabel.setText("📅 No deadline");
        }
        deadlineLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        detailsRow.getChildren().addAll(rewardLabel, difficultyLabel, deadlineLabel);
        
        // Action buttons
        HBox actionRow = new HBox(8);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        
        if (task.getValidationStatus() == ValidationStatus.PENDING && !task.isCompleted()) {
            Button completeButton = new Button("✅ Complete Quest");
            styleActionButton(completeButton, "#4CAF50");
            completeButton.setOnAction(e -> handleTaskCompletion(task));
            actionRow.getChildren().add(completeButton);
        } else if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) {
            Button pendingButton = new Button("⏳ Pending Review");
            styleActionButton(pendingButton, "#FF9800");
            pendingButton.setDisable(true);
            actionRow.getChildren().add(pendingButton);
        } else if (task.getValidationStatus() == ValidationStatus.REJECTED) {
            Button retryButton = new Button("🔄 Try Again");
            styleActionButton(retryButton, "#F44336");
            retryButton.setOnAction(e -> handleTaskRetry(task));
            actionRow.getChildren().add(retryButton);
        } else if (task.isCompleted() && task.getValidationStatus() == ValidationStatus.APPROVED) {
            Button completedButton = new Button("✅ Completed & Approved");
            styleActionButton(completedButton, "#4CAF50");
            completedButton.setDisable(true);
            actionRow.getChildren().add(completedButton);
        }
        
        card.getChildren().addAll(headerRow, descLabel, detailsRow);
        if (!actionRow.getChildren().isEmpty()) {
            card.getChildren().add(actionRow);
        }
        
        return card;
    }
    
    private void handleTaskCompletion(Task task) {
        SoundManager.getInstance().playButtonClick();
        
        Stage parentStage = (Stage) root.getScene().getWindow();
        TaskCompletionDialog dialog = new TaskCompletionDialog(parentStage, task, completedTask -> {
            // Update the task in our list
            int index = assignedTasks.indexOf(task);
            if (index >= 0) {
                assignedTasks.set(index, completedTask);
            }
            refreshTaskList();
            
            // Notify parent component
            if (onTaskCompleted != null) {
                onTaskCompleted.accept(completedTask);
            }
        });
        dialog.show();
    }
    
    private void handleTaskRetry(Task task) {
        SoundManager.getInstance().playButtonClick();
        
        // Reset task status for retry
        task.setCompleted(false);
        task.setValidationStatus(ValidationStatus.PENDING);
        task.setCompletedAt(null);
        
        // Show completion dialog
        handleTaskCompletion(task);
    }
    
    private void updateStats() {
        int totalTasks = assignedTasks.size();
        int activeTasks = (int) assignedTasks.stream()
            .filter(t -> t.getValidationStatus() == ValidationStatus.PENDING && !t.isCompleted())
            .count();
        int pendingReview = (int) assignedTasks.stream()
            .filter(t -> t.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL)
            .count();
        int completedTasks = (int) assignedTasks.stream()
            .filter(t -> t.isCompleted() && t.getValidationStatus() == ValidationStatus.APPROVED)
            .count();
        int rejectedTasks = (int) assignedTasks.stream()
            .filter(t -> t.getValidationStatus() == ValidationStatus.REJECTED)
            .count();
        
        statsLabel.setText(String.format(
            "📊 Total: %d quests | ⚔️ Active: %d | ⏳ Pending: %d | ✅ Completed: %d | ❌ Rejected: %d",
            totalTasks, activeTasks, pendingReview, completedTasks, rejectedTasks
        ));
    }
    
    private List<Task> loadAssignedTasks() {
        // Load tasks assigned to this adventurer
        FirebaseService firebaseService = FirebaseService.getInstance();
        List<Task> allTasks = firebaseService.loadAllTasks();
        
        List<Task> myTasks = new ArrayList<>();
        for (Task task : allTasks) {
            // Check if task is assigned to this adventurer or to all adventurers
            if (currentAdventurer.getUserId().equals(task.getAssignedTo()) || 
                "ALL_ADVENTURERS".equals(task.getAssignedTo())) {
                myTasks.add(task);
            }
        }
        
        return myTasks;
    }
    
    // Utility methods for styling and colors
    private String getTaskBorderColor(Task task) {
        if (task.isCompleted() && task.getValidationStatus() == ValidationStatus.APPROVED) return "#4CAF50";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "#FF9800";
        if (task.getValidationStatus() == ValidationStatus.REJECTED) return "#F44336";
        return "#2196F3";
    }
    
    private String getStatusText(Task task) {
        if (task.isCompleted() && task.getValidationStatus() == ValidationStatus.APPROVED) return "✅ Completed";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "⏳ Pending Review";
        if (task.getValidationStatus() == ValidationStatus.REJECTED) return "❌ Needs Retry";
        return "⚔️ Active";
    }
    
    private String getStatusColor(Task task) {
        if (task.isCompleted() && task.getValidationStatus() == ValidationStatus.APPROVED) return "#4CAF50";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "#FF9800";
        if (task.getValidationStatus() == ValidationStatus.REJECTED) return "#F44336";
        return "#2196F3";
    }
    
    private String getStatusBgColor(Task task) {
        if (task.isCompleted() && task.getValidationStatus() == ValidationStatus.APPROVED) return "rgba(76, 175, 80, 0.1)";
        if (task.getValidationStatus() == ValidationStatus.AWAITING_APPROVAL) return "rgba(255, 152, 0, 0.1)";
        if (task.getValidationStatus() == ValidationStatus.REJECTED) return "rgba(244, 67, 54, 0.1)";
        return "rgba(33, 150, 243, 0.1)";
    }
    
    private String getTaskTypeIcon(com.coincraft.models.TaskType type) {
        switch (type) {
            case LEARNING: return "📚";
            case CHALLENGE: return "🎯";
            case CHORE: return "🏠";
            case QUEST: return "⚔️";
            case DONATION: return "💝";
            case CREATIVE: return "🎨";
            case PHYSICAL: return "💪";
            case SOCIAL: return "👥";
            default: return "📋";
        }
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
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        button.setOnMouseExited(e -> {
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle.replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public void refresh() {
        this.assignedTasks = loadAssignedTasks();
        refreshTaskList();
    }
}
