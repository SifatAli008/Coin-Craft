package com.coincraft.engine.animation;

/**
 * Base animation class
 * Provides common animation functionality
 */
public abstract class Animation {
    protected double duration;
    protected double elapsedTime = 0.0;
    protected boolean active = false;
    protected boolean paused = false;
    protected boolean loop = false;
    protected boolean finished = false;
    
    public Animation(double duration) {
        this.duration = duration;
    }
    
    /**
     * Update the animation
     */
    public void update(double deltaTime) {
        if (!active || paused) return;
        
        elapsedTime += deltaTime;
        
        if (elapsedTime >= duration) {
            if (loop) {
                elapsedTime = 0.0;
            } else {
                elapsedTime = duration;
                active = false;
                finished = true;
            }
        }
        
        updateAnimation();
    }
    
    /**
     * Update the animation implementation
     */
    protected abstract void updateAnimation();
    
    /**
     * Start the animation
     */
    public void start() {
        active = true;
        paused = false;
        finished = false;
        elapsedTime = 0.0;
    }
    
    /**
     * Stop the animation
     */
    public void stop() {
        active = false;
        paused = false;
        finished = true;
    }
    
    /**
     * Pause the animation
     */
    public void pause() {
        paused = true;
    }
    
    /**
     * Resume the animation
     */
    public void resume() {
        paused = false;
    }
    
    /**
     * Reset the animation
     */
    public void reset() {
        elapsedTime = 0.0;
        finished = false;
    }
    
    /**
     * Get animation progress (0.0 to 1.0)
     */
    public double getProgress() {
        return Math.min(1.0, elapsedTime / duration);
    }
    
    /**
     * Check if animation is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Check if animation is paused
     */
    public boolean isPaused() {
        return paused;
    }
    
    /**
     * Check if animation is finished
     */
    public boolean isFinished() {
        return finished;
    }
    
    /**
     * Check if animation loops
     */
    public boolean isLoop() {
        return loop;
    }
    
    /**
     * Set loop state
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    /**
     * Get duration
     */
    public double getDuration() {
        return duration;
    }
    
    /**
     * Set duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }
}
