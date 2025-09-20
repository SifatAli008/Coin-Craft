package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;
import com.coincraft.services.RewardService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog for parents to verify and approve/reject completed tasks
 */
public class TaskVerificationDialog {
    private static final Logger LOGGER = Logger.getLogger(TaskVerificationDialog.class.getName());
    private Stage dialogStage;
    private VBox root;
    private Task task;
    private Consumer<Task> onTaskVerified;
    
    // UI Components
    private TextArea completionNotesArea;
    private TextArea feedbackArea;
    private Button approveButton;
    private Button rejectButton;
    private Button cancelButton;
    
    public TaskVerificationDialog(Stage parentStage, Task task, Consumer<Task> onTaskVerified) {
        this.task = task;
        this.onTaskVerified = onTaskVerified;
        initializeDialog(parentStage);
    }
    
    private void initializeDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("📋 Verify Quest Completion");
        dialogStage.setResizable(false);
        
        createUI();
        
        Scene scene = new Scene(root, 600, 500);
        
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
            "-fx-background-color: linear-gradient(to bottom right, #FFF3E0, #E8F5E8, #FFF3E0);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        createHeader();
        createTaskInfo();
        createCompletionEvidence();
        createActionButtons();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("📋 Verify Quest Completion");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #F57C00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Review the adventurer's completion evidence and decide whether to approve or reject");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createTaskInfo() {
        VBox taskInfo = new VBox(12);
        taskInfo.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        Label taskTitle = new Label("📋 " + task.getTitle());
        taskTitle.setStyle(
            "-fx-font-size: 18px;" +
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
        
        HBox detailsRow = new HBox(20);
        detailsRow.setAlignment(Pos.CENTER_LEFT);
        
        Label rewardInfo = new Label("💰 Reward: " + task.getRewardCoins() + " SmartCoins");
        rewardInfo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label difficultyInfo = new Label("⭐ Difficulty: Level " + task.getDifficultyLevel());
        difficultyInfo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label completedInfo = new Label("✅ Completed: " + task.getCompletedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")));
        completedInfo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        detailsRow.getChildren().addAll(rewardInfo, difficultyInfo, completedInfo);
        taskInfo.getChildren().addAll(taskTitle, taskDesc, detailsRow);
        root.getChildren().add(taskInfo);
    }
    
    private void createCompletionEvidence() {
        VBox evidenceSection = new VBox(12);
        evidenceSection.setAlignment(Pos.CENTER_LEFT);
        
        Label evidenceLabel = new Label("📝 Adventurer's Completion Evidence:");
        evidenceLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        completionNotesArea = new TextArea(task.getCompletionNotes());
        completionNotesArea.setEditable(false);
        completionNotesArea.setPrefRowCount(4);
        completionNotesArea.setWrapText(true);
        completionNotesArea.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label feedbackLabel = new Label("💬 Your Feedback (Optional):");
        feedbackLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        feedbackArea = new TextArea();
        feedbackArea.setPromptText("Provide feedback for the adventurer...");
        feedbackArea.setPrefRowCount(2);
        feedbackArea.setWrapText(true);
        feedbackArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        evidenceSection.getChildren().addAll(evidenceLabel, completionNotesArea, feedbackLabel, feedbackArea);
        root.getChildren().add(evidenceSection);
    }
    
    private void createActionButtons() {
        VBox buttonSection = new VBox(12);
        
        Label actionLabel = new Label("⚖️ Verification Decision:");
        actionLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        
        cancelButton = new Button("❌ Cancel");
        cancelButton.setPrefWidth(120);
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
            SoundManager.getInstance().playButtonClick();
            dialogStage.close();
        });
        
        rejectButton = new Button("❌ Reject");
        rejectButton.setPrefWidth(120);
        rejectButton.setPrefHeight(40);
        rejectButton.setStyle(
            "-fx-background-color: #F44336;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        rejectButton.setOnAction(e -> handleTaskRejection());
        
        approveButton = new Button("✅ Approve");
        approveButton.setPrefWidth(150);
        approveButton.setPrefHeight(40);
        approveButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
        );
        approveButton.setOnAction(e -> handleTaskApproval());
        
        // Hover effects
        addButtonHoverEffects(cancelButton, "#616161", "#757575");
        addButtonHoverEffects(rejectButton, "#D32F2F", "#F44336");
        addButtonHoverEffects(approveButton, "#45A049", "#4CAF50");
        
        buttonBox.getChildren().addAll(cancelButton, rejectButton, approveButton);
        buttonSection.getChildren().addAll(actionLabel, buttonBox);
        root.getChildren().add(buttonSection);
    }
    
    private void addButtonHoverEffects(Button button, String hoverColor, String normalColor) {
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
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
    
    private void handleTaskApproval() {
        // Confirm approval
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("✅ Confirm Approval");
        confirmAlert.setHeaderText("Approve Quest Completion?");
        confirmAlert.setContentText("Are you sure you want to approve this quest completion?\n\nThe adventurer will receive " + task.getRewardCoins() + " SmartCoins.");
        
        confirmAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        
        if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            approveTask();
        }
    }
    
    private void handleTaskRejection() {
        String feedback = feedbackArea.getText().trim();
        
        if (feedback.isEmpty()) {
            showError("Please provide feedback explaining why the quest is being rejected");
            return;
        }
        
        // Confirm rejection
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("❌ Confirm Rejection");
        confirmAlert.setHeaderText("Reject Quest Completion?");
        confirmAlert.setContentText("Are you sure you want to reject this quest completion?\n\nThe adventurer will need to complete the quest again.");
        
        confirmAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        
        if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            rejectTask();
        }
    }
    
    private void approveTask() {
        SoundManager.getInstance().playSuccess();
        
        // Update task status
        task.setValidationStatus(ValidationStatus.APPROVED);
        task.setCompleted(true);
        
        // Add feedback if provided
        String feedback = feedbackArea.getText().trim();
        if (!feedback.isEmpty()) {
            String currentNotes = task.getCompletionNotes();
            task.setCompletionNotes(currentNotes + "\n\nParent Feedback: " + feedback);
        }
        
        // Save to Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.saveTask(task);
        
        // Award coins to the assigned adventurer
        if (task.getAssignedTo() != null && !task.getAssignedTo().equals("ALL_ADVENTURERS")) {
            User adventurer = firebaseService.getUserById(task.getAssignedTo());
            if (adventurer != null) {
                RewardService rewardService = RewardService.getInstance();
                boolean rewardSuccess = rewardService.awardCoinsForTask(adventurer, task);
                
                if (rewardSuccess) {
                    LOGGER.info("Successfully awarded coins to adventurer " + adventurer.getName());
                } else {
                    LOGGER.warning("Failed to award coins to adventurer " + adventurer.getName());
                }
            }
        }
        
        // Show success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("✅ Quest Approved!");
        successAlert.setHeaderText("Quest Completion Approved");
        successAlert.setContentText("The adventurer will receive " + task.getRewardCoins() + " SmartCoins for completing this quest!");
        successAlert.showAndWait();
        
        // Callback to parent
        if (onTaskVerified != null) {
            onTaskVerified.accept(task);
        }
        
        dialogStage.close();
    }
    
    private void rejectTask() {
        SoundManager.getInstance().playError();
        
        // Update task status
        task.setValidationStatus(ValidationStatus.REJECTED);
        task.setCompleted(false); // Allow re-completion
        task.setCompletedAt(null);
        
        // Add rejection feedback
        String feedback = feedbackArea.getText().trim();
        task.setCompletionNotes(task.getCompletionNotes() + "\n\nRejection Reason: " + feedback);
        
        // Save to Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.saveTask(task);
        
        // Show rejection message
        Alert rejectAlert = new Alert(Alert.AlertType.INFORMATION);
        rejectAlert.setTitle("❌ Quest Rejected");
        rejectAlert.setHeaderText("Quest Completion Rejected");
        rejectAlert.setContentText("The quest has been rejected. The adventurer can attempt to complete it again.");
        rejectAlert.showAndWait();
        
        // Callback to parent
        if (onTaskVerified != null) {
            onTaskVerified.accept(task);
        }
        
        dialogStage.close();
    }
    
    private void showError(String message) {
        SoundManager.getInstance().playError();
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Verification Error");
        alert.setHeaderText("Cannot Process Verification");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        dialogStage.show();
    }
}
