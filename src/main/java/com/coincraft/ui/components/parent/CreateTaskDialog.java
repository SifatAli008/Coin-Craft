package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.Task;
import com.coincraft.models.TaskType;
import com.coincraft.models.User;
import com.coincraft.models.ValidationStatus;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog for merchants/parents to create tasks for their adventurers
 * Comprehensive task creation with assignment, rewards, and deadlines
 */
public class CreateTaskDialog {
    private Stage dialogStage;
    private VBox root;
    
    // Form fields
    private TextField taskTitleField;
    private TextArea taskDescriptionArea;
    private ComboBox<TaskType> taskTypeCombo;
    private ComboBox<String> assignToCombo;
    private Spinner<Integer> rewardCoinsSpinner;
    private Spinner<Integer> difficultySpinner;
    private DatePicker deadlinePicker;
    
    // Buttons
    private Button createButton;
    private Button cancelButton;
    
    // Data
    private List<User> availableAdventurers;
    private User currentParent;
    private Consumer<Task> onTaskCreated;
    
    public CreateTaskDialog(Stage parentStage, User parent, List<User> adventurers, Consumer<Task> onTaskCreated) {
        this.currentParent = parent;
        this.availableAdventurers = adventurers;
        this.onTaskCreated = onTaskCreated;
        initializeDialog(parentStage);
    }
    
    private void initializeDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("📋 Create New Quest");
        dialogStage.setResizable(false);
        
        createUI();
        
        Scene scene = new Scene(root, 600, 700);
        
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
            "-fx-background-color: linear-gradient(to bottom right, #E8F5E8, #F0F8F0, #E8F5E8);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        createHeader();
        createForm();
        createButtons();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("📋 Create New Quest");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Assign a new adventure to your brave explorers");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createForm() {
        VBox form = new VBox(16);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(540);
        
        // Task Title
        VBox titleSection = createFormSection("⚔️ Quest Title", "Give your quest an exciting name");
        taskTitleField = new TextField();
        taskTitleField.setPromptText("e.g., Clean Your Adventure Headquarters");
        taskTitleField.setPrefWidth(500);
        styleTextField(taskTitleField);
        titleSection.getChildren().add(taskTitleField);
        
        // Task Description
        VBox descSection = createFormSection("📜 Quest Description", "Describe what the adventurer needs to do");
        taskDescriptionArea = new TextArea();
        taskDescriptionArea.setPromptText("Provide clear instructions for the quest...");
        taskDescriptionArea.setPrefWidth(500);
        taskDescriptionArea.setPrefRowCount(3);
        taskDescriptionArea.setWrapText(true);
        styleTextArea(taskDescriptionArea);
        descSection.getChildren().add(taskDescriptionArea);
        
        // Task Type and Assignment Row
        HBox typeAssignRow = new HBox(16);
        typeAssignRow.setAlignment(Pos.CENTER);
        
        VBox typeSection = createFormSection("🎯 Quest Type", "Choose the type of quest");
        taskTypeCombo = new ComboBox<>();
        taskTypeCombo.getItems().addAll(TaskType.values());
        taskTypeCombo.setValue(TaskType.CHORE);
        taskTypeCombo.setPrefWidth(240);
        styleComboBox(taskTypeCombo);
        typeSection.getChildren().add(taskTypeCombo);
        
        VBox assignSection = createFormSection("⚔️ Assign To", "Choose which adventurer");
        assignToCombo = new ComboBox<>();
        assignToCombo.getItems().add("All Adventurers");
        for (User adventurer : availableAdventurers) {
            assignToCombo.getItems().add(adventurer.getName());
        }
        assignToCombo.setValue("All Adventurers");
        assignToCombo.setPrefWidth(240);
        styleComboBox(assignToCombo);
        assignSection.getChildren().add(assignToCombo);
        
        typeAssignRow.getChildren().addAll(typeSection, assignSection);
        
        // Reward and Difficulty Row
        HBox rewardDiffRow = new HBox(16);
        rewardDiffRow.setAlignment(Pos.CENTER);
        
        VBox rewardSection = createFormSection("💰 Reward (SmartCoins)", "How many coins to award");
        rewardCoinsSpinner = new Spinner<>(1, 100, 10);
        rewardCoinsSpinner.setPrefWidth(240);
        rewardCoinsSpinner.setEditable(true);
        styleSpinner(rewardCoinsSpinner);
        rewardSection.getChildren().add(rewardCoinsSpinner);
        
        VBox difficultySection = createFormSection("⭐ Difficulty Level", "Quest difficulty (1-5)");
        difficultySpinner = new Spinner<>(1, 5, 2);
        difficultySpinner.setPrefWidth(240);
        difficultySpinner.setEditable(true);
        styleSpinner(difficultySpinner);
        difficultySection.getChildren().add(difficultySpinner);
        
        rewardDiffRow.getChildren().addAll(rewardSection, difficultySection);
        
        // Deadline
        VBox deadlineSection = createFormSection("📅 Deadline (Optional)", "When should this quest be completed?");
        deadlinePicker = new DatePicker();
        deadlinePicker.setPrefWidth(500);
        deadlinePicker.setPromptText("Select deadline date...");
        styleDatePicker(deadlinePicker);
        deadlineSection.getChildren().add(deadlinePicker);
        
        form.getChildren().addAll(
            titleSection,
            descSection,
            typeAssignRow,
            rewardDiffRow,
            deadlineSection
        );
        
        root.getChildren().add(form);
    }
    
    private VBox createFormSection(String title, String subtitle) {
        VBox section = new VBox(6);
        section.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #2E7D32;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        section.getChildren().addAll(titleLabel, subtitleLabel);
        return section;
    }
    
    private void createButtons() {
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        
        cancelButton = new Button("❌ Cancel");
        cancelButton.setPrefWidth(200);
        cancelButton.setPrefHeight(45);
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
        
        createButton = new Button("📋 CREATE QUEST");
        createButton.setPrefWidth(280);
        createButton.setPrefHeight(45);
        createButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
        );
        createButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleCreateTask();
        });
        
        // Hover effects
        addButtonHoverEffects(cancelButton, "#616161", "#757575");
        addButtonHoverEffects(createButton, "#45A049", "#4CAF50");
        
        buttonBox.getChildren().addAll(cancelButton, createButton);
        root.getChildren().add(buttonBox);
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
    
    private void handleCreateTask() {
        // Validate inputs
        String title = taskTitleField.getText().trim();
        String description = taskDescriptionArea.getText().trim();
        TaskType type = taskTypeCombo.getValue();
        String assignTo = assignToCombo.getValue();
        int rewardCoins = rewardCoinsSpinner.getValue();
        int difficulty = difficultySpinner.getValue();
        
        if (title.isEmpty()) {
            showError("Please enter a quest title");
            return;
        }
        
        if (description.isEmpty()) {
            showError("Please enter a quest description");
            return;
        }
        
        if (type == null) {
            showError("Please select a quest type");
            return;
        }
        
        if (assignTo == null) {
            showError("Please select who to assign this quest to");
            return;
        }
        
        // Create new task
        Task newTask = new Task();
        newTask.setTaskId("task_" + System.currentTimeMillis());
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setType(type);
        newTask.setAssignedBy(currentParent.getName());
        newTask.setAssignedTo(getAssignedUserId(assignTo));
        newTask.setRewardCoins(rewardCoins);
        newTask.setDifficultyLevel(difficulty);
        newTask.setValidationStatus(ValidationStatus.PENDING);
        newTask.setCompleted(false);
        newTask.setCreatedAt(LocalDateTime.now());
        
        // Set deadline if provided
        if (deadlinePicker.getValue() != null) {
            newTask.setDeadline(deadlinePicker.getValue().atStartOfDay());
        }
        
        // Save task to Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.saveTask(newTask);
        
        // Show success message
        showSuccess(title, assignTo, rewardCoins);
        
        // Callback to parent dashboard
        if (onTaskCreated != null) {
            onTaskCreated.accept(newTask);
        }
        
        // Close dialog after delay
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), e -> dialogStage.close())
        );
        timeline.play();
    }
    
    private void showError(String message) {
        SoundManager.getInstance().playError();
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Quest Creation Error");
        alert.setHeaderText("Cannot Create Quest");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String questTitle, String assignedTo, int reward) {
        SoundManager.getInstance().playSuccess();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Quest Created Successfully!");
        alert.setHeaderText("New Quest Added to Adventure Board");
        alert.setContentText(
            "Quest: " + questTitle + "\n" +
            "Assigned to: " + assignedTo + "\n" +
            "Reward: " + reward + " SmartCoins\n\n" +
            "Your adventurers will see this quest on their dashboard!"
        );
        alert.showAndWait();
    }
    
    // Styling methods
    private void styleTextField(TextField field) {
        field.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void styleTextArea(TextArea area) {
        area.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void styleComboBox(ComboBox<?> combo) {
        combo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void styleSpinner(Spinner<?> spinner) {
        spinner.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void styleDatePicker(DatePicker picker) {
        picker.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private String getAssignedUserId(String assignToName) {
        if ("All Adventurers".equals(assignToName)) {
            return "ALL_ADVENTURERS"; // Special identifier for tasks assigned to all
        }
        
        // Find the user ID for the selected adventurer
        for (User adventurer : availableAdventurers) {
            if (adventurer.getName().equals(assignToName)) {
                return adventurer.getUserId();
            }
        }
        
        return null; // Should not happen if validation is working
    }
    
    public void show() {
        dialogStage.show();
    }
}
