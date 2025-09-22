package com.coincraft.ui;

import java.util.concurrent.CompletableFuture;

import com.coincraft.models.User;
import com.coincraft.models.UserRole;
import com.coincraft.services.FirebaseService;
import com.coincraft.ui.theme.PixelButton;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Parent Registration Screen for CoinCraft
 * Allows parents to register with email/password or Google OAuth
 * Provides access to parent dashboard for managing child accounts
 */
public class ParentRegistrationScreen {
    
    public interface ParentRegistrationCallback {
        void onRegistrationSuccess(User parent);
        void onRegistrationFailed(String error);
        void onBackToLogin();
        void onNavigateToChildRegistration();
    }
    
    private BorderPane root;
    private VBox mainContainer;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;
    private Button googleSignInButton;
    private Button backToLoginButton;
    private Hyperlink childRegistrationLink;
    private Label statusLabel;
    
    private final ParentRegistrationCallback callback;
    private final FirebaseService firebaseService;
    
    public ParentRegistrationScreen(ParentRegistrationCallback callback) {
        this.callback = callback;
        this.firebaseService = FirebaseService.getInstance();
        initializeUI();
        setupEventHandlers();
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.getStyleClass().add("registration-screen");
        
        createMainContainer();
        root.setCenter(mainContainer);
    }
    
    private void createMainContainer() {
        mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setMaxWidth(450);
        
        // Header
        VBox header = createHeader();
        
        // Registration form
        VBox form = createRegistrationForm();
        
        // Alternative registration options
        VBox alternatives = createAlternativeOptions();
        
        // Navigation links
        VBox navigation = createNavigationSection();
        
        mainContainer.getChildren().addAll(header, form, alternatives, navigation);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        // Logo/Icon
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/images/parent-icon.png"));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(80);
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            header.getChildren().add(logoView);
        } catch (Exception e) {
            System.out.println("Could not load parent icon: " + e.getMessage());
        }
        
        // Title and subtitle
        Label titleLabel = new Label("Parent Registration");
        titleLabel.getStyleClass().addAll("title-label", "pixel-font-large");
        
        Label subtitleLabel = new Label("Create your parent account to manage your child's CoinCraft adventure");
        subtitleLabel.getStyleClass().addAll("subtitle-label", "pixel-font-medium");
        subtitleLabel.setWrapText(true);
        subtitleLabel.setMaxWidth(400);
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }
    
    private VBox createRegistrationForm() {
        VBox form = new VBox(15);
        form.setAlignment(Pos.CENTER);
        
        // Parent name field
        VBox nameSection = new VBox(5);
        Label nameLabel = new Label("Full Name:");
        nameLabel.getStyleClass().addAll("field-label", "pixel-font-small");
        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.getStyleClass().addAll("registration-field", "pixel-input");
        nameField.setMaxWidth(350);
        nameSection.getChildren().addAll(nameLabel, nameField);
        
        // Email field
        VBox emailSection = new VBox(5);
        Label emailLabel = new Label("Email Address:");
        emailLabel.getStyleClass().addAll("field-label", "pixel-font-small");
        emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.getStyleClass().addAll("registration-field", "pixel-input");
        emailField.setMaxWidth(350);
        emailSection.getChildren().addAll(emailLabel, emailField);
        
        // Password field
        VBox passwordSection = new VBox(5);
        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().addAll("field-label", "pixel-font-small");
        passwordField = new PasswordField();
        passwordField.setPromptText("Create a secure password");
        passwordField.getStyleClass().addAll("registration-field", "pixel-input");
        passwordField.setMaxWidth(350);
        passwordSection.getChildren().addAll(passwordLabel, passwordField);
        
        // Confirm password field
        VBox confirmPasswordSection = new VBox(5);
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.getStyleClass().addAll("field-label", "pixel-font-small");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.getStyleClass().addAll("registration-field", "pixel-input");
        confirmPasswordField.setMaxWidth(350);
        confirmPasswordSection.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
        
        // Register button
        registerButton = new PixelButton("Create Parent Account");
        registerButton.getStyleClass().addAll("primary-button", "register-button");
        registerButton.setMaxWidth(350);
        
        // Status label
        statusLabel = new Label();
        statusLabel.getStyleClass().addAll("status-label", "pixel-font-small");
        statusLabel.setVisible(false);
        
        form.getChildren().addAll(
            nameSection, 
            emailSection, 
            passwordSection, 
            confirmPasswordSection,
            registerButton,
            statusLabel
        );
        
        return form;
    }
    
    private VBox createAlternativeOptions() {
        VBox alternatives = new VBox(15);
        alternatives.setAlignment(Pos.CENTER);
        
        // Separator
        HBox separator = new HBox();
        separator.setAlignment(Pos.CENTER);
        Region leftLine = new Region();
        leftLine.getStyleClass().add("separator-line");
        HBox.setHgrow(leftLine, Priority.ALWAYS);
        Label orLabel = new Label("OR");
        orLabel.getStyleClass().addAll("separator-text", "pixel-font-small");
        orLabel.setPadding(new Insets(0, 10, 0, 10));
        Region rightLine = new Region();
        rightLine.getStyleClass().add("separator-line");
        HBox.setHgrow(rightLine, Priority.ALWAYS);
        separator.getChildren().addAll(leftLine, orLabel, rightLine);
        
        // Google Sign-in button
        googleSignInButton = new PixelButton("Sign up with Google");
        googleSignInButton.getStyleClass().addAll("google-button", "secondary-button");
        googleSignInButton.setMaxWidth(350);
        
        // Add Google icon if available
        try {
            Image googleIcon = new Image(getClass().getResourceAsStream("/images/google-icon.png"));
            ImageView googleIconView = new ImageView(googleIcon);
            googleIconView.setFitWidth(20);
            googleIconView.setFitHeight(20);
            googleSignInButton.setGraphic(googleIconView);
        } catch (Exception e) {
            System.out.println("Could not load Google icon: " + e.getMessage());
        }
        
        alternatives.getChildren().addAll(separator, googleSignInButton);
        return alternatives;
    }
    
    private VBox createNavigationSection() {
        VBox navigation = new VBox(10);
        navigation.setAlignment(Pos.CENTER);
        
        // Link to child registration
        childRegistrationLink = new Hyperlink("Looking to register a child instead?");
        childRegistrationLink.getStyleClass().addAll("navigation-link", "pixel-font-small");
        
        // Back to login button
        backToLoginButton = new PixelButton("Back to Login");
        backToLoginButton.getStyleClass().addAll("tertiary-button", "back-button");
        backToLoginButton.setMaxWidth(200);
        
        navigation.getChildren().addAll(childRegistrationLink, backToLoginButton);
        return navigation;
    }
    
    private void setupEventHandlers() {
        registerButton.setOnAction(e -> handleRegistration());
        googleSignInButton.setOnAction(e -> handleGoogleSignIn());
        backToLoginButton.setOnAction(e -> callback.onBackToLogin());
        childRegistrationLink.setOnAction(e -> callback.onNavigateToChildRegistration());
        
        // Enter key handling
        nameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> confirmPasswordField.requestFocus());
        confirmPasswordField.setOnAction(e -> handleRegistration());
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showStatus("Please fill in all fields", true);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address", true);
            return;
        }
        
        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters", true);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatus("Passwords do not match", true);
            return;
        }
        
        // Disable form during registration
        setFormEnabled(false);
        showStatus("Creating your parent account...", false);
        
        // Perform registration asynchronously
        CompletableFuture.supplyAsync(() -> {
            // Create parent user profile
            User parent = new User();
            parent.setName(name);
            parent.setEmail(email);
            parent.setRole(UserRole.PARENT);
            parent.setAge(0); // Not applicable for parents
            
            // Register with Firebase
            return firebaseService.registerMockUser(email, password, parent);
        }).thenAccept(success -> {
            Platform.runLater(() -> {
                if (success) {
                    // Registration successful
                    User parent = new User();
                    parent.setName(name);
                    parent.setEmail(email);
                    parent.setRole(UserRole.PARENT);
                    parent.setAge(0);
                    
                    showStatus("Registration successful! Welcome to CoinCraft!", false);
                    // Delay before transitioning to dashboard
                    Platform.runLater(() -> {
                        callback.onRegistrationSuccess(parent);
                    });
                } else {
                    showStatus("Registration failed: Unable to create account", true);
                    setFormEnabled(true);
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                showStatus("Registration failed: " + throwable.getMessage(), true);
                setFormEnabled(true);
            });
            return null;
        });
    }
    
    
    private void handleGoogleSignIn() {
        setFormEnabled(false);
        showStatus("Signing in with Google...", false);
        
        CompletableFuture.supplyAsync(() -> {
            // In a real implementation, this would:
            // 1. Open Google OAuth flow
            // 2. Get the Google ID token
            // 3. Verify with Firebase
            // For now, simulate Google sign-in
            try {
                Thread.sleep(1000); // Simulate network delay
                return true; // Mock successful Google sign-in
            } catch (InterruptedException e) {
                return false;
            }
        }).thenAccept(success -> {
            Platform.runLater(() -> {
                if (success) {
                    // Create parent user profile from Google data (mocked)
                    User parent = new User();
                    parent.setName("Google User");
                    parent.setEmail("user@gmail.com");
                    parent.setRole(UserRole.PARENT);
                    parent.setAge(0);
                    
                    // Register with Firebase
                    boolean registrationSuccess = firebaseService.registerMockUser("user@gmail.com", "google_auth", parent);
                    if (registrationSuccess) {
                        showStatus("Google sign-in successful! Welcome to CoinCraft!", false);
                        Platform.runLater(() -> {
                            callback.onRegistrationSuccess(parent);
                        });
                    } else {
                        showStatus("Google sign-in failed: Could not create account", true);
                        setFormEnabled(true);
                    }
                } else {
                    showStatus("Google sign-in failed: Authentication cancelled", true);
                    setFormEnabled(true);
                }
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                showStatus("Google sign-in failed: " + throwable.getMessage(), true);
                setFormEnabled(true);
            });
            return null;
        });
    }
    
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        if (isError) {
            statusLabel.getStyleClass().removeAll("success-status");
            statusLabel.getStyleClass().add("error-status");
        } else {
            statusLabel.getStyleClass().removeAll("error-status");
            statusLabel.getStyleClass().add("success-status");
        }
    }
    
    private void setFormEnabled(boolean enabled) {
        nameField.setDisable(!enabled);
        emailField.setDisable(!enabled);
        passwordField.setDisable(!enabled);
        confirmPasswordField.setDisable(!enabled);
        registerButton.setDisable(!enabled);
        googleSignInButton.setDisable(!enabled);
    }
    
    public BorderPane getRoot() {
        return root;
    }
}
