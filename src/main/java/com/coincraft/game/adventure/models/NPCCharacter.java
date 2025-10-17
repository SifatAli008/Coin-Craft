package com.coincraft.game.adventure.models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import com.coincraft.engine.physics.PhysicsObject;
import com.coincraft.engine.rendering.Renderable;

/**
 * Enhanced NPC Character class that integrates with the game engine
 * Provides physics, rendering, and interaction capabilities
 */
public class NPCCharacter extends PhysicsObject implements Renderable {
    private final String name;
    private final String emoji;
    private final String greeting;
    private final String[] dialogue;
    private final NPCType type;
    
    // Visual components
    private Node npcSprite;
    private Text npcName;
    private Label dialogueLabel;
    private VBox dialogueBox;
    private boolean isVisible = true;
    private boolean isInteracting = false;
    
    // Interaction state
    private boolean hasInteracted = false;
    private int currentDialogueIndex = 0;
    
    // Physics properties
    private double interactionRadius = 80.0;
    
    public enum NPCType {
        MERCHANT("💼", Color.GOLD),
        ADVENTURER("🗡️", Color.ORANGE),
        SAGE("🧙‍♀️", Color.LIGHTBLUE),
        TEACHER("📚", Color.GREEN),
        GUIDE("🗺️", Color.PURPLE);
        
        private final String emoji;
        private final Color color;
        
        NPCType(String emoji, Color color) {
            this.emoji = emoji;
            this.color = color;
        }
        
        public String getEmoji() { return emoji; }
        public Color getColor() { return color; }
    }
    
    public NPCCharacter(String name, String greeting, String[] dialogue, NPCType type, double x, double y) {
        super(x, y, 40, 40); // Initialize physics object
        this.name = name;
        this.greeting = greeting;
        this.dialogue = dialogue;
        this.type = type;
        this.emoji = type.getEmoji();
        
        // Set physics properties
        setMass(1.0);
        setAffectedByGravity(false);
        
        createNPCSprite();
        createDialogueUI();
        
        System.out.println("Created NPC Character: " + name + " of type " + type + " at (" + x + ", " + y + ")");
    }
    
    private void createNPCSprite() {
        try {
            // Try to load specific NPC image based on type
            String imagePath = getNPCSpritePath();
            System.out.println("Loading NPC sprite for " + name + " from: " + imagePath);
            
            Image npcImage = new Image(imagePath);
            ImageView npcImageView = new ImageView(npcImage);
            npcImageView.setFitWidth(60);
            npcImageView.setFitHeight(60);
            npcImageView.setPreserveRatio(true);
            npcSprite = npcImageView;
            
            System.out.println("Successfully loaded NPC sprite for " + name);
        } catch (Exception e) {
            // Fallback to colored circle based on NPC type
            System.out.println("Failed to load NPC sprite for " + name + ", using fallback: " + e.getMessage());
            npcSprite = new Circle(30, type.getColor());
            ((Circle) npcSprite).setStroke(Color.BLACK);
            ((Circle) npcSprite).setStrokeWidth(2);
        }
        
        // Add name label
        npcName = new Text(name);
        npcName.setFont(Font.font(12));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(-name.length() * 3);
        npcName.setLayoutY(-40);
    }
    
    private String getNPCSpritePath() {
        // Map NPC types to their corresponding sprite files
        return switch (type) {
            case MERCHANT -> getClass().getResource("/Assets/NPC/Smart Businessman.png").toExternalForm();
            case ADVENTURER -> getClass().getResource("/Assets/NPC/Strong Adventurere.png").toExternalForm();
            case SAGE -> getClass().getResource("/Assets/NPC/Wise Lady.png").toExternalForm();
            default -> getClass().getResource("/Assets/NPC/Smart Businessman.png").toExternalForm();
        };
    }
    
    private void createDialogueUI() {
        dialogueLabel = new Label();
        dialogueLabel.setFont(Font.font(14));
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(300);
        dialogueLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 10;");
        
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> nextDialogue());
        
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> closeDialogue());
        
        HBox buttonBox = new HBox(10, nextButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        dialogueBox = new VBox(10, dialogueLabel, buttonBox);
        dialogueBox.setAlignment(Pos.CENTER);
        dialogueBox.setVisible(false);
        dialogueBox.setStyle("-fx-background-color: rgba(0,0,0,0.9); -fx-padding: 15;");
    }
    
    public void render(Pane gameWorld) {
        if (!isVisible) return;
        
        System.out.println("Rendering NPC: " + name + " at position (" + getX() + ", " + getY() + ")");
        
        // Position and add sprite
        npcSprite.setLayoutX(getX() - 30);
        npcSprite.setLayoutY(getY() - 30);
        if (npcSprite.getParent() == null) {
            gameWorld.getChildren().add(npcSprite);
        }
        
        // Position and add name
        npcName.setLayoutX(getX() - name.length() * 3);
        npcName.setLayoutY(getY() - 50);
        if (npcName.getParent() == null) {
            gameWorld.getChildren().add(npcName);
        }
        
        // Position dialogue box
        if (isInteracting) {
            dialogueBox.setLayoutX(getX() - 150);
            dialogueBox.setLayoutY(getY() - 150);
            if (dialogueBox.getParent() == null) {
                gameWorld.getChildren().add(dialogueBox);
            }
        }
        
        System.out.println("NPC " + name + " rendered successfully");
    }
    
    @Override
    public void update(double deltaTime) {
        // Update physics
        super.update(deltaTime);
        
        // Update visual position
        if (npcSprite != null) {
            npcSprite.setLayoutX(getX() - 30);
            npcSprite.setLayoutY(getY() - 30);
        }
        
        if (npcName != null) {
            npcName.setLayoutX(getX() - name.length() * 3);
            npcName.setLayoutY(getY() - 50);
        }
        
        if (isInteracting && dialogueBox != null) {
            dialogueBox.setLayoutX(getX() - 150);
            dialogueBox.setLayoutY(getY() - 150);
        }
    }
    
    public boolean canInteract(AdventurePlayer player) {
        double distance = Math.sqrt(
            Math.pow(player.getCenterX() - getX(), 2) + 
            Math.pow(player.getCenterY() - getY(), 2)
        );
        return distance <= interactionRadius;
    }
    
    public void interact(AdventurePlayer player) {
        if (!canInteract(player)) return;
        
        System.out.println("💬 " + name + " interaction started");
        
        if (!hasInteracted) {
            startDialogue();
            hasInteracted = true;
        } else {
            continueDialogue();
        }
        
        isInteracting = true;
        player.setInteracting(true);
        
        // Emit interaction event (simplified for now)
        System.out.println("📢 NPC interaction event: " + name + " interacted with player");
    }
    
    private void startDialogue() {
        dialogueLabel.setText(greeting);
        dialogueBox.setVisible(true);
        currentDialogueIndex = 0;
        System.out.println("💬 " + name + ": " + greeting);
    }
    
    private void continueDialogue() {
        if (dialogue != null && currentDialogueIndex < dialogue.length) {
            dialogueLabel.setText(dialogue[currentDialogueIndex]);
            System.out.println("💬 " + name + ": " + dialogue[currentDialogueIndex]);
        } else {
            // Show follow-up dialogue
            String[] followUps = {
                "Is there anything else you'd like to know?",
                "Remember what I taught you!",
                "Come back anytime for more advice!",
                "Keep practicing your financial skills!"
            };
            
            String followUp = followUps[(int)(Math.random() * followUps.length)];
            dialogueLabel.setText(followUp);
            System.out.println("💬 " + name + ": " + followUp);
        }
    }
    
    private void nextDialogue() {
        if (dialogue != null && currentDialogueIndex < dialogue.length - 1) {
            currentDialogueIndex++;
            dialogueLabel.setText(dialogue[currentDialogueIndex]);
            System.out.println("💬 " + name + ": " + dialogue[currentDialogueIndex]);
        } else {
            closeDialogue();
        }
    }
    
    private void closeDialogue() {
        dialogueBox.setVisible(false);
        isInteracting = false;
        System.out.println("💬 " + name + " dialogue closed");
        
        // Emit dialogue closed event (simplified for now)
        System.out.println("📢 NPC dialogue closed event: " + name);
    }
    
    public void cleanup() {
        if (npcSprite != null && npcSprite.getParent() != null) {
            ((Pane) npcSprite.getParent()).getChildren().remove(npcSprite);
        }
        if (npcName != null && npcName.getParent() != null) {
            ((Pane) npcName.getParent()).getChildren().remove(npcName);
        }
        if (dialogueBox != null && dialogueBox.getParent() != null) {
            ((Pane) dialogueBox.getParent()).getChildren().remove(dialogueBox);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmoji() { return emoji; }
    public String getGreeting() { return greeting; }
    public NPCType getType() { return type; }
    public boolean hasInteracted() { return hasInteracted; }
    public boolean isInteracting() { return isInteracting; }
    public double getInteractionRadius() { return interactionRadius; }
    
    // Renderable interface implementation
    @Override
    public boolean isVisible() { return isVisible; }
    
    @Override
    public void setVisible(boolean visible) { this.isVisible = visible; }
    
    // Setters
    public void setInteractionRadius(double radius) { this.interactionRadius = radius; }
}
