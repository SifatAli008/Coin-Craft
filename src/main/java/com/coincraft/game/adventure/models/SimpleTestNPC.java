package com.coincraft.game.adventure.models;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Simple Test NPC - Basic shape to verify NPCs can be rendered
 */
public class SimpleTestNPC {
    private final String name;
    private final double x, y;
    private Node npcSprite;
    private Text npcName;
    private final Color npcColor;
    
    public SimpleTestNPC(String name, String emoji, double x, double y, Color color) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.npcColor = color;
        createNPCSprite();
    }
    
    private void createNPCSprite() {
        // Create a simple colored circle
        npcSprite = new Circle(40, npcColor);
        ((Circle) npcSprite).setStroke(Color.BLACK);
        ((Circle) npcSprite).setStrokeWidth(3);
        
        // Add name label
        npcName = new Text(name);
        npcName.setFont(Font.font(16));
        npcName.setFill(Color.WHITE);
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        
        System.out.println("Created SimpleTestNPC: " + name + " at (" + x + ", " + y + ")");
    }
    
    public void render(Pane gameWorld) {
        System.out.println("Rendering SimpleTestNPC: " + name + " at (" + x + ", " + y + ")");
        
        // Position and add sprite
        npcSprite.setLayoutX(x);
        npcSprite.setLayoutY(y);
        gameWorld.getChildren().add(npcSprite);
        
        // Position and add name
        npcName.setLayoutX(x - name.length() * 4);
        npcName.setLayoutY(y - 50);
        gameWorld.getChildren().add(npcName);
        
        System.out.println("SimpleTestNPC " + name + " rendered successfully");
    }
    
    public void cleanup() {
        if (npcSprite != null && npcSprite.getParent() != null) {
            ((Pane) npcSprite.getParent()).getChildren().remove(npcSprite);
        }
        if (npcName != null && npcName.getParent() != null) {
            ((Pane) npcName.getParent()).getChildren().remove(npcName);
        }
    }
    
    // Getters
    public String getName() { return name; }
    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
}
