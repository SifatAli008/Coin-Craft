package com.coincraft.game.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.coincraft.engine.rendering.Sprite;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Manages breakable objects in the game world
 */
public class BreakableObjectManager {
    private final List<BreakableObject> breakableObjects;
    private final Pane worldPane;
    private final Random random;
    
    public BreakableObjectManager(Pane worldPane) {
        this.breakableObjects = new ArrayList<>();
        this.worldPane = worldPane;
        this.random = new Random();
    }
    
    /**
     * Add a breakable object to the world
     */
    public void addBreakableObject(BreakableObject obj) {
        breakableObjects.add(obj);
        worldPane.getChildren().add(obj.getSprite().getNode());
    }
    
    /**
     * Check for attack collisions with breakable objects
     */
    public void checkAttackCollision(double attackX, double attackY, double attackWidth, double attackHeight) {
        for (BreakableObject obj : breakableObjects) {
            if (obj.checkCollision(attackX, attackY, attackWidth, attackHeight)) {
                obj.breakObject();
                System.out.println("Broke " + obj.getObjectType() + "!");
            }
        }
    }
    
    /**
     * Create and add random breakable objects to the world
     */
    public void spawnRandomObjects(int count, double worldWidth, double worldHeight, int tileSize) {
        try {
            // Load barrel and crate images
            Image barrelImg = new Image(getClass().getResourceAsStream("/Assets/Sprites/Objects and buildings/Barrels and crates/spr_barrel1.png"));
            Image crateImg = new Image(getClass().getResourceAsStream("/Assets/Sprites/Objects and buildings/Barrels and crates/spr_crate1.png"));
            
            for (int i = 0; i < count; i++) {
                // Random position (avoid center spawn area)
                double x = random.nextDouble() * (worldWidth - 64);
                double y = random.nextDouble() * (worldHeight - 64);
                
                // Avoid center spawn area
                double centerX = worldWidth / 2;
                double centerY = worldHeight / 2;
                double spawnRadius = 200; // Keep objects away from spawn
                
                if (Math.abs(x - centerX) < spawnRadius && Math.abs(y - centerY) < spawnRadius) {
                    // Move to edge if too close to center
                    if (x < centerX) x = 100;
                    else x = worldWidth - 164;
                    if (y < centerY) y = 100;
                    else y = worldHeight - 164;
                }
                
                // Choose random object type
                boolean isBarrel = random.nextBoolean();
                Image objImage = isBarrel ? barrelImg : crateImg;
                String objType = isBarrel ? "Barrel" : "Crate";
                
                // Create sprite for the object
                Sprite objSprite = SpriteSheetUtil.createSquareRowSprite(objImage);
                objSprite.setSize(64, 64);
                objSprite.setPosition(x, y);
                objSprite.setVisible(true);
                
                // Create breakable object
                BreakableObject breakableObj = new BreakableObject(objSprite, x, y, 64, 64, objType);
                addBreakableObject(breakableObj);
            }
            
        } catch (Exception e) {
            System.err.println("Could not load breakable object images: " + e.getMessage());
        }
    }
    
    /**
     * Get all breakable objects
     */
    public List<BreakableObject> getBreakableObjects() {
        return new ArrayList<>(breakableObjects);
    }
    
    /**
     * Clear all breakable objects
     */
    public void clearAll() {
        for (BreakableObject obj : breakableObjects) {
            worldPane.getChildren().remove(obj.getSprite().getNode());
        }
        breakableObjects.clear();
    }
}
