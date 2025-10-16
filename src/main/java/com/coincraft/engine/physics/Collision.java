package com.coincraft.engine.physics;

/**
 * Represents a collision between two physics objects
 */
public class Collision {
    private final PhysicsObject object1;
    private final PhysicsObject object2;
    private final double penetrationX;
    private final double penetrationY;
    private final double normalX;
    private final double normalY;
    
    public Collision(PhysicsObject object1, PhysicsObject object2) {
        this.object1 = object1;
        this.object2 = object2;
        
        // Calculate penetration and normal
        double dx = object2.getX() - object1.getX();
        double dy = object2.getY() - object1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            this.normalX = dx / distance;
            this.normalY = dy / distance;
        } else {
            this.normalX = 1;
            this.normalY = 0;
        }
        
        // Calculate penetration depth
        double overlapX = (object1.getWidth() + object2.getWidth()) / 2 - Math.abs(dx);
        double overlapY = (object1.getHeight() + object2.getHeight()) / 2 - Math.abs(dy);
        
        this.penetrationX = overlapX;
        this.penetrationY = overlapY;
    }
    
    public PhysicsObject getObject1() {
        return object1;
    }
    
    public PhysicsObject getObject2() {
        return object2;
    }
    
    public double getPenetrationX() {
        return penetrationX;
    }
    
    public double getPenetrationY() {
        return penetrationY;
    }
    
    public double getNormalX() {
        return normalX;
    }
    
    public double getNormalY() {
        return normalY;
    }
}
