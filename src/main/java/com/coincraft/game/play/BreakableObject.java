package com.coincraft.game.play;

import com.coincraft.engine.rendering.Sprite;

/**
 * Represents a breakable object that can be destroyed by player attacks
 */
public class BreakableObject {
    private final Sprite sprite;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private boolean isBroken;
    private final String objectType;
    
    public BreakableObject(Sprite sprite, double x, double y, double width, double height, String objectType) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.objectType = objectType;
        this.isBroken = false;
    }
    
    public boolean isBroken() {
        return isBroken;
    }
    
    public void breakObject() {
        if (!isBroken) {
            isBroken = true;
            sprite.setVisible(false);
        }
    }
    
    public boolean checkCollision(double attackX, double attackY, double attackWidth, double attackHeight) {
        if (isBroken) return false;
        
        // Check if attack area overlaps with object
        return attackX < x + width && 
               attackX + attackWidth > x && 
               attackY < y + height && 
               attackY + attackHeight > y;
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getObjectType() { return objectType; }
}
