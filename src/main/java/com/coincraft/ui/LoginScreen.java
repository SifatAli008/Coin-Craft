package com.coincraft.ui;

import java.util.logging.Logger;

// import com.coincraft.audio.SoundManager; // Removed - using CentralizedMusicManager now
import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;
import com.coincraft.services.GoogleOAuthService;
import com.coincraft.services.FirebaseAuthService;

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
    
    private final LoginCallback callback;
    private GoogleOAuthService googleOAuthService;
    private FirebaseAuthService firebaseAuthService;
    
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailed(String error);
        void onNavigateToParentRegistration();
    }
    
    public LoginScreen(LoginCallback callback) {
        this.callback = callback;
        try {
            this.googleOAuthService = new GoogleOAuthService();
            this.firebaseAuthService = new FirebaseAuthService(new com.coincraft.services.FirebaseConfig());
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize Google OAuth service: " + e.getMessage());
        }
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
        
        // Create main login card with status label container (reduced spacing)
        VBox centerContainer = new VBox(12);
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
        // Music will be started when dashboard loads
    }
    
    private VBox createLoginCard() {
        VBox card = new VBox(16);  // Reduced from 24 to 16
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));  // Reduced from 40 to 30
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
        
        // Header section (reduced spacing)
        VBox header = new VBox(6);  // Reduced from 8 to 6
        header.setAlignment(Pos.CENTER);
        
        // Gaming-style title with pixel accent (reduced spacing)
        VBox titleSection = new VBox(2);  // Reduced from 4 to 2
        titleSection.setAlignment(Pos.CENTER);
        
        Label gameLabel = new Label("Welcome to Coincraft");
        gameLabel.setStyle(
            "-fx-font-size: 14px;" +  // Reduced from 16px to 14px
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-family: 'Minecraft', 'Courier New', monospace;"
        );
        
        Label titleLabel = new Label("CoinCraft");
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +  // Reduced from 28px to 24px
            "-fx-font-weight: 700;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label subtitleLabel = new Label("Level up your money skills");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +  // Reduced from 16px to 14px
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        titleSection.getChildren().addAll(gameLabel, titleLabel, subtitleLabel);
        
        header.getChildren().add(titleSection);
        
        // Form section (reduced spacing)
        VBox form = new VBox(14);  // Reduced from 20 to 14
        form.setAlignment(Pos.CENTER);
        
        // Email field (reduced spacing)
        VBox emailSection = new VBox(6);  // Reduced from 8 to 6
        Label emailLabel = new Label("Email / Adventure Username");
        emailLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        emailField = new TextField();
        emailField.setPromptText("Enter email or Adventure Username");
        emailField.setPrefWidth(340);
        emailField.setPrefHeight(42);  // Reduced from 48 to 42
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
                emailField.setStyle(emailField.getStyle() + "-fx-border-color: #FF9800; -fx-border-width: 2;");
            } else {
                emailField.setStyle(emailField.getStyle().replace("-fx-border-color: #FF9800; -fx-border-width: 2;", "-fx-border-color: #000000; -fx-border-width: 1;"));
            }
        });
        
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Password field (reduced spacing)
        VBox passwordSection = new VBox(6);  // Reduced from 8 to 6
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        passwordField = new PasswordField();
        passwordField.setPrefWidth(340);
        passwordField.setPrefHeight(42);  // Reduced from 48 to 42
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
        loginButton.setPrefHeight(42);  // Reduced from 48 to 42
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
            handleGoogleSignIn();
        });
        
        // Info section for new users
        VBox infoSection = new VBox(12);
        infoSection.setAlignment(Pos.CENTER);
        
        Label infoLabel = new Label("New adventurers: Ask your parent/guardian to create your account from their dashboard");
        infoLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;" +
            "-fx-text-alignment: center;"
        );
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(340);
        
        // Parent registration section
        VBox parentRegSection = new VBox(4);
        parentRegSection.setAlignment(Pos.CENTER);
        
        Label parentLabel = new Label("New parent/guardian?");
        parentLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #666666;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        
        Label parentRegLink = new Label("Create Parent Account");
        parentRegLink.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #4CAF50;" +
            "-fx-font-weight: 700;" +
            "-fx-underline: true;" +
            "-fx-cursor: hand;" +
            "-fx-font-family: 'Minecraft', 'Segoe UI', sans-serif;"
        );
        parentRegLink.setOnMouseClicked(e -> {
            System.out.println("Navigate to parent registration requested");
            
            if (callback != null) {
                callback.onNavigateToParentRegistration();
            }
        });
        
        parentRegSection.getChildren().addAll(parentLabel, parentRegLink);
        infoSection.getChildren().addAll(infoLabel, parentRegSection);
        
        form.getChildren().addAll(emailSection, passwordSection, roleSection, loginButton, divider, googleSignInButton);
        card.getChildren().addAll(header, form, infoSection);
        
        // Store references to UI elements for dynamic updates
        this.dividerElement = divider;
        this.emailLabel = emailLabel;
        
        // Initialize UI based on default role selection
        updateUIForSelectedRole();
        
        return card;
    }
    
    private void handleLogin() {
        String emailOrAdventureId = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (emailOrAdventureId.isEmpty() || password.isEmpty()) {
            UserRole selectedRole = getSelectedUserRole();
            String fieldName = (selectedRole == UserRole.CHILD) ? "Adventure Username and password" : "email and password";
            showStatus("Please enter both " + fieldName, false);
            return;
        }
        
        showStatus("🚀 Starting your adventure...", true);
        loginButton.setText("🌟 LAUNCHING...");
        loginButton.setDisable(true);
        
        // Check if admin role is selected and handle admin authentication
        UserRole selectedRole = getSelectedUserRole();
        if (selectedRole == UserRole.ADMIN) {
            handleAdminLogin(emailOrAdventureId, password);
            return;
        }
        
        // Handle Adventure Username authentication for child users
        final String email;
        final boolean isAdventurerLogin;
        if (selectedRole == UserRole.CHILD) {
            // For adventurers, we'll use direct username authentication
            if (!emailOrAdventureId.contains("@")) {
                isAdventurerLogin = true;
                email = emailOrAdventureId; // Keep original for username auth
                LOGGER.info(() -> "Adventure Username login attempt: " + emailOrAdventureId);
            } else {
                // Still support email format for backwards compatibility
                isAdventurerLogin = false;
                email = emailOrAdventureId.toLowerCase();
            }
        } else {
            isAdventurerLogin = false;
            email = emailOrAdventureId;
        }
        
        // Authenticate in background thread
        new Thread(() -> {
            try {
                FirebaseService firebaseService = FirebaseService.getInstance();
                
                // Ensure Firebase is initialized
                if (!firebaseService.isInitialized()) {
                    LOGGER.info("Firebase not initialized, initializing now...");
                    firebaseService.initialize();
                }
                String userId;
                
                if (isAdventurerLogin) {
                    // Use Adventure Username authentication
                    LOGGER.info("Attempting adventurer login with username: " + email);
                    userId = firebaseService.verifyAdventurerCredentials(email, password);
                    LOGGER.info("Adventurer authentication result - userId: " + (userId != null ? userId : "null"));
                } else {
                    // Use regular email authentication
                    LOGGER.info("Attempting regular email login with email: " + email);
                    userId = firebaseService.authenticateUser(email, password);
                    LOGGER.info("Email authentication result - userId: " + (userId != null ? userId : "null"));
                }
                
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    loginButton.setText("🚀 START ADVENTURE");
                    
                    if (userId != null) {
                        // Load user data
                        LOGGER.info("Loading user data for userId: " + userId);
                        User user = firebaseService.loadUser(userId);
                        LOGGER.info("User load result: " + (user != null ? "SUCCESS - " + user.getName() + " (" + user.getRole() + ")" : "FAILED - user is null"));
                        if (user != null) {
                            // SECURITY: Validate that the selected role matches the user's registered role
                            UserRole userActualRole = user.getRole();
                            
                            if (userActualRole == null) {
                                showStatus("❌ Account not properly registered. Please contact support.", false);
                                return;
                            }
                            
                            // Verify role matches selection (prevent unauthorized role access)
                            if (!userActualRole.equals(selectedRole)) {
                                showStatus("❌ Role mismatch. You are registered as " + userActualRole + 
                                         " but selected " + selectedRole + ". Please select the correct role.", false);
                                return;
                            }
                            
                            // Role-specific setup (only after validation)
                            if (userActualRole == UserRole.PARENT) {
                                user.setName(user.getName() + " (Merchant)");
                                if (user.getSmartCoinBalance() < 100) {
                                    user.setSmartCoinBalance(500); // Merchants start with more
                                }
                            } else if (userActualRole == UserRole.TEACHER) {
                                user.setName(user.getName() + " (Teacher)");
                                if (user.getSmartCoinBalance() < 50) {
                                    user.setSmartCoinBalance(200);
                                }
                            } else if (userActualRole == UserRole.CHILD) {
                                user.setName(user.getName() + " (Adventurer)");
                                // Children keep their existing balance
                            }
                            
                            showStatus("🎉 Adventure started! Welcome, " + user.getName() + "!", true);
                            
                            // Small delay for better UX before transitioning
                            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                                if (callback != null) {
                                    callback.onLoginSuccess(user);
                                }
                            }));
                            timeline.play();
                        } else {
                            String errorMsg = isAdventurerLogin ? 
                                "❌ Could not load adventurer profile. Please try again or contact support." :
                                "❌ Could not load your profile. Please try again.";
                            showStatus(errorMsg, false);
                            LOGGER.warning("Failed to load user profile for userId: " + userId + ", isAdventurerLogin: " + isAdventurerLogin);
                        }
                    } else {
                        String errorMsg = isAdventurerLogin ? 
                            "❌ Invalid Adventure Username or password. Please check and try again." :
                            "❌ Invalid credentials. Check your email and password.";
                        showStatus(errorMsg, false);
                        LOGGER.warning("Authentication failed for: " + email + ", isAdventurerLogin: " + isAdventurerLogin);
                    }
                });
                
            } catch (Exception e) {
                Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    loginButton.setText("🚀 START ADVENTURE");
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
            emailLabel.setText("Adventure Username");
            emailField.setPromptText("Enter your Adventure Username");
            
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
            showStatus("❌ Adventurers must use Adventure Username and password to log in!", false);
            return;
        }
        
        showStatus("Signing in with Google...", true);
        googleSignInButton.setDisable(true);
        
        // Use real Google OAuth authentication
        if (googleOAuthService != null) {
            googleOAuthService.authenticateUser()
                .thenAccept(googleUserInfo -> {
                    Platform.runLater(() -> {
                        try {
                            // Authenticate with Firebase using Google user info
                            FirebaseAuthService.AuthResult authResult = 
                                firebaseAuthService.signInWithGoogleUserInfo(googleUserInfo);
                            
                            if (authResult.isSuccess()) {
                                // Create CoinCraft user from Google and Firebase data
                                User coinCraftUser = new User();
                                coinCraftUser.setName(googleUserInfo.getName());
                                coinCraftUser.setEmail(googleUserInfo.getEmail());
                                coinCraftUser.setRole(selectedRole);
                                coinCraftUser.setAge(selectedRole == UserRole.PARENT ? 35 : 25);
                                coinCraftUser.setSmartCoinBalance(1000);
                                coinCraftUser.setLevel(1);
                                coinCraftUser.setExperiencePoints(0);
                                coinCraftUser.setFirebaseUid(authResult.getUserId());
                                coinCraftUser.setLastLogin(java.time.LocalDateTime.now());
                                
                                String roleMessage = selectedRole == UserRole.PARENT ? "merchant" : 
                                                   selectedRole.name().toLowerCase();
                                showStatus("Google sign-in successful! Welcome " + roleMessage + "!", true);
                                
                                // Small delay before transitioning
                                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                                    if (callback != null) {
                                        callback.onLoginSuccess(coinCraftUser);
                                    }
                                }));
                                timeline.play();
                                
                            } else {
                                googleSignInButton.setDisable(false);
                                showStatus("Google sign-in failed: " + authResult.getErrorMessage(), false);
                                LOGGER.severe("Google sign-in failed: " + authResult.getErrorMessage());
                            }
                            
                        } catch (Exception e) {
                            googleSignInButton.setDisable(false);
                            showStatus("Google sign-in failed: " + e.getMessage(), false);
                            LOGGER.severe(() -> "Google sign-in error: " + e.getMessage());
                        }
                    });
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> {
                        googleSignInButton.setDisable(false);
                        showStatus("Google sign-in failed: " + throwable.getMessage(), false);
                        LOGGER.severe("Google sign-in error: " + throwable.getMessage());
                    });
                    return null;
                });
        } else {
            // Fallback to mock implementation if Google OAuth service is not available
            showStatus("Google OAuth service not available. Using fallback.", false);
            handleGoogleSignInFallback(selectedRole);
        }
    }
    
    /**
     * Fallback Google sign-in implementation for when OAuth service is not available
     */
    private void handleGoogleSignInFallback(UserRole selectedRole) {
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
                        
                        String roleMessage = selectedRole == UserRole.PARENT ? "merchant" : 
                                           selectedRole == UserRole.CHILD ? "adventurer" : selectedRole.name().toLowerCase();
                        showStatus("Google sign-in successful! Welcome " + roleMessage + "! (Demo Mode)", true);
                        
                        // Small delay before transitioning
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                            if (callback != null) {
                                callback.onLoginSuccess(googleUser);
                            }
                        }));
                        timeline.play();
                        
                    } catch (Exception e) {
                        googleSignInButton.setDisable(false);
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