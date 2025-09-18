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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
    
    private void initializeComponent() {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15));
        root.getStyleClass().add("avatar-display");
        
        // Avatar image with circular frame
        avatarImageView = new ImageView();
        avatarImageView.setFitWidth(120);
        avatarImageView.setFitHeight(120);
        avatarImageView.setPreserveRatio(true);
        
        // Create circular clip for avatar
        Circle clip = new Circle(60, 60, 60);
        avatarImageView.setClip(clip);
        
        // Load default avatar image
        try {
            Image defaultAvatar = new Image(getClass().getResourceAsStream("/images/avatars/default_explorer.png"));
            avatarImageView.setImage(defaultAvatar);
        } catch (Exception e) {
            // Create a placeholder if image not found
            avatarImageView.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 60;");
        }
        
        // Avatar name
        nameLabel = new Label("Money Explorer");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.getStyleClass().add("avatar-name");
        
        // Level badge
        levelLabel = new Label("Level 1");
        levelLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        levelLabel.getStyleClass().add("avatar-level");
        levelLabel.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; " +
                          "-fx-padding: 5 10; -fx-background-radius: 15;");
        
        // Customize button
        customizeButton = new Button("Customize Avatar");
        customizeButton.getStyleClass().add("customize-button");
        customizeButton.setOnAction(e -> openCustomization());
        
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
        // TODO: Open avatar customization dialog
        System.out.println("Opening avatar customization...");
        
        // For MVP, show a simple message
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Avatar Customization");
        alert.setHeaderText("Customize Your Character");
        alert.setContentText("Avatar customization will be available soon! Complete more levels to unlock new items.");
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
}
