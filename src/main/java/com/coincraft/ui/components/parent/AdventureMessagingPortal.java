package com.coincraft.ui.components.parent;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coincraft.models.MessageData;
import com.coincraft.models.User;
import com.coincraft.services.MessagingService;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Adventure Messaging Portal for communication between merchants and adventurers
 * Provides a chat-like interface with message history and real-time messaging
 */
public class AdventureMessagingPortal {
    private Stage dialog;
    private User adventurer;
    private ScrollPane messagesScroll;
    private VBox messagesContainer;
    private TextArea messageInputField;
    private Button sendButton;
    private List<MessageData> messageHistory;
    private MessagingService messagingService;
    
    public AdventureMessagingPortal(Stage parentStage, User adventurer) {
        this.adventurer = adventurer;
        this.messageHistory = new ArrayList<>();
        this.messagingService = MessagingService.getInstance();
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
        VBox messageHistorySection = new VBox(8);
        
        Label sectionTitle = new Label("💬 Chat");
        sectionTitle.setFont(Font.font("Ancient Medium", FontWeight.BOLD, 14));
        sectionTitle.setStyle("-fx-text-fill: #333333;");
        
        messagesContainer = new VBox(8);
        messagesContainer.setPadding(new Insets(12));
        messagesContainer.setFillWidth(true);
        
        messagesScroll = new ScrollPane(messagesContainer);
        messagesScroll.setFitToWidth(true);
        messagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messagesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messagesScroll.setStyle(
            "-fx-background-color: white;" +
            "-fx-background: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #e5e7eb;"
        );
        messagesScroll.setPrefHeight(480);
        
        messageHistorySection.getChildren().addAll(sectionTitle, messagesScroll);
        return messageHistorySection;
    }
    
    private HBox createMessageInputSection() {
        HBox messageInputSection = new HBox(10);
        messageInputSection.setAlignment(Pos.CENTER_LEFT);
        
        messageInputField = new TextArea();
        messageInputField.setPromptText("Write a message…  (Enter to send • Shift+Enter for new line)");
        messageInputField.setPrefRowCount(2);
        HBox.setHgrow(messageInputField, Priority.ALWAYS);
        messageInputField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: #e5e7eb;" +
            "-fx-border-width: 1;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Handle Enter to send, Shift+Enter for newline
        messageInputField.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER) {
                if (e.isShiftDown()) {
                    // allow newline
                } else {
                    e.consume();
                    sendMessage();
                }
            }
        });
        
        sendButton = new Button("📤 Send");
        sendButton.setPrefWidth(80);
        sendButton.setPrefHeight(35);
        sendButton.setStyle(
            "-fx-background-color: #FA8A00;" +
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
        
        messageInputSection.getChildren().addAll(messageInputField, sendButton);
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
        // Subscribe to conversation updates
        String conversationId = buildConversationId();
        messageHistory = messagingService.getRecent(conversationId, 100);
        updateMessageDisplay();

        messagingService.addListener(conversationId, new MessagingService.Listener() {
            @Override public void onUpdate(List<MessageData> messages) {
                messageHistory = messages;
                Platform.runLater(() -> updateMessageDisplay());
            }
        });
    }
    
    // Demo seeding removed: messages are loaded from service
    
    private void sendMessage() {
        String messageText = messageInputField.getText().trim();
        if (messageText.isEmpty()) {
            return;
        }
        
        // Persist via messaging service
        String parentId = com.coincraft.ui.routing.DashboardRouter.getInstance().getCurrentUser().getUserId();
        String parentName = com.coincraft.ui.routing.DashboardRouter.getInstance().getCurrentUser().getName();
        messagingService.sendMessage(
            buildConversationId(),
            parentId,
            parentName,
            adventurer.getUserId(),
            adventurer.getName(),
            messageText
        );
        messageInputField.clear();
    }
    
    private void updateMessageDisplay() {
        messagesContainer.getChildren().clear();
        for (MessageData message : messageHistory) {
            messagesContainer.getChildren().add(createBubble(message));
        }
        
        // Scroll to bottom
        Platform.runLater(() -> {
            messagesScroll.setVvalue(1.0);
        });
    }

    private HBox createBubble(MessageData message) {
        boolean isMerchant = !adventurer.getUserId().equals(message.getSenderId());
        HBox row = new HBox();
        row.setAlignment(isMerchant ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        VBox bubble = new VBox(4);
        bubble.setMaxWidth(520);
        bubble.setStyle(
            (isMerchant
                ? "-fx-background-color: #2563EB; -fx-text-fill: white;"
                : "-fx-background-color: #F3F4F6; -fx-text-fill: #111827;") +
            "-fx-padding: 10 12; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0, 0, 1);"
        );
        
        Label content = new Label(message.getContent());
        content.setWrapText(true);
        content.setStyle(isMerchant ? "-fx-text-fill: white;" : "-fx-text-fill: #111827;");
        
        Label meta = new Label(message.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
        meta.setStyle((isMerchant ? "-fx-text-fill: rgba(255,255,255,0.85);" : "-fx-text-fill: #6B7280;") + "-fx-font-size: 10px;");
        
        bubble.getChildren().addAll(content, meta);
        row.getChildren().add(bubble);
        return row;
    }

    private String buildConversationId() {
        String parentId = com.coincraft.ui.routing.DashboardRouter.getInstance().getCurrentUser().getUserId();
        return parentId + "_" + adventurer.getUserId();
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
    
    // Messages are represented by com.coincraft.models.MessageData
}
