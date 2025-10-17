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
 * Test Zone for demonstrating NPC character objects
 * Simple zone to test NPC rendering and interaction
 */
public class NPCTestZone extends AdventureZone {
    
    private final NPCManager npcManager;
    private NPCCharacter testNPC1;
    private NPCCharacter testNPC2;
    private NPCCharacter testNPC3;
    
    public NPCTestZone() {
        super("NPC Test Zone", "Test zone for NPC character objects");
        this.zoneColor = Color.LIGHTCORAL;
        this.backgroundStyle = "linear-gradient(135deg, #FFB6C1 0%, #FF69B4 100%)";
        
        // Initialize NPC Manager
        this.npcManager = new NPCManager();
    }
    
    @Override
    protected void setupZone() {
        System.out.println("🧪 Setting up NPC Test Zone");
        
        // Create test NPCs with different types
        setupTestNPCs();
        
        // Add simple interactables
        setupTestInteractables();
    }
    
    private void setupTestNPCs() {
        // Test NPC 1 - Merchant
        String[] merchantDialogue = {
            "Hello! I'm a test merchant NPC.",
            "I can teach you about business and trade.",
            "Would you like to learn about profit margins?",
            "Remember: Buy low, sell high!"
        };
        
        testNPC1 = npcManager.createMerchant(
            "Test Merchant",
            "Welcome to my test shop! I'm here to demonstrate NPC functionality.",
            merchantDialogue,
            200, 200
        );
        
        // Test NPC 2 - Adventurer
        String[] adventurerDialogue = {
            "Greetings, fellow adventurer!",
            "I'm here to test the NPC interaction system.",
            "This is a demonstration of the new NPC character objects.",
            "Try interacting with me to see the dialogue system!"
        };
        
        testNPC2 = npcManager.createAdventurer(
            "Test Adventurer",
            "Hello! I'm a test adventurer NPC. Let's explore the NPC system together!",
            adventurerDialogue,
            600, 300
        );
        
        // Test NPC 3 - Sage
        String[] sageDialogue = {
            "Welcome, seeker of knowledge.",
            "I am a test sage NPC, here to demonstrate wisdom.",
            "The NPC system allows for rich character interactions.",
            "Each NPC can have unique dialogue and behavior."
        };
        
        testNPC3 = npcManager.createSage(
            "Test Sage",
            "Greetings, young one. I am a test sage NPC demonstrating the new character system.",
            sageDialogue,
            400, 500
        );
        
        System.out.println("✅ Created " + npcManager.getNPCCount() + " test NPCs");
    }
    
    private void setupTestInteractables() {
        // Test Interactable 1
        AdventureInteractable testStation1 = new AdventureInteractable(
            "Test Station 1",
            "🧪",
            "This is a test interactable to demonstrate the system",
            100, 100
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                System.out.println("🧪 Test Station 1 activated!");
                System.out.println("📍 Player position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
                System.out.println("💡 This demonstrates interactable functionality.");
            }
        };
        interactables.add(testStation1);
        
        // Test Interactable 2
        AdventureInteractable testStation2 = new AdventureInteractable(
            "Test Station 2",
            "⚗️",
            "Another test interactable for system demonstration",
            700, 100
        ) {
            @Override
            public void onInteract(AdventurePlayer player) {
                System.out.println("⚗️ Test Station 2 activated!");
                System.out.println("📍 Player position: (" + player.getCenterX() + ", " + player.getCenterY() + ")");
                System.out.println("💡 Multiple interactables can exist in the same zone.");
            }
        };
        interactables.add(testStation2);
    }
    
    @Override
    public void renderZone(Pane gameWorld) {
        System.out.println("🎨 Rendering NPC Test Zone");
        
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
        
        System.out.println("✅ NPC Test Zone rendered with " + npcManager.getNPCCount() + " NPCs");
    }
    
    @Override
    protected void renderZoneElements(Pane gameWorld) {
        // Add test zone decorations
        addTestDecorations(gameWorld);
        addZoneTitle(gameWorld);
        addInstructions(gameWorld);
    }
    
    private void addTestDecorations(Pane gameWorld) {
        // Add test circles
        for (int i = 0; i < 5; i++) {
            Circle testCircle = new Circle(
                150 + i * 200,
                600,
                30,
                Color.LIGHTYELLOW
            );
            testCircle.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(testCircle);
        }
        
        // Add test rectangles
        for (int i = 0; i < 3; i++) {
            Rectangle testRect = new Rectangle(
                50 + i * 300,
                50,
                100,
                50
            );
            testRect.setFill(Color.LIGHTGREEN);
            testRect.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(testRect);
        }
    }
    
    private void addZoneTitle(Pane gameWorld) {
        Text title = new Text(50, 50, "🧪 NPC Test Zone");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);
        title.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(title);
        
        Text subtitle = new Text(50, 80, "Demonstrating NPC Character Objects");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setFill(Color.LIGHTYELLOW);
        subtitle.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(subtitle);
    }
    
    private void addInstructions(Pane gameWorld) {
        Text instructions = new Text(50, 120, "Instructions:");
        instructions.setFont(Font.font("Arial", 14));
        instructions.setFill(Color.WHITE);
        instructions.getStyleClass().add("zone-element");
        gameWorld.getChildren().add(instructions);
        
        String[] instructionLines = {
            "• Walk near NPCs to interact with them",
            "• Each NPC has unique dialogue and behavior",
            "• Test interactables are scattered around the zone",
            "• This demonstrates the new NPC character system"
        };
        
        for (int i = 0; i < instructionLines.length; i++) {
            Text instruction = new Text(70, 140 + i * 20, instructionLines[i]);
            instruction.setFont(Font.font("Arial", 12));
            instruction.setFill(Color.LIGHTYELLOW);
            instruction.getStyleClass().add("zone-element");
            gameWorld.getChildren().add(instruction);
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
            System.out.println("🎉 NPC Test Zone completed! All NPCs interacted with.");
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
    
    @Override
    public void cleanup() {
        if (npcManager != null) {
            npcManager.cleanup();
        }
    }
    
    // Getters for testing
    public NPCCharacter getTestNPC1() { return testNPC1; }
    public NPCCharacter getTestNPC2() { return testNPC2; }
    public NPCCharacter getTestNPC3() { return testNPC3; }
    public NPCManager getNPCManager() { return npcManager; }
}
