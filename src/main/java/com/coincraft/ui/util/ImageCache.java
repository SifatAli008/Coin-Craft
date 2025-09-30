package com.coincraft.ui.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.image.Image;

/**
 * Simple image cache to avoid re-decoding images and GIFs repeatedly.
 */
public final class ImageCache {
    private static final Map<String, Image> pathToImage = new ConcurrentHashMap<>();

    private ImageCache() {}

    /**
     * Get or load an image from classpath.
     * @param resourcePath classpath resource path, e.g. "/images/bg.gif"
     * @param requestedWidth requested width or 0
     * @param requestedHeight requested height or 0
     * @param preserveRatio whether to preserve ratio
     * @param smooth whether to use smooth scaling
     */
    public static Image get(String resourcePath, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
        String key = resourcePath + "|" + requestedWidth + "|" + requestedHeight + "|" + preserveRatio + "|" + smooth;
        return pathToImage.computeIfAbsent(key, k -> {
            var stream = ImageCache.class.getResourceAsStream(resourcePath);
            if (stream == null) {
                return null;
            }
            return new Image(stream, requestedWidth, requestedHeight, preserveRatio, smooth);
        });
    }

    /** Get or load an image with default sizing flags. */
    public static Image get(String resourcePath) {
        return get(resourcePath, 0, 0, true, true);
    }
}


