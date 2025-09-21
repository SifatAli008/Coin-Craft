package com.coincraft.ui.components.parent;

import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.format.DateTimeFormatter;

/**
 * Dialog for viewing and editing detailed adventurer information
 * Includes messaging portal integration
 */
public class ViewAdventurerDetailsDialog {
    private Stage dialog;
    private User adventurer;
    private TextField nameField;
    private TextField adventureIdField;
    private Spinner<Integer> ageSpinner;
    private TextField emailField;
    private TextField smartCoinField;
    private TextField levelField;
    private TextField streakField;
    private TextArea notesArea;
    private Label lastLoginLabel;
    private Label createdAtLabel;
    
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
        dialog.setWidth(800);
        dialog.setHeight(700);
        
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
    }
    
    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Details section
        VBox detailsSection = createDetailsSection();
        
        // Stats section
        VBox statsSection = createStatsSection();
        
        // Notes section
        VBox notesSection = createNotesSection();
        
        // Action buttons
        HBox actionButtons = createActionButtons();
        
        mainContent.getChildren().addAll(
            headerSection,
            detailsSection,
            statsSection,
            notesSection,
            actionButtons
        );
        
        return mainContent;
    }
    
    private VBox createHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setStyle(
            "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;"
        );
        
        Label titleLabel = new Label("⚔️ " + adventurer.getName());
        titleLabel.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Adventure ID: " + adventurer.getUserId());
        subtitleLabel.setFont(Font.font("Ancient Medium", 14));
        subtitleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        return headerSection;
    }
    
    private VBox createDetailsSection() {
        VBox detailsSection = new VBox(15);
        
        Label sectionTitle = new Label("📋 Adventure Details");
        sectionTitle.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 16));
        sectionTitle.setStyle("-fx-text-fill: #333333;");
        
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
        detailsGrid.add(nameField, 1, 0);
        
        // Adventure ID field
        detailsGrid.add(new Label("Adventure ID:"), 0, 1);
        adventureIdField = new TextField(adventurer.getUserId());
        adventureIdField.setPrefWidth(200);
        adventureIdField.setEditable(false); // Don't allow changing ID
        adventureIdField.setStyle("-fx-background-color: #f5f5f5;");
        detailsGrid.add(adventureIdField, 1, 1);
        
        // Age field
        detailsGrid.add(new Label("Age:"), 0, 2);
        ageSpinner = new Spinner<>(5, 18, adventurer.getAge());
        ageSpinner.setPrefWidth(100);
        detailsGrid.add(ageSpinner, 1, 2);
        
        // Email field
        detailsGrid.add(new Label("Email:"), 0, 3);
        emailField = new TextField(adventurer.getEmail() != null ? adventurer.getEmail() : "");
        emailField.setPrefWidth(250);
        detailsGrid.add(emailField, 1, 3);
        
        detailsSection.getChildren().addAll(sectionTitle, detailsGrid);
        return detailsSection;
    }
    
    private VBox createStatsSection() {
        VBox statsSection = new VBox(15);
        
        Label sectionTitle = new Label("📊 Adventure Statistics");
        sectionTitle.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 16));
        sectionTitle.setStyle("-fx-text-fill: #333333;");
        
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
        smartCoinField.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #4CAF50; -fx-font-weight: bold;");
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
        streakField.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #FF9800; -fx-font-weight: bold;");
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
        sectionTitle.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 16));
        sectionTitle.setStyle("-fx-text-fill: #333333;");
        
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
        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setPrefWidth(140);
        saveBtn.setPrefHeight(40);
        saveBtn.setStyle(
            "-fx-background-color: #4CAF50;" +
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
            adventurer.setEmail(emailField.getText().trim().isEmpty() ? null : emailField.getText().trim());
            
            // Save to Firebase
            FirebaseService firebaseService = FirebaseService.getInstance();
            firebaseService.saveUser(adventurer);
            
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
    
    public void show() {
        dialog.showAndWait();
    }
}
