package com.coincraft.ui.components.child;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Task card list component for Child Dashboard
 * Displays active tasks as interactive cards with progress indicators
 * Features complete buttons and visual feedback
 */
public class TaskCardList {
    private VBox root;
    private User currentUser;
    private List<Task> activeTasks;
    
    public TaskCardList(User user) {
        this.currentUser = user;
        this.activeTasks = generateMockTasks(); // In real app, load from database
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(0));
        
        refreshTaskCards();
    }
    
    /**
     * Refresh the task cards display
     */
    public void refresh() {
        root.getChildren().clear();
        refreshTaskCards();
    }
    
    /**
     * Create and display task cards in horizontal grid (3 per row)
     */
    private void refreshTaskCards() {
        if (activeTasks.isEmpty()) {
            // Show empty state
            Label emptyLabel = new Label("🎉 All quests completed! New adventures coming soon!");
            emptyLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: #4CAF50;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 20;"
            );
            root.getChildren().add(emptyLabel);
            return;
        }
        
        // Create horizontal rows with 3 cards each
        HBox currentRow = null;
        int cardCount = 0;
        
        for (Task task : activeTasks) {
            // Create new row every 3 cards with calculated spacing (205px×3 + 12px×2 = 639px)
            if (cardCount % 3 == 0) {
                currentRow = new HBox(12);
                currentRow.setAlignment(Pos.CENTER);
                currentRow.setPadding(new Insets(0, 0, 10, 0));
                root.getChildren().add(currentRow);
            }
            
            // Create perfectly sized task card (3 cards = 640px total)
            VBox taskCard = createCompactTaskCard(task);
            taskCard.setPrefWidth(205);
            taskCard.setMaxWidth(205);
            
            currentRow.getChildren().add(taskCard);
            cardCount++;
        }
    }
    
    /**
     * Create a wide task card for horizontal grid layout with full content visibility
     */
    private VBox createCompactTaskCard(Task task) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: " + getTaskTypeColor(task.getType()) + ";" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 3);"
        );
        
        // Task type icon (large)
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 24px;");
        
        // Task title (maximum width for 205px card)
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
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
    private VBox createTaskCard(Task task) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: " + getTaskTypeColor(task.getType()) + ";" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        // Task header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label typeIcon = new Label(getTaskTypeIcon(task.getType()));
        typeIcon.setStyle("-fx-font-size: 20px;");
        
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
            "-fx-text-fill: #FF9800;" +
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
                "-fx-background-color: #4CAF50;" +
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
            button.setOnAction(e -> completeTask(task));
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
            SoundManager.getInstance().playButtonHover();
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
                "-fx-background-color: #4CAF50;" +
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
            
            button.setOnAction(e -> completeTask(task));
            
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
            SoundManager.getInstance().playButtonHover();
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        return button;
    }
    
    /**
     * Complete a task
     */
    private void completeTask(Task task) {
        SoundManager.getInstance().playButtonClick();
        
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quest Completed!");
        dialog.setHeaderText("🎉 Congratulations, Adventurer!");
        dialog.setContentText(
            "You've completed: " + task.getTitle() + "\n\n" +
            "Rewards earned:\n" +
            "💰 " + task.getRewardCoins() + " SmartCoins\n" +
            "⭐ " + (task.getRewardCoins() * 2) + " Experience Points\n\n" +
            "Keep up the great work!"
        );
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        // Update user stats
        currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + task.getRewardCoins());
        currentUser.setExperiencePoints(currentUser.getExperiencePoints() + (task.getRewardCoins() * 2));
        
        // Remove completed task
        activeTasks.remove(task);
        
        // Play success sound
        SoundManager.getInstance().playSuccess();
        
        dialog.showAndWait();
        refresh(); // Refresh the display
    }
    
    /**
     * View task details
     */
    private void viewTaskDetails(Task task) {
        SoundManager.getInstance().playButtonClick();
        
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Quest Details");
        dialog.setHeaderText(getTaskTypeIcon(task.getType()) + " " + task.getTitle());
        
        String content = task.getDescription() + "\n\n";
        content += "Progress: " + String.format("%.0f%%", task.getProgressPercentage()) + " complete\n";
        content += "Reward: " + task.getRewardCoins() + " SmartCoins\n";
        
        if (task.getDeadline() != null) {
            content += "Deadline: " + task.getDeadline().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) + "\n";
        }
        
        content += "\nTip: " + getTaskTip(task.getType());
        
        dialog.setContentText(content);
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        dialog.showAndWait();
    }
    
    /**
     * Get color for task type
     */
    private String getTaskTypeColor(TaskType type) {
        switch (type) {
            case LEARNING: return "#2196F3";
            case CHORE: return "#FF9800";
            case CREATIVE: return "#9C27B0";
            case PHYSICAL: return "#4CAF50";
            case SOCIAL: return "#E91E63";
            default: return "#607D8B";
        }
    }
    
    /**
     * Get icon for task type
     */
    private String getTaskTypeIcon(TaskType type) {
        switch (type) {
            case LEARNING: return "📚";
            case CHORE: return "🏠";
            case CREATIVE: return "🎨";
            case PHYSICAL: return "⚽";
            case SOCIAL: return "👥";
            default: return "📋";
        }
    }
    
    /**
     * Get helpful tip for task type
     */
    private String getTaskTip(TaskType type) {
        switch (type) {
            case LEARNING: return "Take your time and ask questions if you need help!";
            case CHORE: return "A clean space makes for a happy place!";
            case CREATIVE: return "Let your imagination run wild!";
            case PHYSICAL: return "Stay active and have fun!";
            case SOCIAL: return "Be kind and make new friends!";
            default: return "You've got this, adventurer!";
        }
    }
    
    /**
     * Generate mock tasks for demonstration
     */
    private List<Task> generateMockTasks() {
        List<Task> tasks = new ArrayList<>();
        
        Task task1 = new Task("T001", "Math Adventure: Fractions", 
            "Complete 10 fraction problems in your workbook", TaskType.LEARNING, currentUser.getId());
        task1.setRewardCoins(25);
        task1.setProgressPercentage(75);
        task1.setDeadline(LocalDateTime.now().plusDays(2));
        tasks.add(task1);
        
        Task task2 = new Task("T002", "Clean Your Adventure Base", 
            "Organize your room and make your bed", TaskType.CHORE, currentUser.getId());
        task2.setRewardCoins(15);
        task2.setProgressPercentage(100);
        task2.setDeadline(LocalDateTime.now().plusDays(1));
        tasks.add(task2);
        
        Task task3 = new Task("T003", "Draw Your Dream Castle", 
            "Create a colorful drawing of your ideal castle", TaskType.CREATIVE, currentUser.getId());
        task3.setRewardCoins(20);
        task3.setProgressPercentage(50);
        task3.setDeadline(LocalDateTime.now().plusDays(5));
        tasks.add(task3);
        
        Task task4 = new Task("T004", "Daily Exercise Quest", 
            "Do 20 jumping jacks and run around the yard", TaskType.PHYSICAL, currentUser.getId());
        task4.setRewardCoins(10);
        task4.setProgressPercentage(100);
        task4.setDeadline(LocalDateTime.now());
        tasks.add(task4);
        
        return tasks;
    }
    
    /**
     * Get the root UI component
     */
    public VBox getRoot() {
        return root;
    }
}
