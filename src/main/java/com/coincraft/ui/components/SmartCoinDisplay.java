package com.coincraft.ui.components;

import com.coincraft.models.User;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * SmartCoin balance display component with animated coin icon
 */
public class SmartCoinDisplay {
    private VBox root;
    private Label balanceLabel;
    private ImageView coinIcon;
    private int currentBalance;
    private User currentUser;
    
    public SmartCoinDisplay() {
        initializeComponent();
    }
    
    public SmartCoinDisplay(User user) {
        this.currentUser = user;
        initializeComponent();
        updateBalance();
    }
    
    private void initializeComponent() {
        root = new VBox(6);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setStyle(
            "-fx-background-color: #FFF8DC;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #EAB308;" +
            "-fx-border-radius: 12;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(234,179,8,0.3), 8, 0, 0, 4);"
        );
        root.getStyleClass().add("smartcoin-display");
        root.getStyleClass().add("pixel-card");
        
        // Coin icon
        coinIcon = new ImageView();
        coinIcon.setFitWidth(28);
        coinIcon.setFitHeight(28);
        coinIcon.setPreserveRatio(true);
        
        // Load coin icon or create placeholder
        try {
            Image coinImage = new Image(getClass().getResourceAsStream("/images/icons/smartcoin.png"));
            coinIcon.setImage(coinImage);
        } catch (Exception e) {
            // Create a styled placeholder
            coinIcon.setStyle("-fx-background-color: #FFD700; -fx-background-radius: 14;");
        }
        
        // Balance display
        HBox balanceBox = new HBox(6);
        balanceBox.setAlignment(Pos.CENTER);
        
        balanceLabel = new Label("0");
        balanceLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #B8860B;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        balanceLabel.getStyleClass().add("balance-label");
        
        Label coinText = new Label("SmartCoins");
        coinText.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #8B7355;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        balanceBox.getChildren().addAll(coinIcon, balanceLabel);
        
        root.getChildren().addAll(balanceBox, coinText);
    }
    
    public void updateBalance(int newBalance) {
        if (newBalance != currentBalance) {
            // Animate coin icon when balance changes
            animateCoinIcon();
            
            // Update balance with animation
            animateBalanceChange(newBalance);
            
            currentBalance = newBalance;
        }
    }
    
    private void animateCoinIcon() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), coinIcon);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
    
    private void animateBalanceChange(int newBalance) {
        // Simple number update animation
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        
        int startBalance = currentBalance;
        int difference = newBalance - startBalance;
        int steps = 10;
        
        for (int i = 0; i <= steps; i++) {
            final int step = i;
            javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                Duration.millis(i * 50),
                e -> {
                    int intermediateValue = startBalance + (difference * step / steps);
                    balanceLabel.setText(String.valueOf(intermediateValue));
                }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.play();
    }
    
    public void addCoins(int amount) {
        updateBalance(currentBalance + amount);
    }
    
    public void spendCoins(int amount) {
        updateBalance(Math.max(0, currentBalance - amount));
    }
    
    public Parent getRoot() {
        return root;
    }
    
    public int getCurrentBalance() {
        return currentBalance;
    }
    
    /**
     * Update balance from user data
     */
    private void updateBalance() {
        if (currentUser != null) {
            updateBalance(currentUser.getSmartCoinBalance());
        }
    }
    
    /**
     * Refresh the display (public method)
     */
    public void refresh() {
        updateBalance();
    }
}
