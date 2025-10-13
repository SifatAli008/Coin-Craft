package com.coincraft.ui.components.parent;

import com.coincraft.models.Product;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Dialog for adding new products to the shop
 */
public class AddProductDialog extends Dialog<Product> {
    
    private TextField nameField;
    private TextArea descriptionField;
    private TextField priceField;
    private TextField imageUrlField;
    private ImageView imagePreview;
    private final String parentId;
    private String selectedImagePath;
    
    public AddProductDialog(String parentId) {
        this.parentId = parentId;
        setupDialog();
    }
    
    private void setupDialog() {
        setTitle("🛒 Add New Product");
        setHeaderText("Create a new product for the adventure shop");
        
        // Create main container with better styling
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(24));
        mainContainer.setStyle("-fx-background-color: #f8fafc;");
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Form section
        VBox formSection = createFormSection();
        
        // Image section
        VBox imageSection = createImageSection();
        
        mainContainer.getChildren().addAll(headerSection, formSection, imageSection);
        
        getDialogPane().setContent(mainContainer);
        getDialogPane().setPrefWidth(500);
        getDialogPane().setPrefHeight(600);
        
        // Style the dialog pane
        getDialogPane().setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e2e8f0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );
        
        // Buttons
        ButtonType saveButtonType = new ButtonType("💾 Save Product", ButtonType.OK.getButtonData());
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setStyle(
            "-fx-background-color: #10B981;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 16;"
        );
        
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle(
            "-fx-background-color: #6B7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 16;"
        );
        
        saveButton.setOnAction(e -> {
            if (validateAndSave()) {
                // Dialog will close automatically
            } else {
                e.consume(); // Prevent dialog from closing
            }
        });
        
        // Set result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Product product = new Product(
                        nameField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(priceField.getText()),
                        selectedImagePath != null ? selectedImagePath : imageUrlField.getText(),
                        "General", // Default category since we removed the dropdown
                        parentId
                    );
                    
                    // Save to Firebase
                    FirebaseService.getInstance().saveProduct(product);
                    
                    return product;
                } catch (RuntimeException ex) {
                    showErrorAlert("Error saving product: " + ex.getMessage());
                    return null;
                }
            }
            return null;
        });
    }
    
    private VBox createHeaderSection() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("🎯 New Shop Item");
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1F2937;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Add a new item that children can purchase with SmartCoins");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #6B7280;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        subtitleLabel.setWrapText(true);
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private VBox createFormSection() {
        VBox formSection = new VBox(16);
        
        // Product Name
        VBox nameSection = new VBox(6);
        Label nameLabel = new Label("📝 Product Name");
        nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        nameField = new TextField();
        nameField.setPromptText("e.g., Extra Playtime, Special Snack, Toy Choice");
        nameField.setPrefHeight(40);
        nameField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 12;" +
            "-fx-background-color: white;" +
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        nameSection.getChildren().addAll(nameLabel, nameField);
        
        // Description
        VBox descSection = new VBox(6);
        Label descLabel = new Label("📄 Description");
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        descriptionField = new TextArea();
        descriptionField.setPromptText("Describe what this product offers to the child...");
        descriptionField.setPrefRowCount(3);
        descriptionField.setPrefHeight(80);
        descriptionField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 12;" +
            "-fx-background-color: white;" +
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        descSection.getChildren().addAll(descLabel, descriptionField);
        
        // Price
        VBox priceSection = new VBox(6);
        Label priceLabel = new Label("💰 Price (SmartCoins)");
        priceLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        priceField = new TextField();
        priceField.setPromptText("Enter price in SmartCoins (e.g., 50, 100, 200)");
        priceField.setPrefHeight(40);
        priceField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 12;" +
            "-fx-background-color: white;" +
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        priceSection.getChildren().addAll(priceLabel, priceField);
        
        formSection.getChildren().addAll(nameSection, descSection, priceSection);
        return formSection;
    }
    
    private VBox createImageSection() {
        VBox imageSection = new VBox(12);
        
        Label imageLabel = new Label("🖼️ Product Image");
        imageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        // Image upload section
        VBox uploadSection = new VBox(12);
        uploadSection.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-border-color: #E5E7EB;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 16;"
        );
        
        // Upload button
        Button uploadButton = new Button("📁 Choose Image File");
        uploadButton.setStyle(
            "-fx-background-color: #3B82F6;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 16;" +
            "-fx-font-size: 14px;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        uploadButton.setOnAction(e -> selectImageFile());
        
        // Or use emoji section
        HBox emojiSection = new HBox(8);
        emojiSection.setAlignment(Pos.CENTER_LEFT);
        
        Label orLabel = new Label("or use emoji:");
        orLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #6B7280;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        imageUrlField = new TextField();
        imageUrlField.setPromptText("🎮 🍕 🧸 🎬 🎯");
        imageUrlField.setPrefWidth(120);
        imageUrlField.setPrefHeight(32);
        imageUrlField.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-padding: 6 8;" +
            "-fx-background-color: white;" +
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        emojiSection.getChildren().addAll(orLabel, imageUrlField);
        
        // Image preview
        imagePreview = new ImageView();
        imagePreview.setFitWidth(80);
        imagePreview.setFitHeight(80);
        imagePreview.setStyle(
            "-fx-background-color: #F3F4F6;" +
            "-fx-border-color: #D1D5DB;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        
        uploadSection.getChildren().addAll(uploadButton, emojiSection, imagePreview);
        imageSection.getChildren().addAll(imageLabel, uploadSection);
        
        return imageSection;
    }
    
    private void selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            
            // Load and display image preview
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imagePreview.setImage(image);
                
                // Clear emoji field since we're using an image
                imageUrlField.clear();
                
                // Show success message
                Label successLabel = new Label("✅ Image selected: " + selectedFile.getName());
                successLabel.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-text-fill: #059669;" +
                    "-fx-font-weight: 600;" +
                    "-fx-font-family: 'Segoe UI', sans-serif;"
                );
            } catch (IllegalArgumentException | SecurityException e) {
                showErrorAlert("Error loading image: " + e.getMessage());
            }
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
                showErrorAlert("Price must be greater than 0!");
                return false;
            }
            if (price > 10000) {
                showErrorAlert("Price seems too high! Please enter a reasonable amount.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter a valid price!");
            return false;
        }
        
        // Validate image - either file selected or emoji provided
        if (selectedImagePath == null && imageUrlField.getText().trim().isEmpty()) {
            showErrorAlert("Please either upload an image file or enter an emoji!");
            return false;
        }
        
        return true;
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
