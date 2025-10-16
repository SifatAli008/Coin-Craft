package com.coincraft.engine.rendering;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

/**
 * Sprite class for 2D rendering
 * Handles sprite animation and rendering
 */
public class Sprite implements Renderable {
    private final ImageView imageView;
    private final Image spriteSheet;
    private final int frameWidth;
    private final int frameHeight;
    private final int totalFrames;
    
    // Animation properties
    private int currentFrame = 0;
    private double animationSpeed = 1.0;
    private double animationTimer = 0.0;
    private boolean isAnimating = false;
    private boolean loopAnimation = true;
    
    // Sprite properties
    private double x = 0;
    private double y = 0;
    private double width = 0;
    private double height = 0;
    private double rotation = 0;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    private double opacity = 1.0;
    private boolean visible = true;
    
    // Color tinting
    private Color tint = Color.WHITE;
    
    public Sprite(Image spriteSheet, int frameWidth, int frameHeight, int totalFrames) {
        this.spriteSheet = spriteSheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.totalFrames = totalFrames;
        
        this.imageView = new ImageView(spriteSheet);
        this.imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
        
        updateTransform();
    }
    
    /**
     * Update the sprite
     */
    @Override
    public void update(double deltaTime) {
        if (isAnimating) {
            animationTimer += deltaTime * animationSpeed;
            
            if (animationTimer >= 1.0) {
                animationTimer = 0.0;
                nextFrame();
            }
        }
        
        updateTransform();
    }
    
    /**
     * Update sprite transform
     */
    private void updateTransform() {
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setRotate(rotation);
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);
        imageView.setOpacity(opacity);
        imageView.setVisible(visible);
        
        // Apply tint
        if (tint != Color.WHITE) {
            imageView.setEffect(new javafx.scene.effect.ColorAdjust(
                tint.getRed() - 1.0,
                tint.getGreen() - 1.0,
                tint.getBlue() - 1.0,
                0.0
            ));
        }
    }
    
    /**
     * Move to next frame
     */
    private void nextFrame() {
        currentFrame++;
        if (currentFrame >= totalFrames) {
            if (loopAnimation) {
                currentFrame = 0;
            } else {
                currentFrame = totalFrames - 1;
                isAnimating = false;
            }
        }
        
        updateFrame();
    }
    
    /**
     * Update current frame
     */
    private void updateFrame() {
        int columns = (int) (spriteSheet.getWidth() / frameWidth);
        if (columns <= 0) {
            columns = 1;
        }
        int frameX = (currentFrame % columns) * frameWidth;
        int frameY = (currentFrame / columns) * frameHeight;
        
        imageView.setViewport(new Rectangle2D(frameX, frameY, frameWidth, frameHeight));
    }
    
    /**
     * Start animation
     */
    public void startAnimation() {
        isAnimating = true;
        animationTimer = 0.0;
    }
    
    /**
     * Stop animation
     */
    public void stopAnimation() {
        isAnimating = false;
    }
    
    /**
     * Set animation speed
     */
    public void setAnimationSpeed(double speed) {
        this.animationSpeed = speed;
    }
    
    /**
     * Set current frame
     */
    public void setFrame(int frame) {
        this.currentFrame = Math.max(0, Math.min(frame, totalFrames - 1));
        updateFrame();
    }
    
    /**
     * Set loop animation
     */
    public void setLoopAnimation(boolean loop) {
        this.loopAnimation = loop;
    }
    
    // Position methods
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }
    
    // Size methods
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
    
    public void setWidth(double width) { 
        this.width = width; 
        imageView.setFitWidth(width);
    }
    public void setHeight(double height) { 
        this.height = height; 
        imageView.setFitHeight(height);
    }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    // Transform methods
    public void setRotation(double rotation) { this.rotation = rotation; }
    public double getRotation() { return rotation; }
    
    public void setScale(double scaleX, double scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    public void setScaleX(double scaleX) { this.scaleX = scaleX; }
    public void setScaleY(double scaleY) { this.scaleY = scaleY; }
    public double getScaleX() { return scaleX; }
    public double getScaleY() { return scaleY; }
    
    public void setOpacity(double opacity) { this.opacity = opacity; }
    public double getOpacity() { return opacity; }
    
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }
    
    public void setTint(Color tint) { this.tint = tint; }
    public Color getTint() { return tint; }
    
    // Animation state
    public boolean isAnimating() { return isAnimating; }
    public int getCurrentFrame() { return currentFrame; }
    public int getTotalFrames() { return totalFrames; }
    
    // Get the JavaFX node
    public Node getNode() { return imageView; }
}
