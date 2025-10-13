package com.coincraft.ui.components.parent;

import com.coincraft.models.Product;
import com.coincraft.services.FirebaseService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Dialog for editing existing products in the parent dashboard
 */
public class EditProductDialog extends Dialog<Product> {
    
    private TextField nameField;
    private TextArea descriptionField;
    private TextField priceField;
    private TextField imageUrlField;
    private ImageView imagePreview;
    private final Product originalProduct;
    private String selectedImagePath;
    
    public EditProductDialog(Product product) {
        this.originalProduct = product;
        setupDialog();
    }
    
    private void setupDialog() {
        setTitle("Edit Product");
        setHeaderText("Update Product Details");
        
        // Create main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setMaxWidth(500);
        mainContainer.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1;"
        );
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Form section
        VBox formSection = createFormSection();
        
        // Image section
        VBox imageSection = createImageSection();
        
        mainContainer.getChildren().addAll(headerSection, formSection, imageSection);
        
        // Set dialog content
        getDialogPane().setContent(mainContainer);
        getDialogPane().setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;"
        );
        
        // Buttons
        ButtonType saveButtonType = new ButtonType("Update Product", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // Style buttons
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        Button cancelButton = (Button) getDialogPane().lookupButton(cancelButtonType);
        
        saveButton.setStyle(
            "-fx-background-color: #3b82f6;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 10 20;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        cancelButton.setStyle(
            "-fx-background-color: #6b7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 10 20;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Set result converter
        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                if (validateAndSave()) {
                    return originalProduct;
                }
                return null;
            }
            return null;
        });
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("🛍️ Edit Product");
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Update the product details below");
        subtitleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private VBox createFormSection() {
        VBox form = new VBox(16);
        form.setAlignment(Pos.CENTER_LEFT);
        
        // Product Name
        Label nameLabel = new Label("Product Name *");
        nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        nameField = new TextField(originalProduct.getName());
        nameField.setPromptText("Enter product name");
        nameField.setPrefHeight(40);
        nameField.setStyle(
            "-fx-background-color: #f9fafb;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Product Description
        Label descLabel = new Label("Description *");
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        descriptionField = new TextArea(originalProduct.getDescription());
        descriptionField.setPromptText("Describe the product");
        descriptionField.setPrefHeight(80);
        descriptionField.setStyle(
            "-fx-background-color: #f9fafb;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Price
        Label priceLabel = new Label("Price (SmartCoins) *");
        priceLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        priceField = new TextField(String.valueOf(originalProduct.getPrice()));
        priceField.setPromptText("Enter price in SmartCoins");
        priceField.setPrefHeight(40);
        priceField.setStyle(
            "-fx-background-color: #f9fafb;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        form.getChildren().addAll(nameLabel, nameField, descLabel, descriptionField, priceLabel, priceField);
        return form;
    }
    
    private VBox createImageSection() {
        VBox imageSection = new VBox(12);
        imageSection.setAlignment(Pos.CENTER);
        
        Label imageLabel = new Label("Product Image");
        imageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Image selection button
        Button selectImageButton = new Button("📁 Choose Image File");
        selectImageButton.setPrefHeight(40);
        selectImageButton.setStyle(
            "-fx-background-color: #f59e0b;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-padding: 8 16;"
        );
        selectImageButton.setOnAction(e -> selectImageFile());
        
        // Image preview
        imagePreview = new ImageView();
        imagePreview.setFitWidth(120);
        imagePreview.setFitHeight(120);
        imagePreview.setPreserveRatio(true);
        imagePreview.setStyle(
            "-fx-background-color: #f1f5f9;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #cbd5e1;" +
            "-fx-border-width: 2;"
        );
        
        // Load current image
        loadCurrentImage();
        
        // Fallback emoji field
        Label emojiLabel = new Label("Or use emoji:");
        emojiLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #64748b;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        imageUrlField = new TextField(originalProduct.getImageUrl());
        imageUrlField.setPromptText("Enter emoji (e.g., 🎮, 🍕, 🧸)");
        imageUrlField.setPrefHeight(40);
        imageUrlField.setStyle(
            "-fx-background-color: #f9fafb;" +
            "-fx-border-color: #d1d5db;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        imageUrlField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.isEmpty() && newText.matches("\\p{So}+")) {
                selectedImagePath = null; // Clear image path when emoji is used
            }
        });
        
        imageSection.getChildren().addAll(imageLabel, selectImageButton, imagePreview, emojiLabel, imageUrlField);
        return imageSection;
    }
    
    private void selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imagePreview.setImage(image);
                imageUrlField.clear(); // Clear emoji field when image is selected
            } catch (IllegalArgumentException | SecurityException e) {
                showErrorAlert("Error loading image: " + e.getMessage());
            }
        }
    }
    
    private void loadCurrentImage() {
        if (originalProduct.getImageUrl() != null && !originalProduct.getImageUrl().isEmpty()) {
            if (originalProduct.getImageUrl().matches("\\p{So}+")) {
                // It's an emoji, show placeholder
                Label emojiLabel = new Label(originalProduct.getImageUrl());
                emojiLabel.setStyle("-fx-font-size: 48px;");
                imagePreview.setImage(null);
                // We'll show the emoji in the text field instead
            } else {
                // It's a file path, try to load the image
                try {
                    File imageFile = new File(originalProduct.getImageUrl());
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        imagePreview.setImage(image);
                        selectedImagePath = originalProduct.getImageUrl();
                    } else {
                        // Show placeholder if file doesn't exist
                        Label placeholderLabel = new Label("🛍️");
                        placeholderLabel.setStyle("-fx-font-size: 48px;");
                    }
                } catch (IllegalArgumentException | SecurityException e) {
                    // Show placeholder on error
                    Label placeholderLabel = new Label("🛍️");
                    placeholderLabel.setStyle("-fx-font-size: 48px;");
                }
            }
        } else {
            // Show default placeholder
            Label placeholderLabel = new Label("🛍️");
            placeholderLabel.setStyle("-fx-font-size: 48px;");
        }
    }
    
    private boolean validateAndSave() {
        // Validate inputs
        if (nameField.getText().trim().isEmpty()) {
            showErrorAlert("Product name is required!");
            return false;
        }
        
        if (descriptionField.getText().trim().isEmpty()) {
            showErrorAlert("Product description is required!");
            return false;
        }
        
        try {
            int price = Integer.parseInt(priceField.getText().trim());
            if (price <= 0) {
                showErrorAlert("Price must be a positive number!");
                return false;
            }
            if (price > 10000) {
                showErrorAlert("Price seems too high! Please enter a reasonable amount.");
                return false;
            }
            originalProduct.setPrice(price);
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter a valid price!");
            return false;
        }
        
        // Validate image/emoji
        if (selectedImagePath != null) {
            originalProduct.setImageUrl(selectedImagePath);
        } else if (imageUrlField.getText().trim().isEmpty()) {
            showErrorAlert("Please provide either an image file or an emoji!");
            return false;
        } else {
            originalProduct.setImageUrl(imageUrlField.getText().trim());
        }
        
        // Update product details
        originalProduct.setName(nameField.getText().trim());
        originalProduct.setDescription(descriptionField.getText().trim());
        
        try {
            // Save to Firebase
            FirebaseService.getInstance().updateProduct(originalProduct);
            return true;
        } catch (RuntimeException ex) {
            showErrorAlert("Error updating product: " + ex.getMessage());
            return false;
        }
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please fix the following issues:");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
