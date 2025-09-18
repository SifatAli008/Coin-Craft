package com.coincraft.ui.theme;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * 8-bit style card container with chunky borders
 */
public class PixelCard extends VBox {
    
    public PixelCard() {
        super();
        initializePixelCard();
    }
    
    public PixelCard(double spacing) {
        super(spacing);
        initializePixelCard();
    }
    
    public PixelCard(Node... children) {
        super();
        initializePixelCard();
        getChildren().addAll(children);
    }
    
    private void initializePixelCard() {
        // Apply 8-bit card styling
        getStyleClass().add("pixel-card");
        setPadding(new Insets(12));
        setSpacing(8);
        
        // Add subtle hover effect
        setOnMouseEntered(e -> setTranslateY(-1));
        setOnMouseExited(e -> setTranslateY(0));
    }
    
    /**
     * Create a card with title header
     */
    public static PixelCard withTitle(String title, Node... content) {
        PixelCard card = new PixelCard();
        
        // Add pixel-style title
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(title);
        titleLabel.getStyleClass().add("pixel-title");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #000000;");
        
        card.getChildren().add(titleLabel);
        card.getChildren().addAll(content);
        
        return card;
    }
    
    /**
     * Create a highlighted card (for current/active states)
     */
    public static PixelCard createHighlighted(Node... content) {
        PixelCard card = new PixelCard(content);
        card.getStyleClass().add("pixel-card-highlighted");
        return card;
    }
}
