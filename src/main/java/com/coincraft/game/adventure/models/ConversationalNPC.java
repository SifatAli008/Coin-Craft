package com.coincraft.game.adventure.models;

import com.coincraft.engine.Updatable;
import com.coincraft.engine.physics.PhysicsObject;
import com.coincraft.engine.rendering.Renderable;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Enhanced NPC with comprehensive conversation system
 */
public class ConversationalNPC extends PhysicsObject implements Renderable, Updatable {
    
    public enum NPCType {
        ADVENTURER("🧙‍♂️", Color.BLUE, "Strong Adventurer"),
        SAGE("🧙‍♀️", Color.PURPLE, "Wise Lady"), 
        MERCHANT("👨‍💼", Color.GREEN, "Smart Businessman");
        
        private final String emoji;
        private final Color color;
        private final String displayName;
        
        NPCType(String emoji, Color color, String displayName) {
            this.emoji = emoji;
            this.color = color;
            this.displayName = displayName;
        }
        
        public String getEmoji() { return emoji; }
        public Color getColor() { return color; }
        public String getDisplayName() { return displayName; }
    }
    
    private final String name;
    private final NPCType type;
    private final ConversationSystem conversationSystem;
    private Node npcSprite;
    private Text npcName;
    private boolean isVisible = true;
    private boolean isInteractable = true;
    
    public ConversationalNPC(String name, NPCType type, ConversationSystem conversationSystem, 
                           double x, double y) {
        super(x, y, 40, 40);
        this.name = name;
        this.type = type;
        this.conversationSystem = conversationSystem;
        setMass(1.0);
        setAffectedByGravity(false);
        createNPCSprite();
        System.out.println("Created Conversational NPC: " + name + " of type " + type + " at (" + x + ", " + y + ")");
    }
    
    /**
     * Create the visual representation of the NPC
     */
    private void createNPCSprite() {
        try {
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
            System.out.println("Failed to load NPC sprite for " + name + ", using fallback: " + e.getMessage());
            npcSprite = new Circle(30, type.getColor());
            ((Circle) npcSprite).setStroke(Color.BLACK);
            ((Circle) npcSprite).setStrokeWidth(2);
        }
        
        npcName = new Text(name);
        npcName.setFont(Font.font(12));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(-name.length() * 3);
        npcName.setLayoutY(-40);
    }
    
    /**
     * Get the sprite path based on NPC type
     */
    private String getNPCSpritePath() {
        switch (type) {
            case ADVENTURER:
                return "/Assets/NPC/Strong Adventurere.png";
            case SAGE:
                return "/Assets/NPC/Wise Lady.png";
            case MERCHANT:
                return "/Assets/NPC/Smart Businessman.png";
            default:
                return "/Assets/NPC/Strong Adventurere.png";
        }
    }
    
    /**
     * Start conversation with this NPC
     */
    public void startConversation() {
        if (isInteractable && conversationSystem != null) {
            System.out.println("💬 Starting conversation with " + name);
            conversationSystem.startConversation();
        } else {
            System.out.println("❌ Cannot start conversation with " + name + " (not interactable or no conversation system)");
        }
    }
    
    /**
     * End conversation with this NPC
     */
    public void endConversation() {
        if (conversationSystem != null) {
            conversationSystem.endConversation();
        }
    }
    
    /**
     * Check if this NPC is currently in conversation
     */
    public boolean isInConversation() {
        return conversationSystem != null && conversationSystem.isConversationActive();
    }
    
    @Override
    public void update(double deltaTime) {
        // NPC-specific update logic can be added here
        // For now, NPCs are static
    }
    
    public void render(javafx.scene.layout.Pane parent) {
        if (isVisible && npcSprite != null) {
            npcSprite.setLayoutX(getX());
            npcSprite.setLayoutY(getY());
            if (!parent.getChildren().contains(npcSprite)) {
                parent.getChildren().add(npcSprite);
            }
            
            if (npcName != null) {
                npcName.setLayoutX(getX() - name.length() * 3);
                npcName.setLayoutY(getY() - 40);
                if (!parent.getChildren().contains(npcName)) {
                    parent.getChildren().add(npcName);
                }
            }
        }
    }
    
    @Override
    public boolean isVisible() {
        return isVisible;
    }
    
    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
        if (npcSprite != null) {
            npcSprite.setVisible(visible);
        }
        if (npcName != null) {
            npcName.setVisible(visible);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public NPCType getType() { return type; }
    public Node getNpcSprite() { return npcSprite; }
    public Text getNpcName() { return npcName; }
    public boolean isInteractable() { return isInteractable; }
    
    // Setters
    public void setInteractable(boolean interactable) { this.isInteractable = interactable; }
}
