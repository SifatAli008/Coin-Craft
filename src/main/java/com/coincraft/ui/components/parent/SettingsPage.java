package com.coincraft.ui.components.parent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.coincraft.models.User;
import com.coincraft.services.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Comprehensive settings page for Parent Dashboard
 * Features account management, family settings, preferences, and system configuration
 */
public class SettingsPage {
    private VBox root;
    private final User currentUser;
    private final FirebaseService firebaseService;
    private List<User> children;
    
    // UI Components
    private TextField nameField;
    private TextField emailField;
    private ComboBox<String> themeCombo;
    private ComboBox<String> languageCombo;
    private CheckBox notificationsCheck;
    private CheckBox soundEffectsCheck;
    private CheckBox autoSaveCheck;
    private ComboBox<String> difficultyLevelCombo;
    private TextField maxDailyTasksField;
    private TextField coinRewardMultiplierField;
    
    public SettingsPage(User user, List<User> children) {
        this.currentUser = user;
        this.children = children != null ? children : new ArrayList<>();
        this.firebaseService = FirebaseService.getInstance();
        initializeUI();
        loadUserData();
    }
    
    private void initializeUI() {
        root = new VBox(24);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.setMaxWidth(Double.MAX_VALUE);
        
        createHeader();
        createAccountSection();
        createFamilySection();
        createPreferencesSection();
        createSystemSection();
        createActionButtons();
    }
    
    private void createHeader() {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(Double.MAX_VALUE);
        
        Label titleLabel = new Label("⚙️ Merchant Settings & Configuration");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Manage your account, family settings, and application preferences");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        subtitleLabel.setWrapText(true);
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        root.getChildren().add(header);
    }
    
    private void createAccountSection() {
        VBox accountSection = createSectionCard("👤 Account Information", 
            "Manage your personal account details and security settings");
        
        GridPane accountGrid = new GridPane();
        accountGrid.setHgap(16);
        accountGrid.setVgap(16);
        accountGrid.setPadding(new Insets(16));
        
        // Name field
        Label nameLabel = createFieldLabel("Full Name:");
        nameField = createTextField(currentUser.getName());
        accountGrid.add(nameLabel, 0, 0);
        accountGrid.add(nameField, 1, 0);
        
        // Email field
        Label emailLabel = createFieldLabel("Email Address:");
        emailField = createTextField(currentUser.getEmail() != null ? currentUser.getEmail() : "");
        accountGrid.add(emailLabel, 0, 1);
        accountGrid.add(emailField, 1, 1);
        
        // Role display (read-only)
        Label roleLabel = createFieldLabel("Account Role:");
        Label roleDisplay = new Label("Merchant (Parent)");
        roleDisplay.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-weight: 600;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        accountGrid.add(roleLabel, 0, 2);
        accountGrid.add(roleDisplay, 1, 2);
        
        // Account creation date
        Label createdLabel = createFieldLabel("Member Since:");
        Label createdDisplay = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        createdDisplay.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        accountGrid.add(createdLabel, 0, 3);
        accountGrid.add(createdDisplay, 1, 3);
        
        accountSection.getChildren().add(accountGrid);
        root.getChildren().add(accountSection);
    }
    
    private void createFamilySection() {
        VBox familySection = createSectionCard("🏦 Family Balance & Active Adventures", 
            "Manage your merchant coins and adventurer accounts");
        
        VBox familyContent = new VBox(20);
        familyContent.setPadding(new Insets(20));
        
        // Merchant Balance Section (like Active Adventures)
        VBox merchantBalanceCard = new VBox(16);
        merchantBalanceCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(250, 138, 0, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        // Merchant balance header
        HBox merchantHeader = new HBox(12);
        merchantHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label merchantIcon = new Label("🏪");
        merchantIcon.setStyle("-fx-font-size: 24px;");
        
        VBox merchantInfo = new VBox(4);
        Label merchantTitle = new Label("Merchant Account");
        merchantTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label merchantSubtitle = new Label("Your coin vault for managing adventures");
        merchantSubtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        merchantInfo.getChildren().addAll(merchantTitle, merchantSubtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Merchant coin balance
        VBox balanceInfo = new VBox(4);
        Label balanceLabel = new Label("Current Balance");
        balanceLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label balanceValue = new Label(currentUser.getSmartCoinBalance() + " coins");
        balanceValue.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        balanceInfo.getChildren().addAll(balanceLabel, balanceValue);
        
        merchantHeader.getChildren().addAll(merchantIcon, merchantInfo, spacer, balanceInfo);
        
        // Purchase coins button
        Button purchaseButton = new Button("💰 Purchase More Coins");
        purchaseButton.setPrefHeight(45);
        purchaseButton.setPrefWidth(200);
        purchaseButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        purchaseButton.setOnMouseEntered(e -> {
            purchaseButton.setStyle(
                "-fx-background-color: #E67E00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        purchaseButton.setOnMouseExited(e -> {
            purchaseButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
            );
        });
        
        purchaseButton.setOnAction(e -> {
            // Feature coming soon - coin purchase functionality
            System.out.println("💰 Coin purchase requested!");
        });
        
        merchantBalanceCard.getChildren().addAll(merchantHeader, purchaseButton);
        
        // Active Adventures Section
        VBox activeAdventuresCard = new VBox(16);
        activeAdventuresCard.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(33, 150, 243, 0.3);" +
            "-fx-border-width: 2;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        // Active Adventures header
        HBox adventuresHeader = new HBox(12);
        adventuresHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label adventuresIcon = new Label("⚔️");
        adventuresIcon.setStyle("-fx-font-size: 24px;");
        
        VBox adventuresInfo = new VBox(4);
        Label adventuresTitle = new Label("Active Adventures");
        adventuresTitle.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label adventuresSubtitle = new Label("Manage your adventurer accounts");
        adventuresSubtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        adventuresInfo.getChildren().addAll(adventuresTitle, adventuresSubtitle);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        // Adventure stats
        HBox adventureStats = new HBox(16);
        VBox totalAdventurersCard = createStatCard("Total", String.valueOf(children.size()), "#4CAF50");
        VBox activeAdventurersCard = createStatCard("Active", String.valueOf(getActiveAdventurersCount()), "#2196F3");
        VBox totalCoinsCard = createStatCard("Family Coins", String.valueOf(getTotalFamilyCoins()), "#FA8A00");
        
        adventureStats.getChildren().addAll(totalAdventurersCard, activeAdventurersCard, totalCoinsCard);
        
        adventuresHeader.getChildren().addAll(adventuresIcon, adventuresInfo, spacer2);
        
        // Adventurer list
        VBox adventurerList = new VBox(8);
        if (children.isEmpty()) {
            Label noAdventurersLabel = new Label("No adventurers added yet. Use the 'Children' tab to add adventurers.");
            noAdventurersLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #666666;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-padding: 20;" +
                "-fx-text-alignment: center;"
            );
            adventurerList.getChildren().add(noAdventurersLabel);
        } else {
            for (User child : children) {
                HBox adventurerItem = createAdventurerItem(child);
                adventurerList.getChildren().add(adventurerItem);
            }
        }
        
        activeAdventuresCard.getChildren().addAll(adventuresHeader, adventureStats, adventurerList);
        
        familyContent.getChildren().addAll(merchantBalanceCard, activeAdventuresCard);
        familySection.getChildren().add(familyContent);
        root.getChildren().add(familySection);
    }
    
    private void createPreferencesSection() {
        VBox preferencesSection = createSectionCard("🎨 Preferences", 
            "Customize your experience and interface settings");
        
        GridPane preferencesGrid = new GridPane();
        preferencesGrid.setHgap(16);
        preferencesGrid.setVgap(16);
        preferencesGrid.setPadding(new Insets(16));
        
        // Theme selection
        Label themeLabel = createFieldLabel("Theme:");
        themeCombo = createComboBox(List.of("Default", "Dark Mode", "High Contrast"));
        themeCombo.setValue("Default");
        preferencesGrid.add(themeLabel, 0, 0);
        preferencesGrid.add(themeCombo, 1, 0);
        
        // Language selection
        Label languageLabel = createFieldLabel("Language:");
        languageCombo = createComboBox(List.of("English", "Spanish", "French", "German"));
        languageCombo.setValue("English");
        preferencesGrid.add(languageLabel, 0, 1);
        preferencesGrid.add(languageCombo, 1, 1);
        
        // Notifications
        Label notificationsLabel = createFieldLabel("Notifications:");
        notificationsCheck = createCheckBox("Enable push notifications", true);
        preferencesGrid.add(notificationsLabel, 0, 2);
        preferencesGrid.add(notificationsCheck, 1, 2);
        
        // Sound effects
        Label soundLabel = createFieldLabel("Sound Effects:");
        soundEffectsCheck = createCheckBox("Enable sound effects", true);
        preferencesGrid.add(soundLabel, 0, 3);
        preferencesGrid.add(soundEffectsCheck, 1, 3);
        
        // Auto-save
        Label autoSaveLabel = createFieldLabel("Auto-Save:");
        autoSaveCheck = createCheckBox("Automatically save progress", true);
        preferencesGrid.add(autoSaveLabel, 0, 4);
        preferencesGrid.add(autoSaveCheck, 1, 4);
        
        preferencesSection.getChildren().add(preferencesGrid);
        root.getChildren().add(preferencesSection);
    }
    
    private void createSystemSection() {
        VBox systemSection = createSectionCard("🔧 System Configuration", 
            "Configure learning parameters and reward systems");
        
        GridPane systemGrid = new GridPane();
        systemGrid.setHgap(16);
        systemGrid.setVgap(16);
        systemGrid.setPadding(new Insets(16));
        
        // Difficulty level
        Label difficultyLabel = createFieldLabel("Default Difficulty:");
        difficultyLevelCombo = createComboBox(List.of("Easy", "Medium", "Hard", "Adaptive"));
        difficultyLevelCombo.setValue("Medium");
        systemGrid.add(difficultyLabel, 0, 0);
        systemGrid.add(difficultyLevelCombo, 1, 0);
        
        // Max daily tasks
        Label maxTasksLabel = createFieldLabel("Max Daily Tasks:");
        maxDailyTasksField = createTextField("10");
        maxDailyTasksField.setTooltip(new Tooltip("Maximum number of tasks adventurers can complete per day"));
        systemGrid.add(maxTasksLabel, 0, 1);
        systemGrid.add(maxDailyTasksField, 1, 1);
        
        // Coin reward multiplier
        Label multiplierLabel = createFieldLabel("Coin Multiplier:");
        coinRewardMultiplierField = createTextField("1.0");
        coinRewardMultiplierField.setTooltip(new Tooltip("Multiplier for SmartCoin rewards (1.0 = normal, 2.0 = double)"));
        systemGrid.add(multiplierLabel, 0, 2);
        systemGrid.add(coinRewardMultiplierField, 1, 2);
        
        systemSection.getChildren().add(systemGrid);
        root.getChildren().add(systemSection);
    }
    
    private void createActionButtons() {
        HBox buttonRow = new HBox(16);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(16));
        
        Button saveButton = createActionButton("💾 Save Changes", "#4CAF50", this::saveSettings);
        Button resetButton = createActionButton("🔄 Reset to Defaults", "#FA8A00", this::resetSettings);
        Button exportButton = createActionButton("📤 Export Data", "#2196F3", this::exportData);
        
        buttonRow.getChildren().addAll(saveButton, resetButton, exportButton);
        root.getChildren().add(buttonRow);
    }
    
    private VBox createSectionCard(String title, String description) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-border-color: rgba(255, 255, 255, 0.4);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        
        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label descLabel = new Label(description);
        descLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        descLabel.setWrapText(true);
        
        header.getChildren().addAll(titleLabel, descLabel);
        card.getChildren().add(header);
        
        return card;
    }
    
    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        label.setPrefWidth(150);
        return label;
    }
    
    private TextField createTextField(String initialValue) {
        TextField field = new TextField(initialValue);
        field.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        field.setPrefWidth(250);
        return field;
    }
    
    private ComboBox<String> createComboBox(List<String> items) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #4CAF50;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        combo.setPrefWidth(250);
        return combo;
    }
    
    private CheckBox createCheckBox(String text, boolean selected) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setSelected(selected);
        checkBox.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        return checkBox;
    }
    
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(120);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    private HBox createAdventurerItem(User child) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(8));
        item.setStyle(
            "-fx-background-color: rgba(76, 175, 80, 0.1);" +
            "-fx-background-radius: 8;"
        );
        
        Label nameLabel = new Label("🧙 " + child.getName());
        nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #333333;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label coinsLabel = new Label(child.getSmartCoinBalance() + " coins");
        coinsLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        item.getChildren().addAll(nameLabel, spacer, coinsLabel);
        return item;
    }
    
    private Button createActionButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        button.setPrefWidth(150);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);"
        );
        
        button.setOnAction(e -> action.run());
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
        
        return button;
    }
    
    private void loadUserData() {
        // Load user-specific settings from Firebase or use defaults
        // This would typically load from a user preferences collection
        // For now, we'll use the current user data and children list
    }
    
    private int getActiveAdventurersCount() {
        int count = 0;
        for (User child : children) {
            if (child.getSmartCoinBalance() > 0 || child.getDailyStreaks() > 0) {
                count++;
            }
        }
        return count;
    }
    
    private int getTotalFamilyCoins() {
        int total = 0;
        for (User child : children) {
            total += child.getSmartCoinBalance();
        }
        return total;
    }
    
    private void saveSettings() {
        // Save settings to Firebase
        System.out.println("💾 Saving settings...");
        
        // Update user information
        if (!nameField.getText().trim().isEmpty()) {
            currentUser.setName(nameField.getText().trim());
        }
        
        if (!emailField.getText().trim().isEmpty()) {
            currentUser.setEmail(emailField.getText().trim());
        }
        
        // Save user data
        firebaseService.saveUser(currentUser);
        
        System.out.println("✅ Settings saved successfully!");
    }
    
    private void resetSettings() {
        // Reset all settings to defaults
        System.out.println("🔄 Resetting settings to defaults...");
        
        themeCombo.setValue("Default");
        languageCombo.setValue("English");
        notificationsCheck.setSelected(true);
        soundEffectsCheck.setSelected(true);
        autoSaveCheck.setSelected(true);
        difficultyLevelCombo.setValue("Medium");
        maxDailyTasksField.setText("10");
        coinRewardMultiplierField.setText("1.0");
        
        System.out.println("✅ Settings reset to defaults!");
    }
    
    private void exportData() {
        // Export family data
        System.out.println("📤 Exporting family data...");
        
        // This would typically generate a JSON or CSV file with family data
        System.out.println("✅ Data export completed!");
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public void refreshData(List<User> updatedChildren) {
        this.children = updatedChildren != null ? updatedChildren : new ArrayList<>();
        // Refresh the UI with updated data
        initializeUI();
    }
}
