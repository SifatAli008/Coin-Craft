package com.coincraft.ui.theme;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Core pixel-art rendering utilities for CoinCraft
 * Handles pixel-perfect drawing and scaling
 */
public class PixelRenderer {
    private final GraphicsContext gc;
    private final double pixelScale;
    
    public PixelRenderer(Canvas canvas, double pixelScale) {
        this.gc = canvas.getGraphicsContext2D();
        this.pixelScale = pixelScale;
        
        // Configure for pixel-perfect rendering
        gc.setImageSmoothing(false);
        gc.setLineCap(javafx.scene.shape.StrokeLineCap.SQUARE);
        gc.setLineJoin(javafx.scene.shape.StrokeLineJoin.MITER);
    }
    
    /**
     * Draw a pixel-perfect sprite at specified coordinates
     */
    public void drawSprite(Image sprite, double x, double y) {
        double scaledX = Math.floor(x * pixelScale);
        double scaledY = Math.floor(y * pixelScale);
        double scaledW = sprite.getWidth() * pixelScale;
        double scaledH = sprite.getHeight() * pixelScale;
        
        gc.drawImage(sprite, scaledX, scaledY, scaledW, scaledH);
    }
    
    /**
     * Draw a pixel-perfect rectangle with chunky border
     */
    public void drawPixelRect(double x, double y, double width, double height, 
                             Color fill, Color border, double borderWidth) {
        double scaledX = Math.floor(x * pixelScale);
        double scaledY = Math.floor(y * pixelScale);
        double scaledW = Math.floor(width * pixelScale);
        double scaledH = Math.floor(height * pixelScale);
        double scaledBorder = borderWidth * pixelScale;
        
        // Fill
        gc.setFill(fill);
        gc.fillRect(scaledX, scaledY, scaledW, scaledH);
        
        // Border
        gc.setStroke(border);
        gc.setLineWidth(scaledBorder);
        gc.strokeRect(scaledX, scaledY, scaledW, scaledH);
    }
    
    /**
     * Draw pixel-perfect text with optional shadow
     */
    public void drawPixelText(String text, double x, double y, Color color, boolean shadow) {
        double scaledX = Math.floor(x * pixelScale);
        double scaledY = Math.floor(y * pixelScale);
        
        if (shadow) {
            gc.setFill(Color.BLACK);
            gc.fillText(text, scaledX + pixelScale, scaledY + pixelScale);
        }
        
        gc.setFill(color);
        gc.fillText(text, scaledX, scaledY);
    }
    
    /**
     * Clear the canvas with pixel-perfect background
     */
    public void clear(Color backgroundColor) {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }
    
    public GraphicsContext getGraphicsContext() {
        return gc;
    }
    
    public double getPixelScale() {
        return pixelScale;
    }
}
