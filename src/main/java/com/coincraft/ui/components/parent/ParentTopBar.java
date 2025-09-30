package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.coincraft.models.User;
import com.coincraft.ui.components.SmartCoinDisplay;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Top bar for Parent Dashboard
 * Features welcome message, family balance, and real-time notifications
 * Professional design with family-oriented information display
 */
public class ParentTopBar {
    private HBox root;
    private final User currentUser;
    private final Runnable onNotificationsClick;
    private Label timeLabel;
    private Label notificationLabel;
    private SmartCoinDisplay familyBalanceDisplay;
    private int activeChildren = 0;
    private int pendingTasks = 0;
    private Label activeChildrenValueLabel;
    private Label pendingTasksValueLabel;
    
    public ParentTopBar(User user, Runnable onNotificationsClick) {
        this.currentUser = user;
        this.onNotificationsClick = onNotificationsClick;
        initializeUI();
        startTimeUpdater();
    }
    
    private void initializeUI() {
        root = new HBox(24);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(16, 24, 16, 24));
        root.setPrefHeight(80);
        root.setMaxWidth(Double.MAX_VALUE);
        root.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.98);" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;" +
            "-fx-border-color: rgba(76, 175, 80, 0.2);" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);"
        );
        
        createWelcomeSection();
        createCenterSpacer();
        createFamilyStats();
        createNotificationArea();
    }
    
    private void createWelcomeSection() {
        VBox welcomeSection = new VBox(4);
        welcomeSection.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Good " + getTimeOfDay() + ", " + currentUser.getName() + "!");
        welcomeLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        timeLabel = new Label();
        timeLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-weight: 500;" +
            "-fx-font-family: 'Segoe UI', 'Inter', '  Sans', 'Minecraft', sans-serif;"
        );
        updateTimeLabel();
        
        welcomeSection.getChildren().addAll(welcomeLabel, timeLabel);
        root.getChildren().add(welcomeSection);
    }
    
    private void createCenterSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        root.getChildren().add(spacer);
    }
    
    private void createFamilyStats() {
        HBox statsSection = new HBox(16);
        statsSection.setAlignment(Pos.CENTER);
        
        // Family Balance
        VBox balanceBox = new VBox(6);
        balanceBox.setAlignment(Pos.CENTER);
        
        Label balanceTitle = new Label("Family Balance");
        balanceTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Balance display with purchase button
        HBox balanceRow = new HBox(8);
        balanceRow.setAlignment(Pos.CENTER);
        
        // Use actual user's balance for SmartCoinDisplay
        familyBalanceDisplay = new SmartCoinDisplay(currentUser);
        familyBalanceDisplay.getRoot().setStyle(
            familyBalanceDisplay.getRoot().getStyle() + 
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-padding: 8 16;"
        );
        
        // Round "+" button for purchasing coins
        Button purchaseButton = new Button("+");
        purchaseButton.setPrefSize(32, 32);
        purchaseButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: #E67E00;" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(250,138,0,0.3), 4, 0, 0, 2);"
        );
        
        purchaseButton.setOnMouseEntered(e -> {
            purchaseButton.setStyle(
                "-fx-background-color: #E67E00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: #FA8A00;" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(250,138,0,0.4), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
            );
        });
        
        purchaseButton.setOnMouseExited(e -> {
            purchaseButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: #E67E00;" +
                "-fx-border-width: 2;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(250,138,0,0.3), 4, 0, 0, 2);"
            );
        });
        
        purchaseButton.setOnAction(e -> {
            showPurchaseCoinDialog();
        });
        
        balanceRow.getChildren().addAll(familyBalanceDisplay.getRoot(), purchaseButton);
        balanceBox.getChildren().addAll(balanceTitle, balanceRow);
        
        // Active Adventurers
        VBox childrenBox = new VBox(6);
        childrenBox.setAlignment(Pos.CENTER);
        
        Label childrenTitle = new Label("Active Adventurers");
        childrenTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        activeChildrenValueLabel = new Label(String.valueOf(activeChildren));
        activeChildrenValueLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        childrenBox.getChildren().addAll(childrenTitle, activeChildrenValueLabel);
        
        // Pending Tasks
        VBox tasksBox = new VBox(6);
        tasksBox.setAlignment(Pos.CENTER);
        
        Label tasksTitle = new Label("Pending Review");
        tasksTitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #E67E00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        pendingTasksValueLabel = new Label(String.valueOf(pendingTasks));
        pendingTasksValueLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        tasksBox.getChildren().addAll(tasksTitle, pendingTasksValueLabel);
        
        statsSection.getChildren().addAll(balanceBox, childrenBox, tasksBox);
        root.getChildren().add(statsSection);
    }
    
    
    private void createNotificationArea() {
        VBox notificationArea = new VBox(4);
        notificationArea.setAlignment(Pos.CENTER_RIGHT);
        
        notificationLabel = new Label(formatNotificationText());
        notificationLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
            "-fx-cursor: hand;" +
            "-fx-background-color: rgba(255, 152, 0, 0.15);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 152, 0, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.2), 4, 0, 0, 2);"
        );
        
        notificationLabel.setOnMouseEntered(e -> {
            notificationLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #FA8A00;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-cursor: hand;" +
                "-fx-background-color: rgba(255, 152, 0, 0.25);" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: rgba(255, 152, 0, 0.4);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 6, 0, 0, 3);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        
        notificationLabel.setOnMouseExited(e -> {
            notificationLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #FA8A00;" +
                "-fx-font-weight: 600;" +
                "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;" +
                "-fx-cursor: hand;" +
                "-fx-background-color: rgba(255, 152, 0, 0.15);" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: rgba(255, 152, 0, 0.3);" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 16;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.2), 4, 0, 0, 2);"
            );
        });
        
        notificationLabel.setOnMouseClicked(e -> {
            // Navigate to tasks section to show pending tasks
            System.out.println("🔔 Opening notifications - navigating to tasks section");
            try {
                if (onNotificationsClick != null) {
                    onNotificationsClick.run();
                    return;
                }
                // Fallback to router-based navigation if callback not provided
                com.coincraft.ui.routing.DashboardRouter router = com.coincraft.ui.routing.DashboardRouter.getInstance();
                if (router.getCurrentDashboard() instanceof com.coincraft.ui.dashboards.ParentDashboard parentDashboard) {
                    parentDashboard.navigateToSection("tasks");
                }
            } catch (Exception ex) {
                System.out.println("⚠️ Could not navigate to tasks section: " + ex.getMessage());
            }
        });
        
        notificationArea.getChildren().add(notificationLabel);
        root.getChildren().add(notificationArea);
    }
    
    private String getTimeOfDay() {
        int hour = LocalDateTime.now().getHour();
        if (hour < 12) return "Morning";
        else if (hour < 17) return "Afternoon";
        else return "Evening";
    }
    
    private void updateTimeLabel() {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy • h:mm a"));
        timeLabel.setText(formattedTime);
    }
    
    private void startTimeUpdater() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(60), e -> updateTimeLabel())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    public void updateFamilyBalance(int newBalance) {
        // Update the actual user's balance and refresh display
        currentUser.setSmartCoinBalance(newBalance);
        if (familyBalanceDisplay != null) {
            // Update the existing display instead of creating a new one
            familyBalanceDisplay.updateBalance(newBalance);
        }
    }
    
    public void updateActiveChildren(int count) {
        this.activeChildren = count;
        if (activeChildrenValueLabel != null) {
            activeChildrenValueLabel.setText(String.valueOf(count));
        }
    }
    
    public void updatePendingTasks(int count) {
        this.pendingTasks = count;
        notificationLabel.setText(formatNotificationText());
        if (pendingTasksValueLabel != null) {
            pendingTasksValueLabel.setText(String.valueOf(count));
        }
    }

    private String formatNotificationText() {
        if (pendingTasks <= 0) return "🔔 No new notifications";
        if (pendingTasks == 1) return "🔔 1 task pending review";
        return "🔔 " + pendingTasks + " tasks pending review";
    }
    
    public HBox getRoot() {
        return root;
    }
    
    private void showPurchaseCoinDialog() {
        // Create custom purchase window
        Stage purchaseWindow = new Stage();
        purchaseWindow.setTitle("💰 Purchase SmartCoins");
        purchaseWindow.initModality(Modality.APPLICATION_MODAL);
        purchaseWindow.initStyle(StageStyle.UTILITY);
        purchaseWindow.setResizable(false);
        
        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #FFF8E1, #FFFDE7);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(250, 138, 0, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;" +
            "-fx-font-family: 'Segoe UI', 'Inter', 'Pixelify Sans', 'Minecraft', sans-serif;"
        );
        
        // Header
        Label headerLabel = new Label("💰 SmartCoin Store");
        headerLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Purchase coins for your family adventures");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        
        VBox headerBox = new VBox(8);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(headerLabel, subtitleLabel);
        
        // Current balance display
        HBox balanceBox = new HBox(10);
        balanceBox.setAlignment(Pos.CENTER);
        
        Label currentBalanceLabel = new Label("Current Balance:");
        currentBalanceLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;"
        );
        
        Label balanceValueLabel = new Label(currentUser.getSmartCoinBalance() + " SmartCoins");
        balanceValueLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;"
        );
        
        balanceBox.getChildren().addAll(currentBalanceLabel, balanceValueLabel);
        
        // Pricing section
        VBox pricingSection = new VBox(15);
        pricingSection.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;" +
            "-fx-border-color: rgba(250, 138, 0, 0.2);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;"
        );
        
        Label pricingTitle = new Label("💰 SmartCoin Pricing");
        pricingTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;"
        );
        
        // Price per coin
        HBox pricePerCoinBox = new HBox(10);
        pricePerCoinBox.setAlignment(Pos.CENTER_LEFT);
        
        Label priceLabel = new Label("Price: $0.10 per SmartCoin");
        priceLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;"
        );
        
        Label discountLabel = new Label("(Bulk discounts available!)");
        discountLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-weight: 500;"
        );
        
        pricePerCoinBox.getChildren().addAll(priceLabel, discountLabel);
        
        // Purchase options
        ToggleGroup purchaseGroup = new ToggleGroup();
        
        // Option 1: Buy by coin amount
        ToggleButton buyByCoinsButton = new ToggleButton("Buy by Coin Amount");
        buyByCoinsButton.setToggleGroup(purchaseGroup);
        buyByCoinsButton.setSelected(true);
        buyByCoinsButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 10 20;"
        );
        
        // Option 2: Buy by dollar amount
        ToggleButton buyByDollarsButton = new ToggleButton("Buy by Dollar Amount");
        buyByDollarsButton.setToggleGroup(purchaseGroup);
        buyByDollarsButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #FA8A00;" +
            "-fx-border-width: 2;" +
            "-fx-padding: 10 20;"
        );
        
        HBox purchaseOptions = new HBox(10);
        purchaseOptions.setAlignment(Pos.CENTER);
        purchaseOptions.getChildren().addAll(buyByCoinsButton, buyByDollarsButton);
        
        // Input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(15);
        inputGrid.setAlignment(Pos.CENTER);
        
        Label amountLabel = new Label("Amount:");
        amountLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;"
        );
        
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setPrefWidth(150);
        amountField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #DDD;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;"
        );
        
        Label currencyLabel = new Label("coins");
        currencyLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;"
        );
        
        inputGrid.add(amountLabel, 0, 0);
        inputGrid.add(amountField, 1, 0);
        inputGrid.add(currencyLabel, 2, 0);
        
        // Calculation display
        VBox calculationBox = new VBox(10);
        calculationBox.setStyle(
            "-fx-background-color: rgba(250, 138, 0, 0.1);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 15;" +
            "-fx-border-color: rgba(250, 138, 0, 0.3);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;"
        );
        
        Label calculationLabel = new Label("Purchase Summary");
        calculationLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;"
        );
        
        Label coinsToBuyLabel = new Label("SmartCoins to buy: 0");
        coinsToBuyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        
        Label totalCostLabel = new Label("Total cost: $0.00");
        totalCostLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        
        Label newBalanceLabel = new Label("New balance: " + currentUser.getSmartCoinBalance() + " coins");
        newBalanceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50; -fx-font-weight: 600;");
        
        calculationBox.getChildren().addAll(calculationLabel, coinsToBuyLabel, totalCostLabel, newBalanceLabel);
        
        // Payment method
        VBox paymentBox = new VBox(10);
        
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;"
        );
        
        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("Credit Card", "PayPal", "Bank Transfer", "Cryptocurrency");
        paymentMethod.setValue("Credit Card");
        paymentMethod.setPrefWidth(200);
        paymentMethod.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #DDD;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8;"
        );
        
        paymentBox.getChildren().addAll(paymentLabel, paymentMethod);
        
        pricingSection.getChildren().addAll(pricingTitle, pricePerCoinBox, purchaseOptions, inputGrid, calculationBox, paymentBox);
        
        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: #DDD;" +
            "-fx-border-width: 2;" +
            "-fx-padding: 12 25;"
        );
        
        Button purchaseButton = new Button("💰 Purchase Now");
        purchaseButton.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-padding: 12 25;" +
            "-fx-font-size: 16px;"
        );
        
        buttonBox.getChildren().addAll(cancelButton, purchaseButton);
        
        // Add components to main container
        mainContainer.getChildren().addAll(headerBox, balanceBox, pricingSection, buttonBox);
        
        // Event handlers
        amountField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double amount = Double.parseDouble(newVal.isEmpty() ? "0" : newVal);
                boolean buyingByCoins = buyByCoinsButton.isSelected();
                
                double coins, cost;
                if (buyingByCoins) {
                    coins = amount;
                    cost = amount * 0.10; // $0.10 per coin
                } else {
                    cost = amount;
                    coins = amount / 0.10;
                }
                
                coinsToBuyLabel.setText("SmartCoins to buy: " + String.format("%.0f", coins));
                totalCostLabel.setText("Total cost: $" + String.format("%.2f", cost));
                newBalanceLabel.setText("New balance: " + (currentUser.getSmartCoinBalance() + (int)coins) + " coins");
                
                // Update currency label
                currencyLabel.setText(buyingByCoins ? "coins" : "USD");
                
            } catch (NumberFormatException e) {
                coinsToBuyLabel.setText("SmartCoins to buy: 0");
                totalCostLabel.setText("Total cost: $0.00");
                newBalanceLabel.setText("New balance: " + currentUser.getSmartCoinBalance() + " coins");
            }
        });
        
        buyByCoinsButton.setOnAction(e -> {
            buyByCoinsButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 10 20;"
            );
            buyByDollarsButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #FA8A00;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: #FA8A00;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 20;"
            );
            currencyLabel.setText("coins");
            amountField.setPromptText("Enter number of coins");
        });
        
        buyByDollarsButton.setOnAction(e -> {
            buyByDollarsButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 10 20;"
            );
            buyByCoinsButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #FA8A00;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: #FA8A00;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 20;"
            );
            currencyLabel.setText("USD");
            amountField.setPromptText("Enter dollar amount");
        });
        
        cancelButton.setOnAction(e -> purchaseWindow.close());
        
        purchaseButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showErrorDialog("Invalid Amount", "Please enter a valid amount greater than 0.");
                    return;
                }
                
                boolean buyingByCoins = buyByCoinsButton.isSelected();
                double coins, cost;
                if (buyingByCoins) {
                    coins = amount;
                    cost = amount * 0.10;
                } else {
                    cost = amount;
                    coins = amount / 0.10;
                }
                
                // Process purchase
                processPurchase((int)coins, cost, paymentMethod.getValue(), purchaseWindow);
                
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid Input", "Please enter a valid number.");
            }
        });
        
        purchaseWindow.setScene(new javafx.scene.Scene(mainContainer, 500, 700));
        purchaseWindow.showAndWait();
    }
    
    private void processPurchase(int coins, double cost, String paymentMethod, Stage window) {
        // Show processing dialog
        Alert processingDialog = new Alert(Alert.AlertType.INFORMATION);
        processingDialog.setTitle("Processing Purchase");
        processingDialog.setHeaderText("🔄 Processing Your Purchase");
        processingDialog.setContentText("Please wait while we process your payment...\n\n" +
                "SmartCoins: " + coins + "\n" +
                "Amount: $" + String.format("%.2f", cost) + "\n" +
                "Method: " + paymentMethod);
        
        processingDialog.initModality(Modality.APPLICATION_MODAL);
        processingDialog.initStyle(StageStyle.UTILITY);
        
        // Simulate processing delay
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(Duration.seconds(2), e -> {
                processingDialog.close();
                
                // Update user balance
                currentUser.setSmartCoinBalance(currentUser.getSmartCoinBalance() + coins);
                updateFamilyBalance(currentUser.getSmartCoinBalance());
                
                // Save to Firebase
                try {
                    com.coincraft.services.FirebaseService.getInstance().saveUser(currentUser);
                } catch (Exception ex) {
                    System.out.println("⚠️ Warning: Could not save updated balance: " + ex.getMessage());
                }
                
                // Show success dialog
                Alert successDialog = new Alert(Alert.AlertType.INFORMATION);
                successDialog.setTitle("Purchase Successful!");
                successDialog.setHeaderText("🎉 Purchase Completed Successfully!");
                successDialog.setContentText("Your SmartCoins have been added to your account!\n\n" +
                        "Purchased: " + coins + " SmartCoins\n" +
                        "Amount Paid: $" + String.format("%.2f", cost) + "\n" +
                        "Payment Method: " + paymentMethod + "\n" +
                        "New Balance: " + currentUser.getSmartCoinBalance() + " SmartCoins");
                
                successDialog.initModality(Modality.APPLICATION_MODAL);
                successDialog.initStyle(StageStyle.UTILITY);
                
                successDialog.getDialogPane().setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.98);" +
                    "-fx-border-color: rgba(76, 175, 80, 0.3);" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;"
                );
                
                successDialog.showAndWait();
                window.close();
            })
        );
        timeline.play();
        
        processingDialog.showAndWait();
    }
    
    private void showErrorDialog(String title, String message) {
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle(title);
        errorDialog.setHeaderText("⚠️ " + title);
        errorDialog.setContentText(message);
        errorDialog.initModality(Modality.APPLICATION_MODAL);
        errorDialog.initStyle(StageStyle.UTILITY);
        
        errorDialog.getDialogPane().setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.98);" +
            "-fx-border-color: rgba(244, 67, 54, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );
        
        errorDialog.showAndWait();
    }
    
}
