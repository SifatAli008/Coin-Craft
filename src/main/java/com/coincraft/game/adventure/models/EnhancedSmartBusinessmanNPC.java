package com.coincraft.game.adventure.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Enhanced Smart Businessman NPC with integrated quiz system and personality
 * Demonstrates the full capabilities of the enhanced conversation system
 */
public class EnhancedSmartBusinessmanNPC {
    private final String name = "Alexander Venture";
    private final String emoji = "💼";
    private final String greeting = "Ah, a potential business partner! Test your financial knowledge with my quiz. Correct answers earn coins, wrong answers cost coins!";
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    private EnhancedConversationSystem conversationSystem;
    private NPCPersonalitySystem.PersonalityProfile personalityProfile;
    private EnhancedQuizSystem.QuizSession currentQuizSession;
    
    public EnhancedSmartBusinessmanNPC(double x, double y) {
        this.x = x;
        this.y = y;
        createNPCSprite();
        initializePersonalitySystem();
        initializeConversationSystem();
    }
    
    private void createNPCSprite() {
        try {
            String imagePath = getClass().getResource("/Assets/NPC/Smart Businessman.png").toExternalForm();
            System.out.println("Loading Smart Businessman image from: " + imagePath);
            
            Image npcImage = new Image(imagePath);
            ImageView npcImageView = new ImageView(npcImage);
            npcImageView.setFitWidth(80);
            npcImageView.setFitHeight(80);
            npcImageView.setPreserveRatio(true);
            npcSprite = npcImageView;
            
            System.out.println("Successfully loaded Smart Businessman sprite");
        } catch (Exception e) {
            System.out.println("Failed to load Smart Businessman image: " + e.getMessage());
            npcSprite = new Circle(40, Color.GOLD);
            ((Circle) npcSprite).setStroke(Color.DARKGOLDENROD);
            ((Circle) npcSprite).setStrokeWidth(3);
        }
        
        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(20));
        emojiText.setFill(Color.WHITE);
        emojiText.setLayoutX(x - 10);
        emojiText.setLayoutY(y + 10);
        
        npcName = new Text(name);
        npcName.setFont(Font.font(14));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
    }
    
    public void render(Pane gameWorld) {
        System.out.println("Rendering Smart Businessman at (" + x + ", " + y + ")");
        
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        gameWorld.getChildren().add(npcName);
        
        System.out.println("Smart Businessman rendered successfully");
    }
    
    public void interact(AdventurePlayer player) {
        if (conversationSystem != null) {
            conversationSystem.startConversation();
        } else {
            if (!hasInteracted) {
                showGreeting();
                hasInteracted = true;
            } else {
                showFollowUpDialogue();
            }
        }
        
        player.setInteracting(true);
    }
    
    private void showGreeting() {
        System.out.println("💼 " + name + ": " + greeting);
    }
    
    private void showFollowUpDialogue() {
        String[] followUps = {
            "Ready for another financial challenge?",
            "Remember: Knowledge is the ultimate business asset!",
            "Come back anytime to test your financial skills!",
            "The market rewards those who understand it!"
        };
        
        String followUp = followUps[(int)(Math.random() * followUps.length)];
        System.out.println("💼 " + name + ": " + followUp);
    }
    
    public void cleanup() {
        if (npcSprite != null && npcSprite.getParent() != null) {
            ((Pane) npcSprite.getParent()).getChildren().remove(npcSprite);
        }
        if (npcName != null && npcName.getParent() != null) {
            ((Pane) npcName.getParent()).getChildren().remove(npcName);
        }
    }
    
    /**
     * Initialize the personality system for the Smart Businessman
     */
    private void initializePersonalitySystem() {
        personalityProfile = new NPCPersonalitySystem.PersonalityProfile(
            NPCPersonalitySystem.PersonalityType.BUSINESS
        );
        System.out.println("💼 Smart Businessman personality system initialized");
    }
    
    /**
     * Initialize the enhanced conversation system with quiz integration
     */
    private void initializeConversationSystem() {
        // Create enhanced dialogue tree with quiz integration
        EnhancedConversationSystem.DialogueNode greetingNode = new EnhancedConversationSystem.DialogueNode(
            name, 
            personalityProfile.getPersonalizedGreeting() + " I'm Alexander Venture, and I believe in testing financial knowledge through practical application. " +
            "I have several knowledge tests that will challenge your understanding of financial concepts. " +
            "Correct answers earn you coins, while incorrect answers cost coins. Are you ready to prove your financial acumen?",
            "confident"
        );
        
        // Quiz selection dialogue
        EnhancedConversationSystem.DialogueNode quizSelection = new EnhancedConversationSystem.DialogueNode(
            name,
            """
            Excellent! Choose your challenge level:

            • Beginner: Basic financial concepts (25-50 coins per question)
            • Intermediate: Investment and credit knowledge (50-100 coins per question)
            • Advanced: Complex financial planning (75-150 coins per question)

            What level would you like to attempt?
            """,
            "excited"
        );
        
        // Create quiz sessions for different difficulty levels
        EnhancedConversationSystem.DialogueNode beginnerQuiz = createQuizDialogue("Basic Financial Concepts", EnhancedQuizSystem.DifficultyLevel.BEGINNER);
        EnhancedConversationSystem.DialogueNode intermediateQuiz = createQuizDialogue("Investment Knowledge", EnhancedQuizSystem.DifficultyLevel.INTERMEDIATE);
        EnhancedConversationSystem.DialogueNode advancedQuiz = createQuizDialogue("Advanced Financial Planning", EnhancedQuizSystem.DifficultyLevel.ADVANCED);
        
        // Quiz results dialogue (for future use)
        // EnhancedConversationSystem.DialogueNode quizResults = new EnhancedConversationSystem.DialogueNode(
        //     name,
        //     "Quiz completed! Here are your results:",
        //     "neutral"
        // );
        
        // Set up conversation options
        greetingNode.addOption("I'm ready for the challenge!", quizSelection);
        greetingNode.addOption("Tell me about the quiz system first.", createInfoDialogue());
        greetingNode.addOption("Maybe later, I need to study more.", null);
        
        quizSelection.addOption("Beginner level, please!", beginnerQuiz);
        quizSelection.addOption("Intermediate level!", intermediateQuiz);
        quizSelection.addOption("Advanced level!", advancedQuiz);
        quizSelection.addOption("Go back to main menu.", greetingNode);
        
        // Create enhanced conversation system
        conversationSystem = new EnhancedConversationSystem(name, "Financial Quiz Master", "Business", greetingNode);
        
        System.out.println("💼 Enhanced Smart Businessman conversation system initialized");
    }
    
    /**
     * Create quiz dialogue with dynamic questions
     */
    private EnhancedConversationSystem.DialogueNode createQuizDialogue(String category, EnhancedQuizSystem.DifficultyLevel difficulty) {
        // Create a quiz session
        currentQuizSession = EnhancedQuizSystem.createQuizSession(category, difficulty, 3);
        
        if (currentQuizSession == null) {
            return new EnhancedConversationSystem.DialogueNode(
                name,
                "Sorry, no questions are available for " + category + " at " + difficulty.getName() + " level.",
                "concerned"
            );
        }
        
        EnhancedQuizSystem.QuizQuestion currentQuestion = currentQuizSession.getCurrentQuestion();
        if (currentQuestion == null) {
            return new EnhancedConversationSystem.DialogueNode(
                name,
                "No questions available. Please try a different category or difficulty.",
                "concerned"
            );
        }
        
        // Create question dialogue
        EnhancedConversationSystem.DialogueNode questionNode = new EnhancedConversationSystem.DialogueNode(
            name,
            "Question " + (currentQuizSession.getCurrentQuestionIndex() + 1) + " of " + currentQuizSession.getTotalQuestions() + ":\n\n" +
            currentQuestion.getQuestion() + "\n\n" +
            "A) " + currentQuestion.getOptions()[0] + "\n" +
            "B) " + currentQuestion.getOptions()[1] + "\n" +
            "C) " + currentQuestion.getOptions()[2] + "\n" +
            "D) " + currentQuestion.getOptions()[3],
            "serious"
        );
        
        // Add answer options
        for (int i = 0; i < currentQuestion.getOptions().length; i++) {
            final int answerIndex = i;
            String optionText = (char)('A' + i) + ") " + currentQuestion.getOptions()[i];
            // String tooltip = "Select option " + (char)('A' + i); // For future use
            
            questionNode.addOption(optionText, null, (node) -> {
                // Process the answer
                EnhancedQuizSystem.QuizResult result = currentQuizSession.answerQuestion(answerIndex);
                
                // Update personality based on result
                personalityProfile.updateMood(result.isCorrect(), currentQuestion.getCategory());
                
                // Show result
                showQuizResult(result, currentQuestion);
            });
        }
        
        return questionNode;
    }
    
    /**
     * Show quiz result with enhanced feedback
     */
    private void showQuizResult(EnhancedQuizSystem.QuizResult result, EnhancedQuizSystem.QuizQuestion currentQuestion) {
        String resultMessage = result.getFeedback() + "\n\n";
        resultMessage += result.getEncouragement() + "\n\n";
        resultMessage += "Points: " + (result.isCorrect() ? "+" + result.getPointsEarned() : "-" + result.getPointsLost()) + "\n";
        if (result.getStreakBonus() > 0) {
            resultMessage += "Streak Bonus: +" + result.getStreakBonus() + " points!\n";
        }
        resultMessage += "\nLearning Tip: " + result.getLearningTip();
        resultMessage += "\nCategory: " + currentQuestion.getCategory();
        
        System.out.println("💼 Quiz Result: " + resultMessage);
        
        // Continue to next question or show summary
        if (currentQuizSession.hasNextQuestion()) {
            // Move to next question
            EnhancedQuizSystem.QuizQuestion nextQuestion = currentQuizSession.getCurrentQuestion();
            if (nextQuestion != null) {
                System.out.println("💼 Next question: " + nextQuestion.getQuestion());
                // Create next question dialogue would be handled by the conversation system
            }
        } else {
            // Show final results
            EnhancedQuizSystem.QuizSessionSummary summary = currentQuizSession.getSummary();
            showQuizSummary(summary);
        }
    }
    
    /**
     * Show quiz summary with comprehensive feedback
     */
    private void showQuizSummary(EnhancedQuizSystem.QuizSessionSummary summary) {
        String summaryMessage = "🎯 Quiz Complete!\n\n";
        summaryMessage += "Score: " + summary.getCorrectAnswers() + "/" + summary.getTotalQuestions() + " (" + String.format("%.1f%%", summary.getSuccessRate() * 100) + ")\n";
        summaryMessage += "Grade: " + summary.getGrade() + "\n";
        summaryMessage += "Total Points: " + summary.getTotalPoints() + "\n";
        summaryMessage += "Max Streak: " + summary.getMaxStreak() + "\n";
        summaryMessage += "Time: " + String.format("%.1f", summary.getTimeSpent()) + " seconds\n\n";
        summaryMessage += summary.getPerformanceMessage();
        
        System.out.println("💼 Quiz Summary: " + summaryMessage);
    }
    
    /**
     * Create information dialogue about the quiz system
     */
    private EnhancedConversationSystem.DialogueNode createInfoDialogue() {
        return new EnhancedConversationSystem.DialogueNode(
            name,
            """
            My quiz system is designed to test and improve your financial knowledge:

            🎯 **Difficulty Levels:**
            • Beginner: Basic concepts (25-50 coins)
            • Intermediate: Investment & credit (50-100 coins)
            • Advanced: Complex planning (75-150 coins)

            🏆 **Scoring System:**
            • Correct answers earn coins
            • Wrong answers cost coins
            • Streak bonuses for consecutive correct answers
            • Performance affects my mood and responses

            📚 **Learning Features:**
            • Detailed explanations for each answer
            • Personalized feedback based on performance
            • Adaptive difficulty based on your progress
            • Progress tracking and statistics
            """,
            "wise"
        );
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
    public NPCPersonalitySystem.PersonalityProfile getPersonalityProfile() { return personalityProfile; }
    public EnhancedConversationSystem getConversationSystem() { return conversationSystem; }
}
