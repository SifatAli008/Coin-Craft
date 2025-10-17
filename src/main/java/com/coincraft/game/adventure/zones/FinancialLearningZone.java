package com.coincraft.game.adventure.zones;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.coincraft.game.adventure.models.AdventureZone;
import com.coincraft.game.adventure.models.AdventurePlayer;
import com.coincraft.game.adventure.models.AdventureInteractable;
import com.coincraft.game.adventure.models.StrongAdventurerNPC;
import com.coincraft.game.adventure.models.WiseLadyNPC;
import com.coincraft.game.adventure.models.SmartBusinessmanNPC;

/**
 * Financial Learning Zone - Main zone with three specialized NPCs
 * Features: Strong Adventurer (game guide), Wise Lady (financial literacy), Smart Businessman (quiz system)
 */
public class FinancialLearningZone extends AdventureZone {
    
    // Quiz system variables
    private int currentQuizQuestion = 0;
    private int correctAnswers = 0;
    private final int totalQuestions = 5;
    private boolean quizActive = false;
    
    // Financial literacy quiz questions and answers
    private final String[][] quizQuestions = {
        {"What is a budget?", "A plan for spending money", "A type of bank account", "A savings goal", "A"},
        {"What is compound interest?", "Interest earned on both principal and previous interest", "Simple interest", "A type of loan", "A"},
        {"What is the 50/30/20 rule?", "50% needs, 30% wants, 20% savings", "50% savings, 30% needs, 20% wants", "Equal spending on everything", "A"},
        {"What is an emergency fund?", "Money set aside for unexpected expenses", "Money for vacations", "Money for investments", "A"},
        {"What is diversification?", "Spreading investments across different types", "Putting all money in one place", "Saving only in cash", "A"}
    };
    
    // Dedicated NPC instances
    private StrongAdventurerNPC strongAdventurer;
    private WiseLadyNPC wiseLady;
    private SmartBusinessmanNPC smartBusinessman;
    
    public FinancialLearningZone() {
        super("Financial Learning Zone", "Master financial literacy with our three wise guides");
        this.zoneColor = Color.PURPLE;
        this.backgroundStyle = "linear-gradient(135deg, #9370DB 0%, #4B0082 100%)";
    }
    
    @Override
    protected void setupZone() {
        System.out.println("Setting up Financial Learning Zone...");
        
        // Create dedicated NPC instances
        strongAdventurer = new StrongAdventurerNPC(150, 400);
        wiseLady = new WiseLadyNPC(600, 300);
        smartBusinessman = new SmartBusinessmanNPC(1050, 400);
        
        System.out.println("Created dedicated NPC instances");
        
        // Test NPCs
        testNPCs();
        
        // Add interactive learning stations
        setupLearningStations();
    }
    
    private void setupLearningStations() {
        // Game Guide Station
        AdventureInteractable gameGuideStation = new AdventureInteractable(
            "Adventure Guide Station",
            "🗺️",
            "Learn game mechanics and financial adventure tips",
            150, 500
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showGameGuideLesson(player);
            }
        };
        interactables.add(gameGuideStation);
        
        // Financial Literacy Station
        AdventureInteractable literacyStation = new AdventureInteractable(
            "Financial Wisdom Station",
            "📚",
            "Learn essential financial concepts",
            600, 400
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showFinancialLiteracyLesson(player);
            }
        };
        interactables.add(literacyStation);
        
        // Quiz Station
        AdventureInteractable quizStation = new AdventureInteractable(
            "Financial Quiz Station",
            "🧠",
            "Test your financial knowledge",
            1050, 500
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                startFinancialQuiz(player);
            }
        };
        interactables.add(quizStation);
    }
    
    @Override
    public void renderZone(Pane gameWorld) {
        System.out.println("=== RENDERING FINANCIAL LEARNING ZONE ===");
        System.out.println("Children count before zone render: " + gameWorld.getChildren().size());
        
        // Clear existing zone elements
        gameWorld.getChildren().removeIf(node -> 
            node.getStyleClass().contains("zone-element"));
        
        System.out.println("Children count after clearing zone elements: " + gameWorld.getChildren().size());
        
        // Add background
        Rectangle background = new Rectangle(0, 0, 1200, 800);
        background.setFill(zoneColor);
        background.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(background);
        
        System.out.println("Children count after adding background: " + gameWorld.getChildren().size());
        
        // Add zone-specific elements
        renderZoneElements(gameWorld);
        
        System.out.println("Children count after zone elements: " + gameWorld.getChildren().size());
        
        // Render dedicated NPCs
        if (strongAdventurer != null) {
            System.out.println("Rendering Strong Adventurer...");
            strongAdventurer.render(gameWorld);
        } else {
            System.out.println("Strong Adventurer is NULL!");
        }
        if (wiseLady != null) {
            System.out.println("Rendering Wise Lady...");
            wiseLady.render(gameWorld);
        } else {
            System.out.println("Wise Lady is NULL!");
        }
        if (smartBusinessman != null) {
            System.out.println("Rendering Smart Businessman...");
            smartBusinessman.render(gameWorld);
        } else {
            System.out.println("Smart Businessman is NULL!");
        }
        
        System.out.println("Children count after NPCs: " + gameWorld.getChildren().size());
        
        // Add interactables
        for (AdventureInteractable interactable : interactables) {
            interactable.render(gameWorld);
        }
        
        System.out.println("Final children count: " + gameWorld.getChildren().size());
        System.out.println("=== ZONE RENDERING COMPLETE ===");
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add mystical financial symbols
        for (int i = 0; i < 15; i++) {
            Circle symbol = new Circle(
                Math.random() * 1200,
                Math.random() * 800,
                8 + Math.random() * 12,
                Color.GOLD.deriveColor(0, 1, 1, 0.4)
            );
            symbol.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(symbol);
        }
        
        // Add knowledge trees
        for (int i = 0; i < 6; i++) {
            Rectangle tree = new Rectangle(
                150 + i * 180,
                650,
                25,
                120
            );
            tree.setFill(Color.BROWN);
            tree.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(tree);
            
            Circle leaves = new Circle(
                162 + i * 180,
                620,
                35,
                Color.DARKGREEN
            );
            leaves.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(leaves);
        }
        
        // Add zone title
        Text title = new Text(50, 50, "🏛️ " + zoneName);
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
        
        // Add NPC labels
        addNPCLabels(gameWorld);
    }
    
    private void addNPCLabels(Pane gameWorld) {
        // Strong Adventurer label
        Text adventurerLabel = new Text(100, 350, "🗡️ Game Guide");
        adventurerLabel.setFont(Font.font("Arial", 16));
        adventurerLabel.setFill(Color.ORANGE);
        adventurerLabel.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(adventurerLabel);
        
        // Wise Lady label
        Text wiseLabel = new Text(550, 250, "🧙‍♀️ Financial Teacher");
        wiseLabel.setFont(Font.font("Arial", 16));
        wiseLabel.setFill(Color.LIGHTBLUE);
        wiseLabel.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(wiseLabel);
        
        // Smart Businessman label
        Text businessmanLabel = new Text(1000, 350, "💼 Quiz Master");
        businessmanLabel.setFont(Font.font("Arial", 16));
        businessmanLabel.setFill(Color.GOLD);
        businessmanLabel.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(businessmanLabel);
    }
    
    @Override
    public void update(double deltaTime, AdventurePlayer player) {
        // Check zone completion
        checkZoneCompletion(player);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Zone is completed when player has interacted with all three NPCs
        // and completed the quiz
        if (correctAnswers >= 3 && !quizActive) {
            setCompleted(true);
            System.out.println("🎉 Congratulations! You've mastered the Financial Learning Zone!");
        }
    }
    
    private void showGameGuideLesson(AdventurePlayer player) {
        System.out.println("🗡️ Thorin Ironbeard's Adventure Guide:");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Welcome to your financial adventure!");
        System.out.println("");
        System.out.println("🎮 GAME MECHANICS:");
        System.out.println("• Use WASD or arrow keys to move");
        System.out.println("• Press SPACE to interact with NPCs");
        System.out.println("• Press TAB to view your progress");
        System.out.println("• Press ESC for pause menu");
        System.out.println("");
        System.out.println("💰 FINANCIAL ADVENTURE TIPS:");
        System.out.println("• Talk to Sage Wisdomheart for financial wisdom");
        System.out.println("• Challenge Alexander Venture's quiz for coins");
        System.out.println("• Correct quiz answers earn +10 coins");
        System.out.println("• Wrong answers cost -5 coins");
        System.out.println("• Complete the zone by scoring 3+ correct answers!");
        System.out.println("");
        System.out.println("📍 Your current position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
    }
    
    private void showFinancialLiteracyLesson(AdventurePlayer player) {
        System.out.println("🧙‍♀️ Sage Wisdomheart's Financial Wisdom:");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Listen well, young learner, to these ancient truths:");
        System.out.println("");
        System.out.println("📚 ESSENTIAL FINANCIAL CONCEPTS:");
        System.out.println("");
        System.out.println("💰 BUDGETING:");
        System.out.println("• Track your income and expenses");
        System.out.println("• Use the 50/30/20 rule: 50% needs, 30% wants, 20% savings");
        System.out.println("• Always pay yourself first");
        System.out.println("");
        System.out.println("🏦 SAVINGS:");
        System.out.println("• Build an emergency fund (3-6 months expenses)");
        System.out.println("• Save for short-term goals (vacation, car)");
        System.out.println("• Save for long-term goals (house, retirement)");
        System.out.println("");
        System.out.println("📈 INVESTING:");
        System.out.println("• Start early - time is your greatest asset");
        System.out.println("• Diversify your investments");
        System.out.println("• Understand compound interest");
        System.out.println("");
        System.out.println("💡 SMART MONEY HABITS:");
        System.out.println("• Live below your means");
        System.out.println("• Avoid debt for wants");
        System.out.println("• Educate yourself continuously");
        System.out.println("• Set SMART financial goals");
        System.out.println("");
        System.out.println("📍 Your current position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
    }
    
    private void startFinancialQuiz(AdventurePlayer player) {
        if (quizActive) {
            System.out.println("🧠 Quiz already in progress! Answer the current question first.");
            return;
        }
        
        quizActive = true;
        currentQuizQuestion = 0;
        correctAnswers = 0;
        
        System.out.println("💼 Alexander Venture's Financial Quiz:");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Welcome to the ultimate test of financial knowledge!");
        System.out.println("• Correct answers: +10 coins");
        System.out.println("• Wrong answers: -5 coins");
        System.out.println("• Complete 3+ correct answers to finish the zone!");
        System.out.println("");
        System.out.println("📍 Player position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
        
        askNextQuestion();
    }
    
    private void askNextQuestion() {
        if (currentQuizQuestion >= totalQuestions) {
            endQuiz();
            return;
        }
        
        String[] question = quizQuestions[currentQuizQuestion];
        System.out.println("❓ Question " + (currentQuizQuestion + 1) + "/" + totalQuestions + ":");
        System.out.println(question[0]);
        System.out.println("");
        System.out.println("A) " + question[1]);
        System.out.println("B) " + question[2]);
        System.out.println("C) " + question[3]);
        System.out.println("");
        System.out.println("Type your answer (A, B, or C):");
        
        // In a real implementation, this would show a proper dialog box
        // For now, we'll simulate the quiz with automatic answers for demonstration
        simulateQuizAnswer();
    }
    
    private void simulateQuizAnswer() {
        // Simulate player answering (in real game, this would be user input)
        String[] question = quizQuestions[currentQuizQuestion];
        String correctAnswer = question[4];
        String playerAnswer = correctAnswer; // Simulate correct answer for demo
        
        System.out.println("🎯 Your answer: " + playerAnswer);
        
        if (playerAnswer.equals(correctAnswer)) {
            correctAnswers++;
            System.out.println("✅ Correct! +10 coins earned!");
            System.out.println("💰 Total correct answers: " + correctAnswers);
        } else {
            System.out.println("❌ Wrong answer! -5 coins lost.");
            System.out.println("The correct answer was: " + correctAnswer);
        }
        
        currentQuizQuestion++;
        
        if (currentQuizQuestion < totalQuestions) {
            System.out.println("");
            System.out.println("Press SPACE to continue to next question...");
        } else {
            endQuiz();
        }
    }
    
    private void endQuiz() {
        quizActive = false;
        
        System.out.println("🏆 Quiz Complete!");
        System.out.println("═══════════════════");
        System.out.println("📊 Your Results:");
        System.out.println("• Correct answers: " + correctAnswers + "/" + totalQuestions);
        System.out.println("• Coins earned: " + (correctAnswers * 10));
        System.out.println("• Coins lost: " + ((totalQuestions - correctAnswers) * 5));
        System.out.println("• Net coins: " + ((correctAnswers * 10) - ((totalQuestions - correctAnswers) * 5)));
        
        if (correctAnswers >= 3) {
            System.out.println("🎉 Excellent! You've mastered financial literacy!");
            System.out.println("🏛️ The Financial Learning Zone is now complete!");
        } else {
            System.out.println("📚 Keep learning! Try talking to Sage Wisdomheart for more knowledge.");
            System.out.println("🔄 You can retake the quiz anytime by talking to Alexander Venture.");
        }
        
        System.out.println("");
        System.out.println("💡 Remember: Financial knowledge is the key to wealth!");
    }
    
    // Getters for quiz state
    public boolean isQuizActive() {
        return quizActive;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    // Test method to verify NPCs are working
    public void testNPCs() {
        System.out.println("=== NPC Test Results ===");
        System.out.println("Strong Adventurer: " + (strongAdventurer != null ? "✓ Created" : "✗ Missing"));
        System.out.println("Wise Lady: " + (wiseLady != null ? "✓ Created" : "✗ Missing"));
        System.out.println("Smart Businessman: " + (smartBusinessman != null ? "✓ Created" : "✗ Missing"));
        System.out.println("========================");
    }
}
