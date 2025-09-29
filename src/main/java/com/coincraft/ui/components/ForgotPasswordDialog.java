package com.coincraft.ui.components;

import java.util.logging.Logger;

import com.coincraft.services.FirebaseAuthService;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Dialog for handling forgot password functionality
 */
public class ForgotPasswordDialog {
    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordDialog.class.getName());
    
    private final Stage dialog;
    private final FirebaseAuthService firebaseAuthService;
    private TextField emailField;
    private Button sendButton;
    private Button cancelButton;
    private Label statusLabel;
    
    public ForgotPasswordDialog(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
        this.dialog = new Stage();
        initializeDialog();
    }
    
    private void initializeDialog() {
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Reset Password");
        dialog.setResizable(false);
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: rgba(0, 0, 0, 0.2);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);"
        );
        
        // Title
        Label titleLabel = new Label("🔐 Reset your password");
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        // Description
        Label descLabel = new Label("Enter your email address and we'll send you a link to reset your password.");
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-wrap-text: true;"
        );
        descLabel.setMaxWidth(400);
        descLabel.setWrapText(true);
        
        // Email input
        VBox emailSection = new VBox(8);
        emailSection.setAlignment(Pos.CENTER_LEFT);
        
        Label emailLabel = new Label("Email address:");
        emailLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefWidth(400);
        emailField.setPrefHeight(42);
        emailField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                emailField.setStyle(emailField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #d1d5db; -fx-border-width: 1;"));
            }
        });
        
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-background-color: rgba(0,0,0,0.05);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-font-weight: 600;"
        );
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);
        statusLabel.setAlignment(Pos.CENTER);
        
        // Buttons
        HBox buttonContainer = new HBox(12);
        buttonContainer.setAlignment(Pos.CENTER);
        
        sendButton = new Button("Send reset link");
        sendButton.setPrefWidth(140);
        sendButton.setPrefHeight(40);
        sendButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 5, 0, 0, 2);"
        );
        
        sendButton.setOnMouseEntered(e -> {
            sendButton.setStyle(sendButton.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        sendButton.setOnMouseExited(e -> {
            sendButton.setStyle(sendButton.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        sendButton.setOnAction(e -> handleSendResetEmail());
        
        cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(40);
        cancelButton.setStyle(
            "-fx-background-color: #6b7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(107,114,128,0.3), 5, 0, 0, 2);"
        );
        
        cancelButton.setOnMouseEntered(e -> {
            cancelButton.setStyle(cancelButton.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        cancelButton.setOnMouseExited(e -> {
            cancelButton.setStyle(cancelButton.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        cancelButton.setOnAction(e -> dialog.close());
        
        buttonContainer.getChildren().addAll(sendButton, cancelButton);
        
        // Handle Enter key
        emailField.setOnAction(e -> handleSendResetEmail());
        
        root.getChildren().addAll(titleLabel, descLabel, emailSection, statusLabel, buttonContainer);
        
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        
        // Center dialog on screen
        dialog.setOnShown(e -> {
            dialog.centerOnScreen();
        });
    }
    
    private void handleSendResetEmail() {
        String email = emailField.getText().trim();
        
        if (email.isEmpty()) {
            showStatus("Please enter your email address", false);
            return;
        }
        
        if (!isValidEmail(email)) {
            showStatus("Please enter a valid email address", false);
            return;
        }
        
        showStatus("Sending reset link...", true);
        sendButton.setDisable(true);
        sendButton.setText("Sending...");
        
        // Send reset email in background thread
        new Thread(() -> {
            try {
                FirebaseAuthService.AuthResult result = firebaseAuthService.sendPasswordResetEmail(email);
                
                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    sendButton.setText("Send reset link");
                    
                    if (result.isSuccess()) {
                        showStatus("✅ Password reset link sent! Check your email inbox.", true);
                        // Auto-close after 3 seconds
                        new Thread(() -> {
                            try {
                                Thread.sleep(3000);
                                Platform.runLater(() -> dialog.close());
                            } catch (InterruptedException ignored) {}
                        }).start();
                    } else {
                        showStatus("❌ " + result.getErrorMessage(), false);
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    sendButton.setText("Send reset link");
                    showStatus("❌ Failed to send reset email: " + e.getMessage(), false);
                    LOGGER.severe("Password reset error: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        String base =
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-background-color: rgba(0,0,0,0.05);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-font-weight: 600;";
        String color = isSuccess ? "#059669" : "#dc2626"; // green for success, red for errors
        statusLabel.setStyle(base + "-fx-text-fill: " + color + ";");
    }
    
    public void show() {
        dialog.show();
    }
    
    public void close() {
        dialog.close();
    }
}
