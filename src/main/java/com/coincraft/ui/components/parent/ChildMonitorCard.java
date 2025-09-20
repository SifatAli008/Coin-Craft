package com.coincraft.ui.components.parent;

import java.util.ArrayList;
import java.util.List;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Child monitoring card for Parent Dashboard
 * Displays individual child's progress, recent activity, and quick actions
 * Features modern card design with progress indicators and action buttons
 */
public class ChildMonitorCard {
    private VBox root;
    private User childUser;
    
    // Real data from Firebase
    private List<Task> recentTasks;
    private int weeklyProgress = 0; // Will be calculated from real data
    private int smartCoinsEarned = 0;
    private String lastActivity = "No recent activity";
    
    public ChildMonitorCard(User child) {
        this.childUser = child;
        loadRealData();
        initializeUI();
    }
    
    private void loadRealData() {
        recentTasks = new ArrayList<>();
        
        // Load real task data for this adventurer
        try {
            // Use the child's actual data
            smartCoinsEarned = childUser.getSmartCoinBalance();
            
            // Calculate progress based on real data
            weeklyProgress = Math.min(100, (smartCoinsEarned / 5)); // 5 coins = 1% progress
            
            // Set last activity based on last login
            if (childUser.getLastLogin() != null) {
                lastActivity = "Active recently";
            } else {
                lastActivity = "No recent activity";
            }
            
            // Load real tasks (if available)
            // In a real app, you'd load tasks specific to this child
            // For now, we'll show empty list until tasks are implemented
            
            System.out.println("📊 Loaded real data for adventurer: " + childUser.getName());
            
        } catch (Exception e) {
            System.out.println("⚠️ Could not load real data for " + childUser.getName() + ": " + e.getMessage());
        }
    }
    
    private void initializeUI() {
        root = new VBox(16);
        root.setPadding(new Insets(20));
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);" +
            "-fx-pref-width: 320;" +
            "-fx-max-width: 320;"
        );
        
        createHeader();
        createProgressSection();
        createActivitySection();
        createActionButtons();
    }
    
    private void createHeader() {
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Avatar placeholder
        VBox avatar = new VBox();
        avatar.setPrefSize(50, 50);
        avatar.setMaxSize(50, 50);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #4CAF50, #45A049);" +
            "-fx-background-radius: 25;" +
            "-fx-border-radius: 25;"
        );
        
        Label avatarLabel = new Label(childUser.getName().substring(0, 1).toUpperCase());
        avatarLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        avatar.getChildren().add(avatarLabel);
        
        // Child info
        VBox info = new VBox(4);
        info.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(childUser.getName());
        nameLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label levelLabel = new Label("Level " + childUser.getLevel() + " • " + smartCoinsEarned + " SmartCoins");
        levelLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label statusLabel = new Label("🟢 Active " + lastActivity);
        statusLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        info.getChildren().addAll(nameLabel, levelLabel, statusLabel);
        
        header.getChildren().addAll(avatar, info);
        root.getChildren().add(header);
    }
    
    private void createProgressSection() {
        VBox progressSection = new VBox(8);
        
        Label progressLabel = new Label("Weekly Progress");
        progressLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        ProgressBar progressBar = new ProgressBar(weeklyProgress / 100.0);
        progressBar.setPrefHeight(8);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle(
            "-fx-accent: #4CAF50;" +
            "-fx-background-color: rgba(76, 175, 80, 0.2);" +
            "-fx-background-radius: 4;" +
            "-fx-background-insets: 0;"
        );
        
        HBox progressInfo = new HBox();
        progressInfo.setAlignment(Pos.CENTER_LEFT);
        
        Label percentLabel = new Label(weeklyProgress + "%");
        percentLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label tasksLabel = new Label(recentTasks.size() + " tasks this week");
        tasksLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        progressInfo.getChildren().addAll(percentLabel, spacer, tasksLabel);
        
        progressSection.getChildren().addAll(progressLabel, progressBar, progressInfo);
        root.getChildren().add(progressSection);
    }
    
    private void createActivitySection() {
        VBox activitySection = new VBox(8);
        
        Label activityLabel = new Label("Recent Activity");
        activityLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        VBox tasksList = new VBox(6);
        for (int i = 0; i < Math.min(3, recentTasks.size()); i++) {
            Task task = recentTasks.get(i);
            HBox taskItem = createTaskItem(task);
            tasksList.getChildren().add(taskItem);
        }
        
        activitySection.getChildren().addAll(activityLabel, tasksList);
        root.getChildren().add(activitySection);
    }
    
    private HBox createTaskItem(Task task) {
        HBox taskItem = new HBox(8);
        taskItem.setAlignment(Pos.CENTER_LEFT);
        taskItem.setPadding(new Insets(6, 8, 6, 8));
        taskItem.setStyle(
            "-fx-background-color: rgba(76, 175, 80, 0.1);" +
            "-fx-background-radius: 6;"
        );
        
        // Task type icon
        String icon = getTaskTypeIcon(task.getType());
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 14px;");
        
        // Task info
        VBox taskInfo = new VBox(2);
        
        Label taskName = new Label(task.getDescription());
        taskName.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label taskReward = new Label("+" + task.getRewardCoins() + " SmartCoins");
        taskReward.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        taskInfo.getChildren().addAll(taskName, taskReward);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Status
        Label statusLabel = new Label("✅");
        statusLabel.setStyle("-fx-font-size: 12px;");
        
        taskItem.getChildren().addAll(iconLabel, taskInfo, spacer, statusLabel);
        return taskItem;
    }
    
    private String getTaskTypeIcon(TaskType type) {
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
    
    private void createActionButtons() {
        HBox buttonRow = new HBox(8);
        buttonRow.setAlignment(Pos.CENTER);
        
        Button viewDetailsBtn = createActionButton("View Details", "#2196F3", "#1976D2");
        Button sendMessageBtn = createActionButton("Send Message", "#FF9800", "#F57C00");
        
        viewDetailsBtn.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            openViewDetailsDialog();
        });
        
        sendMessageBtn.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            openMessagingPortal();
        });
        
        buttonRow.getChildren().addAll(viewDetailsBtn, sendMessageBtn);
        root.getChildren().add(buttonRow);
    }
    
    private Button createActionButton(String text, String primaryColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefWidth(120);
        button.setStyle(
            "-fx-background-color: " + primaryColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 6 12;"
        );
        
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            button.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: " + primaryColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;"
            );
        });
        
        return button;
    }
    
    public VBox getRoot() {
        return root;
    }
    
    private void openViewDetailsDialog() {
        try {
            // Get the current stage from the scene
            Stage currentStage = (Stage) root.getScene().getWindow();
            
            // Open the View Details dialog
            ViewAdventurerDetailsDialog detailsDialog = new ViewAdventurerDetailsDialog(currentStage, childUser);
            detailsDialog.show();
            
        } catch (Exception e) {
            System.out.println("Error opening view details dialog: " + e.getMessage());
        }
    }
    
    private void openMessagingPortal() {
        try {
            // Get the current stage from the scene
            Stage currentStage = (Stage) root.getScene().getWindow();
            
            // Open the messaging portal
            AdventureMessagingPortal messagingPortal = new AdventureMessagingPortal(currentStage, childUser);
            messagingPortal.show();
            
        } catch (Exception e) {
            System.out.println("Error opening messaging portal: " + e.getMessage());
        }
    }
    
    public User getChildUser() {
        return childUser;
    }
}
