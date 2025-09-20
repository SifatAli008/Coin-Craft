package com.coincraft.services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
            
            // Initialize services with error handling for missing dependencies
            authService = new FirebaseAuthService(config);
            firestoreService = new FirestoreService(config);
            
            LOGGER.info(() -> "Firebase service initialized successfully for project: " + config.getProjectId());
            initialized = true;
            
        } catch (NoClassDefFoundError e) {
            LOGGER.info(() -> "Firebase libraries not found, initializing in mock mode: " + e.getMessage());
            initializeMockMode();
        } catch (Exception e) {
            LOGGER.warning(() -> "Failed to initialize Firebase, falling back to mock mode: " + e.getMessage());
            initializeMockMode();
        }
    }
    
    /**
     * Fallback to mock mode if Firebase initialization fails
     */
    private void initializeMockMode() {
        LOGGER.info("Initializing Firebase in mock mode");
        initialized = true;
        // Keep existing mock functionality as fallback
    }
    
    /**
     * Authenticate user with email and password
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
                // Fallback to mock authentication
                if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
                    String mockUserId = "user_" + email.hashCode();
                    LOGGER.info("Mock authentication successful for: " + email);
                    return mockUserId;
                }
                return null;
            }
            
        } catch (Exception e) {
            LOGGER.severe("Authentication failed: " + e.getMessage());
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
                    
                    LOGGER.info("User created successfully: " + email);
                    return result.getUserId();
                } else {
                    LOGGER.warning("User creation failed: " + result.getErrorMessage());
                    return null;
                }
            } else {
                // Fallback to mock user creation
                String userId = "user_" + System.currentTimeMillis();
                user.setUserId(userId);
                saveUser(user);
                LOGGER.info("Mock user created successfully: " + userId);
                return userId;
            }
            
        } catch (Exception e) {
            LOGGER.severe("User creation failed: " + e.getMessage());
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
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                boolean success = firestoreService.saveUser(user);
                if (success) {
                    LOGGER.info("User data saved successfully for " + user.getUserId());
                } else {
                    LOGGER.warning("Failed to save user data for " + user.getUserId());
                }
            } else {
                // Save to local storage as fallback when Firebase not available
                saveUserLocally(user);
                LOGGER.info("User data saved locally for " + user.getUserId());
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to save user: " + e.getMessage());
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
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load users: " + e.getMessage());
            return new ArrayList<>();
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
            
            LOGGER.info("User saved locally: " + user.getName());
            
        } catch (Exception e) {
            LOGGER.warning("Failed to save user locally: " + e.getMessage());
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
                LOGGER.info("Loaded " + users.size() + " users from local storage");
                return users;
            } else {
                LOGGER.info("No local user data file found");
                return new ArrayList<>();
            }
            
        } catch (Exception e) {
            LOGGER.warning("Failed to load users locally: " + e.getMessage());
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
                                (user.getLastLogin() != null ? user.getLastLogin().toString() : "") + "|" +
                                (user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
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
                    user.setLastLogin(LocalDateTime.parse(parts[8]));
                } else {
                    user.setLastLogin(LocalDateTime.now());
                }
                if (parts.length > 9 && !parts[9].isEmpty()) {
                    user.setCreatedAt(LocalDateTime.parse(parts[9]));
                }
                
                return user;
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to parse user data: " + userData);
        }
        return null;
    }
    
    /**
     * Parse user data from stored string (legacy format for system properties)
     */
    private User parseUserData(String userData) {
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
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    user.setEmail(parts[6]);
                }
                user.setLastLogin(LocalDateTime.now());
                return user;
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to parse user data: " + userData);
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
            
            if (firestoreService != null && currentIdToken != null) {
                // Use real Firestore
                User user = firestoreService.loadUser(userId);
                if (user != null) {
                    LOGGER.info("User data loaded successfully for " + userId);
                    return user;
                } else {
                    LOGGER.info("User not found: " + userId);
                }
            } else {
                // Mock load operation - create a sample user
                User mockUser = new User();
                mockUser.setUserId(userId);
                mockUser.setName("Test User");
                mockUser.setSmartCoinBalance(50);
                mockUser.setLevel(1);
                LOGGER.info("Mock: Loading user data for " + userId);
                return mockUser;
            }
            
            return null;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load user: " + e.getMessage());
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
            
            // Mock save operation for MVP
            LOGGER.info("Mock: Saving task data for " + task.getTaskId());
            
        } catch (Exception e) {
            LOGGER.severe("Failed to save task: " + e.getMessage());
        }
    }
    
    /**
     * Load tasks for a specific user
     */
    public List<Task> loadUserTasks(String userId) {
        try {
            if (!initialized) {
                throw new IllegalStateException("Firebase not initialized");
            }
            
            // Mock load operation for MVP - return sample tasks
            List<Task> mockTasks = new ArrayList<>();
            
            Task task1 = new Task("task_1", "Complete Level 1: Budget Basics", 
                                 com.coincraft.models.TaskType.LEARNING, 20);
            Task task2 = new Task("task_2", "Set up your first savings goal", 
                                 com.coincraft.models.TaskType.CHALLENGE, 15);
            
            mockTasks.add(task1);
            mockTasks.add(task2);
            
            LOGGER.info("Mock: Loading tasks for user " + userId);
            return mockTasks;
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load tasks: " + e.getMessage());
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
                LOGGER.info("Leaderboard loaded with " + leaderboard.size() + " entries");
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
                
                LOGGER.info("Mock: Loading leaderboard with " + limit + " entries");
                return mockLeaderboard;
            }
            
        } catch (Exception e) {
            LOGGER.severe("Failed to load leaderboard: " + e.getMessage());
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
            LOGGER.severe("Error clearing authentication state: " + e.getMessage());
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
            LOGGER.severe("Error during Firebase shutdown: " + e.getMessage());
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}
