package com.coincraft.ui.components.child;

import java.util.List;
import java.util.function.Consumer;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.User;
import com.coincraft.models.Product;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.components.shared.ProductCard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Modern shop page for Child Dashboard
 * Features categorized items, purchase functionality, and engaging design
 */
public class ShopPage {
    private StackPane root;
    private ScrollPane scrollPane;
    private VBox contentContainer;
    private final User currentUser;
    private Consumer<String> refreshCallback;
    
    public ShopPage(User user) {
        this.currentUser = user;
        initializeUI();
    }
    
    public ShopPage(User user, Consumer<String> refreshCallback) {
        this.currentUser = user;
        this.refreshCallback = refreshCallback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new StackPane();
        root.setStyle("-fx-background-color: transparent;");
        
        // Create scrollable content
        contentContainer = new VBox(24);
        contentContainer.setPadding(new Insets(24));
        contentContainer.setAlignment(Pos.TOP_CENTER);
        
        // Create scroll pane
        scrollPane = new ScrollPane(contentContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
        
        createShopContent();
        
        root.getChildren().add(scrollPane);
    }
    
    private void createShopContent() {
        // Shop header
        VBox header = createShopHeader();
        
        // Load real products from Firebase
        List<Product> products = FirebaseService.getInstance().loadActiveProducts();
        
        if (products.isEmpty()) {
            // Show empty state
            VBox emptyState = createEmptyShopState();
            contentContainer.getChildren().addAll(header, emptyState);
        } else {
            // Display products using unified ProductCard
            VBox productsSection = createModernProductsSection(products);
            contentContainer.getChildren().addAll(header, productsSection);
        }
    }
    
    private VBox createShopHeader() {
        VBox header = new VBox(16);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 24, 0));
        
        // Title
        Label titleLabel = new Label("Adventure Shop");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1a1a1a;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Subtitle
        Label subtitleLabel = new Label("Spend your SmartCoins on amazing items!");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        
        // Balance display
        HBox balanceBox = new HBox(8);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.setPadding(new Insets(16));
        balanceBox.setStyle(
            "-fx-background-color: rgba(255, 152, 0, 0.1);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 2;"
        );
        
        Label balanceLabel = new Label("💰 Your Balance: " + currentUser.getSmartCoins() + " SmartCoins");
        balanceLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        balanceBox.getChildren().add(balanceLabel);
        
        header.getChildren().addAll(titleLabel, subtitleLabel, balanceBox);
        return header;
    }
    
    
    // Shop item categories
    private VBox createEmptyShopState() {
        VBox emptyState = new VBox(16);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(40, 20, 40, 20));
        
        Label emptyIcon = new Label("🛒");
        emptyIcon.setStyle("-fx-font-size: 48px;");
        
        Label emptyTitle = new Label("Shop Coming Soon!");
        emptyTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #374151;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        Label emptySubtitle = new Label("Your parents haven't added any products yet. Ask them to create some items for you to purchase!");
        emptySubtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #6B7280;" +
            "-fx-font-family: 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        emptySubtitle.setWrapText(true);
        emptySubtitle.setMaxWidth(400);
        
        emptyState.getChildren().addAll(emptyIcon, emptyTitle, emptySubtitle);
        return emptyState;
    }
    
    private VBox createModernProductsSection(List<Product> products) {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_CENTER);
        
        // Section title
        Label sectionTitle = new Label("🛍️ Available Products");
        sectionTitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #1e293b;" +
            "-fx-font-family: 'Segoe UI', 'Minecraft', sans-serif;"
        );
        
        // Products grid
        VBox productsGrid = new VBox(16);
        productsGrid.setAlignment(Pos.TOP_CENTER);
        
        // Create product cards in rows of 3
        for (int i = 0; i < products.size(); i += 3) {
            HBox row = new HBox(20);
            row.setAlignment(Pos.CENTER);
            
            for (int j = i; j < Math.min(i + 3, products.size()); j++) {
                Product product = products.get(j);
                ProductCard productCard = new ProductCard(
                    product,
                    "🛒 Purchase", this::purchaseProduct,
                    null, null,
                    null, null
                );
                row.getChildren().add(productCard);
            }
            
            // Fill remaining space if needed
            while (row.getChildren().size() < 3) {
                Region spacer = new Region();
                spacer.setPrefWidth(320);
                row.getChildren().add(spacer);
            }
            
            productsGrid.getChildren().add(row);
        }
        
        section.getChildren().addAll(sectionTitle, productsGrid);
        return section;
    }
    
    private void purchaseProduct(Product product) {
        CentralizedMusicManager.getInstance().playButtonClick();
        
        if (currentUser.getSmartCoins() >= product.getPrice()) {
            // Deduct coins
            currentUser.setSmartCoins(currentUser.getSmartCoins() - product.getPrice());
            
            // Save to Firebase
            try {
                FirebaseService.getInstance().saveUser(currentUser);
            } catch (Exception ex) {
                System.out.println("⚠️ Warning: Could not save updated balance: " + ex.getMessage());
            }
            
            // Show success dialog
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Purchase Successful!");
            successAlert.setHeaderText("🎉 " + product.getName() + " Purchased!");
            successAlert.setContentText(
                "Congratulations! You've successfully purchased " + product.getName() + "!\n\n" +
                "Remaining Balance: " + currentUser.getSmartCoins() + " SmartCoins\n\n" +
                "Enjoy your new item!"
            );
            successAlert.showAndWait();
            
            // Refresh the shop content
            contentContainer.getChildren().clear();
            createShopContent();
            
            // Notify parent dashboard if callback is available
            if (refreshCallback != null) {
                refreshCallback.accept("purchase");
            }
        } else {
            // Show insufficient funds dialog
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Insufficient Funds");
            errorAlert.setHeaderText("Not Enough SmartCoins!");
            errorAlert.setContentText(
                "You need " + product.getPrice() + " SmartCoins to purchase " + product.getName() + ".\n\n" +
                "Your current balance: " + currentUser.getSmartCoins() + " SmartCoins\n\n" +
                "Complete more tasks to earn SmartCoins!"
            );
            errorAlert.showAndWait();
        }
    }
    
    public StackPane getRoot() {
        return root;
    }
    
}
