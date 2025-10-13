package com.coincraft.ui.components.shared;

import com.coincraft.models.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.function.Consumer;

/**
 * Unified Product Card component for both Parent and Child dashboards
 * Provides consistent UI/UX with dashboard-specific buttons
 */
public class ProductCard extends VBox {
    
    private final Product product;
    private Button primaryButton;
    private Button secondaryButton;
    private Button tertiaryButton;
    private Consumer<Product> onPrimaryAction;
    private Consumer<Product> onSecondaryAction;
    private Consumer<Product> onTertiaryAction;
    
    public ProductCard(Product product) {
        this.product = product;
        initializeCard();
    }
    
    public ProductCard(Product product, 
                      String primaryButtonText, Consumer<Product> onPrimaryAction,
                      String secondaryButtonText, Consumer<Product> onSecondaryAction,
                      String tertiaryButtonText, Consumer<Product> onTertiaryAction) {
        this.product = product;
        this.onPrimaryAction = onPrimaryAction;
        this.onSecondaryAction = onSecondaryAction;
        this.onTertiaryAction = onTertiaryAction;
        initializeCard();
        createButtons(primaryButtonText, secondaryButtonText, tertiaryButtonText);
    }
    
    private void initializeCard() {
        setPrefWidth(360);
        setMaxWidth(360);
        setPrefHeight(360);
        setMaxHeight(360);
        setSpacing(14);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        
        // Modern card styling with clean background
        setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 20, 0, 0, 8);" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1;"
        );
        
        createCardContent();
    }
    
    private void createCardContent() {
        // Product Image Section
        VBox imageSection = createImageSection();
        
        // Title and Status Row (inline)
        HBox titleStatusRow = createTitleStatusRow();
        
        // Product Info Section
        VBox infoSection = createInfoSection();
        
        getChildren().addAll(imageSection, titleStatusRow, infoSection);
    }
    
    private VBox createImageSection() {
        VBox imageSection = new VBox(12);
        imageSection.setAlignment(Pos.CENTER);
        
        // Image container with modern styling - larger size
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(150, 150);
        imageContainer.setStyle(
            "-fx-background-color: #f1f5f9;" +
            "-fx-background-radius: 24;" +
            "-fx-border-radius: 24;" +
            "-fx-border-color: #cbd5e1;" +
            "-fx-border-width: 2;"
        );
        
        // Handle both real images and emojis
        javafx.scene.Node imageNode = createImageNode();
        imageContainer.getChildren().add(imageNode);
        
        imageSection.getChildren().add(imageContainer);
        return imageSection;
    }
    
    private javafx.scene.Node createImageNode() {
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty() && !product.getImageUrl().matches("\\p{So}+")) {
            // It's a file path - try to load as image
            try {
                File imageFile = new File(product.getImageUrl());
                if (imageFile.exists()) {
                    ImageView imageView = new ImageView(new Image(imageFile.toURI().toString()));
                    imageView.setFitWidth(120);
                    imageView.setFitHeight(120);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageView.setCache(true);
                    return imageView;
                } else {
                    // Fallback to emoji if file doesn't exist
                    Label emojiLabel = new Label("🛍️");
                    emojiLabel.setStyle("-fx-font-size: 64px;");
                    return emojiLabel;
                }
            } catch (Exception e) {
                Label emojiLabel = new Label("🛍️");
                emojiLabel.setStyle("-fx-font-size: 64px;");
                return emojiLabel;
            }
        } else {
            // It's an emoji
            Label emojiLabel = new Label(product.getImageUrl() != null ? product.getImageUrl() : "🛍️");
            emojiLabel.setStyle("-fx-font-size: 64px;");
            return emojiLabel;
        }
    }
    
    private HBox createTitleStatusRow() {
        HBox titleStatusRow = new HBox(8);
        titleStatusRow.setAlignment(Pos.CENTER);
        titleStatusRow.setPadding(new Insets(4, 0, 0, 0));
        
        // Product Name
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        nameLabel.setWrapText(false);
        nameLabel.setMaxWidth(200);
        
        // Status Badge
        Label statusBadge = new Label(product.isActive() ? "✅ Active" : "❌ Inactive");
        statusBadge.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + (product.isActive() ? "#059669" : "#dc2626") + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-background-color: " + (product.isActive() ? "#dcfce7" : "#fee2e2") + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 4 10;"
        );
        
        titleStatusRow.getChildren().addAll(nameLabel, statusBadge);
        return titleStatusRow;
    }
    
    private VBox createInfoSection() {
        VBox infoSection = new VBox(8);
        infoSection.setAlignment(Pos.CENTER);
        infoSection.setPadding(new Insets(0, 0, 0, 0));
        
        // Product Description
        Label descLabel = new Label(product.getDescription());
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(300);
        descLabel.setMaxHeight(36);
        
        // Price
        Label priceLabel = new Label("💰 " + product.getPrice() + " SmartCoins");
        priceLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #059669;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        infoSection.getChildren().addAll(descLabel, priceLabel);
        return infoSection;
    }
    
    private void createButtons(String primaryText, String secondaryText, String tertiaryText) {
        VBox buttonSection = new VBox(8);
        buttonSection.setAlignment(Pos.CENTER);
        
        // Primary Button (if provided)
        if (primaryText != null && !primaryText.isEmpty()) {
            primaryButton = createModernButton(primaryText, "primary");
            if (onPrimaryAction != null) {
                primaryButton.setOnAction(e -> onPrimaryAction.accept(product));
            }
            buttonSection.getChildren().add(primaryButton);
        }
        
        // Secondary and Tertiary Buttons (if provided)
        if (secondaryText != null || tertiaryText != null) {
            HBox buttonRow = new HBox(6);
            buttonRow.setAlignment(Pos.CENTER);
            
            if (secondaryText != null && !secondaryText.isEmpty()) {
                secondaryButton = createModernButton(secondaryText, "secondary");
                if (onSecondaryAction != null) {
                    secondaryButton.setOnAction(e -> onSecondaryAction.accept(product));
                }
                buttonRow.getChildren().add(secondaryButton);
            }
            
            if (tertiaryText != null && !tertiaryText.isEmpty()) {
                tertiaryButton = createModernButton(tertiaryText, "tertiary");
                if (onTertiaryAction != null) {
                    tertiaryButton.setOnAction(e -> onTertiaryAction.accept(product));
                }
                buttonRow.getChildren().add(tertiaryButton);
            }
            
            buttonSection.getChildren().add(buttonRow);
        }
        
        getChildren().add(buttonSection);
    }
    
    private Button createModernButton(String text, String type) {
        Button button = new Button(text);
        button.setPrefHeight(36);
        button.setMinHeight(36);
        button.setMaxHeight(36);
        button.setStyle(getButtonStyle(type));
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(getButtonHoverStyle(type));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(getButtonStyle(type));
        });
        
        return button;
    }
    
    private String getButtonStyle(String type) {
        return switch (type) {
            case "primary" -> 
                "-fx-background-color: #3b82f6;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 18;" +
                "-fx-border-radius: 18;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(59,130,246,0.3), 8, 0, 0, 3);";
                
            case "secondary" -> 
                "-fx-background-color: #f59e0b;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(245,158,11,0.3), 6, 0, 0, 2);";
                
            case "tertiary" -> 
                "-fx-background-color: #ef4444;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(239,68,68,0.3), 6, 0, 0, 2);";
                
            default -> 
                "-fx-background-color: #6b7280;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;";
        };
    }
    
    private String getButtonHoverStyle(String type) {
        return switch (type) {
            case "primary" -> 
                "-fx-background-color: #2563eb;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 18;" +
                "-fx-border-radius: 18;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.4), 12, 0, 0, 4);";
                
            case "secondary" -> 
                "-fx-background-color: #d97706;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(217,119,6,0.4), 8, 0, 0, 3);";
                
            case "tertiary" -> 
                "-fx-background-color: #dc2626;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                "-fx-padding: 6 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(220,38,38,0.4), 8, 0, 0, 3);";
                
            default -> getButtonStyle(type);
        };
    }
    
    // Getters for buttons (if needed for external styling)
    public Button getPrimaryButton() { return primaryButton; }
    public Button getSecondaryButton() { return secondaryButton; }
    public Button getTertiaryButton() { return tertiaryButton; }
    public Product getProduct() { return product; }
}
