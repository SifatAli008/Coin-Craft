package com.coincraft.ui.theme;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

/**
 * SpriteSheet manager for loading and slicing pixel art assets
 */
public class SpriteSheet {
    private final Image sourceImage;
    private final int tileWidth;
    private final int tileHeight;
    private final List<Image> sprites;
    
    public SpriteSheet(String imagePath, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.sprites = new ArrayList<>();
        
        // Load source image
        Image tempImage = null;
        try {
            var url = getClass().getResourceAsStream(imagePath);
            if (url != null) {
                tempImage = new Image(url);
            } else {
                System.out.println("Warning: Could not load spritesheet: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error loading spritesheet: " + e.getMessage());
        }
        
        this.sourceImage = tempImage;
        if (this.sourceImage != null) {
            sliceSprites();
        }
    }
    
    private void sliceSprites() {
        if (sourceImage == null) return;
        
        PixelReader reader = sourceImage.getPixelReader();
        int cols = (int) (sourceImage.getWidth() / tileWidth);
        int rows = (int) (sourceImage.getHeight() / tileHeight);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileWidth;
                int y = row * tileHeight;
                
                WritableImage sprite = new WritableImage(reader, x, y, tileWidth, tileHeight);
                sprites.add(sprite);
            }
        }
    }
    
    /**
     * Get sprite by index (left-to-right, top-to-bottom)
     */
    public Image getSprite(int index) {
        if (index >= 0 && index < sprites.size()) {
            return sprites.get(index);
        }
        return null;
    }
    
    /**
     * Get sprite by grid coordinates
     */
    public Image getSprite(int col, int row) {
        int cols = (int) (sourceImage.getWidth() / tileWidth);
        int index = row * cols + col;
        return getSprite(index);
    }
    
    /**
     * Get animation frames from a row
     */
    public List<Image> getAnimationFrames(int row, int frameCount) {
        List<Image> frames = new ArrayList<>();
        for (int i = 0; i < frameCount; i++) {
            Image frame = getSprite(i, row);
            if (frame != null) {
                frames.add(frame);
            }
        }
        return frames;
    }
    
    public int getSpriteCount() {
        return sprites.size();
    }
    
    public int getTileWidth() {
        return tileWidth;
    }
    
    public int getTileHeight() {
        return tileHeight;
    }
    
    public boolean isLoaded() {
        return sourceImage != null && !sprites.isEmpty();
    }
}
