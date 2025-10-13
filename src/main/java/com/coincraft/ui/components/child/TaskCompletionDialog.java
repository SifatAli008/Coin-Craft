package com.coincraft.ui.components.child;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.Task;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog for adventurers to mark tasks as complete and provide evidence
 */
public class TaskCompletionDialog {
    private Stage dialogStage;
    private VBox root;
    private final Task task;
    private final Consumer<Task> onTaskCompleted;
    
    // UI Components
    private TextArea completionNotesArea;
    private Button completeButton;
    private Button cancelButton;
    
    public TaskCompletionDialog(Stage parentStage, Task task, Consumer<Task> onTaskCompleted) {
        this.task = task;
        this.onTaskCompleted = onTaskCompleted;
        initializeDialog(parentStage);
    }
    
    private void initializeDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("⚔️ Complete Task");
        dialogStage.setResizable(true);
        
        createUI();
        
        Scene scene = new Scene(root, 640, 520);
        dialogStage.setMinWidth(600);
        dialogStage.setMinHeight(480);
        
        // Load CSS styles
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        dialogStage.setScene(scene);
    }
    
    private void createUI() {
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #F3E5F5, #FFF8E1);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        createHeader();
        createTaskInfo();
        createCompletionForm();
        createButtons();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("⚔️ Complete Task");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1976D2;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Mark your task as complete and provide evidence");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createTaskInfo() {
        VBox taskInfo = new VBox(8);
        taskInfo.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        Label taskTitle = new Label("📋 " + task.getTitle());
        taskTitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label taskDesc = new Label(task.getDescription());
        taskDesc.setWrapText(true);
        taskDesc.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #555555;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label rewardInfo = new Label("💰 Reward: " + task.getRewardCoins() + " SmartCoins");
        rewardInfo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        taskInfo.getChildren().addAll(taskTitle, taskDesc, rewardInfo);
        root.getChildren().add(taskInfo);
    }
    
    private void createCompletionForm() {
        VBox form = new VBox(12);
        form.setAlignment(Pos.CENTER_LEFT);
        
        Label notesLabel = new Label("📝 Completion Evidence:");
        notesLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label notesSubtitle = new Label("Describe what you did to complete this task (required for approval)");
        notesSubtitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        completionNotesArea = new TextArea();
        completionNotesArea.setPromptText("I completed this task by...\n\nProvide specific details about what you did...");
        completionNotesArea.setPrefRowCount(6);
        completionNotesArea.setPrefWidth(560);
        completionNotesArea.setWrapText(true);
        completionNotesArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        form.getChildren().addAll(notesLabel, notesSubtitle, completionNotesArea);
        root.getChildren().add(form);
    }
    
    private void createButtons() {
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        
        cancelButton = new Button("❌ Cancel");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(40);
        cancelButton.setStyle(
            "-fx-background-color: #757575;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        cancelButton.setOnAction(e -> {
            CentralizedMusicManager.getInstance().playButtonClick();
            dialogStage.close();
        });
        
        completeButton = new Button("✅ COMPLETE TASK");
        completeButton.setPrefWidth(200);
        completeButton.setPrefHeight(40);
        completeButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
        );
        completeButton.setOnAction(e -> handleTaskCompletion());
        
        // Hover effects
        addButtonHoverEffects(cancelButton, "#616161", "#757575");
        addButtonHoverEffects(completeButton, "#45A049", "#4CAF50");
        
        buttonBox.getChildren().addAll(cancelButton, completeButton);
        root.getChildren().add(buttonBox);
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
    
    private void handleTaskCompletion() {
        String completionNotes = completionNotesArea.getText().trim();
        
        if (completionNotes.isEmpty()) {
            showError("Please provide evidence of completion");
            return;
        }
        
        if (completionNotes.length() < 10) {
            showError("Please provide more detailed evidence (at least 10 characters)");
            return;
        }
        
        // Update task status
        task.setCompleted(true);
        task.setCompletedAt(LocalDateTime.now());
        task.setCompletionNotes(completionNotes);
        task.setValidationStatus(ValidationStatus.AWAITING_APPROVAL);
        task.setProgressPercentage(100.0);
        
        // Save to Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.saveTask(task);
        
        // Show success message
        showSuccess();
        
        // Callback to parent
        if (onTaskCompleted != null) {
            onTaskCompleted.accept(task);
        }
        
        // Close dialog after delay
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), e -> dialogStage.close())
        );
        timeline.play();
    }
    
    private void showError(String message) {
        CentralizedMusicManager.getInstance().playError();
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Task Completion Error");
        alert.setHeaderText("Cannot Complete Task");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess() {
        CentralizedMusicManager.getInstance().playSuccess();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Task Submitted for Review!");
        alert.setHeaderText("Task Completion Submitted");
        alert.setContentText("""
            Your task has been marked as complete and submitted for review!
            
            A parent or guardian will review your completion evidence and approve or reject it.
            
            You'll receive your %d SmartCoins once approved!
            """.formatted(task.getRewardCoins()));
        alert.showAndWait();
    }
    
    public void show() {
        dialogStage.show();
    }
}
