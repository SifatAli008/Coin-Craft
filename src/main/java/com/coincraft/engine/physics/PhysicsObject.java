package com.coincraft.engine.physics;

import javafx.scene.Node;

/**
 * Physics object for 2D physics simulation
 * Represents an object that can be affected by physics
 */
public abstract class PhysicsObject {
    // Position and size
    protected double x, y;
    protected double width, height;
    
    // Velocity and acceleration
    protected double velocityX, velocityY;
    protected double accelerationX, accelerationY;
    
    // Physics properties
    protected double mass = 1.0;
    protected double restitution = 0.5; // Bounciness (0 = no bounce, 1 = perfect bounce)
    protected double friction = 0.1;
    
    // State
    protected boolean active = true;
    protected boolean dynamic = true; // Can move
    protected boolean collidable = true;
    protected boolean affectedByGravity = true;
    
    // Visual representation
    protected Node node;
    
    public PhysicsObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Update the physics object
     */
    public void update(double deltaTime) {
        if (!active || !dynamic) return;
        
        // Update velocity based on acceleration
        velocityX += accelerationX * deltaTime;
        velocityY += accelerationY * deltaTime;
        
        // Update position based on velocity
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        
        // Reset acceleration
        accelerationX = 0;
        accelerationY = 0;
        
        // Update visual representation
        if (node != null) {
            node.setLayoutX(x);
            node.setLayoutY(y);
        }
    }
    
    /**
     * Add force to the object
     */
    public void addForce(double forceX, double forceY) {
        if (!dynamic) return;
        
        accelerationX += forceX / mass;
        accelerationY += forceY / mass;
    }
    
    /**
     * Add velocity to the object
     */
    public void addVelocity(double velocityX, double velocityY) {
        if (!dynamic) return;
        
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }
    
    /**
     * Set velocity
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    /**
     * Set position
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        
        if (node != null) {
            node.setLayoutX(x);
            node.setLayoutY(y);
        }
    }
    
    /**
     * Set size
     */
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public double getMass() { return mass; }
    public double getRestitution() { return restitution; }
    public double getFriction() { return friction; }
    public boolean isActive() { return active; }
    public boolean isDynamic() { return dynamic; }
    public boolean isCollidable() { return collidable; }
    public boolean isAffectedByGravity() { return affectedByGravity; }
    public Node getNode() { return node; }
    
    // Setters
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    public void setMass(double mass) { this.mass = mass; }
    public void setRestitution(double restitution) { this.restitution = restitution; }
    public void setFriction(double friction) { this.friction = friction; }
    public void setActive(boolean active) { this.active = active; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    public void setCollidable(boolean collidable) { this.collidable = collidable; }
    public void setAffectedByGravity(boolean affectedByGravity) { this.affectedByGravity = affectedByGravity; }
    public void setNode(Node node) { this.node = node; }
}
