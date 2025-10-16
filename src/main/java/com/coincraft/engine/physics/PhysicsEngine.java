package com.coincraft.engine.physics;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Physics engine for 2D game physics
 * Handles collision detection, gravity, and physics simulation
 */
public class PhysicsEngine {
    private final List<PhysicsObject> objects = new CopyOnWriteArrayList<>();
    private final List<Collision> collisions = new ArrayList<>();
    
    // Physics properties
    private double gravity = 980.0; // pixels per second squared
    private double airResistance = 0.99;
    private boolean enableGravity = true;
    private boolean enableCollisions = true;
    
    // Performance tracking
    private int collisionChecks = 0;
    private int lastCollisionChecks = 0;
    
    /**
     * Update physics simulation
     */
    public void update(double deltaTime) {
        collisionChecks = 0;
        collisions.clear();
        
        // Update all physics objects
        for (PhysicsObject obj : objects) {
            if (obj.isActive()) {
                updateObject(obj, deltaTime);
            }
        }
        
        // Check collisions
        if (enableCollisions) {
            checkCollisions();
        }
        
        // Resolve collisions
        resolveCollisions();

        // Record metrics for this frame
        lastCollisionChecks = collisionChecks;
    }
    
    /**
     * Update a physics object
     */
    private void updateObject(PhysicsObject obj, double deltaTime) {
        // Apply gravity
        if (enableGravity && obj.isAffectedByGravity()) {
            obj.addForce(0, gravity * obj.getMass() * deltaTime);
        }
        
        // Apply air resistance
        if (airResistance < 1.0) {
            obj.setVelocity(obj.getVelocityX() * airResistance, obj.getVelocityY() * airResistance);
        }
        
        // Update position
        obj.update(deltaTime);
    }
    
    /**
     * Check for collisions between all objects
     */
    private void checkCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                PhysicsObject obj1 = objects.get(i);
                PhysicsObject obj2 = objects.get(j);
                
                if (obj1.isActive() && obj2.isActive() && 
                    obj1.isCollidable() && obj2.isCollidable()) {
                    
                    collisionChecks++;
                    
                    if (checkCollision(obj1, obj2)) {
                        Collision collision = new Collision(obj1, obj2);
                        collisions.add(collision);
                    }
                }
            }
        }
    }
    
    /**
     * Check collision between two objects
     */
    private boolean checkCollision(PhysicsObject obj1, PhysicsObject obj2) {
        // AABB (Axis-Aligned Bounding Box) collision detection
        return obj1.getX() < obj2.getX() + obj2.getWidth() &&
               obj1.getX() + obj1.getWidth() > obj2.getX() &&
               obj1.getY() < obj2.getY() + obj2.getHeight() &&
               obj1.getY() + obj1.getHeight() > obj2.getY();
    }
    
    /**
     * Resolve all collisions
     */
    private void resolveCollisions() {
        for (Collision collision : collisions) {
            resolveCollision(collision);
        }
    }
    
    /**
     * Resolve a single collision
     */
    private void resolveCollision(Collision collision) {
        PhysicsObject obj1 = collision.getObject1();
        PhysicsObject obj2 = collision.getObject2();
        
        // Simple elastic collision response
        if (obj1.isDynamic() && obj2.isDynamic()) {
            // Calculate collision normal
            double dx = obj2.getX() - obj1.getX();
            double dy = obj2.getY() - obj1.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > 0) {
                double nx = dx / distance;
                double ny = dy / distance;
                
                // Separate objects
                double overlap = (obj1.getWidth() + obj2.getWidth()) / 2 - distance;
                if (overlap > 0) {
                    double separationX = nx * overlap * 0.5;
                    double separationY = ny * overlap * 0.5;
                    
                    obj1.setPosition(obj1.getX() - separationX, obj1.getY() - separationY);
                    obj2.setPosition(obj2.getX() + separationX, obj2.getY() + separationY);
                }
                
                // Exchange momentum
                double relativeVelocityX = obj2.getVelocityX() - obj1.getVelocityX();
                double relativeVelocityY = obj2.getVelocityY() - obj1.getVelocityY();
                double velocityAlongNormal = relativeVelocityX * nx + relativeVelocityY * ny;
                
        if (velocityAlongNormal > 0) {
            return; // Objects separating
        }
                
                double restitution = Math.min(obj1.getRestitution(), obj2.getRestitution());
                double impulse = -(1 + restitution) * velocityAlongNormal;
                impulse /= obj1.getMass() + obj2.getMass();
                
                double impulseX = impulse * nx;
                double impulseY = impulse * ny;
                
                obj1.addVelocity(-impulseX * obj2.getMass(), -impulseY * obj2.getMass());
                obj2.addVelocity(impulseX * obj1.getMass(), impulseY * obj1.getMass());
            }
        }
    }
    
    /**
     * Add a physics object
     */
    public void addObject(PhysicsObject obj) {
        objects.add(obj);
    }
    
    /**
     * Remove a physics object
     */
    public void removeObject(PhysicsObject obj) {
        objects.remove(obj);
    }
    
    /**
     * Set gravity
     */
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }
    
    /**
     * Set air resistance
     */
    public void setAirResistance(double resistance) {
        this.airResistance = resistance;
    }
    
    /**
     * Enable/disable gravity
     */
    public void setEnableGravity(boolean enable) {
        this.enableGravity = enable;
    }
    
    /**
     * Enable/disable collisions
     */
    public void setEnableCollisions(boolean enable) {
        this.enableCollisions = enable;
    }
    
    /**
     * Get all physics objects
     */
    public List<PhysicsObject> getObjects() {
        return new ArrayList<>(objects);
    }
    
    /**
     * Get collision checks count
     */
    public int getCollisionChecks() {
        return collisionChecks;
    }
    
    /**
     * Get last frame collision checks
     */
    public int getLastCollisionChecks() {
        return lastCollisionChecks;
    }
    
    /**
     * Clear all objects
     */
    public void clear() {
        objects.clear();
        collisions.clear();
    }
}
