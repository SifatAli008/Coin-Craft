package com.coincraft.engine.animation;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Animation manager for handling sprite animations
 * Manages all animations in the game
 */
public class AnimationManager {
    private final List<Animation> animations = new CopyOnWriteArrayList<>();
    private final List<SpriteAnimation> spriteAnimations = new CopyOnWriteArrayList<>();
    
    /**
     * Update all animations
     */
    public void update(double deltaTime) {
        // Update regular animations
        for (Animation animation : animations) {
            if (animation.isActive()) {
                animation.update(deltaTime);
            }
        }
        
        // Update sprite animations
        for (SpriteAnimation animation : spriteAnimations) {
            if (animation.isActive()) {
                animation.update(deltaTime);
            }
        }
    }
    
    /**
     * Add an animation
     */
    public void addAnimation(Animation animation) {
        animations.add(animation);
    }
    
    /**
     * Remove an animation
     */
    public void removeAnimation(Animation animation) {
        animations.remove(animation);
    }
    
    /**
     * Add a sprite animation
     */
    public void addSpriteAnimation(SpriteAnimation animation) {
        spriteAnimations.add(animation);
    }
    
    /**
     * Remove a sprite animation
     */
    public void removeSpriteAnimation(SpriteAnimation animation) {
        spriteAnimations.remove(animation);
    }
    
    /**
     * Stop all animations
     */
    public void stopAllAnimations() {
        for (Animation animation : animations) {
            animation.stop();
        }
        for (SpriteAnimation animation : spriteAnimations) {
            animation.stop();
        }
    }
    
    /**
     * Pause all animations
     */
    public void pauseAllAnimations() {
        for (Animation animation : animations) {
            animation.pause();
        }
        for (SpriteAnimation animation : spriteAnimations) {
            animation.pause();
        }
    }
    
    /**
     * Resume all animations
     */
    public void resumeAllAnimations() {
        for (Animation animation : animations) {
            animation.resume();
        }
        for (SpriteAnimation animation : spriteAnimations) {
            animation.resume();
        }
    }
    
    /**
     * Get all animations
     */
    public List<Animation> getAnimations() {
        return new ArrayList<>(animations);
    }
    
    /**
     * Get all sprite animations
     */
    public List<SpriteAnimation> getSpriteAnimations() {
        return new ArrayList<>(spriteAnimations);
    }
    
    /**
     * Clear all animations
     */
    public void clear() {
        animations.clear();
        spriteAnimations.clear();
    }
}
