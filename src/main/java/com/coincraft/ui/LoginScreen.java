package com.coincraft.ui;

import java.util.logging.Logger;

import com.coincraft.audio.SoundManager;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Modern hackday-style login screen based on sprightly-platypus-6aa71e.netlify.app
 */
public class LoginScreen {
    private static final Logger LOGGER = Logger.getLogger(LoginScreen.class.getName());
    
    private StackPane root;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button googleSignInButton;
    private Label statusLabel;
    
    private final LoginCallback callback;
    
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailed(String error);
        void onNavigateToSignUp();
    }
    
    public LoginScreen(LoginCallback callback) {
        this.callback = callback;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new StackPane();
        root.setPadding(new Insets(0));
        root.setAlignment(Pos.CENTER);
        
        // Load Minecraft font
        try {
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 16);
            Font.loadFont(getClass().getResourceAsStream("/Fonts/minecraft/Minecraft.ttf"), 18);
        } catch (Exception e) {
            System.out.println("Could not load Minecraft font: " + e.getMessage());
        }
        
        // Set animated GIF background
        String backgroundImage = getClass().getResource("/images/bd565dcc0a556add0b0a0ed6b26d686e.gif").toExternalForm();
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
        
        // Create main login card with status label container
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setMaxWidth(420);
        
        VBox loginCard = createLoginCard();
        
        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 0 0 0;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);
        statusLabel.setAlignment(Pos.CENTER);
        
        centerContainer.getChildren().addAll(loginCard, statusLabel);
        
        root.getChildren().addAll(darkOverlay, centerContainer);
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
        
        Label gameLabel = new Label("Wealcome to Coincraft");
        gameLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Courier New', monospace;"
        );
        
        Label titleLabel = new Label("CoinCraft");
        titleLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Level up your money skills");
        subtitleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                SoundManager.getInstance().playInputClick();
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                emailField.setStyle(emailField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Password field
        VBox passwordSection = new VBox(8);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                SoundManager.getInstance().playInputClick();
                passwordField.setStyle(passwordField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                passwordField.setStyle(passwordField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        // Forgot password link
        Label forgotLabel = new Label("Forgot password?");
        forgotLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-padding: 8 0 0 0;"
        );
        forgotLabel.setOnMouseClicked(e -> showStatus("Password reset coming soon!", true));
        
        HBox forgotContainer = new HBox();
        forgotContainer.setAlignment(Pos.CENTER_RIGHT);
        forgotContainer.getChildren().add(forgotLabel);
        
        passwordSection.getChildren().addAll(passwordLabel, passwordField, forgotContainer);
        
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
        );
        loginButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            loginButton.setStyle(
                "-fx-background-color: #000000;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 700;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
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
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 8, 0, 0, 2);"
            );
        });
        loginButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleLogin();
        });
        
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
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Region rightLine = new Region();
        rightLine.setPrefHeight(1);
        rightLine.setStyle("-fx-background-color: #d1d5db;");
        HBox.setHgrow(rightLine, Priority.ALWAYS);
        
        divider.getChildren().addAll(leftLine, orLabel, rightLine);
        
        // Google Sign-in button
        googleSignInButton = new Button("🔍 SIGN IN WITH GOOGLE");
        googleSignInButton.setPrefWidth(340);
        googleSignInButton.setPrefHeight(48);
        googleSignInButton.setStyle(
            "-fx-background-color: #4285f4;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-effect: dropshadow(gaussian, rgba(66,133,244,0.4), 8, 0, 0, 2);"
        );
        googleSignInButton.setOnMouseEntered(e -> {
            SoundManager.getInstance().playButtonHover();
            googleSignInButton.setStyle(
                "-fx-background-color: #3367d6;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(51,103,214,0.8), 16, 0, 0, 4);" +
                "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"
            );
        });
        googleSignInButton.setOnMouseExited(e -> {
            googleSignInButton.setStyle(
                "-fx-background-color: #4285f4;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: 600;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
                "-fx-effect: dropshadow(gaussian, rgba(66,133,244,0.4), 8, 0, 0, 2);"
            );
        });
        googleSignInButton.setOnAction(e -> {
            SoundManager.getInstance().playButtonClick();
            handleGoogleSignIn();
        });
        
        // Sign up link
        HBox signupSection = new HBox(4);
        signupSection.setAlignment(Pos.CENTER);
        
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label signupLabel = new Label("Sign up");
        signupLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: 700;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        signupLabel.setOnMouseClicked(e -> {
            SoundManager.getInstance().playButtonClick();
            if (callback != null) {
                callback.onNavigateToSignUp();
            }
        });
        
        signupSection.getChildren().addAll(noAccountLabel, signupLabel);
        
        form.getChildren().addAll(emailSection, passwordSection, loginButton, divider, googleSignInButton);
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
        
        showStatus("🚀 Starting your adventure...", true);
        loginButton.setText("🌟 LAUNCHING...");
        loginButton.setDisable(true);
        
        // Authenticate in background thread
        new Thread(() -> {
            try {
                FirebaseService firebaseService = FirebaseService.getInstance();
                String userId = firebaseService.authenticateUser(email, password);
                
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    loginButton.setText("🚀 START ADVENTURE");
                    
                    if (userId != null) {
                        // Load user data
                        User user = firebaseService.loadUser(userId);
                        if (user != null) {
                            SoundManager.getInstance().playAdventureStart();
                            showStatus("🎉 Adventure started! Welcome, " + user.getName() + "!", true);
                            
                            // Small delay for better UX before transitioning
                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                                if (callback != null) {
                                    callback.onLoginSuccess(user);
                                }
                            }));
                            timeline.play();
                        } else {
                            SoundManager.getInstance().playError();
                            showStatus("❌ Could not load your profile. Please try again.", false);
                        }
                    } else {
                        SoundManager.getInstance().playError();
                        showStatus("❌ Invalid credentials. Check your email and password.", false);
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    loginButton.setText("🚀 START ADVENTURE");
                    SoundManager.getInstance().playError();
                    showStatus("⚠️ Connection error: " + e.getMessage(), false);
                    LOGGER.severe(() -> "Login error: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void handleGoogleSignIn() {
        showStatus("Signing in with Google...", true);
        googleSignInButton.setDisable(true);
        
        // Simulate Google OAuth flow (in a real app, this would open browser/WebView)
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                
                Platform.runLater(() -> {
                    try {
                        // For now, create a demo Google user
                        // In production, this would handle actual Google OAuth response
                        User googleUser = new User("google_user_123", "Google User", UserRole.CHILD, 10);
                        googleUser.setEmail("user@gmail.com");
                        googleUser.setSmartCoinBalance(50);
                        googleUser.setLevel(1);
                        googleUser.setDailyStreaks(1);
                        googleUser.setLastLogin(java.time.LocalDateTime.now());
                        
                        SoundManager.getInstance().playSuccess();
                        showStatus("Google sign-in successful! Welcome!", true);
                        
                        // Small delay before transitioning
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                            if (callback != null) {
                                callback.onLoginSuccess(googleUser);
                            }
                        }));
                        timeline.play();
                        
                    } catch (Exception e) {
                        googleSignInButton.setDisable(false);
                        SoundManager.getInstance().playError();
                        showStatus("Google sign-in failed: " + e.getMessage(), false);
                        LOGGER.severe(() -> "Google sign-in error: " + e.getMessage());
                    }
                });
            } catch (InterruptedException e) {
                Platform.runLater(() -> {
                    googleSignInButton.setDisable(false);
                    showStatus("Google sign-in cancelled", false);
                });
            }
        }).start();
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