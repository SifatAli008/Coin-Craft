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
 * Wise Lady NPC - Sage Wisdomheart
 * Financial literacy teacher and wisdom provider
 */
public class WiseLadyNPC {
    private final String name = "Sage Wisdomheart";
    private final String emoji = "🧙‍♀️";
    private final String greeting = "Greetings, young learner. I shall impart the ancient wisdom of financial literacy. Listen well to my teachings!";
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    private ConversationSystem conversationSystem;
    
    public WiseLadyNPC(double x, double y) {
        this.x = x;
        this.y = y;
        createNPCSprite();
        initializeConversationSystem();
    }
    
    private void createNPCSprite() {
        try {
            // Direct path to Wise Lady image
            String imagePath = getClass().getResource("/Assets/NPC/Wise Lady.png").toExternalForm();
            System.out.println("Loading Wise Lady image from: " + imagePath);
            
            Image npcImage = new Image(imagePath);
            ImageView npcImageView = new ImageView(npcImage);
            npcImageView.setFitWidth(80);
            npcImageView.setFitHeight(80);
            npcImageView.setPreserveRatio(true);
            npcSprite = npcImageView;
            
            System.out.println("Successfully loaded Wise Lady sprite");
        } catch (Exception e) {
            System.out.println("Failed to load Wise Lady image: " + e.getMessage());
            // Fallback to distinctive circle
            npcSprite = new Circle(40, Color.LIGHTBLUE);
            ((Circle) npcSprite).setStroke(Color.DARKBLUE);
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
        System.out.println("Rendering Wise Lady at (" + x + ", " + y + ")");
        
        // Position and add sprite
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        // Position and add name
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        gameWorld.getChildren().add(npcName);
        
        System.out.println("Wise Lady rendered successfully");
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
        System.out.println("🧙‍♀️ " + name + ": " + greeting);
    }
    
    private void showFollowUpDialogue() {
        String[] followUps = {
            "Seek more wisdom, young one. Knowledge is the key to wealth!",
            "Remember: A wise person learns from every experience!",
            "Come back anytime for more financial guidance!",
            "The path to financial freedom begins with education!"
        };
        
        String followUp = followUps[(int)(Math.random() * followUps.length)];
        System.out.println("🧙‍♀️ " + name + ": " + followUp);
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
     * Initialize the conversation system with financial literacy teachings
     */
    private void initializeConversationSystem() {
        // Create dialogue tree for financial literacy conversations
        ConversationSystem.DialogueNode greetingNode = new ConversationSystem.DialogueNode(
            name, 
            "Greetings, young learner. I am Sage Wisdomheart, keeper of ancient financial wisdom. I shall impart knowledge that will guide you to prosperity and financial freedom. What aspect of financial literacy do you wish to learn?"
        );
        
        // Budgeting and saving wisdom
        ConversationSystem.DialogueNode budgeting = new ConversationSystem.DialogueNode(
            name,
            """
            Ah, the foundation of all financial wisdom - budgeting! Listen well:

            • The 50/30/20 Rule: 50% for needs, 30% for wants, 20% for savings
            • Track every coin that enters and leaves your purse
            • Pay yourself first - save before spending
            • Build an emergency fund of 3-6 months' expenses
            • Remember: A budget is not a restriction, it's a plan for your financial future!
            """
        );
        
        // Investment wisdom
        ConversationSystem.DialogueNode investing = new ConversationSystem.DialogueNode(
            name,
            """
            Investment wisdom, the path to wealth multiplication:

            • Start early - time is your greatest ally in investing
            • Diversify your investments - don't put all eggs in one basket
            • Understand compound interest - the eighth wonder of the world
            • Invest in what you understand
            • Dollar-cost averaging reduces risk over time
            • Remember: The best time to plant a tree was 20 years ago, the second best time is now!
            """
        );
        
        // Debt management
        ConversationSystem.DialogueNode debtManagement = new ConversationSystem.DialogueNode(
            name,
            """
            Debt management, the art of financial liberation:

            • Pay off high-interest debt first (credit cards, payday loans)
            • Use the debt snowball method for motivation
            • Avoid new debt unless absolutely necessary
            • Good debt vs. bad debt - mortgages and student loans can be investments
            • Credit cards are tools, not emergency funds
            • Remember: Debt is like a heavy chain - the sooner you break free, the lighter you feel!
            """
        );
        
        // Credit wisdom
        ConversationSystem.DialogueNode creditWisdom = new ConversationSystem.DialogueNode(
            name,
            """
            Credit wisdom, the key to financial opportunities:

            • Your credit score opens doors to better rates
            • Pay bills on time - this is 35% of your credit score
            • Keep credit utilization below 30%
            • Don't close old accounts - length of credit history matters
            • Check your credit report regularly
            • Remember: Good credit is like a good reputation - it takes time to build but can be lost quickly!
            """
        );
        
        // Financial goals
        ConversationSystem.DialogueNode financialGoals = new ConversationSystem.DialogueNode(
            name,
            """
            Financial goal setting, the compass of your financial journey:

            • Set SMART goals: Specific, Measurable, Achievable, Relevant, Time-bound
            • Short-term goals (1-2 years): Emergency fund, vacation
            • Medium-term goals (3-5 years): House down payment, car
            • Long-term goals (10+ years): Retirement, children's education
            • Write down your goals - they become more real
            • Review and adjust goals regularly
            • Remember: A goal without a plan is just a wish!
            """
        );
        
        // Set up conversation options
        greetingNode.addOption("Teach me about budgeting and saving!", budgeting);
        greetingNode.addOption("I want to learn about investing!", investing);
        greetingNode.addOption("How do I manage debt wisely?", debtManagement);
        greetingNode.addOption("Tell me about credit and credit scores!", creditWisdom);
        greetingNode.addOption("How do I set financial goals?", financialGoals);
        greetingNode.addOption("Thank you for your wisdom!", null);
        
        budgeting.addOption("What about investing?", investing);
        budgeting.addOption("How do I handle debt?", debtManagement);
        budgeting.addOption("Tell me about credit!", creditWisdom);
        budgeting.addOption("How do I set goals?", financialGoals);
        budgeting.addOption("Thank you for the budgeting wisdom!", null);
        
        investing.addOption("What about budgeting first?", budgeting);
        investing.addOption("How do I manage debt while investing?", debtManagement);
        investing.addOption("How does credit affect investing?", creditWisdom);
        investing.addOption("What goals should I set for investing?", financialGoals);
        investing.addOption("Thank you for the investment wisdom!", null);
        
        debtManagement.addOption("How do I budget to pay off debt?", budgeting);
        debtManagement.addOption("Should I invest while paying debt?", investing);
        debtManagement.addOption("How does debt affect my credit?", creditWisdom);
        debtManagement.addOption("What goals help with debt payoff?", financialGoals);
        debtManagement.addOption("Thank you for the debt wisdom!", null);
        
        creditWisdom.addOption("How does budgeting help my credit?", budgeting);
        creditWisdom.addOption("How does credit affect investing?", investing);
        creditWisdom.addOption("How do I rebuild credit after debt?", debtManagement);
        creditWisdom.addOption("What goals involve credit?", financialGoals);
        creditWisdom.addOption("Thank you for the credit wisdom!", null);
        
        financialGoals.addOption("How do I budget for my goals?", budgeting);
        financialGoals.addOption("What investment goals should I have?", investing);
        financialGoals.addOption("How do I set debt payoff goals?", debtManagement);
        financialGoals.addOption("How do goals affect my credit?", creditWisdom);
        financialGoals.addOption("Thank you for the goal-setting wisdom!", null);
        
        // Create conversation system
        conversationSystem = new ConversationSystem(name, "Financial Sage", greetingNode);
        
        System.out.println("🧙‍♀️ Wise Lady conversation system initialized");
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
}
