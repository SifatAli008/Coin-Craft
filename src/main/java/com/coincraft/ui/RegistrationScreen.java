package com.coincraft.ui;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.coincraft.audio.CentralizedMusicManager;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.util.Fonts;
import com.coincraft.ui.util.ImageCache;

import animatefx.animation.FadeIn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Registration screen for new CoinCraft adventurers
 */
public class RegistrationScreen {
    private static final Logger LOGGER = Logger.getLogger(RegistrationScreen.class.getName());
    
    private StackPane root;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button signUpButton;
    private Label statusLabel;
    
    private final RegistrationCallback callback;
    
    public interface RegistrationCallback {
        void onRegistrationSuccess(User user);
        void onRegistrationFailed(String error);
        void onBackToLogin();
    }
    
    public RegistrationScreen(RegistrationCallback callback) {
        this.callback = callback;
        initializeUI();
    }
    
    public Parent getRoot() {
        return root;
    }
    
    private void initializeUI() {
        root = new StackPane();
        root.setPadding(new Insets(0));
        root.setAlignment(Pos.CENTER);
        
        // Load fonts once
        Fonts.ensureLoaded();
        
        // Animated background via ImageView (more efficient than CSS) with safe fallback
        ImageView bg;
        {
            var cached = ImageCache.get(
                "/images/588a44195922117.66168b374ece8-ezgif.com-webp-to-gif-converter.gif"
            );
            if (cached != null) {
                bg = new ImageView(cached);
                bg.setPreserveRatio(false);
                bg.setSmooth(true);
                bg.setCache(true);
                bg.setFitWidth(1200);
                bg.setFitHeight(800);
                bg.fitWidthProperty().bind(root.widthProperty());
                bg.fitHeightProperty().bind(root.heightProperty());
            } else {
                bg = new ImageView();
                var res = getClass().getResource("/images/588a44195922117.66168b374ece8-ezgif.com-webp-to-gif-converter.gif");
                if (res != null) {
                    root.setStyle(
                        "-fx-background-image: url('" + res.toExternalForm() + "');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
                    );
                } else {
                    root.setStyle("-fx-background-color: linear-gradient(to bottom, #111827, #1f2937);");
                }
            }
        }
        
        // Add semi-transparent dark overlay
        Region darkOverlay = new Region();
        darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        darkOverlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMouseTransparent(true); // Allow clicks to pass through
        
        // Create main registration card with status label container
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setMaxWidth(650);
        
        VBox registrationCard = createRegistrationCard();
        
        // Status label (high-contrast over animated background)
        statusLabel = new Label();
        statusLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-background-color: rgba(0,0,0,0.55);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-font-weight: 600;"
        );
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(620);
        statusLabel.setAlignment(Pos.CENTER);
        
        centerContainer.getChildren().addAll(registrationCard, statusLabel);
        
        root.getChildren().addAll(bg, darkOverlay, centerContainer);
        try { new FadeIn(root).play(); } catch (Throwable ignored) {}
        
        // Ensure single background music instance
        // Music will be started when dashboard loads
    }
    
    private VBox createRegistrationCard() {
        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setPrefWidth(650);
        card.setMaxWidth(650);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.75);" +
            "-fx-background-radius: 16;" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );
        
        // Header section
        VBox header = createHeader();
        
        // Form section
        VBox form = createForm();
        
        // Back to login section
        HBox backSection = createBackSection();
        
        card.getChildren().addAll(header, form, backSection);
        
        return card;
    }
    
    private VBox createHeader() {
        VBox titleSection = new VBox(12);
        titleSection.setAlignment(Pos.CENTER);
        
        Label gameLabel = new Label("Welcome to CoinCraft");
        gameLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-family: 'Minecraft', 'Courier New', monospace;"
        );
        
        Label titleLabel = new Label("🏰 Join the Adventure!");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Create your account and start learning about money!");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMaxWidth(580);
        
        titleSection.getChildren().addAll(gameLabel, titleLabel, subtitleLabel);
        return titleSection;
    }
    
    private VBox createForm() {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);
        
        // First row: Name and Email
        HBox firstRow = new HBox(20);
        firstRow.setAlignment(Pos.CENTER);
        
        // Name section
        VBox nameSection = new VBox(8);
        nameSection.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setPrefWidth(280);
        nameField.setPrefHeight(48);
        nameField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        nameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                CentralizedMusicManager.getInstance().playButtonHover();
                nameField.setStyle(nameField.getStyle() + "-fx-border-color: #FA8A00; -fx-border-width: 2;");
            } else {
                nameField.setStyle(nameField.getStyle().replace("-fx-border-color: #FA8A00; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        nameSection.getChildren().addAll(nameLabel, nameField);
        
        // Email section
        VBox emailSection = new VBox(8);
        emailSection.setAlignment(Pos.CENTER_LEFT);
        
        Label emailLabel = new Label("Email");
        emailLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefWidth(280);
        emailField.setPrefHeight(48);
        emailField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                CentralizedMusicManager.getInstance().playButtonHover();
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: #FA8A00; -fx-border-width: 2;");
            } else {
                emailField.setStyle(emailField.getStyle().replace("-fx-border-color: #FA8A00; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Add name and email to first row
        firstRow.getChildren().addAll(nameSection, emailSection);
        
        
        // Password section
        VBox passwordSection = new VBox(8);
        passwordSection.setAlignment(Pos.CENTER_LEFT);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Create a strong password");
        passwordField.setPrefWidth(280);
        passwordField.setPrefHeight(48);
        passwordField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                CentralizedMusicManager.getInstance().playButtonHover();
                passwordField.setStyle(passwordField.getStyle() + "-fx-border-color: #FA8A00; -fx-border-width: 2;");
            } else {
                passwordField.setStyle(passwordField.getStyle().replace("-fx-border-color: #FA8A00; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        passwordSection.getChildren().addAll(passwordLabel, passwordField);
        
        // Confirm password section
        VBox confirmPasswordSection = new VBox(8);
        confirmPasswordSection.setAlignment(Pos.CENTER_LEFT);
        
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setPrefWidth(280);
        confirmPasswordField.setPrefHeight(48);
        confirmPasswordField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        confirmPasswordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                CentralizedMusicManager.getInstance().playButtonHover();
                confirmPasswordField.setStyle(confirmPasswordField.getStyle() + "-fx-border-color: #FA8A00; -fx-border-width: 2;");
            } else {
                confirmPasswordField.setStyle(confirmPasswordField.getStyle().replace("-fx-border-color: #FA8A00; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        confirmPasswordSection.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
        
        // Third row: Password fields
        HBox thirdRow = new HBox(20);
        thirdRow.setAlignment(Pos.CENTER);
        thirdRow.getChildren().addAll(passwordSection, confirmPasswordSection);
        
        // Registration button
        signUpButton = new Button("⚔️ Create your merchant ID");
        signUpButton.setPrefWidth(580);
        signUpButton.setPrefHeight(48);
        signUpButton.setStyle(
            "-fx-background-color: #FA8A00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
        );
        signUpButton.setOnMouseEntered(e -> {
            CentralizedMusicManager.getInstance().playButtonHover();
            signUpButton.setStyle(
                "-fx-background-color: #E67E00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(46,125,50,0.8), 16, 0, 0, 4);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        signUpButton.setOnMouseExited(e -> {
            signUpButton.setStyle(
                "-fx-background-color: #FA8A00;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.4), 8, 0, 0, 2);"
            );
        });
        signUpButton.setOnAction(e -> {
            CentralizedMusicManager.getInstance().playButtonClick();
            handleRegistration();
        });
        
        form.getChildren().addAll(firstRow, thirdRow, signUpButton);
        
        return form;
    }
    
    private HBox createBackSection() {
        HBox backSection = new HBox(4);
        backSection.setAlignment(Pos.CENTER);
        
        Label alreadyHaveAccountLabel = new Label("Already have an account?");
        alreadyHaveAccountLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label backToLoginLabel = new Label("Sign in");
        backToLoginLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FA8A00;" +
            "-fx-font-weight: 700;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        backToLoginLabel.setOnMouseClicked(e -> {
            CentralizedMusicManager.getInstance().playButtonClick();
            if (callback != null) {
                callback.onBackToLogin();
            }
        });
        
        backSection.getChildren().addAll(alreadyHaveAccountLabel, backToLoginLabel);
        
        return backSection;
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            CentralizedMusicManager.getInstance().playError();
            showStatus("❌ Please fill in all fields", false);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            CentralizedMusicManager.getInstance().playError();
            showStatus("❌ Please enter a valid email address", false);
            return;
        }
        
        if (password.length() < 6) {
            CentralizedMusicManager.getInstance().playError();
            showStatus("❌ Password must be at least 6 characters long", false);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            CentralizedMusicManager.getInstance().playError();
            showStatus("❌ Passwords do not match", false);
            return;
        }
        
        showStatus("🏰 Creating your merchant profile...", true);
        signUpButton.setText("⚡ Creating...");
        signUpButton.setDisable(true);
        
        // Create user in background thread
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                
                Platform.runLater(() -> {
                    try {
                        // Default age and role for parent registration
                        int age = 25; // Default adult age
                        UserRole userRole = UserRole.PARENT; // All registrations are for parents
                        
                        // Create new user with proper role validation
                        User newUser = new User("user_" + System.currentTimeMillis(), name, userRole, age);
                        newUser.setEmail(email);
                        newUser.setSmartCoinBalance(userRole == UserRole.CHILD ? 25 : 100); // Different starting amounts
                        newUser.setLevel(1);
                        newUser.setDailyStreaks(0);
                        newUser.setLastLogin(LocalDateTime.now());
                        
                        // Register user in Firebase/Mock registry
                        FirebaseService firebaseService = FirebaseService.getInstance();
                        boolean registrationSuccess = firebaseService.registerMockUser(email, password, newUser);
                        
                        if (!registrationSuccess) {
                            throw new Exception("Failed to register user in system");
                        }
                        
                        CentralizedMusicManager.getInstance().playSuccess();
                        showStatus("🎉 Welcome to CoinCraft, " + name + "! Your adventure begins now!", true);
                        
                        // Small delay before transitioning
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
                            if (callback != null) {
                                callback.onRegistrationSuccess(newUser);
                            }
                        }));
                        timeline.play();
                        
                    } catch (Exception e) {
                        signUpButton.setDisable(false);
                        signUpButton.setText("⚔️ Create your merchant ID");
                        CentralizedMusicManager.getInstance().playError();
                        showStatus("❌ Failed to create account: " + e.getMessage(), false);
                        LOGGER.severe(() -> "Registration error: " + e.getMessage());
                    }
                });
            } catch (InterruptedException e) {
                Platform.runLater(() -> {
                    signUpButton.setDisable(false);
                    signUpButton.setText("⚔️ Create adventurer");
                    showStatus("❌ Account creation cancelled", false);
                });
            }
        }).start();
    }
    
    
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        String base =
            "-fx-font-size: 14px;" +
            "-fx-padding: 8 12;" +
            "-fx-background-color: rgba(0,0,0,0.55);" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-font-weight: 600;";
        String color = isSuccess ? "#10b981" : "#FFE082"; // green for success, amber for errors
        statusLabel.setStyle(base + "-fx-text-fill: " + color + ";");
    }
}
