package com.coincraft.ui.components.child;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
        
        // Categories
        VBox avatarSection = createShopCategory("🎭 Avatar Items", getAvatarItems());
        VBox toolsSection = createShopCategory("🛠️ Adventure Tools", getToolItems());
        VBox specialSection = createShopCategory("✨ Special Items", getSpecialItems());
        
        contentContainer.getChildren().addAll(header, avatarSection, toolsSection, specialSection);
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
    
    private VBox createShopCategory(String categoryTitle, List<ShopItem> items) {
        VBox category = new VBox(16);
        category.setAlignment(Pos.CENTER);
        
        // Category header
        Label categoryLabel = new Label(categoryTitle);
        categoryLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #1a1a1a;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Items grid
        GridPane itemsGrid = new GridPane();
        itemsGrid.setHgap(16);
        itemsGrid.setVgap(16);
        itemsGrid.setAlignment(Pos.CENTER);
        
        int column = 0;
        int row = 0;
        for (ShopItem item : items) {
            VBox itemCard = createShopItemCard(item);
            itemsGrid.add(itemCard, column, row);
            
            column++;
            if (column >= 4) { // 4 items per row
                column = 0;
                row++;
            }
        }
        
        category.getChildren().addAll(categoryLabel, itemsGrid);
        return category;
    }
    
    private VBox createShopItemCard(ShopItem item) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setMaxWidth(200);
        card.setPrefHeight(220);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: " + item.getRarityColor() + ";" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);" +
            "-fx-cursor: hand;"
        );
        
        // Item icon
        Label iconLabel = new Label(item.getIcon());
        iconLabel.setStyle("-fx-font-size: 40px;");
        
        // Item name
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;" +
            "-fx-wrap-text: true;"
        );
        nameLabel.setMaxWidth(160);
        
        // Price
        Label priceLabel = new Label("💰 " + item.getPrice() + " coins");
        priceLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-background-color: rgba(255, 152, 0, 0.1);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 4 8;"
        );
        
        // Purchase button
        Button purchaseBtn = new Button();
        if (currentUser.getSmartCoins() >= item.getPrice()) {
            purchaseBtn.setText("BUY NOW");
            purchaseBtn.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 15, 0, 0, 5);"
            );
            purchaseBtn.setOnAction(e -> purchaseItem(item));
        } else {
            purchaseBtn.setText("NOT ENOUGH COINS");
            purchaseBtn.setDisable(true);
            purchaseBtn.setStyle(
                "-fx-background-color: #9E9E9E;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 8 16;"
            );
        }
        
        purchaseBtn.setPrefWidth(140);
        
        // Hover effects
        card.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            card.setStyle(card.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(card.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        card.getChildren().addAll(iconLabel, nameLabel, priceLabel, purchaseBtn);
        return card;
    }
    
    private void purchaseItem(ShopItem item) {
        SoundManager.getInstance().playButtonClick();
        
        if (currentUser.getSmartCoins() >= item.getPrice()) {
            // Deduct coins (in real app, this would be handled by a service)
            currentUser.setSmartCoins(currentUser.getSmartCoins() - item.getPrice());
            
            // Save to Firebase
            try {
                com.coincraft.services.FirebaseService.getInstance().saveUser(currentUser);
            } catch (Exception ex) {
                System.out.println("⚠️ Warning: Could not save updated balance: " + ex.getMessage());
            }
            
            // Show success dialog
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Purchase Successful!");
            successAlert.setHeaderText("🎉 " + item.getName() + " Purchased!");
            successAlert.setContentText(
                "Congratulations! You've successfully purchased " + item.getName() + "!\n\n" +
                "Remaining Balance: " + currentUser.getSmartCoins() + " SmartCoins\n\n" +
                "Your new item will appear in your inventory!"
            );
            
            successAlert.getDialogPane().setStyle(
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 16;"
            );
            
            successAlert.showAndWait();
            
            // Refresh the shop to update buttons
            refreshShop();
            
            // Notify dashboard to refresh balance display
            if (refreshCallback != null) {
                refreshCallback.accept("balance_updated");
            }
            
        } else {
            // Show insufficient funds dialog
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setTitle("Insufficient Coins");
            errorAlert.setHeaderText("💰 Not Enough SmartCoins");
            errorAlert.setContentText(
                "You need " + item.getPrice() + " SmartCoins to purchase " + item.getName() + ".\n\n" +
                "Your current balance: " + currentUser.getSmartCoins() + " SmartCoins\n" +
                "You need " + (item.getPrice() - currentUser.getSmartCoins()) + " more coins!\n\n" +
                "Complete more quests to earn SmartCoins!"
            );
            
            errorAlert.getDialogPane().setStyle(
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-background-radius: 16;"
            );
            
            SoundManager.getInstance().playError();
            errorAlert.showAndWait();
        }
    }
    
    private void refreshShop() {
        contentContainer.getChildren().clear();
        createShopContent();
    }
    
    // Shop item categories
    private List<ShopItem> getAvatarItems() {
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("Cool Hat", "🎩", 50, "common", "A stylish hat for your avatar"));
        items.add(new ShopItem("Magic Cape", "🧙‍♂️", 100, "rare", "A mysterious cape with special powers"));
        items.add(new ShopItem("Golden Crown", "👑", 200, "legendary", "A crown fit for a SmartCoin champion"));
        items.add(new ShopItem("Ninja Mask", "🥷", 75, "uncommon", "Stealth mode activated!"));
        return items;
    }
    
    private List<ShopItem> getToolItems() {
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("Calculator Pro", "🧮", 30, "common", "Advanced math calculations"));
        items.add(new ShopItem("Piggy Bank", "🐷", 80, "uncommon", "Store your coins safely"));
        items.add(new ShopItem("Treasure Map", "🗺️", 120, "rare", "Find hidden quests"));
        items.add(new ShopItem("Magic Wand", "🪄", 150, "legendary", "Unlock special abilities"));
        return items;
    }
    
    private List<ShopItem> getSpecialItems() {
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("Extra Life", "💖", 100, "rare", "Get an extra chance on quests"));
        items.add(new ShopItem("Double XP", "⭐", 75, "uncommon", "Double experience for 1 day"));
        items.add(new ShopItem("Coin Magnet", "🧲", 200, "legendary", "Attract more SmartCoins"));
        items.add(new ShopItem("Time Freeze", "⏰", 150, "rare", "Pause quest timers"));
        return items;
    }
    
    public StackPane getRoot() {
        return root;
    }
    
    // Shop item class
    public static class ShopItem {
        private final String name;
        private final String icon;
        private final int price;
        private final String rarity;
        private final String description;
        
        public ShopItem(String name, String icon, int price, String rarity, String description) {
            this.name = name;
            this.icon = icon;
            this.price = price;
            this.rarity = rarity;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public int getPrice() { return price; }
        public String getRarity() { return rarity; }
        public String getDescription() { return description; }
        
        public String getRarityColor() {
            return switch (rarity) {
                case "common" -> "#9E9E9E";
                case "uncommon" -> "#4CAF50";
                case "rare" -> "#2196F3";
                case "legendary" -> "#FA8A00";
                default -> "#9E9E9E";
            };
        }
    }
    
}
