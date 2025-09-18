package com.coincraft.ui.components;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.ValidationStatus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Task panel component displaying active quests and challenges
 */
public class TaskPanel {
    private VBox root;
    private VBox taskContainer;
    private List<Task> currentTasks;
    private Label emptyLabel;
    
    public TaskPanel() {
        this.currentTasks = new ArrayList<>();
        initializeComponent();
    }
    
    private void initializeComponent() {
        root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("task-panel");
        root.getStyleClass().add("pixel-card");
        
        // Task container with scroll capability
        taskContainer = new VBox(8);
        taskContainer.setPadding(new Insets(5));
        
        ScrollPane scrollPane = new ScrollPane(taskContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        // Empty state label
        emptyLabel = new Label("No active quests.\nComplete levels to unlock new challenges!");
        emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        emptyLabel.setStyle("-fx-text-fill: #8B7355; -fx-text-alignment: center;");
        emptyLabel.setWrapText(true);
        emptyLabel.setVisible(true);
        
        root.getChildren().addAll(scrollPane, emptyLabel);
        
        // Add some sample tasks for MVP
        addSampleTasks();
    }
    
    private void addSampleTasks() {
        List<Task> sampleTasks = new ArrayList<>();
        
        Task task1 = new Task("task_1", "Complete your first budget", TaskType.LEARNING, 20);
        task1.setValidationStatus(ValidationStatus.PENDING);
        
        Task task2 = new Task("task_2", "Set a savings goal", TaskType.CHALLENGE, 15);
        task2.setValidationStatus(ValidationStatus.PENDING);
        
        Task task3 = new Task("task_3", "Help with household chores", TaskType.CHORE, 10);
        task3.setValidationStatus(ValidationStatus.PENDING);
        task3.setAssignedBy("Parent");
        
        sampleTasks.add(task1);
        sampleTasks.add(task2);
        sampleTasks.add(task3);
        
        updateTasks(sampleTasks);
    }
    
    public void updateTasks(List<Task> tasks) {
        this.currentTasks = tasks != null ? new ArrayList<>(tasks) : new ArrayList<>();
        refreshTaskDisplay();
    }
    
    private void refreshTaskDisplay() {
        taskContainer.getChildren().clear();
        
        if (currentTasks.isEmpty()) {
            emptyLabel.setVisible(true);
            return;
        }
        
        emptyLabel.setVisible(false);
        
        for (Task task : currentTasks) {
            VBox taskItem = createTaskItem(task);
            taskContainer.getChildren().add(taskItem);
        }
    }
    
    private VBox createTaskItem(Task task) {
        VBox taskItem = new VBox(5);
        taskItem.setPadding(new Insets(10));
        taskItem.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-border-width: 1;");
        
        // Task header with title and status
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(task.getDescription());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(150);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusLabel = createStatusLabel(task);
        
        header.getChildren().addAll(titleLabel, spacer, statusLabel);
        
        // Task details
        HBox details = new HBox(15);
        details.setAlignment(Pos.CENTER_LEFT);
        
        Label typeLabel = new Label(task.getType().getDisplayName());
        typeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        typeLabel.setStyle("-fx-text-fill: #666666;");
        
        Label rewardLabel = new Label(task.getRewardCoins() + " SmartCoins");
        rewardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        rewardLabel.setStyle("-fx-text-fill: #DAA520;");
        
        details.getChildren().addAll(typeLabel, rewardLabel);
        
        // Action button
        Button actionButton = createActionButton(task);
        
        taskItem.getChildren().addAll(header, details, actionButton);
        
        return taskItem;
    }
    
    private Label createStatusLabel(Task task) {
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        statusLabel.setPadding(new Insets(2, 8, 2, 8));
        statusLabel.setStyle("-fx-background-radius: 10;");
        
        ValidationStatus status = task.getValidationStatus();
        switch (status) {
            case PENDING -> {
                statusLabel.setText("PENDING");
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #FFA500; -fx-text-fill: white;");
            }
            case APPROVED -> {
                statusLabel.setText("COMPLETED");
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #32CD32; -fx-text-fill: white;");
            }
            case REJECTED -> {
                statusLabel.setText("NEEDS WORK");
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #FF6347; -fx-text-fill: white;");
            }
            case AUTO_APPROVED -> {
                statusLabel.setText("COMPLETED");
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #32CD32; -fx-text-fill: white;");
            }
        }
        
        return statusLabel;
    }
    
    private Button createActionButton(Task task) {
        Button actionButton = new Button();
        actionButton.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        actionButton.setPrefWidth(120);
        
        ValidationStatus status = task.getValidationStatus();
        
        if (status == ValidationStatus.PENDING && !task.isCompleted()) {
            actionButton.setText("Mark Complete");
            actionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            actionButton.setOnAction(e -> handleTaskComplete(task));
        } else if (status == ValidationStatus.REJECTED) {
            actionButton.setText("Try Again");
            actionButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
            actionButton.setOnAction(e -> handleTaskRetry(task));
        } else {
            actionButton.setText("View Details");
            actionButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            actionButton.setOnAction(e -> handleTaskDetails(task));
        }
        
        return actionButton;
    }
    
    private void handleTaskComplete(Task task) {
        // Mark task as completed and awaiting validation
        task.setCompleted(true);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Completed!");
        alert.setHeaderText("Great job!");
        alert.setContentText("You've marked \"" + task.getDescription() + "\" as complete.\n\n" +
                "Your parent or teacher will review and approve your work to award " + 
                task.getRewardCoins() + " SmartCoins!");
        alert.showAndWait();
        
        refreshTaskDisplay();
    }
    
    private void handleTaskRetry(Task task) {
        // Reset task for retry
        task.setCompleted(false);
        task.setValidationStatus(ValidationStatus.PENDING);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Try Again");
        alert.setHeaderText("Ready for Another Attempt");
        alert.setContentText("\"" + task.getDescription() + "\" has been reset.\n\n" +
                "Review the feedback and try again when you're ready!");
        alert.showAndWait();
        
        refreshTaskDisplay();
    }
    
    private void handleTaskDetails(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText(task.getDescription());
        
        StringBuilder details = new StringBuilder();
        details.append("Type: ").append(task.getType().getDisplayName()).append("\n");
        details.append("Reward: ").append(task.getRewardCoins()).append(" SmartCoins\n");
        details.append("Status: ").append(task.getValidationStatus().getDisplayName()).append("\n");
        
        if (task.getAssignedBy() != null) {
            details.append("Assigned by: ").append(task.getAssignedBy()).append("\n");
        }
        
        if (task.getDeadline() != null) {
            details.append("Deadline: ").append(task.getDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))).append("\n");
        }
        
        if (task.getCompletionNotes() != null && !task.getCompletionNotes().isEmpty()) {
            details.append("\nNotes: ").append(task.getCompletionNotes());
        }
        
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public List<Task> getCurrentTasks() {
        return new ArrayList<>(currentTasks);
    }
}
