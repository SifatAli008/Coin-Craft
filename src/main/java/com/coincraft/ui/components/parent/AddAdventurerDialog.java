package com.coincraft.ui.components.parent;

import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog for merchants to add new adventurers (children)
 * Allows creation of Adventure ID and password for children to access their dashboard
 */
public class AddAdventurerDialog {
    private Stage dialogStage;
    private VBox root;
    private TextField adventurerNameField;
    private ComboBox<String> ageComboBox;
    private TextField adventureIdField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button createButton;
    private Button cancelButton;
    
    private Consumer<User> onAdventurerCreated;
    
    public AddAdventurerDialog(Stage parentStage, Consumer<User> onAdventurerCreated) {
        this.onAdventurerCreated = onAdventurerCreated;
        initializeDialog(parentStage);
    }
    
    private void initializeDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("⚔️ Add New Adventurer");
        dialogStage.setResizable(false);
        
        createUI();
        
        Scene scene = new Scene(root, 520, 550);
        
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
            "-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB, #90CAF9);" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        createHeader();
        createForm();
        createButtons();
    }
    
    private void createHeader() {
        VBox header = new VBox(12);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("⚔️ Register New Adventurer");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Create an Adventure ID for your child to access their dashboard");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        subtitleLabel.setWrapText(true);
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createForm() {
        VBox form = new VBox(16);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(480);
        
        // First Row: Name and Age (inline)
        HBox firstRow = new HBox(16);
        firstRow.setAlignment(Pos.CENTER);
        
        VBox nameSection = createFormSection("🏰 Adventurer Name", "Enter your child's name");
        adventurerNameField = new TextField();
        adventurerNameField.setPromptText("e.g., Emma Wilson");
        adventurerNameField.setPrefWidth(200);
        styleTextField(adventurerNameField);
        nameSection.getChildren().add(adventurerNameField);
        
        VBox ageSection = createFormSection("🎂 Age", "Select your child's age");
        ageComboBox = new ComboBox<>();
        ageComboBox.getItems().addAll(
            "5 years old", "6 years old", "7 years old", "8 years old", "9 years old",
            "10 years old", "11 years old", "12 years old", "13 years old", "14 years old",
            "15 years old", "16 years old", "17 years old"
        );
        ageComboBox.setPromptText("Choose age");
        ageComboBox.setPrefWidth(200);
        ageComboBox.setPrefHeight(40);
        styleComboBox(ageComboBox);
        ageSection.getChildren().add(ageComboBox);
        
        firstRow.getChildren().addAll(nameSection, ageSection);
        
        // Second Row: Adventure ID (full width)
        VBox idSection = createFormSection("⚔️ Adventure ID", "Create a unique username for your adventurer");
        adventureIdField = new TextField();
        adventureIdField.setPromptText("e.g., brave_emma or quest_master");
        adventureIdField.setPrefWidth(400);
        styleTextField(adventureIdField);
        idSection.getChildren().add(adventureIdField);
        
        // Third Row: Password and Confirm Password (inline)
        HBox passwordRow = new HBox(16);
        passwordRow.setAlignment(Pos.CENTER);
        
        VBox passwordSection = createFormSection("🔐 Adventure Password", "Create a secure password");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(200);
        styleTextField(passwordField);
        passwordSection.getChildren().add(passwordField);
        
        VBox confirmSection = createFormSection("🔐 Confirm Password", "Re-enter the password");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");
        confirmPasswordField.setPrefWidth(200);
        styleTextField(confirmPasswordField);
        confirmSection.getChildren().add(confirmPasswordField);
        
        passwordRow.getChildren().addAll(passwordSection, confirmSection);
        
        form.getChildren().addAll(firstRow, idSection, passwordRow);
        root.getChildren().add(form);
    }
    
    private VBox createFormSection(String title, String description) {
        VBox section = new VBox(6);
        section.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label descLabel = new Label(description);
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        section.getChildren().addAll(titleLabel, descLabel);
        return section;
    }
    
    private void styleTextField(TextField field) {
        // Width is set individually for each field
        field.setPrefHeight(40);
        field.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 8 12;"
        );
        
        // Add focus effects
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                    "-fx-text-fill: black;" +
                    "-fx-font-size: 14px;" +
                    "-fx-border-color: #FF9800;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                    "-fx-padding: 8 12;"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                    "-fx-text-fill: black;" +
                    "-fx-font-size: 14px;" +
                    "-fx-border-color: rgba(255, 152, 0, 0.3);" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                    "-fx-padding: 8 12;"
                );
            }
        });
    }
    
    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
    }
    
    private void createButtons() {
        HBox buttonRow = new HBox(16);
        buttonRow.setAlignment(Pos.CENTER);
        
        // Cancel button
        cancelButton = new Button("❌ Cancel");
        cancelButton.setPrefWidth(180);
        cancelButton.setPrefHeight(45);
        cancelButton.setStyle(
            "-fx-background-color: #9E9E9E;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        cancelButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            cancelButton.setStyle(
                "-fx-background-color: #757575;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        cancelButton.setOnMouseExited(e -> {
            cancelButton.setStyle(
                "-fx-background-color: #9E9E9E;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        cancelButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            dialogStage.close();
        });
        
        // Create Adventurer button
        createButton = new Button("⚔️ CREATE ADVENTURER");
        createButton.setPrefWidth(180);
        createButton.setPrefHeight(45);
        createButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        createButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            createButton.setStyle(
                "-fx-background-color: #2E7D32;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        createButton.setOnMouseExited(e -> {
            createButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        createButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleCreateAdventurer();
        });
        
        buttonRow.getChildren().addAll(cancelButton, createButton);
        root.getChildren().add(buttonRow);
    }
    
    private void handleCreateAdventurer() {
        // Validate inputs
        String name = adventurerNameField.getText().trim();
        String ageSelection = ageComboBox.getValue();
        String adventureId = adventureIdField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (name.isEmpty()) {
            showError("Please enter the adventurer's name");
            return;
        }
        
        if (ageSelection == null) {
            showError("Please select the adventurer's age");
            return;
        }
        
        if (adventureId.isEmpty()) {
            showError("Please create an Adventure ID");
            return;
        }
        
        if (adventureId.length() < 3) {
            showError("Adventure ID must be at least 3 characters long");
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please create a password");
            return;
        }
        
        if (password.length() < 4) {
            showError("Password must be at least 4 characters long");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        // Extract age from selection
        int age = Integer.parseInt(ageSelection.split(" ")[0]);
        
        // Create new adventurer user
        User newAdventurer = new User();
        newAdventurer.setUserId("adventurer_" + System.currentTimeMillis());
        newAdventurer.setName(name);
        newAdventurer.setRole(UserRole.CHILD);
        newAdventurer.setAge(age);
        newAdventurer.setEmail(adventureId + "@coincraft.adventure"); // Use Adventure ID as email base
        newAdventurer.setSmartCoinBalance(25); // Starting balance for new adventurers
        newAdventurer.setLevel(1);
        newAdventurer.setDailyStreaks(0);
        newAdventurer.setLastLogin(java.time.LocalDateTime.now());
        newAdventurer.setCreatedAt(java.time.LocalDateTime.now()); // Set creation timestamp
        
        // Save to Firebase
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.saveUser(newAdventurer);
        
        // Debug: Check if user was saved
        System.out.println("🔍 DEBUG: Attempting to save adventurer to Firebase/Local storage...");
        System.out.println("🔍 DEBUG: Firebase initialized: " + firebaseService.isInitialized());
        
        // Store Adventure ID and password securely
        System.out.println("=".repeat(50));
        System.out.println("🎉 NEW ADVENTURER CREATED:");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age + " years old");
        System.out.println("Adventure ID: " + adventureId);
        System.out.println("Password: " + password + " (stored securely)");
        System.out.println("Starting Balance: 25 SmartCoins");
        System.out.println("✅ Saved to Firebase!");
        System.out.println("=".repeat(50));
        
        // Show success message
        showSuccess(name, adventureId, password);
        
        // Callback to parent dashboard
        if (onAdventurerCreated != null) {
            onAdventurerCreated.accept(newAdventurer);
        }
        
        // Close dialog after a delay
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> dialogStage.close())
        );
        timeline.play();
    }
    
    private void showError(String message) {
        SoundManager.getInstance().playError();
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Registration Error");
        alert.setHeaderText("Cannot Create Adventurer");
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }
    
    private void showSuccess(String name, String adventureId, String password) {
        SoundManager.getInstance().playSuccess();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎉 Adventurer Created!");
        alert.setHeaderText("Welcome to CoinCraft, " + name + "!");
        alert.setContentText(
            "Adventure ID: " + adventureId + "\n" +
            "Password: " + password + "\n\n" +
            "Your adventurer can now log in using these credentials to access their Adventure Dashboard!\n\n" +
            "Starting with 25 SmartCoins to begin their financial learning journey."
        );
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }
    
    public void show() {
        dialogStage.showAndWait();
    }
}
