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
 * Strong Adventurer NPC - Thorin Ironbeard
 * Game guide and adventure tips provider
 */
public class StrongAdventurerNPC {
    private final String name = "Thorin Ironbeard";
    private final String emoji = "🗡️";
    private final String greeting = "Welcome, brave adventurer! I'll guide you through this financial realm and teach you the ways of smart money management!";
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private boolean hasInteracted = false;
    private EnhancedConversationSystem conversationSystem;
    private NPCPersonalitySystem.PersonalityProfile personalityProfile;
    
    public StrongAdventurerNPC(double x, double y) {
        this.x = x;
        this.y = y;
        createNPCSprite();
        initializePersonalitySystem();
        initializeConversationSystem();
    }
    
    private void createNPCSprite() {
        try {
            // Direct path to Strong Adventurer image
            String imagePath = getClass().getResource("/Assets/NPC/Strong Adventurere.png").toExternalForm();
            System.out.println("Loading Strong Adventurer image from: " + imagePath);
            
            Image npcImage = new Image(imagePath);
            ImageView npcImageView = new ImageView(npcImage);
            npcImageView.setFitWidth(80);
            npcImageView.setFitHeight(80);
            npcImageView.setPreserveRatio(true);
            npcSprite = npcImageView;
            
            System.out.println("Successfully loaded Strong Adventurer sprite");
        } catch (Exception e) {
            System.out.println("Failed to load Strong Adventurer image: " + e.getMessage());
            // Fallback to distinctive circle
            npcSprite = new Circle(40, Color.ORANGE);
            ((Circle) npcSprite).setStroke(Color.DARKORANGE);
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
        System.out.println("Rendering Strong Adventurer at (" + x + ", " + y + ")");
        
        // Position and add sprite
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        // Position and add name
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        gameWorld.getChildren().add(npcName);
        
        System.out.println("Strong Adventurer rendered successfully");
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
        System.out.println("🗡️ " + name + ": " + greeting);
    }
    
    private void showFollowUpDialogue() {
        String[] followUps = {
            "Need more adventure tips? I'm here to help!",
            "Remember: Every great financial journey starts with a single step!",
            "Ready for your next challenge, brave adventurer?",
            "The path to wealth is paved with knowledge and courage!"
        };
        
        String followUp = followUps[(int)(Math.random() * followUps.length)];
        System.out.println("🗡️ " + name + ": " + followUp);
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
     * Initialize the personality system for the Strong Adventurer
     */
    private void initializePersonalitySystem() {
        personalityProfile = new NPCPersonalitySystem.PersonalityProfile(
            NPCPersonalitySystem.PersonalityType.ADVENTUROUS
        );
        System.out.println("🗡️ Strong Adventurer personality system initialized");
    }
    
    /**
     * Initialize the enhanced conversation system with adventure-themed dialogue
     */
    private void initializeConversationSystem() {
        // Create enhanced dialogue tree for adventure conversations
        EnhancedConversationSystem.DialogueNode greetingNode = new EnhancedConversationSystem.DialogueNode(
            name, 
            personalityProfile.getPersonalizedGreeting() + " I'm Thorin Ironbeard, veteran of countless financial quests. I can teach you the ways of smart money management and adventure strategies!",
            "excited"
        );
        
        // Adventure tips conversation
        EnhancedConversationSystem.DialogueNode adventureTips = new EnhancedConversationSystem.DialogueNode(
            name,
            personalityProfile.getTopicResponse("adventure", true) + " Let me share the secrets of successful adventuring: Always plan your financial journey, never invest more than you can afford to lose, and remember - every great adventure starts with a single step!",
            "confident"
        );
        
        // Financial adventure strategies
        EnhancedConversationSystem.DialogueNode financialStrategies = new EnhancedConversationSystem.DialogueNode(
            name,
            personalityProfile.getTopicResponse("strategy", true) + " Here are my battle-tested strategies:\n\n" +
            "1. Diversify your treasure - don't put all your gold in one chest!\n" +
            "2. Save for emergencies - every adventurer needs a safety net!\n" +
            "3. Learn from failures - even the greatest heroes make mistakes!\n" +
            "4. Invest in knowledge - the best weapon is a sharp mind!",
            "wise"
        );
        
        // Adventure motivation
        EnhancedConversationSystem.DialogueNode motivation = new EnhancedConversationSystem.DialogueNode(
            name,
            personalityProfile.getTopicResponse("motivation", true) + " The path to wealth is not a sprint, it's a marathon! Every great hero started as a beginner. Stay persistent, keep learning, and never give up on your financial quest!",
            "encouraging"
        );
        
        // Adventure challenges
        EnhancedConversationSystem.DialogueNode challenges = new EnhancedConversationSystem.DialogueNode(
            name,
            personalityProfile.getTopicResponse("challenges", true) + " Market volatility is like a dragon - scary but beatable with the right strategy. Economic downturns are like dungeons - they seem dark but contain hidden treasures for the prepared!",
            "confident"
        );
        
        // Set up conversation options
        greetingNode.addOption("Tell me about adventure strategies!", adventureTips);
        greetingNode.addOption("What financial wisdom do you have?", financialStrategies);
        greetingNode.addOption("I need motivation!", motivation);
        greetingNode.addOption("How do I handle financial challenges?", challenges);
        greetingNode.addOption("Thank you, wise adventurer!", null);
        
        adventureTips.addOption("Tell me more about financial strategies!", financialStrategies);
        adventureTips.addOption("How do I stay motivated?", motivation);
        adventureTips.addOption("What about challenges?", challenges);
        adventureTips.addOption("Thank you for the wisdom!", null);
        
        financialStrategies.addOption("How do I stay motivated on this journey?", motivation);
        financialStrategies.addOption("What about facing challenges?", challenges);
        financialStrategies.addOption("Tell me more adventure tips!", adventureTips);
        financialStrategies.addOption("Thank you for the guidance!", null);
        
        motivation.addOption("What strategies should I use?", financialStrategies);
        motivation.addOption("How do I handle challenges?", challenges);
        motivation.addOption("More adventure wisdom please!", adventureTips);
        motivation.addOption("I'm ready for my quest!", null);
        
        challenges.addOption("What strategies help with challenges?", financialStrategies);
        challenges.addOption("How do I stay motivated?", motivation);
        challenges.addOption("More adventure tips!", adventureTips);
        challenges.addOption("I understand now!", null);
        
        // Create enhanced conversation system
        conversationSystem = new EnhancedConversationSystem(name, "Adventure Guide", "Adventurous", greetingNode);
        
        System.out.println("🗡️ Strong Adventurer conversation system initialized");
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public boolean hasInteracted() { return hasInteracted; }
}
