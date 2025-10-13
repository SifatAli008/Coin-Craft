package com.coincraft.game.ui;

import com.coincraft.game.models.GameLevel;
import com.coincraft.game.models.GameState;
import com.coincraft.game.models.Question;
import com.coincraft.game.models.QuestionChoice;
import com.coincraft.game.services.GameDataLoader;
import com.coincraft.game.services.GameProgressService;
import com.coincraft.models.User;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main game window for Financial Literacy Adventure
 */
public class GameWindow {
    private Stage stage;
    private VBox root;
    private User currentUser;
    private GameState gameState;
    private GameLevel currentLevel;
    private int currentQuestionIndex = 0;
    private int sessionCoins = 0;
    
    private final GameDataLoader dataLoader;
    private final GameProgressService progressService;
    
    // UI Components
    private Label coinLabel;
    private VBox contentArea;
    
    public GameWindow(User user) {
        this.currentUser = user;
        this.dataLoader = GameDataLoader.getInstance();
        this.progressService = GameProgressService.getInstance();
        this.gameState = progressService.loadGameState(user.getUserId());
        
        initializeUI();
    }
    
    private void initializeUI() {
        stage = new Stage();
        stage.setTitle("Financial Literacy Adventure");
        
        root = new VBox(0);
        root.setStyle("-fx-background-color: #f8fafc;");
        
        // Top bar with coins and progress
        HBox topBar = createTopBar();
        
        // Main content area
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setPadding(new Insets(20));
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        
        root.getChildren().addAll(topBar, contentArea);
        
        // Show level select screen
        showLevelSelect();
        
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16));
        topBar.setStyle(
            "-fx-background-color: linear-gradient(to right, #667EEA, #764BA2);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
        );
        
        // Adventurer avatar
        StackPane avatarContainer = AdventurerAvatar.createCircularAvatar(48);
        
        // Game title with adventurer name
        VBox titleBox = new VBox(2);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("💰 Financial Adventure");
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        Label adventurerLabel = new Label("⚔️ " + currentUser.getName() + " the Knight");
        adventurerLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        titleBox.getChildren().addAll(titleLabel, adventurerLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        coinLabel = new Label("💰 " + currentUser.getSmartCoinBalance() + " Coins");
        coinLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 20;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        topBar.getChildren().addAll(avatarContainer, titleBox, spacer, coinLabel);
        return topBar;
    }
    
    private void showLevelSelect() {
        contentArea.getChildren().clear();
        
        Label selectLabel = new Label("Choose Your Adventure");
        selectLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        VBox levelsContainer = new VBox(16);
        levelsContainer.setAlignment(Pos.CENTER);
        levelsContainer.setMaxWidth(600);
        
        // Get total levels
        int totalLevels = dataLoader.getTotalLevels();
        
        for (int i = 1; i <= totalLevels; i++) {
            final int levelId = i;
            boolean isCompleted = gameState.isLevelCompleted(levelId);
            boolean isLocked = levelId > 1 && !gameState.isLevelCompleted(levelId - 1);
            
            HBox levelCard = createLevelCard(levelId, isCompleted, isLocked);
            levelsContainer.getChildren().add(levelCard);
        }
        
        contentArea.getChildren().addAll(selectLabel, levelsContainer);
    }
    
    private HBox createLevelCard(int levelId, boolean isCompleted, boolean isLocked) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefWidth(550);
        card.setStyle(
            "-fx-background-color: " + (isLocked ? "#e2e8f0" : "white") + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + (isCompleted ? "#10B981" : "#cbd5e1") + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        // Load level data for title
        GameLevel level = dataLoader.loadLevel(levelId);
        String levelTitle = level != null ? level.getTitle() : "Level " + levelId;
        
        // Create NPC preview (smaller version)
        javafx.scene.Node npcPreview = createNPCPreview(level);

        
        VBox infoBox = new VBox(4);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(levelTitle);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (isLocked ? "#94a3b8" : "#1e293b") + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label statusLabel = new Label(
            isCompleted ? "✅ Completed" : 
            isLocked ? "🔒 Locked" : 
            "▶️ Start"
        );
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + (isLocked ? "#94a3b8" : "#64748b") + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        infoBox.getChildren().addAll(titleLabel, statusLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button playButton = new Button(isCompleted ? "Replay" : "Play");
        playButton.setDisable(isLocked);
        playButton.setPrefWidth(100);
        playButton.setPrefHeight(40);
        playButton.setStyle(
            "-fx-background-color: " + (isLocked ? "#cbd5e1" : "#FA8A00") + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: " + (isLocked ? "not-allowed" : "hand") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        if (!isLocked) {
            playButton.setOnAction(e -> startLevel(levelId));
            
            playButton.setOnMouseEntered(e -> {
                playButton.setStyle(
                    "-fx-background-color: #E67E00;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
                );
            });
            
            playButton.setOnMouseExited(e -> {
                playButton.setStyle(
                    "-fx-background-color: #FA8A00;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 700;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
                );
            });
        }
        
        card.getChildren().addAll(npcPreview, infoBox, spacer, playButton);
        return card;
    }
    
    /**
     * Create smaller NPC preview for level select cards
     */
    private javafx.scene.Node createNPCPreview(GameLevel level) {
        if (level == null) {
            Label fallback = new Label("🎮");
            fallback.setStyle("-fx-font-size: 48px;");
            return fallback;
        }
        
        String imagePath = level.getNpcImagePath();
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.Image npcImage = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(imagePath)
                );
                
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(npcImage);
                imageView.setFitWidth(64);
                imageView.setFitHeight(64);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                
                return imageView;
                
            } catch (Exception e) {
                // Fallback to emoji
            }
        }
        
        // Emoji fallback
        Label emojiLabel = new Label(level.getNpcEmoji());
        emojiLabel.setStyle("-fx-font-size: 48px;");
        return emojiLabel;
    }
    
    private void startLevel(int levelId) {
        currentLevel = dataLoader.loadLevel(levelId);
        if (currentLevel == null) {
            System.err.println("Could not load level " + levelId);
            return;
        }
        
        currentQuestionIndex = 0;
        sessionCoins = 0;
        showNPCIntro();
    }
    
    private void showNPCIntro() {
        contentArea.getChildren().clear();
        
        // Background with level style
        contentArea.setStyle("-fx-background: " + currentLevel.getBackgroundStyle() + ";");
        
        VBox introBox = new VBox(30);
        introBox.setAlignment(Pos.CENTER);
        introBox.setMaxWidth(700);
        
        // NPC character sprite or emoji
        javafx.scene.Node npcDisplay = createNPCDisplay();
        
        // NPC name
        Label npcName = new Label(currentLevel.getNpcName());
        npcName.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        // Dialog box
        VBox dialogBox = new VBox(16);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(30));
        dialogBox.setMaxWidth(600);
        dialogBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, 8);"
        );
        
        Label greetingText = new Label(currentLevel.getNpcGreeting());
        greetingText.setWrapText(true);
        greetingText.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        Button startButton = new Button("Let's Begin!");
        startButton.setPrefWidth(200);
        startButton.setPrefHeight(50);
        startButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        startButton.setOnAction(e -> showQuestion());
        
        dialogBox.getChildren().addAll(greetingText, startButton);
        introBox.getChildren().addAll(npcDisplay, npcName, dialogBox);
        
        contentArea.getChildren().add(introBox);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(500), introBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private void showQuestion() {
        if (currentQuestionIndex >= currentLevel.getQuestions().size()) {
            completeLevel();
            return;
        }
        
        contentArea.getChildren().clear();
        
        Question question = currentLevel.getQuestions().get(currentQuestionIndex);
        
        VBox questionBox = new VBox(30);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setMaxWidth(700);
        questionBox.setPadding(new Insets(40));
        
        // Question number
        Label questionNum = new Label("Question " + (currentQuestionIndex + 1) + " of " + currentLevel.getQuestions().size());
        questionNum.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: rgba(255,255,255,0.9);" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Question card
        VBox questionCard = new VBox(20);
        questionCard.setAlignment(Pos.CENTER);
        questionCard.setPadding(new Insets(30));
        questionCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 16, 0, 0, 8);"
        );
        
        Label questionText = new Label(question.getText());
        questionText.setWrapText(true);
        questionText.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        VBox choicesBox = new VBox(12);
        choicesBox.setAlignment(Pos.CENTER);
        
        for (int i = 0; i < question.getChoices().size(); i++) {
            QuestionChoice choice = question.getChoices().get(i);
            Button choiceButton = createChoiceButton(choice, question);
            choicesBox.getChildren().add(choiceButton);
        }
        
        questionCard.getChildren().addAll(questionText, choicesBox);
        questionBox.getChildren().addAll(questionNum, questionCard);
        
        contentArea.getChildren().add(questionBox);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(400), questionBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private Button createChoiceButton(QuestionChoice choice, Question question) {
        Button button = new Button(choice.getText());
        button.setPrefWidth(550);
        button.setPrefHeight(60);
        button.setWrapText(true);
        button.setStyle(
            "-fx-background-color: #f1f5f9;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #cbd5e1;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: #e0e7ff;" +
                "-fx-text-fill: #1e293b;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #667EEA;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: #f1f5f9;" +
                "-fx-text-fill: #1e293b;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #cbd5e1;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-text-alignment: center;"
            );
        });
        
        button.setOnAction(e -> handleAnswer(choice, question));
        
        return button;
    }
    
    private void handleAnswer(QuestionChoice choice, Question question) {
        boolean isCorrect = choice.isCorrect();
        int coinChange = isCorrect ? choice.getReward() : choice.getPenalty();
        
        // Update coins
        sessionCoins += coinChange;
        currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + coinChange);
        updateCoinDisplay();
        
        // Record answer
        gameState.recordAnswer(isCorrect);
        
        // Show result
        showAnswerResult(isCorrect, coinChange, question.getExplanation());
    }
    
    private void showAnswerResult(boolean isCorrect, int coinChange, String explanation) {
        contentArea.getChildren().clear();
        
        VBox resultBox = new VBox(30);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.setMaxWidth(600);
        
        // Show knight reaction animation
        ImageView knightReaction = isCorrect ? 
            AdventurerAvatar.createAvatar(120, AdventurerAvatar.KnightPose.JUMP) :
            AdventurerAvatar.createAvatar(120, AdventurerAvatar.KnightPose.HURT);
        
        // Result icon
        Label resultIcon = new Label(isCorrect ? "✅" : "❌");
        resultIcon.setStyle("-fx-font-size: 100px;");
        
        // Result message
        Label resultMessage = new Label(isCorrect ? "Correct!" : "Not quite right");
        resultMessage.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (isCorrect ? "#10B981" : "#EF4444") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Coins earned/lost
        Label coinsLabel = new Label((coinChange >= 0 ? "+" : "") + coinChange + " Coins");
        coinsLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + (coinChange >= 0 ? "#F59E0B" : "#EF4444") + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Explanation
        VBox explanationBox = new VBox(10);
        explanationBox.setAlignment(Pos.CENTER);
        explanationBox.setPadding(new Insets(20));
        explanationBox.setMaxWidth(550);
        explanationBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 12;"
        );
        
        Label explanationText = new Label(explanation);
        explanationText.setWrapText(true);
        explanationText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        explanationBox.getChildren().add(explanationText);
        
        // Next button
        Button nextButton = new Button("Next Question");
        nextButton.setPrefWidth(200);
        nextButton.setPrefHeight(50);
        nextButton.setStyle(
            "-fx-background-color: #667EEA;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        nextButton.setOnAction(e -> {
            currentQuestionIndex++;
            showQuestion();
        });
        
        resultBox.getChildren().addAll(knightReaction, resultIcon, resultMessage, coinsLabel, explanationBox, nextButton);
        contentArea.getChildren().add(resultBox);
        
        // Scale animation for result icon
        ScaleTransition scale = new ScaleTransition(Duration.millis(400), resultIcon);
        scale.setFromX(0);
        scale.setFromY(0);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();
    }
    
    private void completeLevel() {
        contentArea.getChildren().clear();
        
        // Mark level as complete
        gameState.completeLevel(currentLevel.getLevelId());
        gameState.addCoins(currentLevel.getCompletionReward());
        
        // Award completion bonus
        currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + currentLevel.getCompletionReward());
        sessionCoins += currentLevel.getCompletionReward();
        updateCoinDisplay();
        
        // Save progress
        progressService.saveGameState(gameState);
        progressService.awardCoins(currentUser, 0); // Sync user balance
        
        VBox completeBox = new VBox(30);
        completeBox.setAlignment(Pos.CENTER);
        completeBox.setMaxWidth(600);
        
        // Victorious knight!
        ImageView victoriousKnight = AdventurerAvatar.createAvatar(150, AdventurerAvatar.KnightPose.ATTACK_3);
        
        Label completeIcon = new Label("🎉");
        completeIcon.setStyle("-fx-font-size: 120px;");
        
        Label completeTitle = new Label("Level Complete!");
        completeTitle.setStyle(
            "-fx-font-size: 36px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
        );
        
        VBox statsBox = new VBox(12);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(30));
        statsBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;"
        );
        
        Label bonusLabel = new Label("🎁 Completion Bonus: +" + currentLevel.getCompletionReward() + " Coins");
        bonusLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #F59E0B;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label totalLabel = new Label("💰 Total Earned: +" + sessionCoins + " Coins");
        totalLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #10B981;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        Label accuracyLabel = new Label("🎯 Accuracy: " + gameState.getAccuracyPercentage() + "%");
        accuracyLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        statsBox.getChildren().addAll(bonusLabel, totalLabel, accuracyLabel);
        
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button backButton = new Button("Back to Levels");
        backButton.setPrefWidth(180);
        backButton.setPrefHeight(50);
        backButton.setStyle(
            "-fx-background-color: #667EEA;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        backButton.setOnAction(e -> {
            contentArea.setStyle(""); // Reset background
            showLevelSelect();
        });
        
        buttonBox.getChildren().add(backButton);
        
        completeBox.getChildren().addAll(victoriousKnight, completeIcon, completeTitle, statsBox, buttonBox);
        contentArea.getChildren().add(completeBox);
        
        // Scale animation
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), completeIcon);
        scale.setFromX(0);
        scale.setFromY(0);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();
    }
    
    /**
     * Create NPC display - uses sprite image if available, otherwise emoji
     */
    private javafx.scene.Node createNPCDisplay() {
        String imagePath = currentLevel.getNpcImagePath();
        
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                javafx.scene.image.Image npcImage = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(imagePath)
                );
                
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(npcImage);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setStyle(
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 16, 0, 0, 8);"
                );
                
                System.out.println("✅ Loaded NPC sprite: " + imagePath);
                return imageView;
                
            } catch (Exception e) {
                System.err.println("⚠️ Could not load NPC sprite, using emoji fallback: " + e.getMessage());
            }
        }
        
        // Fallback to emoji
        Label emojiLabel = new Label(currentLevel.getNpcEmoji());
        emojiLabel.setStyle("-fx-font-size: 120px;");
        return emojiLabel;
    }
    
    private void updateCoinDisplay() {
        coinLabel.setText("💰 " + currentUser.getSmartCoinBalance() + " Coins");
        
        // Pulse animation
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), coinLabel);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.play();
    }
    
    public void show() {
        stage.show();
    }
    
    public Stage getStage() {
        return stage;
    }
}

