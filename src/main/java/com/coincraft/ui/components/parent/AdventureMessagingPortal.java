package com.coincraft.ui.components.parent;

import com.coincraft.models.User;

import javafx.application.Platform;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adventure Messaging Portal for communication between merchants and adventurers
 * Provides a chat-like interface with message history and real-time messaging
 */
public class AdventureMessagingPortal {
    private Stage dialog;
    private User adventurer;
    private TextArea messageHistoryArea;
    private TextField messageInputField;
    private Button sendButton;
    private List<Message> messageHistory;
    
    public AdventureMessagingPortal(Stage parentStage, User adventurer) {
        this.adventurer = adventurer;
        this.messageHistory = new ArrayList<>();
        createDialog(parentStage);
        loadMessageHistory();
    }
    
    private void createDialog(Stage parentStage) {
        dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("💬 Adventure Messaging Portal - " + adventurer.getName());
        dialog.setResizable(true);
        dialog.setWidth(900);
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
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(15));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Message history area
        VBox messageHistorySection = createMessageHistorySection();
        
        // Message input section
        HBox messageInputSection = createMessageInputSection();
        
        // Action buttons
        HBox actionButtons = createActionButtons();
        
        mainContent.getChildren().addAll(
            headerSection,
            messageHistorySection,
            messageInputSection,
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
            "-fx-padding: 15;"
        );
        
        Label titleLabel = new Label("💬 Adventure Messaging Portal");
        titleLabel.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Communicate with " + adventurer.getName() + " (Adventure ID: " + adventurer.getUserId() + ")");
        subtitleLabel.setFont(Font.font("Ancient Medium", 12));
        subtitleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        return headerSection;
    }
    
    private VBox createMessageHistorySection() {
        VBox messageHistorySection = new VBox(10);
        
        Label sectionTitle = new Label("📜 Message History");
        sectionTitle.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 14));
        sectionTitle.setStyle("-fx-text-fill: #333333;");
        
        messageHistoryArea = new TextArea();
        messageHistoryArea.setPrefHeight(400);
        messageHistoryArea.setEditable(false);
        messageHistoryArea.setWrapText(true);
        messageHistoryArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;" +
            "-fx-font-family: 'Consolas', 'Monaco', monospace;" +
            "-fx-font-size: 12px;"
        );
        
        messageHistorySection.getChildren().addAll(sectionTitle, messageHistoryArea);
        return messageHistorySection;
    }
    
    private HBox createMessageInputSection() {
        HBox messageInputSection = new HBox(10);
        messageInputSection.setAlignment(Pos.CENTER_LEFT);
        
        Label inputLabel = new Label("💬 New Message:");
        inputLabel.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 12));
        inputLabel.setStyle("-fx-text-fill: #333333;");
        
        messageInputField = new TextField();
        messageInputField.setPromptText("Type your message to " + adventurer.getName() + "...");
        messageInputField.setPrefHeight(35);
        HBox.setHgrow(messageInputField, Priority.ALWAYS);
        messageInputField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Handle Enter key to send message
        messageInputField.setOnAction(e -> sendMessage());
        
        sendButton = new Button("📤 Send");
        sendButton.setPrefWidth(80);
        sendButton.setPrefHeight(35);
        sendButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        sendButton.setOnAction(e -> sendMessage());
        
        messageInputSection.getChildren().addAll(inputLabel, messageInputField, sendButton);
        return messageInputSection;
    }
    
    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        // Quick message templates
        Button templateBtn = new Button("📝 Quick Messages");
        templateBtn.setPrefWidth(140);
        templateBtn.setPrefHeight(35);
        templateBtn.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        templateBtn.setOnAction(e -> showQuickMessages());
        
        // Clear history button
        Button clearBtn = new Button("🗑️ Clear History");
        clearBtn.setPrefWidth(120);
        clearBtn.setPrefHeight(35);
        clearBtn.setStyle(
            "-fx-background-color: #FF5722;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        clearBtn.setOnAction(e -> clearMessageHistory());
        
        // Close button
        Button closeBtn = new Button("❌ Close");
        closeBtn.setPrefWidth(80);
        closeBtn.setPrefHeight(35);
        closeBtn.setStyle(
            "-fx-background-color: #757575;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        
        closeBtn.setOnAction(e -> dialog.close());
        
        actionButtons.getChildren().addAll(templateBtn, clearBtn, closeBtn);
        return actionButtons;
    }
    
    private void loadMessageHistory() {
        // Load sample messages for demonstration
        // In a real implementation, this would load from Firebase
        addSampleMessages();
        updateMessageDisplay();
    }
    
    private void addSampleMessages() {
        LocalDateTime now = LocalDateTime.now();
        
        // Add welcome message
        messageHistory.add(new Message(
            "Merchant",
            "Welcome to your adventure, " + adventurer.getName() + "! 🎉",
            now.minusHours(2)
        ));
        
        // Add some sample conversation
        messageHistory.add(new Message(
            adventurer.getName(),
            "Hi! I'm excited to start my financial adventure! 💰",
            now.minusHours(1).minusMinutes(30)
        ));
        
        messageHistory.add(new Message(
            "Merchant",
            "Great to hear! Remember to complete your daily quests to earn SmartCoins. 🏆",
            now.minusHours(1)
        ));
        
        messageHistory.add(new Message(
            adventurer.getName(),
            "I completed my first quest! I earned 25 SmartCoins! ⭐",
            now.minusMinutes(30)
        ));
        
        messageHistory.add(new Message(
            "Merchant",
            "Excellent work! Keep up the great progress! 🎯",
            now.minusMinutes(15)
        ));
    }
    
    private void sendMessage() {
        String messageText = messageInputField.getText().trim();
        if (messageText.isEmpty()) {
            return;
        }
        
        // Create new message
        Message newMessage = new Message(
            "Merchant",
            messageText,
            LocalDateTime.now()
        );
        
        messageHistory.add(newMessage);
        updateMessageDisplay();
        messageInputField.clear();
        
        // In a real implementation, save to Firebase here
        System.out.println("Message sent to " + adventurer.getName() + ": " + messageText);
    }
    
    private void updateMessageDisplay() {
        StringBuilder displayText = new StringBuilder();
        
        for (Message message : messageHistory) {
            String timestamp = message.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
            String sender = message.getSender().equals("Merchant") ? "🏪 Merchant" : "⚔️ " + message.getSender();
            
            displayText.append(String.format("[%s] %s: %s\n\n", 
                timestamp, sender, message.getContent()));
        }
        
        messageHistoryArea.setText(displayText.toString());
        
        // Scroll to bottom
        Platform.runLater(() -> {
            messageHistoryArea.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    private void showQuickMessages() {
        String[] quickMessages = {
            "Great job on completing your quest! 🎉",
            "Remember to check your daily tasks! 📋",
            "You're making excellent progress! ⭐",
            "Keep up the good work! 💪",
            "Your SmartCoin balance is growing! 💰",
            "Don't forget to save some coins! 🏦",
            "You're becoming a financial wizard! 🧙‍♂️",
            "Adventure awaits! Ready for the next challenge? ⚔️"
        };
        
        ChoiceDialog<String> quickMessageDialog = new ChoiceDialog<>(quickMessages[0], quickMessages);
        quickMessageDialog.setTitle("📝 Quick Messages");
        quickMessageDialog.setHeaderText("Select a quick message to send:");
        quickMessageDialog.setContentText("Choose a message:");
        
        Optional<String> result = quickMessageDialog.showAndWait();
        if (result.isPresent()) {
            messageInputField.setText(result.get());
            sendMessage();
        }
    }
    
    private void clearMessageHistory() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("🗑️ Clear Message History");
        confirmDialog.setHeaderText("Are you sure you want to clear the message history?");
        confirmDialog.setContentText("This action cannot be undone.");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messageHistory.clear();
            updateMessageDisplay();
        }
    }
    
    public void show() {
        dialog.showAndWait();
    }
    
    /**
     * Inner class to represent a message
     */
    private static class Message {
        private String sender;
        private String content;
        private LocalDateTime timestamp;
        
        public Message(String sender, String content, LocalDateTime timestamp) {
            this.sender = sender;
            this.content = content;
            this.timestamp = timestamp;
        }
        
        public String getSender() { return sender; }
        public String getContent() { return content; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}
