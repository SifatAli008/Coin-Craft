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
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> roleSelector;
    private Button loginButton;
    private Button googleSignInButton;
    private Label statusLabel;
    
    // Additional UI references for dynamic updates
    private HBox dividerElement;
    private Label emailLabel;
    private Label signupLabel;
    private boolean isNavigatingToSignUp = false;
    
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
        String backgroundImage = getClass().getResource("/images/588a44195922117.66168b374ece8-ezgif.com-webp-to-gif-converter.gif").toExternalForm();
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
        
        // Ensure single background music instance
        SoundManager.getInstance().ensureSingleMusicInstance();
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
        Label emailLabel = new Label("Email / Adventure ID");
        emailLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        emailField = new TextField();
        emailField.setPromptText("Enter email or Adventure ID");
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
        
        // Role selector
        VBox roleSection = createRoleSelector();
        
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
        googleSignInButton = new Button("🔐 SIGN IN WITH GOOGLE");
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
        
        signupLabel = new Label("Sign up");
        signupLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: 700;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        signupLabel.setOnMouseClicked(e -> {
            if (isNavigatingToSignUp) {
                return; // Prevent multiple rapid clicks
            }
            
            SoundManager.getInstance().playButtonClick();
            System.out.println("Navigate to sign up requested");
            
            if (callback != null) {
                isNavigatingToSignUp = true;
                signupLabel.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #CCCCCC;" +
                    "-fx-font-weight: 700;" +
                    "-fx-underline: true;" +
                    "-fx-cursor: default;" +
                    "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
                );
                
                // Add a small delay to prevent rapid clicking
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), ev -> {
                    callback.onNavigateToSignUp();
                }));
                timeline.play();
            }
        });
        
        signupSection.getChildren().addAll(noAccountLabel, signupLabel);
        
        form.getChildren().addAll(emailSection, passwordSection, roleSection, loginButton, divider, googleSignInButton);
        card.getChildren().addAll(header, form, signupSection);
        
        // Store references to UI elements for dynamic updates
        this.dividerElement = divider;
        this.emailLabel = emailLabel;
        
        // Initialize UI based on default role selection
        updateUIForSelectedRole();
        
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
        
        // Check if admin role is selected and handle admin authentication
        UserRole selectedRole = getSelectedUserRole();
        if (selectedRole == UserRole.ADMIN) {
            handleAdminLogin(email, password);
            return;
        }
        
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
                            // Set the user role based on selection (override Firebase data for demo)
                            user.setRole(selectedRole);
                            
                            // Adjust user properties based on role
                            if (selectedRole == UserRole.PARENT) {
                                user.setName(user.getName() + " (Merchant)");
                                if (user.getSmartCoinBalance() < 100) {
                                    user.setSmartCoinBalance(500); // Merchants start with more
                                }
                            } else if (selectedRole == UserRole.TEACHER) {
                                user.setName(user.getName() + " (Teacher)");
                                if (user.getSmartCoinBalance() < 50) {
                                    user.setSmartCoinBalance(200);
                                }
                            } else if (selectedRole == UserRole.ADMIN) {
                                user.setName(user.getName() + " (Admin)");
                                user.setSmartCoinBalance(1000);
                            }
                            
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
    
    private void handleAdminLogin(String usernameOrEmail, String password) {
        System.out.println("Admin login attempt - Username/Email: " + usernameOrEmail + ", Password: " + password);
        
        // Admin credentials - supports multiple authentication methods
        final String ADMIN_USERNAME = "Admin";
        final String ADMIN_PASSWORD = "Admin";
        final String ADMIN_EMAIL = "admin@coincraft.com";
        final String ADMIN_EMAIL_PASSWORD = "admin123";
        
        boolean isValidAdmin = false;
        
        // Method 1: Username/Password
        if (ADMIN_USERNAME.equals(usernameOrEmail) && ADMIN_PASSWORD.equals(password)) {
            isValidAdmin = true;
            System.out.println("Admin authenticated via Method 1: Username/Password");
        }
        // Method 2: Email/Password
        else if (ADMIN_EMAIL.equals(usernameOrEmail) && ADMIN_EMAIL_PASSWORD.equals(password)) {
            isValidAdmin = true;
            System.out.println("Admin authenticated via Method 2: Email/Password");
        }
        // Method 3: Email/Admin Password (for flexibility)
        else if (ADMIN_EMAIL.equals(usernameOrEmail) && ADMIN_PASSWORD.equals(password)) {
            isValidAdmin = true;
            System.out.println("Admin authenticated via Method 3: Email/Admin Password");
        }
        // Method 4: Gmail domains with admin password
        else if (usernameOrEmail.toLowerCase().endsWith("@gmail.com") && ADMIN_PASSWORD.equals(password)) {
            isValidAdmin = true;
            System.out.println("Admin authenticated via Method 4: Gmail");
        }
        // Method 5: Any email ending with @coincraft.com
        else if (usernameOrEmail.toLowerCase().endsWith("@coincraft.com") && 
                (ADMIN_PASSWORD.equals(password) || ADMIN_EMAIL_PASSWORD.equals(password))) {
            isValidAdmin = true;
            System.out.println("Admin authenticated via Method 5: Company email");
        }
        
        // Reset button state
        loginButton.setDisable(false);
        loginButton.setText("🚀 START ADVENTURE");
        
        if (isValidAdmin) {
            System.out.println("Admin authentication successful, creating admin user...");
            // Create admin user
            User adminUser = new User();
            adminUser.setUserId("admin-001");
            adminUser.setName("Administrator");
            adminUser.setEmail(usernameOrEmail.contains("@") ? usernameOrEmail : ADMIN_EMAIL);
            adminUser.setRole(UserRole.ADMIN);
            adminUser.setAge(30);
            adminUser.setSmartCoinBalance(1000);
            adminUser.setLevel(10); // Max level for admin
            adminUser.setLastLogin(java.time.LocalDateTime.now());
            
            System.out.println("Admin user created: " + adminUser.getName() + " with role: " + adminUser.getRole());
            
            SoundManager.getInstance().playAdventureStart();
            showStatus("🎉 Admin access granted! Welcome, Administrator!", true);
            
            // Small delay for better UX before transitioning
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                System.out.println("Calling login success callback for admin user...");
                if (callback != null) {
                    callback.onLoginSuccess(adminUser);
                } else {
                    System.err.println("ERROR: Login callback is null!");
                }
            }));
            timeline.play();
        } else {
            System.out.println("Admin authentication failed for: " + usernameOrEmail + "/" + password);
            SoundManager.getInstance().playError();
            showStatus("❌ Invalid admin credentials. Try: Admin/Admin or admin@coincraft.com/admin123", false);
        }
    }
    
    private VBox createRoleSelector() {
        VBox roleSection = new VBox(8);
        
        Label roleLabel = new Label("I am a:");
        roleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        roleSelector = new ComboBox<>();
        roleSelector.getItems().addAll("Adventurer (Child)", "Merchant (Parent)", "Teacher", "Admin");
        roleSelector.setValue("Merchant (Parent)"); // Default to Merchant for easier testing
        roleSelector.setPrefWidth(340);
        roleSelector.setPrefHeight(40);
        roleSelector.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.9);" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-border-color: rgba(255, 255, 255, 0.8);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        // Add change listener for role selection
        roleSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Role selection changed - update UI based on role
            updateUIForSelectedRole();
        });
        
        roleSection.getChildren().addAll(roleLabel, roleSelector);
        return roleSection;
    }
    
    private UserRole getSelectedUserRole() {
        String selected = roleSelector.getValue();
        switch (selected) {
            case "Merchant (Parent)":
                return UserRole.PARENT;
            case "Teacher":
                return UserRole.TEACHER;
            case "Admin":
                return UserRole.ADMIN;
            case "Adventurer (Child)":
            default:
                return UserRole.CHILD;
        }
    }
    
    /**
     * Updates the UI elements based on the selected role
     * Adventurers (Children) should only see username/password login
     * Parents, Teachers, and Admins can use Google sign-in
     */
    private void updateUIForSelectedRole() {
        UserRole selectedRole = getSelectedUserRole();
        
        if (selectedRole == UserRole.CHILD) {
            // For adventurers (children): hide Google sign-in, update labels
            emailLabel.setText("Adventure ID");
            emailField.setPromptText("Enter your Adventure ID");
            
            // Hide Google sign-in option for children
            dividerElement.setVisible(false);
            dividerElement.setManaged(false);
            googleSignInButton.setVisible(false);
            googleSignInButton.setManaged(false);
            
        } else {
            // For parents, teachers, admins: show Google sign-in, update labels
            emailLabel.setText("Email");
            emailField.setPromptText("Enter your email address");
            
            // Show Google sign-in option for adults
            dividerElement.setVisible(true);
            dividerElement.setManaged(true);
            googleSignInButton.setVisible(true);
            googleSignInButton.setManaged(true);
        }
    }
    
    private void handleGoogleSignIn() {
        UserRole selectedRole = getSelectedUserRole();
        
        // Prevent children from using Google sign-in
        if (selectedRole == UserRole.CHILD) {
            SoundManager.getInstance().playError();
            showStatus("❌ Adventurers must use Adventure ID and password to log in!", false);
            return;
        }
        
        showStatus("Signing in with Google...", true);
        googleSignInButton.setDisable(true);
        
        // Simulate Google OAuth flow (in a real app, this would open browser/WebView)
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate network delay
                
                Platform.runLater(() -> {
                    try {
                        // Create Google user based on selected role
                        User googleUser;
                        
                        if (selectedRole == UserRole.PARENT) {
                            // Create merchant user with real Google account name
                            googleUser = new User("google_merchant_123", "John Smith", UserRole.PARENT, 1);
                            googleUser.setEmail("john.smith@gmail.com");
                            googleUser.setSmartCoinBalance(500); // Merchants have different balance
                            googleUser.setLevel(1); // Merchants don't have levels like adventurers
                            googleUser.setLastLogin(java.time.LocalDateTime.now());
                        } else if (selectedRole == UserRole.TEACHER) {
                            // Create teacher user
                            googleUser = new User("google_teacher_123", "Ms. Smith", UserRole.TEACHER, 1);
                            googleUser.setEmail("teacher@gmail.com");
                            googleUser.setSmartCoinBalance(100);
                            googleUser.setLevel(1);
                            googleUser.setLastLogin(java.time.LocalDateTime.now());
                        } else if (selectedRole == UserRole.ADMIN) {
                            // Create admin user
                            googleUser = new User("google_admin_123", "Admin User", UserRole.ADMIN, 1);
                            googleUser.setEmail("admin@gmail.com");
                            googleUser.setSmartCoinBalance(1000);
                            googleUser.setLevel(1);
                            googleUser.setLastLogin(java.time.LocalDateTime.now());
                        } else {
                            // This should never happen since we block children from Google sign-in
                            throw new IllegalStateException("Children cannot use Google authentication");
                        }
                        
                        SoundManager.getInstance().playSuccess();
                        String roleMessage = selectedRole == UserRole.PARENT ? "merchant" : 
                                           selectedRole == UserRole.CHILD ? "adventurer" : selectedRole.name().toLowerCase();
                        showStatus("Google sign-in successful! Welcome " + roleMessage + "!", true);
                        
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