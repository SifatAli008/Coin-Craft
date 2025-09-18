package com.coincraft.ui;

import java.util.logging.Logger;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Modern hackday-style login screen based on sprightly-platypus-6aa71e.netlify.app
 */
public class LoginScreen {
    private static final Logger LOGGER = Logger.getLogger(LoginScreen.class.getName());
    
    private VBox root;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button demoButton;
    private Label statusLabel;
    
    private final LoginCallback callback;
    
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailed(String error);
    }
    
    public LoginScreen(LoginCallback callback) {
        this.callback = callback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(0);
        root.setPadding(new Insets(0));
        root.setAlignment(Pos.CENTER);
        // Load Pixelify Sans font
        try {
            Font.loadFont(getClass().getResourceAsStream("/Fonts/Pixelify_Sans/static/PixelifySans-Regular.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/Fonts/Pixelify_Sans/static/PixelifySans-Bold.ttf"), 14);
        } catch (Exception e) {
            System.out.println("Could not load Pixelify Sans font: " + e.getMessage());
        }
        
        // Set animated GIF background
        String backgroundImage = getClass().getResource("/images/3a8256b391b0de71639848f2815c2b14.gif").toExternalForm();
        root.setStyle(
            "-fx-background-image: url('" + backgroundImage + "');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center;" +
            "-fx-background-repeat: no-repeat;"
        );
        
        // Add semi-transparent dark overlay
        Region darkOverlay = new Region();
        darkOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        darkOverlay.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        darkOverlay.setMouseTransparent(true); // Allow clicks to pass through
        
        // Create main login card
        VBox loginCard = createLoginCard();
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 20 0 0 0;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);
        statusLabel.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(darkOverlay, loginCard, statusLabel);
        try { new FadeIn(root).play(); } catch (Throwable ignored) {}
    }
    
    private VBox createLoginCard() {
        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setPrefWidth(420);
        card.setMaxWidth(420);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.75);" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10);" +
            "-fx-border-color: rgba(255, 255, 255, 0.6);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 20;"
        );
        
        // Header section
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        
        // Gaming-style title with pixel accent
        VBox titleSection = new VBox(4);
        titleSection.setAlignment(Pos.CENTER);
        
        Label gameLabel = new Label("🏰 COINCRAFT 💰");
        gameLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Pixelify Sans', 'Courier New', monospace;"
        );
        
        Label titleLabel = new Label("Welcome to Treasure Town");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Begin your financial adventure • Level up your money skills");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        titleSection.getChildren().addAll(gameLabel, titleLabel, subtitleLabel);
        
        header.getChildren().add(titleSection);
        
        // Form section
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);
        
        // Email field
        VBox emailSection = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(340);
        emailField.setPrefHeight(48);
        emailField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                emailField.setStyle(emailField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Password field
        VBox passwordSection = new VBox(8);
        
        HBox passwordHeader = new HBox();
        passwordHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label forgotLabel = new Label("Forgot password?");
        forgotLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        forgotLabel.setOnMouseClicked(e -> showStatus("Password reset coming soon!", true));
        
        passwordHeader.getChildren().addAll(passwordLabel, spacer, forgotLabel);
        
        passwordField = new PasswordField();
        passwordField.setPrefWidth(340);
        passwordField.setPrefHeight(48);
        passwordField.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #000000;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 12 16;" +
            "-fx-font-size: 14px;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle(passwordField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                passwordField.setStyle(passwordField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        passwordSection.getChildren().addAll(passwordHeader, passwordField);
        
        // Gaming-style login button
        loginButton = new Button("🚀 START ADVENTURE");
        loginButton.setPrefWidth(340);
        loginButton.setPrefHeight(48);
        loginButton.setStyle(
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 700;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
        );
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                "-fx-background-color: #000000;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 16, 0, 0, 4);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(
                "-fx-background-color: #FF9800;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
            );
        });
        loginButton.setOnAction(e -> handleLogin());
        
        // Divider
        HBox divider = new HBox(12);
        divider.setAlignment(Pos.CENTER);
        
        Region leftLine = new Region();
        leftLine.setPrefHeight(1);
        leftLine.setStyle("-fx-background-color: #d1d5db;");
        HBox.setHgrow(leftLine, Priority.ALWAYS);
        
        Label orLabel = new Label("OR");
        orLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #6b7280;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        Region rightLine = new Region();
        rightLine.setPrefHeight(1);
        rightLine.setStyle("-fx-background-color: #d1d5db;");
        HBox.setHgrow(rightLine, Priority.ALWAYS);
        
        divider.getChildren().addAll(leftLine, orLabel, rightLine);
        
        // Gaming demo button
        demoButton = new Button("🎮 DEMO MODE");
        demoButton.setPrefWidth(340);
        demoButton.setPrefHeight(48);
        demoButton.setStyle(
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
        );
        demoButton.setOnMouseEntered(e -> {
            demoButton.setStyle(
                "-fx-background-color: #000000;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 16, 0, 0, 4);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        demoButton.setOnMouseExited(e -> {
            demoButton.setStyle(
                "-fx-background-color: #FF9800;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
            );
        });
        demoButton.setOnAction(e -> handleDemoLogin());
        
        // Sign up link
        HBox signupSection = new HBox(4);
        signupSection.setAlignment(Pos.CENTER);
        
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        
        Label signupLabel = new Label("Sign up");
        signupLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: 600;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Pixelify Sans', 'Segoe UI', sans-serif;"
        );
        signupLabel.setOnMouseClicked(e -> showStatus("Registration coming soon!", true));
        
        signupSection.getChildren().addAll(noAccountLabel, signupLabel);
        
        form.getChildren().addAll(emailSection, passwordSection, loginButton, divider, demoButton);
        card.getChildren().addAll(header, form, signupSection);
        
        return card;
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both email and password", false);
            return;
        }
        
        showStatus("Signing in...", true);
        loginButton.setDisable(true);
        
        // Authenticate in background thread
        new Thread(() -> {
            try {
                FirebaseService firebaseService = FirebaseService.getInstance();
                String userId = firebaseService.authenticateUser(email, password);
                
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    
                    if (userId != null) {
                        // Load user data
                        User user = firebaseService.loadUser(userId);
                        if (user != null) {
                            showStatus("Login successful! Welcome back, " + user.getName(), true);
                            if (callback != null) {
                                callback.onLoginSuccess(user);
                            }
                        } else {
                            showStatus("Login failed: Could not load user data", false);
                        }
                    } else {
                        showStatus("Login failed: Invalid email or password", false);
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    showStatus("Login error: " + e.getMessage(), false);
                    LOGGER.severe(() -> "Login error: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void handleDemoLogin() {
        showStatus("Loading demo mode...", true);
        
        // Create demo user
        User demoUser = new User("demo_user", "Money Explorer", UserRole.CHILD, 10);
        demoUser.setSmartCoinBalance(100);
        demoUser.setLevel(2);
        demoUser.setDailyStreaks(3);
        
        if (callback != null) {
            callback.onLoginSuccess(demoUser);
        }
    }
    
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: #10b981;");
        } else {
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: #ef4444;");
        }
    }
    
    public Parent getRoot() {
        return root;
    }
}