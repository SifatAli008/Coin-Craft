package com.coincraft.engine.resources;

import javafx.scene.image.Image;
import java.util.Map;
import java.util.HashMap;

/**
 * Resource manager for loading and caching assets
 */
public class ResourceManager {
    private final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Load image from resources with caching
     */
    public Image loadImage(String resourcePath) {
        if (imageCache.containsKey(resourcePath)) {
            return imageCache.get(resourcePath);
        }
        try {
            Image image = new Image(getClass().getResource(resourcePath).toExternalForm());
            imageCache.put(resourcePath, image);
            return image;
        } catch (Exception e) {
            System.err.println("❌ Failed to load image: " + resourcePath + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Clear caches
     */
    public void cleanup() {
        imageCache.clear();
    }
}


