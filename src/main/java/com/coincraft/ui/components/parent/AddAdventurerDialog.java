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
import javafx.scene.control.ScrollPane;
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
    private TextField adventureUsernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button createButton;
    private Button cancelButton;
    private Label adventureUsernameStatusLabel;
    
    private final Consumer<User> onAdventurerCreated;
    private final User creatorParent;
    
    public AddAdventurerDialog(Stage parentStage, Consumer<User> onAdventurerCreated) {
        this.onAdventurerCreated = onAdventurerCreated;
        this.creatorParent = null;
        initializeDialog(parentStage);
    }

    public AddAdventurerDialog(Stage parentStage, Consumer<User> onAdventurerCreated, User currentParent) {
        this.onAdventurerCreated = onAdventurerCreated;
        this.creatorParent = currentParent;
        initializeDialog(parentStage);
    }
    
    private void initializeDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("⚔️ Add New Adventurer");
        dialogStage.setResizable(true);
        
        createUI();
        
        // Wrap content in a scroll container to avoid clipping on smaller screens
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(scrollPane, 520, 550);
        
        // Load CSS styles
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        dialogStage.setScene(scene);
        dialogStage.centerOnScreen();
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
        
        // Second Row: Adventure Username (full width)
        VBox usernameSection = createFormSection("⚔️ Adventure Username", "Create a unique username for your adventurer");
        adventureUsernameField = new TextField();
        adventureUsernameField.setPromptText("e.g., brave_emma or quest_master");
        adventureUsernameField.setPrefWidth(400);
        styleTextField(adventureUsernameField);
        
        // Add real-time validation for Adventure Username
        adventureUsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateAdventureUsernameRealTime(newValue);
        });
        
        // Add status label for Adventure Username validation feedback
        adventureUsernameStatusLabel = new Label();
        adventureUsernameStatusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 4 0 0 0;"
        );
        adventureUsernameStatusLabel.setVisible(false);
        
        usernameSection.getChildren().addAll(adventureUsernameField, adventureUsernameStatusLabel);
        
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
        
        form.getChildren().addAll(firstRow, usernameSection, passwordRow);
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
        String adventureUsername = adventureUsernameField.getText().trim();
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
        
        if (adventureUsername.isEmpty()) {
            showError("Please create an Adventure Username");
            return;
        }
        
        if (adventureUsername.length() < 3) {
            showError("Adventure Username must be at least 3 characters long");
            return;
        }
        
        // Check for invalid characters
        if (!adventureUsername.matches("^[a-zA-Z0-9_]+$")) {
            showError("Adventure Username can only contain letters, numbers, and underscores");
            return;
        }
        
        // Check if Adventure Username is already taken
        FirebaseService firebaseService = FirebaseService.getInstance();
        if (firebaseService.isAdventureUsernameTaken(adventureUsername)) {
            showError("Adventure Username '" + adventureUsername + "' is already taken. Please choose a different one.");
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
        
        try {
            // Extract age from selection
            int age = Integer.parseInt(ageSelection.split(" ")[0]);
            
            // Create new adventurer user
            User newAdventurer = new User();
            newAdventurer.setUserId("adventurer_" + System.currentTimeMillis());
            newAdventurer.setName(name);
            newAdventurer.setUsername(adventureUsername); // Set the adventure username
            newAdventurer.setRole(UserRole.CHILD);
            newAdventurer.setAge(age);
            newAdventurer.setEmail(adventureUsername + "@coincraft.adventure"); // Use Adventure Username as email base
            newAdventurer.setSmartCoinBalance(25); // Starting balance for new adventurers
            newAdventurer.setLevel(1);
            newAdventurer.setDailyStreaks(0);
            newAdventurer.setLastLogin(java.time.LocalDateTime.now());
            newAdventurer.setCreatedAt(java.time.LocalDateTime.now()); // Set creation timestamp
            // Link this child to the creating merchant/parent
            try {
                // Prefer parent passed in from dashboard
                if (creatorParent != null) {
                    String pid = creatorParent.getUserId();
                    if (pid == null || pid.isEmpty()) {
                        String email = creatorParent.getEmail();
                        if (email != null && !email.isEmpty()) {
                            pid = "email:" + email.toLowerCase();
                        }
                    }
                    if (pid != null && !pid.isEmpty()) {
                        newAdventurer.setParentId(pid);
                    }
                } else {
                    // Fallback: fetch current parent from router if available
                    com.coincraft.ui.routing.DashboardRouter router = com.coincraft.ui.routing.DashboardRouter.getInstance();
                    com.coincraft.models.User currentParent = router.getCurrentUser();
                    if (currentParent != null) {
                        String pid = currentParent.getUserId();
                        if (pid == null || pid.isEmpty()) {
                            String email = currentParent.getEmail();
                            if (email != null && !email.isEmpty()) {
                                pid = "email:" + email.toLowerCase();
                            }
                        }
                        if (pid != null && !pid.isEmpty()) {
                            newAdventurer.setParentId(pid);
                        }
                    }
                }
            } catch (Exception ignored) {}
        
        // Ensure Firebase is initialized before saving
        if (!firebaseService.isInitialized()) {
            System.out.println("🔍 DEBUG: Firebase not initialized, initializing now...");
            firebaseService.initialize();
        }
        
        // Save to Firebase/Local storage
        System.out.println("🔍 DEBUG: Saving adventurer to Firebase/Local storage...");
        firebaseService.saveUser(newAdventurer);
        
        // Store Adventure Username and password mapping for authentication
        System.out.println("🔍 DEBUG: Storing adventurer credentials...");
        firebaseService.storeAdventurerCredentials(adventureUsername, password, newAdventurer.getUserId());
        
        // Verify the adventurer was saved correctly
        User savedAdventurer = firebaseService.loadUser(newAdventurer.getUserId());
        if (savedAdventurer != null) {
            System.out.println("✅ DEBUG: Adventurer successfully saved and verified!");
            System.out.println("✅ DEBUG: Saved adventurer name: " + savedAdventurer.getName());
            System.out.println("✅ DEBUG: Saved adventurer username: " + savedAdventurer.getUsername());
            System.out.println("✅ DEBUG: Saved adventurer role: " + savedAdventurer.getRole());
        } else {
            System.out.println("⚠️ DEBUG: Warning - Could not verify adventurer was saved!");
        }
        
        // Verify credentials were stored correctly
        String verifiedUserId = firebaseService.verifyAdventurerCredentials(adventureUsername, password);
        if (verifiedUserId != null && verifiedUserId.equals(newAdventurer.getUserId())) {
            System.out.println("✅ DEBUG: Adventurer credentials successfully stored and verified!");
            
            // Run complete flow test to ensure everything works
            System.out.println("🧪 DEBUG: Running complete adventurer flow test...");
            boolean flowTestPassed = firebaseService.testAdventurerFlow(adventureUsername, password, name, age);
            if (flowTestPassed) {
                System.out.println("✅ DEBUG: Complete adventurer flow test PASSED!");
            } else {
                System.out.println("❌ DEBUG: Complete adventurer flow test FAILED!");
            }
        } else {
            System.out.println("⚠️ DEBUG: Warning - Could not verify adventurer credentials!");
        }
        
        System.out.println("🔍 DEBUG: Firebase initialized: " + firebaseService.isInitialized());
        
        // Store Adventure Username and password securely
        System.out.println("=".repeat(50));
        System.out.println("🎉 NEW ADVENTURER CREATED:");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age + " years old");
        System.out.println("Adventure Username: " + adventureUsername);
        System.out.println("Password: " + password + " (stored securely)");
        System.out.println("Starting Balance: 25 SmartCoins");
        System.out.println("✅ Saved to Firebase!");
        System.out.println("=".repeat(50));
        
        // Show success message
        showSuccess(name, adventureUsername, password);
        
        // Callback to parent dashboard
        if (onAdventurerCreated != null) {
            onAdventurerCreated.accept(newAdventurer);
        }
        
            // Close dialog after a delay
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> dialogStage.close())
            );
            timeline.play();
            
        } catch (Exception e) {
            // Handle any errors during adventurer creation
            SoundManager.getInstance().playError();
            System.err.println("❌ ERROR: Failed to create adventurer: " + e.getMessage());
            e.printStackTrace();
            
            // Show error to user
            showError("Failed to create adventurer: " + e.getMessage() + 
                     "\n\nPlease try again or contact support if the problem persists.");
            
            // Re-enable the create button
            createButton.setText("⚔️ CREATE ADVENTURER");
            createButton.setDisable(false);
        }
    }
    
    /**
     * Validates Adventure Username in real-time as user types
     */
    private void validateAdventureUsernameRealTime(String adventureUsername) {
        if (adventureUsername == null || adventureUsername.trim().isEmpty()) {
            adventureUsernameStatusLabel.setVisible(false);
            return;
        }
        
        String trimmedUsername = adventureUsername.trim();
        
        // Check basic requirements first
        if (trimmedUsername.length() < 3) {
            showAdventureUsernameStatus("⚠️ Must be at least 3 characters", false);
            return;
        }
        
        // Check for invalid characters
        if (!trimmedUsername.matches("^[a-zA-Z0-9_]+$")) {
            showAdventureUsernameStatus("⚠️ Only letters, numbers, and underscores allowed", false);
            return;
        }
        
        // Check if Adventure Username is taken (with debouncing to avoid excessive calls)
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(500), e -> {
                FirebaseService firebaseService = FirebaseService.getInstance();
                if (firebaseService.isAdventureUsernameTaken(trimmedUsername)) {
                    showAdventureUsernameStatus("❌ This Adventure Username is already taken", false);
                } else {
                    showAdventureUsernameStatus("✅ Adventure Username is available!", true);
                }
            })
        );
        timeline.play();
    }
    
    /**
     * Shows status message for Adventure Username validation
     */
    private void showAdventureUsernameStatus(String message, boolean isSuccess) {
        adventureUsernameStatusLabel.setText(message);
        adventureUsernameStatusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 4 0 0 0;" +
            "-fx-text-fill: " + (isSuccess ? "#10b981;" : "#ef4444;")
        );
        adventureUsernameStatusLabel.setVisible(true);
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
    
    private void showSuccess(String name, String adventureUsername, String password) {
        SoundManager.getInstance().playSuccess();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🎉 Adventurer Created Successfully!");
        alert.setHeaderText("Welcome to CoinCraft, " + name + "!");
        alert.setContentText(
            "✅ Adventure Username: " + adventureUsername + "\n" +
            "✅ Password: " + password + "\n\n" +
            "🚀 HOW YOUR CHILD CAN LOG IN:\n" +
            "1. On the login screen, select 'Child' role\n" +
            "2. Enter Adventure Username: " + adventureUsername + "\n" +
            "3. Enter Password: " + password + "\n" +
            "4. Click 'START ADVENTURE'\n\n" +
            "💰 Starting with 25 SmartCoins\n" +
            "🎯 Ready to begin their financial learning journey!\n\n" +
            "📝 Write down these credentials for your child."
        );
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }
    
    public void show() {
        dialogStage.centerOnScreen();
        dialogStage.showAndWait();
    }
}
