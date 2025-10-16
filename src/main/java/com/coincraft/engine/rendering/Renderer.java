package com.coincraft.engine.rendering;

import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Rendering system for the game engine
 * Manages all visual rendering operations
 */
public class Renderer {
    private final Pane gameWorld;
    private final List<Renderable> renderables = new CopyOnWriteArrayList<>();
    private final List<Sprite> sprites = new CopyOnWriteArrayList<>();
    
    // Rendering properties
    
    // Performance tracking
    private int drawCalls = 0;
    
    public Renderer(Pane gameWorld) {
        this.gameWorld = gameWorld;
    }
    
    /**
     * Add a renderable object
     */
    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
        if (renderable instanceof Node node) {
            gameWorld.getChildren().add(node);
        }
    }
    
    /**
     * Remove a renderable object
     */
    public void removeRenderable(Renderable renderable) {
        renderables.remove(renderable);
        if (renderable instanceof Node node) {
            gameWorld.getChildren().remove(node);
        }
    }
    
    /**
     * Add a sprite
     */
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
        gameWorld.getChildren().add(sprite.getNode());
    }
    
    /**
     * Remove a sprite
     */
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
        gameWorld.getChildren().remove(sprite.getNode());
    }
    
    /**
     * Update the renderer
     */
    public void update(double deltaTime) {
        drawCalls = 0;
        
        // Update all renderables
        for (Renderable renderable : renderables) {
            if (renderable.isVisible()) {
                renderable.update(deltaTime);
                drawCalls++;
            }
        }
        
        // Update all sprites
        for (Sprite sprite : sprites) {
            if (sprite.isVisible()) {
                sprite.update(deltaTime);
                drawCalls++;
            }
        }
    }
    
    /**
     * Set background color
     */
    public void setBackgroundColor(Color color) {
        gameWorld.setStyle("-fx-background-color: " + colorToHex(color));
    }
    
    /**
     * Convert color to hex string
     */
    private String colorToHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    /**
     * Get game world
     */
    public Pane getGameWorld() {
        return gameWorld;
    }
    
    /**
     * Get draw calls count
     */
    public int getDrawCalls() {
        return drawCalls;
    }
    
    /**
     * Clear all renderables
     */
    public void clear() {
        renderables.clear();
        sprites.clear();
        gameWorld.getChildren().clear();
    }
}
