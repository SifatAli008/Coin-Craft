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
import com.coincraft.game.adventure.models.NPCCharacter;
import com.coincraft.game.adventure.models.NPCManager;

/**
 * Enhanced Financial Learning Zone with advanced NPC system
 * Demonstrates the new NPC character objects integration
 */
public class EnhancedFinancialZone extends AdventureZone {
    
    private final NPCManager npcManager;
    private NPCCharacter merchantNPC;
    private NPCCharacter adventurerNPC;
    private NPCCharacter sageNPC;
    private NPCCharacter teacherNPC;
    private NPCCharacter guideNPC;
    
    public EnhancedFinancialZone() {
        super("Enhanced Financial Learning Zone", "Advanced financial education with interactive NPCs");
        this.zoneColor = Color.LIGHTBLUE;
        this.backgroundStyle = "linear-gradient(135deg, #87CEEB 0%, #4682B4 100%)";
        
        // Initialize NPC Manager
        this.npcManager = new NPCManager();
    }
    
    @Override
    protected void setupZone() {
        System.out.println("🏗️ Setting up Enhanced Financial Zone with NPCs");
        
        // Create specialized NPCs with rich dialogue
        setupMerchantNPC();
        setupAdventurerNPC();
        setupSageNPC();
        setupTeacherNPC();
        setupGuideNPC();
        
        // Add traditional interactables
        setupInteractables();
    }
    
    private void setupMerchantNPC() {
        String[] merchantDialogue = {
            "Welcome to my financial emporium! I deal in knowledge and wisdom.",
            "The key to wealth is understanding the difference between assets and liabilities.",
            "Remember: 'A penny saved is a penny earned' - Benjamin Franklin",
            "Would you like to learn about investment strategies?",
            "Diversification is your best friend in the financial world!"
        };
        
        merchantNPC = npcManager.createMerchant(
            "Alexander Venture",
            "Greetings, young entrepreneur! I'm here to teach you the art of business and investment.",
            merchantDialogue,
            200, 300
        );
        
        System.out.println("✅ Created Merchant NPC: " + merchantNPC.getName());
    }
    
    private void setupAdventurerNPC() {
        String[] adventurerDialogue = {
            "Welcome, brave soul! I'll guide you through the financial wilderness.",
            "Every great journey begins with a single step - and a solid budget!",
            "Risk management is like having a good map in uncharted territory.",
            "The greatest treasure isn't gold - it's financial knowledge!",
            "Ready for your next financial adventure?"
        };
        
        adventurerNPC = npcManager.createAdventurer(
            "Thorin Ironbeard",
            "Hail, fellow adventurer! I'm here to help you navigate the treacherous waters of personal finance.",
            adventurerDialogue,
            600, 200
        );
        
        System.out.println("✅ Created Adventurer NPC: " + adventurerNPC.getName());
    }
    
    private void setupSageNPC() {
        String[] sageDialogue = {
            "Welcome, seeker of wisdom. I shall impart ancient financial knowledge.",
            "The wise person saves for tomorrow while living today.",
            "Compound interest is the eighth wonder of the world - Einstein said so!",
            "Patience and discipline are the keys to financial success.",
            "True wealth is measured not in coins, but in financial freedom."
        };
        
        sageNPC = npcManager.createSage(
            "Sage Wisdomheart",
            "Greetings, young learner. I possess the ancient wisdom of financial literacy.",
            sageDialogue,
            400, 500
        );
        
        System.out.println("✅ Created Sage NPC: " + sageNPC.getName());
    }
    
    private void setupTeacherNPC() {
        String[] teacherDialogue = {
            "Hello, student! I'm here to make finance fun and easy to understand.",
            "Let's start with the basics: needs vs. wants. Can you tell the difference?",
            "A budget is like a recipe for your money - follow it and you'll succeed!",
            "Saving money is like planting seeds - the more you plant, the more you harvest!",
            "Remember: learning about money is an investment in yourself!"
        };
        
        teacherNPC = npcManager.createTeacher(
            "Professor Pennywise",
            "Welcome to my classroom! I make financial education engaging and accessible for everyone.",
            teacherDialogue,
            800, 400
        );
        
        System.out.println("✅ Created Teacher NPC: " + teacherNPC.getName());
    }
    
    private void setupGuideNPC() {
        String[] guideDialogue = {
            "Welcome to the Financial Learning Zone! I'm your guide to success.",
            "This zone is designed to teach you essential financial skills.",
            "Each NPC here has unique knowledge to share. Talk to them all!",
            "Complete all interactions to unlock advanced financial concepts.",
            "You're doing great! Keep exploring and learning!"
        };
        
        guideNPC = npcManager.createGuide(
            "Guide Goldie",
            "Hello there! I'm your friendly guide to the world of financial education.",
            guideDialogue,
            100, 100
        );
        
        System.out.println("✅ Created Guide NPC: " + guideNPC.getName());
    }
    
    private void setupInteractables() {
        // Financial Calculator Station
        AdventureInteractable calculator = new AdventureInteractable(
            "Financial Calculator",
            "🧮",
            "Calculate your financial goals and savings potential",
            500, 600
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showFinancialCalculator(player);
            }
        };
        interactables.add(calculator);
        
        // Investment Simulator
        AdventureInteractable simulator = new AdventureInteractable(
            "Investment Simulator",
            "📈",
            "Practice investing without real money",
            300, 100
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showInvestmentSimulator(player);
            }
        };
        interactables.add(simulator);
        
        // Budget Planner
        AdventureInteractable budgetPlanner = new AdventureInteractable(
            "Budget Planner",
            "📊",
            "Create and manage your personal budget",
            700, 300
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                showBudgetPlanner(player);
            }
        };
        interactables.add(budgetPlanner);
    }
    
    @Override
    public void renderZone(Pane gameWorld) {
        System.out.println("🎨 Rendering Enhanced Financial Zone");
        
        // Clear existing zone elements
        gameWorld.getChildren().removeIf(node -> 
            node.getStyleClass().contains("zone-element"));
        
        // Add background
        Rectangle background = new Rectangle(0, 0, 1200, 800);
        background.setFill(zoneColor);
        background.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(background);
        
        // Add zone-specific elements
        renderZoneElements(gameWorld);
        
        // Render NPCs using the NPC Manager
        npcManager.render(gameWorld);
        
        // Add interactables
        for (AdventureInteractable interactable : interactables) {
            interactable.render(gameWorld);
        }
        
        System.out.println("✅ Enhanced Financial Zone rendered with " + npcManager.getNPCCount() + " NPCs");
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add financial symbols and decorations
        addFinancialSymbols(gameWorld);
        addZoneTitle(gameWorld);
        addProgressIndicators(gameWorld);
    }
    
    private void addFinancialSymbols(Pane gameWorld) {
        // Add coin symbols
        for (int i = 0; i < 10; i++) {
            Circle coin = new Circle(
                100 + i * 100,
                50 + Math.random() * 50,
                15,
                Color.GOLD
            );
            coin.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(coin);
        }
        
        // Add chart symbols
        for (int i = 0; i < 5; i++) {
            Rectangle chart = new Rectangle(
                50 + i * 200,
                700,
                80,
                20 + i * 10
            );
            chart.setFill(Color.LIGHTGREEN);
            chart.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(chart);
        }
    }
    
    private void addZoneTitle(Pane gameWorld) {
        Text title = new Text(50, 50, "💰 Enhanced Financial Learning Zone");
        title.setFont(Font.font("Arial", 28));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
        
        Text subtitle = new Text(50, 80, "Interactive NPCs • Advanced Learning • Real-world Skills");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.LIGHTYELLOW);
        subtitle.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(subtitle);
    }
    
    private void addProgressIndicators(Pane gameWorld) {
        // Add progress bars for different learning areas
        String[] learningAreas = {"Budgeting", "Saving", "Investing", "Planning"};
        for (int i = 0; i < learningAreas.length; i++) {
            Text label = new Text(50, 150 + i * 30, learningAreas[i] + ":");
            label.setFont(Font.font(14));
            label.setFill(Color.WHITE);
            label.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(label);
            
            Rectangle progressBar = new Rectangle(150, 140 + i * 30, 200, 20);
            progressBar.setFill(Color.DARKGREEN);
            progressBar.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(progressBar);
        }
    }
    
    @Override
    public void update(double deltaTime, AdventurePlayer player) {
        // Update NPC Manager
        npcManager.setCurrentPlayer(player);
        npcManager.update(deltaTime);
        
        // Update interactables
        for (AdventureInteractable interactable : interactables) {
            interactable.update(deltaTime, player);
        }
        
        // Check for zone completion
        checkZoneCompletion(player);
    }
    
    @Override
    protected void checkZoneCompletion(AdventurePlayer player) {
        // Check if player has interacted with all NPCs
        boolean allNPCsInteracted = npcManager.getAllNPCs().stream()
            .allMatch(NPCCharacter::hasInteracted);
        
        if (allNPCsInteracted) {
            setCompleted(true);
            System.out.println("🎉 Enhanced Financial Zone completed! All NPCs interacted with.");
        }
    }
    
    @Override
    public void handlePlayerInteraction(AdventurePlayer player) {
        // Handle NPC interactions through the NPC Manager
        npcManager.handlePlayerInteraction(player);
        
        // Handle traditional interactables
        for (AdventureInteractable interactable : interactables) {
            // Check if player is close enough to interact
            double distance = Math.sqrt(
                Math.pow(player.getCenterX() - interactable.getCenterX(), 2) + 
                Math.pow(player.getCenterY() - interactable.getCenterY(), 2)
            );
            if (distance <= 50.0) { // 50 pixel interaction range
                interactable.interact(player);
            }
        }
    }
    
    // Interaction methods for interactables
    private void showFinancialCalculator(AdventurePlayer player) {
        System.out.println("🧮 Financial Calculator: Let's calculate your savings potential!");
        System.out.println("💰 Current position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
        System.out.println("📊 Example: Save $50/month for 1 year = $600 + interest!");
    }
    
    private void showInvestmentSimulator(AdventurePlayer player) {
        System.out.println("📈 Investment Simulator: Practice makes perfect!");
        System.out.println("🎯 Current position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
        System.out.println("💡 Try different investment strategies risk-free!");
    }
    
    private void showBudgetPlanner(AdventurePlayer player) {
        System.out.println("📊 Budget Planner: Plan your financial future!");
        System.out.println("📍 Current position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
        System.out.println("📝 Track income, expenses, and savings goals!");
    }
    
    public void cleanup() {
        if (npcManager != null) {
            npcManager.cleanup();
        }
    }
    
    // Getters for NPCs (useful for testing and external access)
    public NPCCharacter getMerchantNPC() { return merchantNPC; }
    public NPCCharacter getAdventurerNPC() { return adventurerNPC; }
    public NPCCharacter getSageNPC() { return sageNPC; }
    public NPCCharacter getTeacherNPC() { return teacherNPC; }
    public NPCCharacter getGuideNPC() { return guideNPC; }
    public NPCManager getNPCManager() { return npcManager; }
}
