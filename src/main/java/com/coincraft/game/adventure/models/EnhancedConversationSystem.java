package com.coincraft.game.adventure.models;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Enhanced conversation system with animations, sound effects, progress tracking,
 * personality system, and improved UI/UX
 */
public class EnhancedConversationSystem {
    
    public static class DialogueNode {
        private final String speaker;
        private final String text;
        private final List<DialogueOption> options;
        private final Consumer<DialogueNode> onEnter;
        private final Consumer<DialogueNode> onExit;
        private final String emotion;
        private final int difficulty;
        private final String[] keywords;
        
        public DialogueNode(String speaker, String text) {
            this(speaker, text, "neutral", 1, new String[0]);
        }
        
        public DialogueNode(String speaker, String text, String emotion) {
            this(speaker, text, emotion, 1, new String[0]);
        }
        
        public DialogueNode(String speaker, String text, String emotion, int difficulty, String[] keywords) {
            this.speaker = speaker;
            this.text = text;
            this.emotion = emotion;
            this.difficulty = difficulty;
            this.keywords = keywords;
            this.options = new ArrayList<>();
            this.onEnter = null;
            this.onExit = null;
        }
        
        public DialogueNode(String speaker, String text, Consumer<DialogueNode> onEnter, Consumer<DialogueNode> onExit) {
            this(speaker, text, "neutral", 1, new String[0]);
            // Note: onEnter and onExit would need to be set separately in a real implementation
        }
        
        public void addOption(String text, DialogueNode nextNode) {
            options.add(new DialogueOption(text, nextNode));
        }
        
        public void addOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect) {
            options.add(new DialogueOption(text, nextNode, onSelect));
        }
        
        public void addOption(String text, DialogueNode nextNode, String tooltip) {
            options.add(new DialogueOption(text, nextNode, null, tooltip));
        }
        
        // Getters
        public String getSpeaker() { return speaker; }
        public String getText() { return text; }
        public String getEmotion() { return emotion; }
        public int getDifficulty() { return difficulty; }
        public String[] getKeywords() { return keywords; }
        public List<DialogueOption> getOptions() { return options; }
        public Consumer<DialogueNode> getOnEnter() { return onEnter; }
        public Consumer<DialogueNode> getOnExit() { return onExit; }
    }
    
    public static class DialogueOption {
        private final String text;
        private final DialogueNode nextNode;
        private final Consumer<DialogueNode> onSelect;
        private final String tooltip;
        private final String emotion;
        private final int points;
        
        public DialogueOption(String text, DialogueNode nextNode) {
            this(text, nextNode, null, null, "neutral", 0);
        }
        
        public DialogueOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect) {
            this(text, nextNode, onSelect, null, "neutral", 0);
        }
        
        public DialogueOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect, String tooltip) {
            this(text, nextNode, onSelect, tooltip, "neutral", 0);
        }
        
        public DialogueOption(String text, DialogueNode nextNode, Consumer<DialogueNode> onSelect, String tooltip, String emotion, int points) {
            this.text = text;
            this.nextNode = nextNode;
            this.onSelect = onSelect;
            this.tooltip = tooltip;
            this.emotion = emotion;
            this.points = points;
        }
        
        // Getters
        public String getText() { return text; }
        public DialogueNode getNextNode() { return nextNode; }
        public Consumer<DialogueNode> getOnSelect() { return onSelect; }
        public String getTooltip() { return tooltip; }
        public String getEmotion() { return emotion; }
        public int getPoints() { return points; }
    }
    
    // Conversation tracking and personality system
    public static class ConversationStats {
        private int totalInteractions = 0;
        private int correctAnswers = 0;
        private int wrongAnswers = 0;
        private int coinsEarned = 0;
        private int coinsLost = 0;
        private final Map<String, Integer> topicFrequency = new HashMap<>();
        private final Map<String, Integer> emotionResponses = new HashMap<>();
        private final List<String> conversationHistory = new ArrayList<>();
        
        public void recordInteraction(String topic, String emotion, boolean correct, int coins) {
            totalInteractions++;
            if (correct) {
                correctAnswers++;
                coinsEarned += coins;
            } else {
                wrongAnswers++;
                coinsLost += coins;
            }
            
            topicFrequency.put(topic, topicFrequency.getOrDefault(topic, 0) + 1);
            emotionResponses.put(emotion, emotionResponses.getOrDefault(emotion, 0) + 1);
        }
        
        public double getSuccessRate() {
            return totalInteractions > 0 ? (double) correctAnswers / totalInteractions : 0.0;
        }
        
        public String getFavoriteTopic() {
            return topicFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
        }
        
        public String getPreferredEmotion() {
            return emotionResponses.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");
        }
        
        // Getters
        public int getTotalInteractions() { return totalInteractions; }
        public int getCorrectAnswers() { return correctAnswers; }
        public int getWrongAnswers() { return wrongAnswers; }
        public int getCoinsEarned() { return coinsEarned; }
        public int getCoinsLost() { return coinsLost; }
        public Map<String, Integer> getTopicFrequency() { return topicFrequency; }
        public Map<String, Integer> getEmotionResponses() { return emotionResponses; }
        public List<String> getConversationHistory() { return conversationHistory; }
    }
    
    private DialogueNode currentNode;
    private final String npcName;
    private final String npcType;
    private final String npcPersonality;
    private Stage conversationStage;
    private ConversationStats stats;
    private int currentStreak = 0;
    private int maxStreak = 0;
    
    public EnhancedConversationSystem(String npcName, String npcType, String npcPersonality, DialogueNode startNode) {
        this.npcName = npcName;
        this.npcType = npcType;
        this.npcPersonality = npcPersonality;
        this.currentNode = startNode;
        this.stats = new ConversationStats();
    }
    
    /**
     * Start a conversation with enhanced features
     */
    public void startConversation() {
        System.out.println("💬 Starting enhanced conversation with " + npcName + " (" + npcType + ")");
        playSound("conversation_start");
        showDialogueWindow(currentNode);
    }
    
    /**
     * Show the enhanced dialogue window with animations and effects
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
        
        // Create enhanced dialogue window
        createEnhancedDialogueWindow(node);
    }
    
    /**
     * Create and display the enhanced dialogue window with animations
     */
    private void createEnhancedDialogueWindow(DialogueNode node) {
        // Close existing window if open
        if (conversationStage != null && conversationStage.isShowing()) {
            conversationStage.close();
        }
        
        conversationStage = new Stage();
        conversationStage.initModality(Modality.APPLICATION_MODAL);
        conversationStage.initStyle(StageStyle.UTILITY);
        conversationStage.setTitle("💬 " + npcName + " - " + npcType);
        conversationStage.setResizable(false);
        
        // Create main container with gradient background
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));
        
        // Create gradient background (applied via CSS)
        mainBox.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e); " +
                        "-fx-border-color: #3498db; -fx-border-width: 3; " +
                        "-fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Add drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.3));
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        mainBox.setEffect(shadow);
        
        // NPC Header with personality indicator
        HBox headerBox = createNPCCHeader(node);
        mainBox.getChildren().add(headerBox);
        
        // Progress bar for conversation progress
        ProgressBar progressBar = createProgressBar();
        mainBox.getChildren().add(progressBar);
        
        // Dialogue text with typing animation
        Label dialogueLabel = createDialogueLabel();
        mainBox.getChildren().add(dialogueLabel);
        
        // Add conversation options with enhanced styling
        if (!node.getOptions().isEmpty()) {
            Label optionsLabel = new Label("Choose your response:");
            optionsLabel.setFont(Font.font("Arial", 14));
            optionsLabel.setTextFill(Color.WHITE);
            optionsLabel.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #000000, 2, 0.5, 1, 1);");
            mainBox.getChildren().add(optionsLabel);
            
            for (int i = 0; i < node.getOptions().size(); i++) {
                DialogueOption option = node.getOptions().get(i);
                Button optionButton = createEnhancedOptionButton(option, i + 1);
                mainBox.getChildren().add(optionButton);
            }
        } else {
            // No options - continue button with animation
            Button continueButton = createContinueButton(node);
            mainBox.getChildren().add(continueButton);
        }
        
        // Stats and close section
        HBox footerBox = createFooterBox();
        mainBox.getChildren().add(footerBox);
        
        Scene scene = new Scene(mainBox);
        conversationStage.setScene(scene);
        conversationStage.sizeToScene();
        
        // Add entrance animation
        addEntranceAnimation(mainBox);
        conversationStage.show();
        
        // Start typing animation for dialogue
        startTypingAnimation(dialogueLabel, node.getText());
        
        System.out.println("💬 Showing enhanced dialogue: " + node.getText());
    }
    
    /**
     * Create NPC header with personality and emotion indicators
     */
    private HBox createNPCCHeader(DialogueNode node) {
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        // NPC Name with personality color
        Label nameLabel = new Label(npcName + " (" + npcType + ")");
        nameLabel.setFont(Font.font("Arial", 18));
        nameLabel.setTextFill(getPersonalityColor());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #000000, 3, 0.7, 2, 2);");
        
        // Emotion indicator
        Label emotionLabel = new Label(getEmotionEmoji(node.getEmotion()));
        emotionLabel.setFont(Font.font(24));
        
        // Personality indicator
        Label personalityLabel = new Label("[" + npcPersonality + "]");
        personalityLabel.setFont(Font.font("Arial", 12));
        personalityLabel.setTextFill(Color.LIGHTGRAY);
        personalityLabel.setStyle("-fx-font-style: italic;");
        
        headerBox.getChildren().addAll(nameLabel, emotionLabel, personalityLabel);
        return headerBox;
    }
    
    /**
     * Create progress bar showing conversation progress
     */
    private ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(stats.getTotalInteractions() / 10.0); // Normalize to 0-1
        progressBar.setStyle("-fx-accent: #3498db; -fx-background-color: #2c3e50;");
        progressBar.setPrefWidth(400);
        return progressBar;
    }
    
    /**
     * Create dialogue label with enhanced styling
     */
    private Label createDialogueLabel() {
        Label dialogueLabel = new Label();
        dialogueLabel.setFont(Font.font("Arial", 14));
        dialogueLabel.setTextFill(Color.LIGHTGRAY);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(500);
        dialogueLabel.setTextAlignment(TextAlignment.LEFT);
        dialogueLabel.setStyle("-fx-padding: 15; -fx-background-color: rgba(0,0,0,0.3); " +
                              "-fx-background-radius: 8; -fx-border-color: #3498db; " +
                              "-fx-border-width: 1; -fx-border-radius: 8;");
        return dialogueLabel;
    }
    
    /**
     * Create enhanced option button with animations and tooltips
     */
    private Button createEnhancedOptionButton(DialogueOption option, int optionNumber) {
        Button button = new Button(optionNumber + ". " + option.getText());
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(40);
        
        // Enhanced styling based on option type
        String baseStyle = "-fx-background-color: " + getOptionColor(option) + "; " +
                          "-fx-text-fill: white; -fx-font-weight: bold; " +
                          "-fx-padding: 10; -fx-background-radius: 8; " +
                          "-fx-border-color: #34495e; -fx-border-width: 1; " +
                          "-fx-border-radius: 8;";
        
        button.setStyle(baseStyle);
        
        // Add tooltip if available
        if (option.getTooltip() != null) {
            Tooltip tooltip = new Tooltip(option.getTooltip());
            tooltip.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                           "-fx-font-size: 12; -fx-padding: 5;");
            button.setTooltip(tooltip);
        }
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + "-fx-effect: dropshadow(gaussian, #3498db, 10, 0.5, 0, 0);");
            playSound("button_hover");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle);
        });
        
        // Add click animation and action
        button.setOnAction(e -> {
            playClickAnimation(button);
            playSound("button_click");
            
            System.out.println("🎯 Player chose: " + option.getText());
            
            // Record interaction
            stats.recordInteraction("conversation", option.getEmotion(), 
                                  option.getPoints() > 0, option.getPoints());
            
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
                playSound("conversation_end");
            }
        });
        
        return button;
    }
    
    /**
     * Create continue button with animation
     */
    private Button createContinueButton(DialogueNode node) {
        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                               "-fx-font-weight: bold; -fx-padding: 10; " +
                               "-fx-background-radius: 8; -fx-pref-width: 120;");
        
        continueButton.setOnAction(e -> {
            playSound("button_click");
            conversationStage.close();
            if (node.getOnExit() != null) {
                node.getOnExit().accept(node);
            }
        });
        
        return continueButton;
    }
    
    /**
     * Create footer with stats and close button
     */
    private HBox createFooterBox() {
        HBox footerBox = new HBox(20);
        footerBox.setAlignment(Pos.CENTER);
        
        // Stats display
        Label statsLabel = new Label("Interactions: " + stats.getTotalInteractions() + 
                                   " | Success: " + String.format("%.1f%%", stats.getSuccessRate() * 100) +
                                   " | Coins: +" + stats.getCoinsEarned() + "/-" + stats.getCoinsLost());
        statsLabel.setFont(Font.font("Arial", 10));
        statsLabel.setTextFill(Color.LIGHTGRAY);
        
        // Close button
        Button closeButton = new Button("End Conversation");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-padding: 8; " +
                           "-fx-background-radius: 6;");
        
        closeButton.setOnAction(e -> {
            playSound("conversation_end");
            conversationStage.close();
        });
        
        footerBox.getChildren().addAll(statsLabel, closeButton);
        return footerBox;
    }
    
    /**
     * Add entrance animation to the dialogue window
     */
    private void addEntranceAnimation(VBox mainBox) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mainBox);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), mainBox);
        scaleIn.setFromX(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setFromY(0.8);
        scaleIn.setToY(1.0);
        
        fadeIn.play();
        scaleIn.play();
    }
    
    /**
     * Start typing animation for dialogue text
     */
    private void startTypingAnimation(Label dialogueLabel, String fullText) {
        dialogueLabel.setText("");
        
        Timeline typingTimeline = new Timeline();
        for (int i = 0; i <= fullText.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 30), e -> {
                dialogueLabel.setText(fullText.substring(0, index));
            });
            typingTimeline.getKeyFrames().add(keyFrame);
        }
        
        typingTimeline.play();
    }
    
    /**
     * Add click animation to buttons
     */
    private void playClickAnimation(Button button) {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setFromX(1.0);
        scaleDown.setToX(0.95);
        scaleDown.setFromY(1.0);
        scaleDown.setToY(0.95);
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
        scaleUp.setFromX(0.95);
        scaleUp.setToX(1.0);
        scaleUp.setFromY(0.95);
        scaleUp.setToY(1.0);
        
        scaleDown.setOnFinished(e -> scaleUp.play());
        scaleDown.play();
    }
    
    /**
     * Get personality-based color for NPC
     */
    private Color getPersonalityColor() {
        return switch (npcPersonality.toLowerCase()) {
            case "adventurous" -> Color.ORANGE;
            case "wise" -> Color.LIGHTBLUE;
            case "business" -> Color.GOLD;
            case "friendly" -> Color.LIGHTGREEN;
            case "mysterious" -> Color.PURPLE;
            default -> Color.WHITE;
        };
    }
    
    /**
     * Get emotion emoji for display
     */
    private String getEmotionEmoji(String emotion) {
        return switch (emotion.toLowerCase()) {
            case "happy" -> "😊";
            case "excited" -> "🤩";
            case "wise" -> "🧙‍♀️";
            case "confident" -> "💪";
            case "mysterious" -> "🤔";
            case "friendly" -> "😄";
            case "serious" -> "😐";
            case "encouraging" -> "👍";
            default -> "💬";
        };
    }
    
    /**
     * Get option color based on type
     */
    private String getOptionColor(DialogueOption option) {
        if (option.getPoints() > 0) return "#27ae60"; // Green for positive
        if (option.getPoints() < 0) return "#e74c3c"; // Red for negative
        return "#3498db"; // Blue for neutral
    }
    
    /**
     * Play sound effect (placeholder - would integrate with actual sound system)
     */
    private void playSound(String soundName) {
        System.out.println("🔊 Playing sound: " + soundName);
        // TODO: Integrate with actual sound system
        // SoundManager.getInstance().playSound(soundName + ".wav");
    }
    
    /**
     * End the current conversation
     */
    public void endConversation() {
        if (conversationStage != null && conversationStage.isShowing()) {
            conversationStage.close();
        }
        playSound("conversation_end");
        System.out.println("💬 Enhanced conversation with " + npcName + " ended");
    }
    
    /**
     * Check if conversation is currently active
     */
    public boolean isConversationActive() {
        return conversationStage != null && conversationStage.isShowing();
    }
    
    /**
     * Get conversation statistics
     */
    public ConversationStats getStats() {
        return stats;
    }
    
    /**
     * Get current streak
     */
    public int getCurrentStreak() {
        return currentStreak;
    }
    
    /**
     * Get maximum streak achieved
     */
    public int getMaxStreak() {
        return maxStreak;
    }
}
