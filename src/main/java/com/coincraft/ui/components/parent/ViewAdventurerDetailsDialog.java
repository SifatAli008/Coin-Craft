package com.coincraft.ui.components.parent;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Dialog for viewing and editing detailed adventurer information
 * Includes messaging portal integration
 */
public class ViewAdventurerDetailsDialog {
    private Stage dialog;
    private final User adventurer;
    private TextField nameField;
    private TextField adventureUsernameField;
    private Button copyUsernameBtn;
    private Spinner<Integer> ageSpinner;
    private TextField emailField;
    private TextField smartCoinField;
    private TextField levelField;
    private TextField streakField;
    private TextArea notesArea;
    private Label lastLoginLabel;
    private Label createdAtLabel;
    private Button changePasswordBtn;
    private Button saveBtn;
    
    public ViewAdventurerDetailsDialog(Stage parentStage, User adventurer) {
        this.adventurer = adventurer;
        createDialog(parentStage);
    }
    
    private void createDialog(Stage parentStage) {
        dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("⚔️ Adventure Details - " + adventurer.getName());
        dialog.setResizable(true);
        dialog.setWidth(860);
        dialog.setHeight(760);
        
        // Create main content
        VBox mainContent = createMainContent();
        
        // Create scene
        Scene scene = new Scene(mainContent);
        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles/coincraft-styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load CSS styles: " + e.getMessage());
        }
        
        dialog.setScene(scene);
        try {
            // Keyboard shortcuts: Ctrl+S to save, Esc to close
            javafx.scene.input.KeyCombination saveComb = new javafx.scene.input.KeyCodeCombination(
                javafx.scene.input.KeyCode.S, javafx.scene.input.KeyCombination.CONTROL_DOWN);
            scene.getAccelerators().put(saveComb, () -> { if (saveBtn != null && !saveBtn.isDisable()) saveBtn.fire(); });
            javafx.scene.input.KeyCombination escComb = new javafx.scene.input.KeyCodeCombination(
                javafx.scene.input.KeyCode.ESCAPE);
            scene.getAccelerators().put(escComb, () -> dialog.close());
        } catch (Exception ignored) {}
    }
    
    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Details and Stats sections (side-by-side)
        VBox detailsSection = createDetailsSection();
        VBox statsSection = createStatsSection();
        HBox twoColumn = new HBox(20);
        twoColumn.getChildren().addAll(detailsSection, statsSection);
        HBox.setHgrow(detailsSection, Priority.ALWAYS);
        HBox.setHgrow(statsSection, Priority.NEVER);
        detailsSection.setPrefWidth(560);
        statsSection.setPrefWidth(260);

        // Notes section
        VBox notesSection = createNotesSection();
        
        // Action buttons
        HBox actionButtons = createActionButtons();
        
        mainContent.getChildren().addAll(headerSection, twoColumn, notesSection, actionButtons);
        
        return mainContent;
    }
    
    private VBox createHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setStyle(
            "-fx-background-color: linear-gradient(135deg, #FB8C00 0%, #F59E0B 60%, #FFB74D 100%);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;"
        );
        
        Label titleLabel = new Label("⚔️ " + adventurer.getName());
        titleLabel.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 800;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 6, 0, 0, 1);"
        );
        
        String usernameDisplay = adventurer.getUsername() != null ? adventurer.getUsername() : "No Username Set";
        Label subtitleLabel = new Label("Adventure Username: " + usernameDisplay);
        subtitleLabel.setStyle(
            "-fx-text-fill: rgba(255,255,255,0.96);" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 4, 0, 0, 1);"
        );

        // Quick stats chips in header
        HBox chips = new HBox(8);
        chips.setAlignment(Pos.CENTER);
        chips.getChildren().addAll(
            createChip("💰 " + adventurer.getSmartCoinBalance(), "#22c55e"),
            createChip("⭐ " + adventurer.getLevel(), "#3b82f6"),
            createChip("🔥 " + adventurer.getDailyStreaks(), "#f59e0b")
        );
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel, chips);
        return headerSection;
    }
    
    private VBox createDetailsSection() {
        VBox detailsSection = new VBox(15);
        
        Label sectionTitle = new Label("📋 Adventure Details");
        sectionTitle.setStyle("-fx-text-fill: #333333; -fx-font-weight: 800; -fx-font-size: 16px;");
        
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));
        detailsGrid.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;"
        );
        
        // Name field
        detailsGrid.add(new Label("Adventurer Name:"), 0, 0);
        nameField = new TextField(adventurer.getName());
        nameField.setPrefWidth(200);
        nameField.textProperty().addListener((o, ov, nv) -> markDirty());
        detailsGrid.add(nameField, 1, 0);
        
        // Adventure Username field
        detailsGrid.add(new Label("Adventure Username:"), 0, 1);
        String usernameValue = adventurer.getUsername() != null ? adventurer.getUsername() : "";
        adventureUsernameField = new TextField(usernameValue);
        adventureUsernameField.setPrefWidth(200);
        adventureUsernameField.setEditable(false); // Don't allow changing username directly
        adventureUsernameField.setStyle("-fx-background-color: #f5f5f5;");
        detailsGrid.add(adventureUsernameField, 1, 1);
        
        // Change Password button (next to username)
        changePasswordBtn = new Button("🔐 Change Password");
        changePasswordBtn.setPrefWidth(150);
        changePasswordBtn.setPrefHeight(32);
        changePasswordBtn.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        changePasswordBtn.setOnAction(e -> openChangePasswordDialog());
        detailsGrid.add(changePasswordBtn, 2, 1);

        // Copy username button
        copyUsernameBtn = new Button("📋 Copy");
        copyUsernameBtn.setPrefHeight(32);
        copyUsernameBtn.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-border-color: #e5e7eb;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 10;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        copyUsernameBtn.setOnAction(e -> {
            String text = adventureUsernameField.getText();
            if (text != null && !text.isEmpty()) {
                javafx.scene.input.ClipboardContent cc = new javafx.scene.input.ClipboardContent();
                cc.putString(text);
                javafx.scene.input.Clipboard.getSystemClipboard().setContent(cc);
            }
        });
        detailsGrid.add(copyUsernameBtn, 3, 1);
        
        // Age field
        detailsGrid.add(new Label("Age:"), 0, 2);
        ageSpinner = new Spinner<>(5, 18, adventurer.getAge());
        ageSpinner.setPrefWidth(100);
        ageSpinner.valueProperty().addListener((o, ov, nv) -> markDirty());
        detailsGrid.add(ageSpinner, 1, 2);
        
        // Email field (auto-generated, read-only)
        detailsGrid.add(new Label("Email:"), 0, 3);
        emailField = new TextField(adventurer.getEmail() != null ? adventurer.getEmail() : "");
        emailField.setPrefWidth(250);
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: #f5f5f5;");
        detailsGrid.add(emailField, 1, 3);
        
        detailsSection.getChildren().addAll(sectionTitle, detailsGrid);
        return detailsSection;
    }
    
    private VBox createStatsSection() {
        VBox statsSection = new VBox(15);
        
        Label sectionTitle = new Label("📊 Adventure Statistics");
        sectionTitle.setStyle("-fx-text-fill: #333333; -fx-font-weight: 800; -fx-font-size: 16px;");
        
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(10));
        statsGrid.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;"
        );
        
        // SmartCoin balance
        statsGrid.add(new Label("💰 SmartCoin Balance:"), 0, 0);
        smartCoinField = new TextField(String.valueOf(adventurer.getSmartCoinBalance()));
        smartCoinField.setPrefWidth(100);
        smartCoinField.setEditable(false);
        smartCoinField.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #FA8A00; -fx-font-weight: bold;");
        statsGrid.add(smartCoinField, 1, 0);
        
        // Level
        statsGrid.add(new Label("⭐ Level:"), 0, 1);
        levelField = new TextField(String.valueOf(adventurer.getLevel()));
        levelField.setPrefWidth(50);
        levelField.setEditable(false);
        levelField.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #2196F3; -fx-font-weight: bold;");
        statsGrid.add(levelField, 1, 1);
        
        // Daily streak
        statsGrid.add(new Label("🔥 Daily Streak:"), 0, 2);
        streakField = new TextField(String.valueOf(adventurer.getDailyStreaks()));
        streakField.setPrefWidth(50);
        streakField.setEditable(false);
        streakField.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #FA8A00; -fx-font-weight: bold;");
        statsGrid.add(streakField, 1, 2);
        
        // Last login
        statsGrid.add(new Label("🕒 Last Login:"), 0, 3);
        lastLoginLabel = new Label(adventurer.getLastLogin() != null ? 
            adventurer.getLastLogin().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) : "Never");
        lastLoginLabel.setStyle("-fx-text-fill: #666666;");
        statsGrid.add(lastLoginLabel, 1, 3);
        
        // Created date
        statsGrid.add(new Label("📅 Adventure Started:"), 0, 4);
        createdAtLabel = new Label(adventurer.getCreatedAt() != null ? 
            adventurer.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "Unknown");
        createdAtLabel.setStyle("-fx-text-fill: #666666;");
        statsGrid.add(createdAtLabel, 1, 4);
        
        statsSection.getChildren().addAll(sectionTitle, statsGrid);
        return statsSection;
    }
    
    private VBox createNotesSection() {
        VBox notesSection = new VBox(15);
        
        Label sectionTitle = new Label("📝 Adventure Notes");
        sectionTitle.setStyle("-fx-text-fill: #333333; -fx-font-weight: 800; -fx-font-size: 16px;");
        
        notesArea = new TextArea();
        notesArea.setPrefHeight(100);
        notesArea.setPromptText("Add notes about this adventurer's progress, achievements, or any special considerations...");
        notesArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        notesSection.getChildren().addAll(sectionTitle, notesArea);
        return notesSection;
    }
    
    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        // Message Adventurer button
        Button messageBtn = new Button("💬 Message Adventurer");
        messageBtn.setPrefWidth(180);
        messageBtn.setPrefHeight(40);
        messageBtn.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
        );
        
        messageBtn.setOnAction(e -> openMessagingPortal());
        
        // Save Changes button
        saveBtn = new Button("💾 Save Changes");
        saveBtn.setPrefWidth(140);
        saveBtn.setPrefHeight(40);
        saveBtn.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
        );
        
        saveBtn.setOnAction(e -> saveChanges());
        saveBtn.setDisable(true);
        
        // Cancel button
        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setPrefWidth(100);
        cancelBtn.setPrefHeight(40);
        cancelBtn.setStyle(
            "-fx-background-color: #757575;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 6, 0, 0, 2);"
        );
        
        cancelBtn.setOnAction(e -> dialog.close());
        
        actionButtons.getChildren().addAll(messageBtn, saveBtn, cancelBtn);
        return actionButtons;
    }

    private HBox createChip(String text, String color) {
        Label l = new Label(text);
        l.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 700;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 4 10;" +
            "-fx-background-radius: 9999;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        HBox box = new HBox(l);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /** Mark form dirty and enable save button. */
    private void markDirty() {
        if (saveBtn != null) {
            saveBtn.setDisable(false);
        }
    }
    
    private void openMessagingPortal() {
        // Close this dialog and open messaging portal
        dialog.close();
        
        AdventureMessagingPortal messagingPortal = new AdventureMessagingPortal(
            (Stage) dialog.getOwner(), adventurer
        );
        messagingPortal.show();
    }
    
    private void saveChanges() {
        try {
            // Update adventurer information
            adventurer.setName(nameField.getText().trim());
            adventurer.setAge(ageSpinner.getValue());
            // Email is auto-generated and read-only, so we don't update it from the field
            
            // Save to Firebase
            FirebaseService firebaseService = FirebaseService.getInstance();
            firebaseService.saveUser(adventurer);
            
            if (saveBtn != null) saveBtn.setDisable(true);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("✅ Success");
            alert.setHeaderText("Adventure Details Updated");
            alert.setContentText("The adventurer's information has been successfully updated!");
            alert.showAndWait();
            
            dialog.close();
            
        } catch (Exception e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("❌ Error");
            alert.setHeaderText("Failed to Update Adventure Details");
            alert.setContentText("Could not save the changes: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void openChangePasswordDialog() {
        CentralizedMusicManager.getInstance().playButtonClick();
        
        // Create custom change password dialog
        Dialog<ButtonType> passwordDialog = new Dialog<>();
        passwordDialog.setTitle("🔐 Change Adventure Password");
        passwordDialog.setHeaderText("Change password for " + adventurer.getName());
        passwordDialog.initOwner(dialog);
        passwordDialog.initModality(Modality.WINDOW_MODAL);
        
        // Create dialog content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #f8f9fa;");
        
        // Current password field
        Label currentPasswordLabel = new Label("Current Password:");
        currentPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Enter current password");
        currentPasswordField.setPrefWidth(300);
        stylePasswordField(currentPasswordField);
        
        // New password field
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password (min 4 characters)");
        newPasswordField.setPrefWidth(300);
        stylePasswordField(newPasswordField);
        
        // Confirm new password field
        Label confirmPasswordLabel = new Label("Confirm New Password:");
        confirmPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333333;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setPrefWidth(300);
        stylePasswordField(confirmPasswordField);
        
        // Password requirements label
        Label requirementsLabel = new Label("• Password must be at least 4 characters long\n• Make it memorable for your adventurer");
        requirementsLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");
        requirementsLabel.setWrapText(true);
        
        content.getChildren().addAll(
            currentPasswordLabel, currentPasswordField,
            newPasswordLabel, newPasswordField,
            confirmPasswordLabel, confirmPasswordField,
            requirementsLabel
        );
        
        passwordDialog.getDialogPane().setContent(content);
        
        // Add buttons
        ButtonType changeButtonType = new ButtonType("🔐 Change Password", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("❌ Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        passwordDialog.getDialogPane().getButtonTypes().addAll(changeButtonType, cancelButtonType);
        
        // Style the buttons
        Button changeButton = (Button) passwordDialog.getDialogPane().lookupButton(changeButtonType);
        Button cancelButton = (Button) passwordDialog.getDialogPane().lookupButton(cancelButtonType);
        
        styleDialogButton(changeButton, "#4CAF50");
        styleDialogButton(cancelButton, "#757575");
        
        // Disable change button initially
        changeButton.setDisable(true);
        
        // Add validation
        Runnable validateFields = () -> {
            boolean isValid = !currentPasswordField.getText().isEmpty() &&
                            !newPasswordField.getText().isEmpty() &&
                            !confirmPasswordField.getText().isEmpty() &&
                            newPasswordField.getText().length() >= 4 &&
                            newPasswordField.getText().equals(confirmPasswordField.getText());
            changeButton.setDisable(!isValid);
        };
        
        currentPasswordField.textProperty().addListener((obs, old, text) -> validateFields.run());
        newPasswordField.textProperty().addListener((obs, old, text) -> validateFields.run());
        confirmPasswordField.textProperty().addListener((obs, old, text) -> validateFields.run());
        
        // Handle the result
        Optional<ButtonType> result = passwordDialog.showAndWait();
        
        if (result.isPresent() && result.get() == changeButtonType) {
            handlePasswordChange(
                currentPasswordField.getText(),
                newPasswordField.getText(),
                confirmPasswordField.getText()
            );
        }
    }
    
    private void stylePasswordField(PasswordField field) {
        field.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: #4CAF50; -fx-border-width: 2;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: #4CAF50; -fx-border-width: 2;", "-fx-border-color: #e0e0e0; -fx-border-width: 1;"));
            }
        });
    }
    
    private void styleDialogButton(Button button, String color) {
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 8 16;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
    }
    
    private void handlePasswordChange(String currentPassword, String newPassword, String confirmPassword) {
        try {
            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                showErrorAlert("Passwords don't match", "The new password and confirmation password don't match. Please try again.");
                return;
            }
            
            // Validate password length
            if (newPassword.length() < 4) {
                showErrorAlert("Password too short", "The new password must be at least 4 characters long.");
                return;
            }
            
            // Get the adventurer's username
            String username = adventurer.getUsername();
            if (username == null || username.isEmpty()) {
                showErrorAlert("No Username Found", "This adventurer doesn't have a username set. Cannot change password.");
                return;
            }
            
            // Verify current password
            FirebaseService firebaseService = FirebaseService.getInstance();
            String verifiedUserId = firebaseService.verifyAdventurerCredentials(username, currentPassword);
            
            if (verifiedUserId == null || !verifiedUserId.equals(adventurer.getUserId())) {
                CentralizedMusicManager.getInstance().playError();
                showErrorAlert("Incorrect Current Password", "The current password you entered is incorrect. Please try again.");
                return;
            }
            
            // Update password in credentials storage
            boolean passwordUpdated = firebaseService.updateAdventurerPassword(username, currentPassword, newPassword);
            
            if (passwordUpdated) {
                CentralizedMusicManager.getInstance().playSuccess();
                
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("🎉 Password Changed Successfully!");
                alert.setHeaderText("Adventure Password Updated");
                alert.setContentText(
                    "The password for " + adventurer.getName() + " has been successfully changed!\n\n" +
                    "Adventure Username: " + username + "\n" +
                    "New Password: " + newPassword + "\n\n" +
                    "Make sure your adventurer knows their new password for future logins."
                );
                alert.initOwner(dialog);
                alert.showAndWait();
                
            } else {
                CentralizedMusicManager.getInstance().playError();
                showErrorAlert("Password Change Failed", "Could not update the password. Please try again.");
            }
            
        } catch (Exception e) {
            CentralizedMusicManager.getInstance().playError();
            showErrorAlert("Error", "An error occurred while changing the password: " + e.getMessage());
        }
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ " + title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.initOwner(dialog);
        alert.showAndWait();
    }
    
    public void show() {
        dialog.showAndWait();
    }
}
