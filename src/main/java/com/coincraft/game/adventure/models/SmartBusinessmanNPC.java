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
 * Smart Businessman NPC - Alexander Venture
 * Quiz master and financial knowledge tester
 */
public class SmartBusinessmanNPC {
    private final String name = "Alexander Venture";
    private final String emoji = "💼";
    private final String greeting = "Ah, a potential business partner! Test your financial knowledge with my quiz. Correct answers earn coins, wrong answers cost coins!";
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    private ConversationSystem conversationSystem;
    
    public SmartBusinessmanNPC(double x, double y) {
        this.x = x;
        this.y = y;
        createNPCSprite();
        initializeConversationSystem();
    }
    
    private void createNPCSprite() {
        try {
            // Direct path to Smart Businessman image
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
            // Fallback to distinctive circle
            npcSprite = new Circle(40, Color.GOLD);
            ((Circle) npcSprite).setStroke(Color.DARKGOLDENROD);
            ((Circle) npcSprite).setStrokeWidth(3);
        }
        
        // Add emoji text
        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(20));
        emojiText.setFill(Color.WHITE);
        emojiText.setLayoutX(x - 10);
        emojiText.setLayoutY(y + 10);
        
        // Add name label
        npcName = new Text(name);
        npcName.setFont(Font.font(14));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
    }
    
    public void render(Pane gameWorld) {
        System.out.println("Rendering Smart Businessman at (" + x + ", " + y + ")");
        
        // Position and add sprite
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        // Position and add name
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        gameWorld.getChildren().add(npcName);
        
        System.out.println("Smart Businessman rendered successfully");
    }
    
    public void interact(AdventurePlayer player) {
        if (conversationSystem != null) {
            conversationSystem.startConversation();
        } else {
            // Fallback to simple interaction
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
     * Initialize the conversation system with financial knowledge tests
     */
    private void initializeConversationSystem() {
        // Create dialogue tree for financial knowledge testing
        ConversationSystem.DialogueNode greetingNode = new ConversationSystem.DialogueNode(
            name, 
            "Welcome, potential business partner! I'm Alexander Venture, and I believe in testing financial knowledge through practical application. " +
            "I have several knowledge tests that will challenge your understanding of financial concepts. " +
            "Correct answers earn you coins, while incorrect answers cost coins. Are you ready to prove your financial acumen?"
        );
        
        // Basic Financial Concepts Quiz
        ConversationSystem.DialogueNode basicQuiz = new ConversationSystem.DialogueNode(
            name,
            """
            Excellent! Let's start with Basic Financial Concepts. Here's your first question:

            What is the primary purpose of an emergency fund?

            A) To invest in stocks
            B) To cover unexpected expenses without going into debt
            C) To pay for vacations
            D) To buy luxury items
            """
        );
        
        ConversationSystem.DialogueNode basicQuizCorrect = new ConversationSystem.DialogueNode(
            name,
            """
            🎉 Correct! An emergency fund is designed to cover unexpected expenses without going into debt. 
            This financial safety net prevents you from using credit cards or loans for emergencies. 
            You've earned 50 coins! +50 coins

            Would you like to try another quiz?
            """
        );
        
        ConversationSystem.DialogueNode basicQuizWrong = new ConversationSystem.DialogueNode(
            name,
            """
            ❌ Incorrect! The correct answer is B) To cover unexpected expenses without going into debt. 
            An emergency fund is your financial safety net. You've lost 25 coins. -25 coins

            Would you like to try another quiz?
            """
        );
        
        // Investment Knowledge Quiz
        ConversationSystem.DialogueNode investmentQuiz = new ConversationSystem.DialogueNode(
            name,
            """
            Great! Now let's test your Investment Knowledge:

            What is the main advantage of dollar-cost averaging?

            A) It guarantees higher returns
            B) It reduces the impact of market volatility
            C) It eliminates all investment risk
            D) It only works with stocks
            """
        );
        
        ConversationSystem.DialogueNode investmentQuizCorrect = new ConversationSystem.DialogueNode(
            name,
            """
            🎉 Excellent! Dollar-cost averaging reduces the impact of market volatility by spreading your investments over time. 
            This strategy helps you avoid the stress of timing the market. You've earned 75 coins! +75 coins

            Ready for the next challenge?
            """
        );
        
        ConversationSystem.DialogueNode investmentQuizWrong = new ConversationSystem.DialogueNode(
            name,
            """
            ❌ Not quite! The correct answer is B) It reduces the impact of market volatility. 
            Dollar-cost averaging helps smooth out market fluctuations. You've lost 30 coins. -30 coins

            Want to try another question?
            """
        );
        
        // Credit Knowledge Quiz
        ConversationSystem.DialogueNode creditQuiz = new ConversationSystem.DialogueNode(
            name,
            """
            Perfect! Let's test your Credit Knowledge:

            What percentage of your credit score is based on payment history?

            A) 15%
            B) 25%
            C) 35%
            D) 45%
            """
        );
        
        ConversationSystem.DialogueNode creditQuizCorrect = new ConversationSystem.DialogueNode(
            name,
            """
            🎉 Outstanding! Payment history accounts for 35% of your credit score, making it the most important factor. 
            Consistent on-time payments are crucial for building good credit. You've earned 100 coins! +100 coins

            You're doing well! Another quiz?
            """
        );
        
        ConversationSystem.DialogueNode creditQuizWrong = new ConversationSystem.DialogueNode(
            name,
            """
            ❌ Close, but not quite! Payment history accounts for 35% of your credit score. 
            This is why paying bills on time is so important. You've lost 40 coins. -40 coins

            Ready for another challenge?
            """
        );
        
        // Advanced Financial Planning Quiz
        ConversationSystem.DialogueNode advancedQuiz = new ConversationSystem.DialogueNode(
            name,
            """
            Excellent! Now for the Advanced Financial Planning challenge:

            What is the 'Rule of 72' used for?

            A) Calculating tax deductions
            B) Estimating how long it takes for money to double at a given interest rate
            C) Determining credit card interest
            D) Calculating mortgage payments
            """
        );
        
        ConversationSystem.DialogueNode advancedQuizCorrect = new ConversationSystem.DialogueNode(
            name,
            """
            🎉 Brilliant! The Rule of 72 helps estimate how long it takes for money to double at a given interest rate. 
            Divide 72 by the interest rate to get the approximate years. You've earned 150 coins! +150 coins

            You're a financial wizard! Want to test more knowledge?
            """
        );
        
        ConversationSystem.DialogueNode advancedQuizWrong = new ConversationSystem.DialogueNode(
            name,
            """
            ❌ Not this time! The Rule of 72 estimates how long it takes for money to double at a given interest rate. 
            It's a handy mental math tool for investors. You've lost 50 coins. -50 coins

            Don't give up! Try another quiz?
            """
        );
        
        // Set up conversation options
        greetingNode.addOption("I'm ready for the Basic Financial Concepts quiz!", basicQuiz);
        greetingNode.addOption("Challenge me with Investment Knowledge!", investmentQuiz);
        greetingNode.addOption("Test my Credit Knowledge!", creditQuiz);
        greetingNode.addOption("Give me the Advanced Financial Planning quiz!", advancedQuiz);
        greetingNode.addOption("Maybe later, I need to study more.", null);
        
        // Basic quiz options
        basicQuiz.addOption("A) To invest in stocks", basicQuizWrong);
        basicQuiz.addOption("B) To cover unexpected expenses without going into debt", basicQuizCorrect);
        basicQuiz.addOption("C) To pay for vacations", basicQuizWrong);
        basicQuiz.addOption("D) To buy luxury items", basicQuizWrong);
        
        // Investment quiz options
        investmentQuiz.addOption("A) It guarantees higher returns", investmentQuizWrong);
        investmentQuiz.addOption("B) It reduces the impact of market volatility", investmentQuizCorrect);
        investmentQuiz.addOption("C) It eliminates all investment risk", investmentQuizWrong);
        investmentQuiz.addOption("D) It only works with stocks", investmentQuizWrong);
        
        // Credit quiz options
        creditQuiz.addOption("A) 15%", creditQuizWrong);
        creditQuiz.addOption("B) 25%", creditQuizWrong);
        creditQuiz.addOption("C) 35%", creditQuizCorrect);
        creditQuiz.addOption("D) 45%", creditQuizWrong);
        
        // Advanced quiz options
        advancedQuiz.addOption("A) Calculating tax deductions", advancedQuizWrong);
        advancedQuiz.addOption("B) Estimating how long it takes for money to double at a given interest rate", advancedQuizCorrect);
        advancedQuiz.addOption("C) Determining credit card interest", advancedQuizWrong);
        advancedQuiz.addOption("D) Calculating mortgage payments", advancedQuizWrong);
        
        // Follow-up options for all quiz results
        basicQuizCorrect.addOption("Yes, give me the Investment Knowledge quiz!", investmentQuiz);
        basicQuizCorrect.addOption("Test my Credit Knowledge!", creditQuiz);
        basicQuizCorrect.addOption("Challenge me with Advanced Financial Planning!", advancedQuiz);
        basicQuizCorrect.addOption("That's enough for now, thank you!", null);
        
        basicQuizWrong.addOption("Yes, let me try the Investment Knowledge quiz!", investmentQuiz);
        basicQuizWrong.addOption("Test my Credit Knowledge!", creditQuiz);
        basicQuizWrong.addOption("Give me the Advanced Financial Planning quiz!", advancedQuiz);
        basicQuizWrong.addOption("I need to study more first.", null);
        
        investmentQuizCorrect.addOption("Yes, test my Credit Knowledge!", creditQuiz);
        investmentQuizCorrect.addOption("Give me the Advanced Financial Planning quiz!", advancedQuiz);
        investmentQuizCorrect.addOption("Let me try the Basic Financial Concepts quiz!", basicQuiz);
        investmentQuizCorrect.addOption("I'm satisfied with my performance!", null);
        
        investmentQuizWrong.addOption("Yes, test my Credit Knowledge!", creditQuiz);
        investmentQuizWrong.addOption("Give me the Advanced Financial Planning quiz!", advancedQuiz);
        investmentQuizWrong.addOption("Let me try the Basic Financial Concepts quiz!", basicQuiz);
        investmentQuizWrong.addOption("I need more practice!", null);
        
        creditQuizCorrect.addOption("Yes, give me the Advanced Financial Planning quiz!", advancedQuiz);
        creditQuizCorrect.addOption("Let me try the Investment Knowledge quiz!", investmentQuiz);
        creditQuizCorrect.addOption("Test me with Basic Financial Concepts!", basicQuiz);
        creditQuizCorrect.addOption("I've learned enough for today!", null);
        
        creditQuizWrong.addOption("Yes, give me the Advanced Financial Planning quiz!", advancedQuiz);
        creditQuizWrong.addOption("Let me try the Investment Knowledge quiz!", investmentQuiz);
        creditQuizWrong.addOption("Test me with Basic Financial Concepts!", basicQuiz);
        creditQuizWrong.addOption("I need to study credit concepts more!", null);
        
        advancedQuizCorrect.addOption("Yes, let me try the Basic Financial Concepts quiz!", basicQuiz);
        advancedQuizCorrect.addOption("Test my Investment Knowledge!", investmentQuiz);
        advancedQuizCorrect.addOption("Challenge me with Credit Knowledge!", creditQuiz);
        advancedQuizCorrect.addOption("I'm ready to apply this knowledge!", null);
        
        advancedQuizWrong.addOption("Yes, let me try the Basic Financial Concepts quiz!", basicQuiz);
        advancedQuizWrong.addOption("Test my Investment Knowledge!", investmentQuiz);
        advancedQuizWrong.addOption("Challenge me with Credit Knowledge!", creditQuiz);
        advancedQuizWrong.addOption("I need to study financial planning more!", null);
        
        // Create conversation system
        conversationSystem = new ConversationSystem(name, "Financial Quiz Master", greetingNode);
        
        System.out.println("💼 Smart Businessman conversation system initialized");
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
}
