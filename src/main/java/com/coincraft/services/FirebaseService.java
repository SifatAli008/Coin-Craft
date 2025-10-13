package com.coincraft.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.coincraft.models.Task;
import com.coincraft.models.User;
import com.coincraft.models.UserRole;

/**
 * Firebase service for authentication and Firestore database operations
 * Now uses real Firebase implementation with REST API
 */
public class FirebaseService {
    private static final Logger LOGGER = Logger.getLogger(FirebaseService.class.getName());
    private static FirebaseService instance;
    
    private FirebaseConfig config;
    private FirebaseAuthService authService;
    private FirestoreService firestoreService;
    private boolean initialized = false;
    private String currentIdToken;
    
    // Mock user registry for testing (in production, this would be in Firebase)
    private static final Map<String, User> mockUserRegistry = new HashMap<>();
    
    // Collection names for future use
    // private static final String USERS_COLLECTION = "users";
    // private static final String TASKS_COLLECTION = "tasks";
    // private static final String BADGES_COLLECTION = "badges";
    // private static final String LEADERBOARD_COLLECTION = "leaderboard";
    
    private FirebaseService() {}
    
    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }
    
    /**
     * Initialize Firebase with real configuration
     * Uses dynamic loading to handle optional Firebase dependencies
     */
    public void initialize() {
        try {
            if (initialized) {
                LOGGER.info("Firebase already initialized");
                return;
            }
            
            // Try to load Firebase configuration
            config = FirebaseConfig.loadFromResources();
            
            // Log configuration details
            LOGGER.info("🔥 Firebase Configuration Loaded:");
            LOGGER.info("  Project ID: %s".formatted(config.getProjectId()));
            LOGGER.info("  Auth Domain: %s".formatted(config.getAuthDomain()));
            LOGGER.info("  Database URL: %s".formatted(config.getDatabaseURL()));
            LOGGER.info("  Storage Bucket: %s".formatted(config.getStorageBucket()));
            LOGGER.info("  App ID: %s".formatted(config.getAppId()));
            
            // Initialize Firebase Admin SDK first
            FirebaseAdminService.initialize();
            
            // Initialize Firebase Storage (with error handling)
            try {
                // Use simplified storage service to avoid dependency conflicts
                FirebaseStorageService.initialize();
            } catch (Exception e) {
                LOGGER.warning(() -> "Firebase Storage initialization failed, continuing with local storage: " + e.getMessage());
            }
            
            // Initialize REST API services
            authService = new FirebaseAuthService(config);
            firestoreService = new FirestoreService(config);
            
            LOGGER.info("✅ Firebase service initialized successfully for project: %s".formatted(config.getProjectId()));
            LOGGER.info("  Admin SDK Status: %s".formatted(FirebaseAdminService.getConnectionStatus()));
            LOGGER.info("  Storage Status: %s".formatted(FirebaseStorageService.getConnectionStatus()));
            initialized = true;
            
        } catch (NoClassDefFoundError e) {
            LOGGER.info("Firebase libraries not found, initializing in mock mode: %s".formatted(e.getMessage()));
            initializeMockMode();
        } catch (Exception e) {
            LOGGER.warning("Failed to initialize Firebase, falling back to mock mode: %s".formatted(e.getMessage()));
            initializeMockMode();
        }
    }

    // ===================== Messaging (Service facade) =====================
    public boolean saveMessage(com.coincraft.models.MessageData message) {
        try {
            if (!initialized) initialize();
            if (firestoreService != null && currentIdToken != null) {
                firestoreService.setIdToken(currentIdToken);
                return firestoreService.saveMessage(message);
            }
            // Fallback: append to local file
            java.nio.file.Path dataDir = java.nio.file.Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            java.nio.file.Files.createDirectories(dataDir);
            java.nio.file.Path file = dataDir.resolve("messages.txt");
            String line = String.join("|",
                message.getMessageId(),
                message.getConversationId(),
                message.getSenderId(),
                message.getSenderName() != null ? message.getSenderName() : "",
                message.getRecipientId(),
                message.getRecipientName() != null ? message.getRecipientName() : "",
                message.getTimestamp() != null ? message.getTimestamp().toString() : "",
                message.getContent().replace("\n", "\\n")
            );
            try (java.io.PrintWriter w = new java.io.PrintWriter(java.nio.file.Files.newBufferedWriter(file, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND))) {
                w.println(line);
            }
            return true;
        } catch (IOException e) {
            LOGGER.warning("Failed to save message locally: %s".formatted(e.getMessage()));
            return false;
        }
    }

    public java.util.List<com.coincraft.models.MessageData> loadConversation(String conversationId, int limit) {
        try {
            if (!initialized) initialize();
            if (firestoreService != null && currentIdToken != null) {
                firestoreService.setIdToken(currentIdToken);
                return firestoreService.loadConversation(conversationId, limit);
            }
            // Fallback local read
            java.util.List<com.coincraft.models.MessageData> list = new java.util.ArrayList<>();
            java.nio.file.Path dataDir = java.nio.file.Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            java.nio.file.Path file = dataDir.resolve("messages.txt");
            if (!java.nio.file.Files.exists(file)) return list;
            try (java.io.BufferedReader r = java.nio.file.Files.newBufferedReader(file)) {
                String line;
                while ((line = r.readLine()) != null) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 8 && conversationId.equals(parts[1])) {
                        com.coincraft.models.MessageData m = new com.coincraft.models.MessageData();
                        m.setMessageId(parts[0]);
                        m.setConversationId(parts[1]);
                        m.setSenderId(parts[2]);
                        m.setSenderName(parts[3]);
                        m.setRecipientId(parts[4]);
                        m.setRecipientName(parts[5]);
                        if (!parts[6].isEmpty()) m.setTimestamp(java.time.LocalDateTime.parse(parts[6]));
                        m.setContent(parts[7].replace("\\n", "\n"));
                        list.add(m);
                    }
                }
            }
            list.sort((a,b) -> a.getTimestamp() != null && b.getTimestamp() != null ? a.getTimestamp().compareTo(b.getTimestamp()) : 0);
            if (list.size() > limit) {
                return new java.util.ArrayList<>(list.subList(Math.max(0, list.size()-limit), list.size()));
            }
            return list;
        } catch (IOException e) {
            LOGGER.warning("Failed to load conversation locally: %s".formatted(e.getMessage()));
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Fallback to mock mode if Firebase initialization fails
     */
    private void initializeMockMode() {
        LOGGER.info("Initializing Firebase in mock mode");
        initialized = true;
        
        // Initialize some demo accounts for testing
        initializeDemoAccounts();
    }
    
    /**
     * Authenticate user with email and password
     * For adventurers (children), email should already be in format: adventureId@coincraft.adventure
     * Returns user ID if successful, null if failed
     */
    public String authenticateUser(String email, String password) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (authService != null) {
                // Use real Firebase authentication
                FirebaseAuthService.AuthResult result = authService.signInUser(email, password);
                if (result.isSuccess()) {
                    currentIdToken = result.getIdToken();
                    firestoreService.setIdToken(currentIdToken);
                    LOGGER.info(() -> "Authentication successful for: " + email);
                    return result.getUserId();
                } else {
                    LOGGER.warning(() -> "Authentication failed: " + result.getErrorMessage());
                    return null;
                }
            } else {
                // Fallback to mock authentication - check both registry and local storage
                if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
                    // First check mock registry (for demo accounts)
                    String mockUserId = "user_" + email.hashCode();
                    if (mockUserRegistry.containsKey(mockUserId)) {
                        LOGGER.info("Mock authentication successful for registered user: %s".formatted(email));
                        return mockUserId;
                    }
                    
                    // Then check local storage for created adventurers
                    List<User> allUsers = loadUsersLocally();
                    for (User user : allUsers) {
                        if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                            // For simplicity, we're not storing passwords in local storage
                            // In production, passwords would be hashed and verified properly
                            LOGGER.info("Local storage authentication successful for: %s".formatted(email));
                            return user.getUserId();
                        }
                    }
                    
                    LOGGER.warning("Authentication failed - user not found: %s".formatted(email));
                    return null;
                }
                return null;
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Authentication failed: %s".formatted(e.getMessage()));
            return null;
        }
    }
    
    /**
     * Create a new user account
     */
    public String createUser(String email, String password, User user) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (authService != null) {
                // Use real Firebase authentication
                FirebaseAuthService.AuthResult result = authService.registerUser(email, password, user.getName());
                if (result.isSuccess()) {
                    currentIdToken = result.getIdToken();
                    firestoreService.setIdToken(currentIdToken);
                    
                    user.setUserId(result.getUserId());
                    saveUser(user);
                    
                    LOGGER.info("User created successfully: %s".formatted(email));
                    return result.getUserId();
                } else {
                    LOGGER.warning("User creation failed: %s".formatted(result.getErrorMessage()));
                    return null;
                }
            } else {
                // Fallback to mock user creation
                String userId = "user_" + System.currentTimeMillis();
                user.setUserId(userId);
                saveUser(user);
                LOGGER.info("Mock user created successfully: %s".formatted(userId));
                return userId;
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("User creation failed: %s".formatted(e.getMessage()));
            return null;
        }
    }
    
    /**
     * Save user data to Firestore
     */
    public void saveUser(User user) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // Try Admin SDK first (preferred method)
            if (FirebaseAdminService.isInitialized()) {
                boolean success = FirebaseAdminService.saveUserToFirestore(user);
                if (success) {
                    LOGGER.info("User data saved successfully via Admin SDK for %s".formatted(user.getUserId()));
                    return;
                } else {
                    LOGGER.warning("Failed to save user data via Admin SDK for %s".formatted(user.getUserId()));
                }
            }
            
            // Fallback to REST API
            if (firestoreService != null && currentIdToken != null) {
                boolean success = firestoreService.saveUser(user);
                if (success) {
                    LOGGER.info("User data saved successfully via REST API for %s".formatted(user.getUserId()));
                } else {
                    LOGGER.warning("Failed to save user data via REST API for %s".formatted(user.getUserId()));
                }
            } else {
                // Save to local storage as final fallback
                saveUserLocally(user);
                LOGGER.info("User data saved locally for %s".formatted(user.getUserId()));
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to save user: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Get a specific user by their user ID
     */
    public User getUserById(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore - would need implementation
                return null;
            } else {
                // Load from local storage as fallback
                return getUserByIdLocally(userId);
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to get user by ID: %s".formatted(e.getMessage()));
            return null;
        }
    }
    
    /**
     * Get a specific user by their user ID from local storage
     */
    private User getUserByIdLocally(String userId) {
        try {
            List<User> allUsers = loadUsersLocally();
            for (User user : allUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to get user by ID locally: %s".formatted(e.getMessage()));
            return null;
        }
    }
    
    /**
     * Get all users from Firestore (for merchant to see their adventurers)
     */
    public List<User> getAllUsers() {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                return firestoreService.getAllUsers();
            } else {
                // Load from local storage as fallback
                return loadUsersLocally();
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load users: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Store adventurer credentials for authentication
     * In production, this would use proper password hashing and secure storage
     */
    public void storeAdventurerCredentials(String adventureUsername, String password, String userId) {
        try {
            // Create credentials directory if it doesn't exist
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Files.createDirectories(dataDir);
            
            // Store credentials in a secure file (in production, use proper encryption)
            Path credentialsFile = dataDir.resolve("adventurer_credentials.txt");
            
            String credentialEntry = adventureUsername.toLowerCase() + "|" + password + "|" + userId + "|" + LocalDateTime.now().toString();
            
            // Append to credentials file
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(credentialsFile, 
                    java.nio.file.StandardOpenOption.CREATE, 
                    java.nio.file.StandardOpenOption.APPEND))) {
                writer.println(credentialEntry);
            }
            
            LOGGER.info("Adventurer credentials stored for Adventure Username: %s".formatted(adventureUsername));
            
        } catch (IOException e) {
            LOGGER.warning("Failed to store adventurer credentials: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Verify adventurer credentials for login
     */
    public String verifyAdventurerCredentials(String adventureUsername, String password) {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path credentialsFile = dataDir.resolve("adventurer_credentials.txt");
            
            if (!Files.exists(credentialsFile)) {
                return null;
            }
            
            try (BufferedReader reader = Files.newBufferedReader(credentialsFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String storedAdventureUsername = parts[0];
                        String storedPassword = parts[1];
                        String userId = parts[2];
                        
                        if (storedAdventureUsername.equalsIgnoreCase(adventureUsername.toLowerCase()) && 
                            storedPassword.equals(password)) {
                            LOGGER.info("Adventurer credentials verified for: %s".formatted(adventureUsername));
                            return userId;
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            LOGGER.warning("Failed to verify adventurer credentials: %s".formatted(e.getMessage()));
        }
        
        return null;
    }
    
    /**
     * Update adventurer password
     */
    public boolean updateAdventurerPassword(String adventureUsername, String currentPassword, String newPassword) {
        try {
            // First verify current credentials
            String userId = verifyAdventurerCredentials(adventureUsername, currentPassword);
            if (userId == null) {
                LOGGER.warning("Cannot update password - current password verification failed for: %s".formatted(adventureUsername));
                return false;
            }
            
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path credentialsFile = dataDir.resolve("adventurer_credentials.txt");
            
            if (!Files.exists(credentialsFile)) {
                LOGGER.warning("Credentials file not found, cannot update password");
                return false;
            }
            
            // Read all credentials
            List<String> allCredentials = new ArrayList<>();
            boolean passwordUpdated = false;
            
            try (BufferedReader reader = Files.newBufferedReader(credentialsFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String storedUsername = parts[0];
                        String storedPassword = parts[1];
                        String storedUserId = parts[2];
                        
                        if (storedUsername.equalsIgnoreCase(adventureUsername.toLowerCase()) && 
                            storedPassword.equals(currentPassword) && 
                            storedUserId.equals(userId)) {
                            // Update this line with new password
                            String updatedLine = storedUsername + "|" + newPassword + "|" + storedUserId + "|" + LocalDateTime.now().toString();
                            allCredentials.add(updatedLine);
                            passwordUpdated = true;
                            LOGGER.info("Password updated for adventurer: %s".formatted(adventureUsername));
                        } else {
                            // Keep original line
                            allCredentials.add(line);
                        }
                    }
                }
            }
            
            if (passwordUpdated) {
                // Write all credentials back to file
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(credentialsFile))) {
                    for (String credential : allCredentials) {
                        writer.println(credential);
                    }
                }
                return true;
            } else {
                LOGGER.warning("Could not find matching credentials to update for: %s".formatted(adventureUsername));
                return false;
            }
            
        } catch (IOException e) {
            LOGGER.warning("Failed to update adventurer password: %s".formatted(e.getMessage()));
            return false;
        }
    }
    
    /**
     * Check if an Adventure Username is already taken
     */
    public boolean isAdventureUsernameTaken(String adventureUsername) {
        try {
            if (adventureUsername == null || adventureUsername.trim().isEmpty()) {
                return false;
            }
            
            // Normalize the Adventure Username
            String normalizedUsername = adventureUsername.trim().toLowerCase();
            
            // Get all users and check for Adventure Username collision
            List<User> allUsers = getAllUsers();
            for (User user : allUsers) {
                if (user.getRole() == UserRole.CHILD && user.getUsername() != null) {
                    String userUsername = user.getUsername().toLowerCase();
                    if (userUsername.equals(normalizedUsername)) {
                        LOGGER.info("Adventure Username '%s' is already taken".formatted(adventureUsername));
                        return true;
                    }
                }
            }
            
            // Also check credentials file for username availability
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path credentialsFile = dataDir.resolve("adventurer_credentials.txt");
            
            if (Files.exists(credentialsFile)) {
                try (BufferedReader reader = Files.newBufferedReader(credentialsFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 1) {
                            String storedUsername = parts[0];
                            if (storedUsername.equalsIgnoreCase(normalizedUsername)) {
                                LOGGER.info("Adventure Username '%s' is already taken (in credentials)".formatted(adventureUsername));
                                return true;
                            }
                        }
                    }
                }
            }
            
            LOGGER.info("Adventure Username '%s' is available".formatted(adventureUsername));
            return false;
            
        } catch (IOException e) {
            LOGGER.severe("Error checking Adventure Username availability: %s".formatted(e.getMessage()));
            // In case of error, assume it's taken to be safe
            return true;
        }
    }
    
    /**
     * Save user to local storage as fallback
     */
    private void saveUserLocally(User user) {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Files.createDirectories(dataDir);
            
            // Create user file
            Path userFile = dataDir.resolve("users.txt");
            
            // Load existing users
            List<User> existingUsers = loadUsersFromFile(userFile);
            
            // Remove existing user with same ID (update scenario)
            existingUsers.removeIf(u -> u.getUserId().equals(user.getUserId()));
            
            // Add the new/updated user
            existingUsers.add(user);
            
            // Save all users back to file
            saveUsersToFile(existingUsers, userFile);
            
            LOGGER.info("User saved locally: %s".formatted(user.getName()));
            
        } catch (IOException e) {
            LOGGER.warning("Failed to save user locally: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Load users from local storage as fallback
     */
    private List<User> loadUsersLocally() {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path userFile = dataDir.resolve("users.txt");
            
            if (Files.exists(userFile)) {
                List<User> users = loadUsersFromFile(userFile);
                LOGGER.info("Loaded %d users from local storage".formatted(users.size()));
                return users;
            } else {
                LOGGER.info("No local user data file found");
                return new ArrayList<>();
            }
            
        } catch (IOException e) {
            LOGGER.warning("Failed to load users locally: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Save users to file
     */
    private void saveUsersToFile(List<User> users, Path userFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(userFile))) {
            for (User user : users) {
                String userData = user.getUserId() + "|" + user.getName() + "|" + user.getRole() + "|" + 
                                user.getAge() + "|" + user.getSmartCoinBalance() + "|" + user.getLevel() + "|" + 
                                user.getDailyStreaks() + "|" + (user.getEmail() != null ? user.getEmail() : "") + "|" +
                                (user.getUsername() != null ? user.getUsername() : "") + "|" +
                                (user.getLastLogin() != null ? user.getLastLogin().toString() : "") + "|" +
                                (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "") + "|" +
                                (user.getParentId() != null ? user.getParentId() : "");
                writer.println(userData);
            }
        }
    }
    
    /**
     * Load users from file
     */
    private List<User> loadUsersFromFile(Path userFile) throws IOException {
        List<User> users = new ArrayList<>();
        if (!Files.exists(userFile)) {
            return users;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(userFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = parseUserDataFromFile(line);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        
        return users;
    }
    
    /**
     * Parse user data from stored string (file format)
     */
    private User parseUserDataFromFile(String userData) {
        try {
            String[] parts = userData.split("\\|");
            if (parts.length >= 6) {
                User user = new User();
                user.setUserId(parts[0]);
                user.setName(parts[1]);
                user.setRole(UserRole.valueOf(parts[2]));
                user.setAge(Integer.parseInt(parts[3]));
                user.setSmartCoinBalance(Integer.parseInt(parts[4]));
                user.setLevel(Integer.parseInt(parts[5]));
                
                // Handle optional fields
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    user.setDailyStreaks(Integer.parseInt(parts[6]));
                }
                if (parts.length > 7 && !parts[7].isEmpty()) {
                    user.setEmail(parts[7]);
                }
                if (parts.length > 8 && !parts[8].isEmpty()) {
                    user.setUsername(parts[8]);
                }
                if (parts.length > 9 && !parts[9].isEmpty()) {
                    user.setLastLogin(LocalDateTime.parse(parts[9]));
                } else {
                    user.setLastLogin(LocalDateTime.now());
                }
                if (parts.length > 10 && !parts[10].isEmpty()) {
                    user.setCreatedAt(LocalDateTime.parse(parts[10]));
                }
                if (parts.length > 11 && !parts[11].isEmpty()) {
                    user.setParentId(parts[11]);
                }
                
                return user;
            }
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to parse user data: %s".formatted(userData));
        }
        return null;
    }
    
    
    /**
     * Load user data from Firestore
     */
    public User loadUser(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // Try Admin SDK first (preferred method)
            if (FirebaseAdminService.isInitialized()) {
                User user = FirebaseAdminService.loadUserFromFirestore(userId);
                if (user != null) {
                    LOGGER.info("User data loaded successfully via Admin SDK for %s".formatted(userId));
                    return user;
                } else {
                    LOGGER.info("User not found via Admin SDK: %s".formatted(userId));
                }
            }
            
            // Fallback to REST API
            if (firestoreService != null && currentIdToken != null) {
                User user = firestoreService.loadUser(userId);
                if (user != null) {
                    LOGGER.info("User data loaded successfully via REST API for %s".formatted(userId));
                    return user;
                } else {
                    LOGGER.info("User not found via REST API: %s".formatted(userId));
                }
            }
            
            // Fallback to local storage and mock registry
            // First check mock registry (for demo accounts)
            User mockUser = getMockUser(userId);
            if (mockUser != null) {
                LOGGER.info("Mock: Loading user data for %s".formatted(userId));
                return mockUser;
            }
            
            // Then check local storage (for created adventurers)
            User localUser = getUserByIdLocally(userId);
            if (localUser != null) {
                LOGGER.info("Local: Loading user data for %s".formatted(userId));
                return localUser;
            }
            
            LOGGER.info("User not found in any storage: %s".formatted(userId));
            return null;
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load user: %s".formatted(e.getMessage()));
            return null;
        }
    }
    
    /**
     * Save task data to Firestore
     */
    public void saveTask(Task task) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                boolean success = firestoreService.saveTask(task);
                if (success) {
                    LOGGER.info("Task saved successfully: %s".formatted(task.getTaskId()));
                } else {
                    LOGGER.warning("Failed to save task: %s".formatted(task.getTaskId()));
                }
            } else {
                // Save to local storage as fallback
                saveTaskLocally(task);
                LOGGER.info("Task saved locally: %s".formatted(task.getTaskId()));
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to save task: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Save task to local storage as fallback
     */
    private void saveTaskLocally(Task task) {
        try {
            // Create data directory if it doesn't exist
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Files.createDirectories(dataDir);
            
            // Create tasks file
            Path tasksFile = dataDir.resolve("tasks.txt");
            
            // Load existing tasks
            List<Task> existingTasks = loadTasksFromFile(tasksFile);
            
            // Remove existing task with same ID (update scenario)
            existingTasks.removeIf(t -> t.getTaskId().equals(task.getTaskId()));
            
            // Add the new/updated task
            existingTasks.add(task);
            
            // Save all tasks back to file
            saveTasksToFile(existingTasks, tasksFile);
            
            LOGGER.info("Task saved locally: %s".formatted(task.getTitle()));
            
        } catch (IOException e) {
            LOGGER.warning("Failed to save task locally: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Save tasks to file
     */
    private void saveTasksToFile(List<Task> tasks, Path tasksFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tasksFile))) {
            for (Task task : tasks) {
                String taskData = task.getTaskId() + "|" + 
                                task.getTitle() + "|" + 
                                task.getDescription() + "|" + 
                                task.getType() + "|" + 
                                task.getAssignedBy() + "|" + 
                                (task.getAssignedTo() != null ? task.getAssignedTo() : "") + "|" + // new field (v2)
                                task.getRewardCoins() + "|" + 
                                task.getDifficultyLevel() + "|" + 
                                task.isCompleted() + "|" + 
                                task.getValidationStatus() + "|" + 
                                (task.getDeadline() != null ? task.getDeadline().toString() : "") + "|" +
                                (task.getCreatedAt() != null ? task.getCreatedAt().toString() : "") + "|" +
                                (task.getCompletedAt() != null ? task.getCompletedAt().toString() : "") + "|" +
                                (task.getCompletionNotes() != null ? task.getCompletionNotes() : "");
                writer.println(taskData);
            }
        }
    }
    
    /**
     * Load tasks from file
     */
    private List<Task> loadTasksFromFile(Path tasksFile) throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(tasksFile)) {
            return tasks;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(tasksFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTaskDataFromFile(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
        
        return tasks;
    }
    
    /**
     * Parse task data from stored string (file format)
     */
    private Task parseTaskDataFromFile(String taskData) {
        try {
            String[] parts = taskData.split("\\|", -1);
            if (parts.length >= 9) {
                Task task = new Task();
                task.setTaskId(parts[0]);
                task.setTitle(parts[1]);
                task.setDescription(parts[2]);
                task.setType(com.coincraft.models.TaskType.valueOf(parts[3]));
                task.setAssignedBy(parts[4]);
                
                // Backward-compatible parsing: v1 had no assignedTo.
                // v2 adds assignedTo at index 5 and shifts subsequent fields by +1.
                int idx = 5;
                boolean v1Format = parts.length >= 10 && parts[idx] != null && parts[idx].matches("-?\\d+");
                if (v1Format) {
                    // No assignedTo stored
                    task.setAssignedTo("");
                } else {
                    // New format with assignedTo
                    if (parts.length > idx) task.setAssignedTo(parts[idx]);
                    idx++;
                }
                
                int rewardIndex = idx;
                int difficultyIndex = idx + 1;
                int completedIndex = idx + 2;
                int validationIndex = idx + 3;
                int deadlineIndex = idx + 4;
                int createdAtIndex = idx + 5;
                int completedAtIndex = idx + 6;
                int notesIndex = idx + 7;
                
                if (parts.length > rewardIndex && !parts[rewardIndex].isEmpty()) {
                    task.setRewardCoins(Integer.parseInt(parts[rewardIndex]));
                }
                if (parts.length > difficultyIndex && !parts[difficultyIndex].isEmpty()) {
                    task.setDifficultyLevel(Integer.parseInt(parts[difficultyIndex]));
                }
                if (parts.length > completedIndex && !parts[completedIndex].isEmpty()) {
                    task.setCompleted(Boolean.parseBoolean(parts[completedIndex]));
                }
                if (parts.length > validationIndex && !parts[validationIndex].isEmpty()) {
                    task.setValidationStatus(com.coincraft.models.ValidationStatus.valueOf(parts[validationIndex]));
                }
                
                // Parse optional fields
                if (parts.length > deadlineIndex && !parts[deadlineIndex].isEmpty()) {
                    task.setDeadline(LocalDateTime.parse(parts[deadlineIndex]));
                }
                if (parts.length > createdAtIndex && !parts[createdAtIndex].isEmpty()) {
                    task.setCreatedAt(LocalDateTime.parse(parts[createdAtIndex]));
                }
                if (parts.length > completedAtIndex && !parts[completedAtIndex].isEmpty()) {
                    task.setCompletedAt(LocalDateTime.parse(parts[completedAtIndex]));
                }
                if (parts.length > notesIndex && !parts[notesIndex].isEmpty()) {
                    task.setCompletionNotes(parts[notesIndex]);
                }
                
                return task;
            }
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to parse task data: %s - %s".formatted(taskData, e.getMessage()));
        }
        return null;
    }
    
    /**
     * Load tasks for a specific user
     */
    public List<Task> loadUserTasks(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                return firestoreService.loadUserTasks(userId);
            } else {
                // Load from local storage as fallback
                return loadUserTasksLocally(userId);
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load tasks: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Load all tasks from storage
     */
    public List<Task> loadAllTasks() {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore - would need implementation
                return new ArrayList<>();
            } else {
                // Load from local storage as fallback
                return loadAllTasksLocally();
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load all tasks: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Load tasks for a specific user from local storage
     */
    private List<Task> loadUserTasksLocally(String userId) {
        try {
            List<Task> allTasks = loadAllTasksLocally();
            List<Task> userTasks = new ArrayList<>();
            
            // Filter tasks assigned to this user (simplified - in real app would have proper assignment logic)
            for (Task task : allTasks) {
                // For now, return all tasks as they could be assigned to any adventurer
                userTasks.add(task);
            }
            
            LOGGER.info("Loaded %d tasks for user: %s".formatted(userTasks.size(), userId));
            return userTasks;
            
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to load user tasks locally: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Load all tasks from local storage
     */
    private List<Task> loadAllTasksLocally() {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path tasksFile = dataDir.resolve("tasks.txt");
            
            if (Files.exists(tasksFile)) {
                List<Task> tasks = loadTasksFromFile(tasksFile);
                LOGGER.info("Loaded %d tasks from local storage".formatted(tasks.size()));
                return tasks;
            } else {
                LOGGER.info("No local tasks file found");
                return new ArrayList<>();
            }
            
        } catch (IOException e) {
            LOGGER.warning("Failed to load tasks locally: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    /**
     * Get leaderboard data
     */
    public List<User> getLeaderboard(int limit) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                List<User> leaderboard = firestoreService.getLeaderboard(limit);
                LOGGER.info("Leaderboard loaded with %d entries".formatted(leaderboard.size()));
                return leaderboard;
            } else {
                // Mock leaderboard for fallback
                List<User> mockLeaderboard = new ArrayList<>();
                
                for (int i = 1; i <= Math.min(limit, 10); i++) {
                    User user = new User();
                    user.setUserId("user_" + i);
                    user.setName("Player " + i);
                    user.setSmartCoinBalance(100 - (i * 5));
                    user.setLevel(Math.max(1, 5 - i));
                    user.setLeaderboardRank(i);
                    mockLeaderboard.add(user);
                }
                
                LOGGER.info("Mock: Loading leaderboard with %d entries".formatted(limit));
                return mockLeaderboard;
            }
            
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load leaderboard: %s".formatted(e.getMessage()));
            return new ArrayList<>();
        }
    }
    
    // Removed unused convertUserToMap method - functionality moved to FirestoreService
    
    /**
     * Clear authentication state (used during logout)
     */
    public void clearAuthState() {
        try {
            currentIdToken = null;
            if (firestoreService != null) {
                firestoreService.setIdToken(null);
            }
            LOGGER.info("Firebase authentication state cleared");
        } catch (Exception e) {
            LOGGER.severe("Error clearing authentication state: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Shutdown Firebase connection
     */
    public void shutdown() {
        try {
            // Clear authentication state
            clearAuthState();
            
            // Firebase shutdown commented out for MVP
            // if (firebaseApp != null) {
            //     firebaseApp.delete();
            // }
            initialized = false;
            LOGGER.info("Firebase service shutdown successfully");
        } catch (Exception e) {
            LOGGER.severe("Error during Firebase shutdown: %s".formatted(e.getMessage()));
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Get Firebase connection status
     */
    public String getConnectionStatus() {
        if (!initialized) {
            return "❌ Not Initialized";
        }
        
        if (authService != null && firestoreService != null) {
            return "✅ Firebase Connected";
        } else {
            return "⚠️ Local Storage Mode";
        }
    }
    
    /**
     * Get detailed Firebase configuration info
     */
    public String getConfigInfo() {
        if (config == null) {
            return "No configuration loaded";
        }
        
        return String.format(
            "Project: %s | Auth: %s | Database: %s",
            config.getProjectId(),
            config.getAuthDomain(),
            config.getDatabaseURL() != null ? "Connected" : "Not Available"
        );
    }
    
    /**
     * Get the current ID token
     */
    public String getCurrentIdToken() {
        return currentIdToken;
    }
    
    /**
     * Set the current ID token
     */
    public void setCurrentIdToken(String idToken) {
        this.currentIdToken = idToken;
        LOGGER.info("🔑 Firebase ID token updated");
    }
    
    /**
     * Get the Firestore service instance
     */
    public FirestoreService getFirestoreService() {
        return firestoreService;
    }
    
    /**
     * Get mock user from registry (simulates Firebase user lookup)
     */
    private User getMockUser(String userId) {
        return mockUserRegistry.get(userId);
    }
    
    /**
     * Register a mock user (simulates Firebase user registration)
     */
    public boolean registerMockUser(String email, String password, User user) {
        try {
            String userId = "user_" + email.hashCode();
            user.setUserId(userId);
            user.setEmail(email);
            user.setLastLogin(LocalDateTime.now());
            user.setCreatedAt(LocalDateTime.now());
            
            mockUserRegistry.put(userId, user);
            LOGGER.info("Mock user registered: %s with role: %s".formatted(email, user.getRole()));
            return true;
        } catch (Exception e) {
            LOGGER.severe("Failed to register mock user: %s".formatted(e.getMessage()));
            return false;
        }
    }
    
    /**
     * Check if a user exists in mock registry
     */
    public boolean mockUserExists(String email) {
        String userId = "user_" + email.hashCode();
        return mockUserRegistry.containsKey(userId);
    }
    
    /**
     * Initialize demo accounts for testing
     */
    private void initializeDemoAccounts() {
        // Demo accounts are disabled per project requirements
        LOGGER.info("Skipping demo account initialization");
        // Clean up any legacy demo data that might exist from previous runs
        try { purgeDemoAccounts(); } catch (Exception ignored) {}
    }
    

    /**
     * Remove legacy demo accounts and credentials from local storage
     */
    public void purgeDemoAccounts() {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Files.createDirectories(dataDir);
            Path userFile = dataDir.resolve("users.txt");

            // Filter users file
            if (Files.exists(userFile)) {
                List<User> users = loadUsersFromFile(userFile);
                List<User> filtered = new ArrayList<>();
                for (User u : users) {
                    if (u == null) continue;
                    String email = u.getEmail();
                    String name = u.getName();
                    String uid = u.getUserId();
                    String username = u.getUsername();
                    boolean isDemo = (email != null && email.endsWith("@demo.com")) ||
                                     ("Demo Child".equalsIgnoreCase(name)) ||
                                     ("Demo Parent".equalsIgnoreCase(name)) ||
                                     ("Demo Teacher".equalsIgnoreCase(name)) ||
                                     ("Connection Test User".equalsIgnoreCase(name)) ||
                                     (username != null && "testadventurer".equalsIgnoreCase(username)) ||
                                     (uid != null && (uid.startsWith("adventurer_test_") || uid.startsWith("connection_test_")));
                    if (!isDemo) {
                        filtered.add(u);
                    }
                }
                saveUsersToFile(filtered, userFile);
            }

            // Filter credentials file
            Path credentialsFile = dataDir.resolve("adventurer_credentials.txt");
            if (Files.exists(credentialsFile)) {
                List<String> kept = new ArrayList<>();
                try (BufferedReader reader = Files.newBufferedReader(credentialsFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        String username = parts.length > 0 ? parts[0] : null;
                        String uid = parts.length > 2 ? parts[2] : null;
                        boolean isDemo = (username != null && username.equalsIgnoreCase("testadventurer")) ||
                                         (uid != null && (uid.startsWith("adventurer_test_") || uid.startsWith("connection_test_")));
                        if (!isDemo) kept.add(line);
                    }
                }
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(credentialsFile))) {
                    for (String l : kept) writer.println(l);
                }
            }

            LOGGER.info("Purged legacy demo accounts and credentials");
        } catch (IOException e) {
            LOGGER.warning("Failed to purge demo data: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Test the complete adventurer creation and login flow
     */
    public boolean testAdventurerFlow(String username, String password, String name, int age) {
        try {
            LOGGER.info("Testing adventurer flow for username: %s".formatted(username));
            
            // Step 1: Create adventurer
            User testAdventurer = new User();
            testAdventurer.setUserId("test_adventurer_" + System.currentTimeMillis());
            testAdventurer.setName(name);
            testAdventurer.setUsername(username);
            testAdventurer.setRole(UserRole.CHILD);
            testAdventurer.setAge(age);
            testAdventurer.setEmail(username + "@coincraft.adventure");
            testAdventurer.setSmartCoinBalance(25);
            testAdventurer.setLevel(1);
            testAdventurer.setDailyStreaks(0);
            testAdventurer.setLastLogin(LocalDateTime.now());
            testAdventurer.setCreatedAt(LocalDateTime.now());
            
            // Step 2: Save user
            LOGGER.info("Step 1: Saving user...");
            saveUser(testAdventurer);
            
            // Step 3: Store credentials
            LOGGER.info("Step 2: Storing credentials...");
            storeAdventurerCredentials(username, password, testAdventurer.getUserId());
            
            // Step 4: Verify user can be loaded
            LOGGER.info("Step 3: Testing user load...");
            User loadedUser = loadUser(testAdventurer.getUserId());
            if (loadedUser == null) {
                LOGGER.severe("FAILED: Could not load saved user");
                return false;
            }
            LOGGER.info("SUCCESS: User loaded - %s".formatted(loadedUser.getName()));
            
            // Step 5: Verify credentials work
            LOGGER.info("Step 4: Testing credential verification...");
            String verifiedUserId = verifyAdventurerCredentials(username, password);
            if (verifiedUserId == null || !verifiedUserId.equals(testAdventurer.getUserId())) {
                LOGGER.severe("FAILED: Credential verification failed");
                return false;
            }
            LOGGER.info("SUCCESS: Credentials verified");
            
            // Step 6: Test complete login flow
            LOGGER.info("Step 5: Testing complete login flow...");
            User loginUser = loadUser(verifiedUserId);
            if (loginUser == null) {
                LOGGER.severe("FAILED: Could not load user after credential verification");
                return false;
            }
            
            LOGGER.info("✅ COMPLETE SUCCESS: Adventurer flow test passed!");
            LOGGER.info("  Username: %s".formatted(username));
            LOGGER.info("  User ID: %s".formatted(testAdventurer.getUserId()));
            LOGGER.info("  Name: %s".formatted(loginUser.getName()));
            LOGGER.info("  Role: %s".formatted(loginUser.getRole()));
            
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("FAILED: Exception during adventurer flow test: %s".formatted(e.getMessage()));
            System.err.println("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
    
    /**
     * Comprehensive Firebase connection test
     */
    public boolean testFirebaseConnection() {
        try {
            LOGGER.info("🧪 Starting comprehensive Firebase connection test...");
            
            // Test 1: Configuration Loading
            if (config == null) {
                LOGGER.severe("FAILED: Firebase configuration not loaded");
                return false;
            }
            LOGGER.info("✅ Test 1 PASSED: Firebase configuration loaded");
            
            // Test 2: Service Initialization
            if (authService == null || firestoreService == null) {
                LOGGER.warning("⚠️ Test 2: Firebase services not initialized (using mock mode)");
            } else {
                LOGGER.info("✅ Test 2 PASSED: Firebase services initialized");
            }
            
            // Test 3: User Storage (Local/Firebase)
            User testUser = new User();
            testUser.setUserId("connection_test_" + System.currentTimeMillis());
            testUser.setName("Connection Test User");
            testUser.setUsername("testconnection");
            testUser.setRole(UserRole.CHILD);
            testUser.setAge(10);
            testUser.setEmail("testconnection@coincraft.adventure");
            testUser.setSmartCoinBalance(25);
            testUser.setLevel(1);
            testUser.setCreatedAt(LocalDateTime.now());
            
            saveUser(testUser);
            User loadedUser = loadUser(testUser.getUserId());
            
            if (loadedUser == null) {
                LOGGER.severe("FAILED: Test 3 - Could not save/load user");
                return false;
            }
            LOGGER.info("✅ Test 3 PASSED: User storage and retrieval working");
            
            // Test 4: Credential Storage
            storeAdventurerCredentials("testconnection", "testpass", testUser.getUserId());
            String verifiedUserId = verifyAdventurerCredentials("testconnection", "testpass");
            
            if (verifiedUserId == null || !verifiedUserId.equals(testUser.getUserId())) {
                LOGGER.severe("FAILED: Test 4 - Credential storage/verification failed");
                return false;
            }
            LOGGER.info("✅ Test 4 PASSED: Credential storage and verification working");
            
            // Test 5: Username Availability Check
            boolean isUsernameTaken = isAdventureUsernameTaken("testconnection");
            if (!isUsernameTaken) {
                LOGGER.severe("FAILED: Test 5 - Username availability check failed");
                return false;
            }
            LOGGER.info("✅ Test 5 PASSED: Username availability check working");
            
            LOGGER.info("🎉 ALL FIREBASE CONNECTION TESTS PASSED!");
            return true;
            
        } catch (Exception e) {
            LOGGER.severe("FAILED: Exception during Firebase connection test: %s".formatted(e.getMessage()));
            System.err.println("Stack trace: " + java.util.Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
    
    /**
     * Get game progress data for a user
     */
    public Map<String, Object> getGameProgress(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // For now, use local storage as the primary method
            // In the future, this could be extended to use Firebase Admin SDK or REST API
            return getGameProgressLocally(userId);
            
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to get game progress: %s".formatted(e.getMessage()));
            return new HashMap<>();
        }
    }
    
    /**
     * Save game progress data for a user
     */
    public void saveGameProgress(String userId, Map<String, Object> data) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // For now, use local storage as the primary method
            // In the future, this could be extended to use Firebase Admin SDK or REST API
            saveGameProgressLocally(userId, data);
            LOGGER.info("Game progress saved locally for %s".formatted(userId));
            
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to save game progress: %s".formatted(e.getMessage()));
        }
    }
    
    /**
     * Get game progress from local storage
     */
    private Map<String, Object> getGameProgressLocally(String userId) {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Path progressFile = dataDir.resolve("game_progress_" + userId + ".json");
            
            if (Files.exists(progressFile)) {
                // Simple JSON parsing - in production, use a proper JSON library
                Map<String, Object> data = new HashMap<>();
                // For now, return empty map - proper JSON parsing would be implemented here
                return data;
            }
        } catch (RuntimeException e) {
            LOGGER.warning("Failed to load game progress locally: %s".formatted(e.getMessage()));
        }
        return new HashMap<>();
    }
    
    /**
     * Save game progress to local storage
     */
    private void saveGameProgressLocally(String userId, Map<String, Object> data) {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"), ".coincraft", "data");
            Files.createDirectories(dataDir);
            Path progressFile = dataDir.resolve("game_progress_" + userId + ".json");
            
            // Simple JSON serialization - in production, use a proper JSON library
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"userId\": \"").append(userId).append("\",\n");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                json.append("  \"").append(entry.getKey()).append("\": ");
                if (entry.getValue() instanceof String) {
                    json.append("\"").append(entry.getValue()).append("\"");
                } else {
                    json.append(entry.getValue());
                }
                json.append(",\n");
            }
            json.append("}\n");
            
            Files.writeString(progressFile, json.toString());
        } catch (IOException e) {
            LOGGER.warning("Failed to save game progress locally: %s".formatted(e.getMessage()));
        }
    }
}
