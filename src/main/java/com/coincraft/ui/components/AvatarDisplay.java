package com.coincraft.ui.components;

import com.coincraft.models.User;
import com.coincraft.models.Avatar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Avatar display component showing the user's customizable character
 */
public class AvatarDisplay {
    private VBox root;
    private ImageView avatarImageView;
    private Label nameLabel;
    private Label levelLabel;
    private Button customizeButton;
    private User currentUser;
    
    public AvatarDisplay() {
        initializeComponent();
    }
    
    public AvatarDisplay(User user) {
        this.currentUser = user;
        initializeComponent();
        updateDisplay();
    }
    
    private void initializeComponent() {
        root = new VBox(8);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(12));
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255, 255, 255, 0.6);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 4);"
        );
        root.getStyleClass().add("avatar-display");
        
        // Avatar image with circular frame and border
        avatarImageView = new ImageView();
        avatarImageView.setFitWidth(70);
        avatarImageView.setFitHeight(70);
        avatarImageView.setPreserveRatio(true);
        
        // Create circular clip for avatar
        Circle clip = new Circle(35, 35, 35);
        avatarImageView.setClip(clip);
        
        // Add border effect to avatar
        avatarImageView.setStyle(
            "-fx-effect: dropshadow(gaussian, #FA8A00, 8, 0, 0, 0)," +
            "           dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 3);"
        );
        
        // Load default avatar image
        try {
            Image defaultAvatar = new Image(getClass().getResourceAsStream("/images/avatars/default_explorer.png"));
            avatarImageView.setImage(defaultAvatar);
        } catch (Exception e) {
            // Create a colorful placeholder if image not found
            avatarImageView.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FA8A00, #E67E00);" +
                "-fx-background-radius: 35;" +
                "-fx-effect: dropshadow(gaussian, #FA8A00, 6, 0, 0, 0)," +
                "           dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);"
            );
        }
        
        // Avatar name with gaming style
        nameLabel = new Label("Money Explorer");
        nameLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 2, 0, 0, 1);"
        );
        nameLabel.getStyleClass().add("avatar-name");
        
        // Level badge with gaming style
        levelLabel = new Label("Level 1");
        levelLabel.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 700;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 4 8;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 4, 0, 0, 2);"
        );
        levelLabel.getStyleClass().add("avatar-level");
        
        // Customize button with gaming style
        customizeButton = new Button("🎨 Customize");
        customizeButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 4, 0, 0, 2);"
        );
        customizeButton.getStyleClass().add("customize-button");
        customizeButton.setOnAction(e -> openCustomization());
        
        // Add hover effects
        customizeButton.setOnMouseEntered(e -> {
            customizeButton.setStyle(
                "-fx-background-color: #E67E00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 4 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(56,142,60,0.5), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        customizeButton.setOnMouseExited(e -> {
            customizeButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 4 8;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 4, 0, 0, 2);"
            );
        });
        
        root.getChildren().addAll(avatarImageView, nameLabel, levelLabel, customizeButton);
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        
        if (user != null) {
            nameLabel.setText(user.getName());
            levelLabel.setText("Level " + user.getLevel());
            
            // Update avatar image based on user's avatar
            Avatar avatar = user.getAvatar();
            if (avatar != null) {
                loadAvatarImage(avatar);
            }
        }
    }
    
    private void loadAvatarImage(Avatar avatar) {
        try {
            String imagePath = avatar.getDisplayImagePath();
            Image avatarImage = new Image(getClass().getResourceAsStream(imagePath));
            avatarImageView.setImage(avatarImage);
        } catch (Exception e) {
            // Keep default image if loading fails
            System.out.println("Could not load avatar image: " + e.getMessage());
        }
    }
    
    private void openCustomization() {
        System.out.println("Opening avatar customization...");
        
        // Create gaming-style customization dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Avatar Workshop");
        alert.setHeaderText("🎨 Customize Your Adventurer!");
        
        String content = "Welcome to the Avatar Workshop!\n\n" +
                        "🎯 Current Customization Options:\n" +
                        "• Hair styles and colors\n" +
                        "• Clothing and accessories\n" +
                        "• Special adventure gear\n" +
                        "• Unlock rewards from completed quests\n\n" +
                        "🔓 Unlock More Items:\n" +
                        "• Complete learning quests\n" +
                        "• Earn special badges\n" +
                        "• Reach higher levels\n" +
                        "• Participate in events\n\n" +
                        "✨ Full customization system coming soon!\n" +
                        "Keep adventuring to unlock amazing new items!";
        
        alert.setContentText(content);
        
        // Style the dialog with game theme
        alert.getDialogPane().setStyle(
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: #FA8A00;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 16, 0, 0, 8);"
        );
        
        // Play sound effect if available
        try {
            // In a real implementation, you might play a customization sound here
            System.out.println("🔊 Playing customization workshop sound...");
        } catch (Exception e) {
            // Sound not available, continue silently
        }
        
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Update the display with current user data
     */
    private void updateDisplay() {
        if (currentUser != null) {
            nameLabel.setText(currentUser.getName());
            levelLabel.setText("Level " + currentUser.getLevel());
            loadAvatarImage(currentUser.getAvatar());
        }
    }
    
    /**
     * Refresh the display (public method)
     */
    public void refresh() {
        updateDisplay();
    }
}
