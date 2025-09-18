package com.coincraft.ui.theme;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Simple sprite animation system for 8-bit style animations
 */
public class SpriteAnimator {
    private final ImageView imageView;
    private final List<Image> frames;
    private final Timeline timeline;
    private int currentFrame = 0;
    
    public SpriteAnimator(ImageView imageView, List<Image> frames, double frameRate) {
        this.imageView = imageView;
        this.frames = frames;
        
        // Apply pixel-perfect settings
        PixelSkin.crisp(imageView);
        
        // Create animation timeline
        this.timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        Duration frameDuration = Duration.millis(1000.0 / frameRate);
        KeyFrame keyFrame = new KeyFrame(frameDuration, e -> nextFrame());
        timeline.getKeyFrames().add(keyFrame);
    }
    
    private void nextFrame() {
        if (!frames.isEmpty()) {
            imageView.setImage(frames.get(currentFrame));
            currentFrame = (currentFrame + 1) % frames.size();
        }
    }
    
    public void play() {
        if (!frames.isEmpty()) {
            imageView.setImage(frames.get(0));
            timeline.play();
        }
    }
    
    public void stop() {
        timeline.stop();
    }
    
    public void pause() {
        timeline.pause();
    }
    
    public boolean isPlaying() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }
}
