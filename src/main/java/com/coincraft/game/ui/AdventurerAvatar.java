package com.coincraft.game.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

/**
 * Adventurer Avatar - Shows the child's knight character
 */
public class AdventurerAvatar {
    private static final String KNIGHT_BASE_PATH = "/character/craftpix-net-803217-free-knight-character-sprites-pixel-art/Knight_1/";
    
    /**
     * Available knight animations
     */
    public enum KnightPose {
        IDLE("Idle.png"),
        WALK("Walk.png"),
        RUN("Run.png"),
        JUMP("Jump.png"),
        ATTACK_1("Attack 1.png"),
        ATTACK_2("Attack 2.png"),
        ATTACK_3("Attack 3.png"),
        DEFEND("Defend.png"),
        PROTECT("Protect.png"),
        HURT("Hurt.png"),
        DEAD("Dead.png"),
        RUN_ATTACK("Run+Attack.png");
        
        private final String fileName;
        
        KnightPose(String fileName) {
            this.fileName = fileName;
        }
        
        public String getFileName() {
            return fileName;
        }
    }
    
    /**
     * Create knight avatar with default idle pose
     */
    public static ImageView createAvatar(int size) {
        return createAvatar(size, KnightPose.IDLE);
    }
    
    /**
     * Create knight avatar with specific pose
     */
    public static ImageView createAvatar(int size, KnightPose pose) {
        try {
            String imagePath = KNIGHT_BASE_PATH + pose.getFileName();
            Image knightImage = new Image(
                AdventurerAvatar.class.getResourceAsStream(imagePath)
            );
            
            ImageView imageView = new ImageView(knightImage);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            
            System.out.println("✅ Loaded Knight avatar: " + pose.getFileName());
            return imageView;
            
        } catch (Exception e) {
            System.err.println("⚠️ Could not load knight sprite: " + e.getMessage());
            
            // Fallback to empty imageview
            ImageView fallback = new ImageView();
            fallback.setFitWidth(size);
            fallback.setFitHeight(size);
            return fallback;
        }
    }
    
    /**
     * Create circular knight avatar (for profile pictures)
     */
    public static StackPane createCircularAvatar(int size) {
        return createCircularAvatar(size, KnightPose.IDLE);
    }
    
    /**
     * Create circular knight avatar with specific pose
     */
    public static StackPane createCircularAvatar(int size, KnightPose pose) {
        ImageView avatar = createAvatar(size, pose);
        
        // Create circular clip
        Circle clip = new Circle(size / 2.0, size / 2.0, size / 2.0);
        avatar.setClip(clip);
        
        // Wrap in StackPane for proper centering
        StackPane container = new StackPane(avatar);
        container.setPrefSize(size, size);
        container.setMaxSize(size, size);
        container.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"
        );
        
        return container;
    }
    
    /**
     * Create knight avatar with border and effects
     */
    public static StackPane createStyledAvatar(int size, KnightPose pose) {
        ImageView avatar = createAvatar(size, pose);
        
        StackPane container = new StackPane(avatar);
        container.setPrefSize(size, size);
        container.setMaxSize(size, size);
        container.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #667EEA, #764BA2);" +
            "-fx-background-radius: " + (size / 10) + ";" +
            "-fx-padding: 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);"
        );
        
        return container;
    }
}

