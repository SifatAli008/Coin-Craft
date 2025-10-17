package com.coincraft.game.adventure.models;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Comprehensive conversation system for NPCs with dialogue trees, 
 * multiple conversation options, and interactive responses.
 */
public class ConversationSystem {
    
    public static class DialogueNode {
        private final String speaker;
        private final String text;
        private final List<DialogueOption> options;
        private final Consumer<DialogueNode> onEnter;
        private final Consumer<DialogueNode> onExit;
        
        public DialogueNode(String speaker, String text) {
            this.speaker = speaker;
            this.text = text;
            this.options = new ArrayList<>();
            this.onEnter = null;
            this.onExit = null;
        }
        
        public DialogueNode(String speaker, String text, Consumer<DialogueNode> onEnter, Consumer<DialogueNode> onExit) {
            this.speaker = speaker;
            this.text = text;
            this.options = new ArrayList<>();
            this.onEnter = onEnter;
            this.onExit = onExit;
        }
        
        public void addOption(String text, DialogueNode nextNode) {
            options.add(new DialogueOption(text, nextNode));
        }
        
        public void addOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect) {
            options.add(new DialogueOption(text, nextNode, onSelect));
        }
        
        // Getters
        public String getSpeaker() { return speaker; }
        public String getText() { return text; }
        public List<DialogueOption> getOptions() { return options; }
        public Consumer<DialogueNode> getOnEnter() { return onEnter; }
        public Consumer<DialogueNode> getOnExit() { return onExit; }
    }
    
    public static class DialogueOption {
        private final String text;
        private final DialogueNode nextNode;
        private final Consumer<DialogueNode> onSelect;
        
        public DialogueOption(String text, DialogueNode nextNode) {
            this.text = text;
            this.nextNode = nextNode;
            this.onSelect = null;
        }
        
        public DialogueOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect) {
            this.text = text;
            this.nextNode = nextNode;
            this.onSelect = onSelect;
        }
        
        // Getters
        public String getText() { return text; }
        public DialogueNode getNextNode() { return nextNode; }
        public Consumer<DialogueNode> getOnSelect() { return onSelect; }
    }
    
    private DialogueNode currentNode;
    private final String npcName;
    private final String npcType;
    private Stage conversationStage;
    
    public ConversationSystem(String npcName, String npcType, DialogueNode startNode) {
        this.npcName = npcName;
        this.npcType = npcType;
        this.currentNode = startNode;
    }
    
    /**
     * Start a conversation with the NPC
     */
    public void startConversation() {
        System.out.println("💬 Starting conversation with " + npcName + " (" + npcType + ")");
        showDialogueWindow(currentNode);
    }
    
    /**
     * Show the dialogue window with current node
     */
    private void showDialogueWindow(DialogueNode node) {
        if (node == null) {
            System.out.println("❌ Dialogue node is null, ending conversation");
            return;
        }
        
        // Execute onEnter callback
        if (node.getOnEnter() != null) {
            node.getOnEnter().accept(node);
        }
        
        // Create dialogue window
        createDialogueWindow(node);
    }
    
    /**
     * Create and display the dialogue window
     */
    private void createDialogueWindow(DialogueNode node) {
        // Close existing window if open
        if (conversationStage != null && conversationStage.isShowing()) {
            conversationStage.close();
        }
        
        conversationStage = new Stage();
        conversationStage.initModality(Modality.APPLICATION_MODAL);
        conversationStage.initStyle(StageStyle.UTILITY);
        conversationStage.setTitle("Conversation with " + npcName);
        conversationStage.setResizable(false);
        
        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; -fx-border-width: 2;");
        
        // NPC Name and Type
        Label nameLabel = new Label(npcName + " (" + npcType + ")");
        nameLabel.setFont(Font.font("Arial", 16));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-weight: bold;");
        
        // Dialogue Text
        Label dialogueLabel = new Label(node.getText());
        dialogueLabel.setFont(Font.font("Arial", 14));
        dialogueLabel.setTextFill(Color.LIGHTGRAY);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(500);
        dialogueLabel.setTextAlignment(TextAlignment.LEFT);
        
        mainBox.getChildren().addAll(nameLabel, dialogueLabel);
        
        // Add conversation options
        if (!node.getOptions().isEmpty()) {
            Label optionsLabel = new Label("Choose your response:");
            optionsLabel.setFont(Font.font("Arial", 12));
            optionsLabel.setTextFill(Color.WHITE);
            optionsLabel.setStyle("-fx-font-weight: bold;");
            mainBox.getChildren().add(optionsLabel);
            
            for (int i = 0; i < node.getOptions().size(); i++) {
                DialogueOption option = node.getOptions().get(i);
                Button optionButton = createOptionButton(option, i + 1);
                mainBox.getChildren().add(optionButton);
            }
        } else {
            // No options - just a continue button
            Button continueButton = new Button("Continue");
            continueButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
            continueButton.setOnAction(e -> {
                conversationStage.close();
                if (node.getOnExit() != null) {
                    node.getOnExit().accept(node);
                }
            });
            mainBox.getChildren().add(continueButton);
        }
        
        // Add close button
        Button closeButton = new Button("End Conversation");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> {
            conversationStage.close();
            if (node.getOnExit() != null) {
                node.getOnExit().accept(node);
            }
        });
        mainBox.getChildren().add(closeButton);
        
        Scene scene = new Scene(mainBox);
        conversationStage.setScene(scene);
        conversationStage.sizeToScene();
        conversationStage.show();
        
        System.out.println("💬 Showing dialogue: " + node.getText());
    }
    
    /**
     * Create an option button for dialogue choices
     */
    private Button createOptionButton(DialogueOption option, int optionNumber) {
        Button button = new Button(optionNumber + ". " + option.getText());
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8;");
        button.setOnAction(e -> {
            System.out.println("🎯 Player chose: " + option.getText());
            
            // Execute option callback
            if (option.getOnSelect() != null) {
                option.getOnSelect().accept(currentNode);
            }
            
            // Move to next node
            currentNode = option.getNextNode();
            conversationStage.close();
            
            if (currentNode != null) {
                showDialogueWindow(currentNode);
            } else {
                System.out.println("💬 Conversation ended");
            }
        });
        
        return button;
    }
    
    /**
     * End the current conversation
     */
    public void endConversation() {
        if (conversationStage != null && conversationStage.isShowing()) {
            conversationStage.close();
        }
        System.out.println("💬 Conversation with " + npcName + " ended");
    }
    
    /**
     * Check if conversation is currently active
     */
    public boolean isConversationActive() {
        return conversationStage != null && conversationStage.isShowing();
    }
}
