package com.coincraft.engine.rendering;

/**
 * Interface for renderable objects
 * All objects that can be rendered must implement this interface
 */
public interface Renderable {
    /**
     * Update the renderable object
     * @param deltaTime Time elapsed since last update
     */
    void update(double deltaTime);
    
    /**
     * Check if the object is visible
     * @return true if visible, false otherwise
     */
    boolean isVisible();
    
    /**
     * Set visibility
     * @param visible true to make visible, false to hide
     */
    void setVisible(boolean visible);
}
