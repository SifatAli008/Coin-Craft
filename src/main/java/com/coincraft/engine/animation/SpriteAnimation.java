package com.coincraft.engine.animation;

import com.coincraft.engine.rendering.Sprite;

/**
 * Sprite animation class
 * Handles sprite-based animations
 */
public class SpriteAnimation extends Animation {
    private final Sprite sprite;
    private final int startFrame;
    private final int endFrame;
    private final double frameDuration;
    
    private int currentFrame;
    private double frameTimer = 0.0;
    
    public SpriteAnimation(Sprite sprite, int startFrame, int endFrame, double frameDuration) {
        super((endFrame - startFrame + 1) * frameDuration);
        this.sprite = sprite;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.frameDuration = frameDuration;
        this.currentFrame = startFrame;
    }
    
    @Override
    protected void updateAnimation() {
        frameTimer += getDeltaTime();
        
        if (frameTimer >= frameDuration) {
            frameTimer = 0.0;
            currentFrame++;
            
            if (currentFrame > endFrame) {
                if (loop) {
                    currentFrame = startFrame;
                } else {
                    currentFrame = endFrame;
                }
            }
            
            sprite.setFrame(currentFrame);
        }
    }
    
    /**
     * Get delta time for this frame
     */
    private double getDeltaTime() {
        return 1.0 / 60.0; // Assume 60 FPS for frame timing
    }
    
    /**
     * Get current frame
     */
    public int getCurrentFrame() {
        return currentFrame;
    }
    
    /**
     * Get start frame
     */
    public int getStartFrame() {
        return startFrame;
    }
    
    /**
     * Get end frame
     */
    public int getEndFrame() {
        return endFrame;
    }
    
    /**
     * Get frame duration
     */
    public double getFrameDuration() {
        return frameDuration;
    }
}
